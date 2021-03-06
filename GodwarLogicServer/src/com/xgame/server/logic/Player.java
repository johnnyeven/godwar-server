package com.xgame.server.logic;

import java.nio.channels.AsynchronousSocketChannel;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xgame.server.cards.Card;
import com.xgame.server.cards.HeroCard;
import com.xgame.server.cards.SoulCard;
import com.xgame.server.common.database.DatabaseRouter;
import com.xgame.server.network.GameSession;
import com.xgame.server.pool.SoulCardPool;

public class Player
{
	private UUID						guid;
	public long							accountId		= Long.MIN_VALUE;
	public int							level			= 0;
	public String						name			= "";
	public long							accountCash		= Long.MIN_VALUE;
	public String						rolePicture		= "";
	public int							winningCount	= 0;
	public int							battleCount		= 0;
	public int							honor			= 0;
	public int							phase			= -1;

	private AsynchronousSocketChannel	channel;
	private GameSession					session;
	private Room						currentRoom;
	private IHall						currentHall;
	private int							currentGroup;										// 玩家阵营
																							// 1=红队
																							// 2=蓝队
	private int							currentPosition;
	private int							currentCardGroup;

	private HeroCard					heroCard;
	private List< SoulCard >			soulCardList;
	private List< Card >				supplyCardList;
	private Map< String, Card >			cardMap;
	private List< Card >				cardHand;
	private Map< String, Card >			handMap;

	private SoulCard					cardDefenser;
	private SoulCard					cardAttacker1;
	private SoulCard					cardAttacker2;
	private SoulCard					cardAttacker3;
	private boolean						isDeploy		= false;

	private static Log					log				= LogFactory
																.getLog( Player.class );

	public Player()
	{
		soulCardList = new ArrayList< SoulCard >();
		supplyCardList = new ArrayList< Card >();
		cardMap = new HashMap< String, Card >();
		cardHand = new ArrayList< Card >();
		handMap = new HashMap< String, Card >();
	}

	public boolean loadFromDatabase()
	{
		if ( accountId == Long.MIN_VALUE )
		{
			log.error( "loadFromDatabase() accountId没有初始化" );
			return false;
		}
		try
		{
			String sql = "SELECT * FROM `game_account` WHERE `account_id`="
					+ accountId;
			PreparedStatement st = DatabaseRouter.getInstance()
					.getConnection( "gamedb" ).prepareStatement( sql );
			ResultSet rs = st.executeQuery();

			if ( rs.first() )
			{
				level = rs.getInt( "level" );
				name = rs.getString( "nick_name" );
				accountCash = rs.getLong( "account_cash" );
				rolePicture = rs.getString( "role_picture" );
				winningCount = rs.getInt( "winning_count" );
				battleCount = rs.getInt( "battle_count" );
				honor = rs.getInt( "honor" );
			}
			else
			{
				log.error( "[loadFromDatabase] 没有找到对应的角色数据 accountId="
						+ accountId );
				return false;
			}
			String gameGuid = rs.getString( "game_guid" );

			if ( !gameGuid.equals( session.getGuid() ) )
			{
				log.error( "[loadFromDatabase] accountId与WorldSession使用的accountId不匹配" );
				return false;
			}
			if ( !gameGuid.isEmpty() )
			{
				setGuid( UUID.fromString( gameGuid ) );
			}

			rs.close();

			sql = "SELECT * FROM `game_card_group` WHERE `account_id`="
					+ accountId + " AND `current`=1";
			st = DatabaseRouter.getInstance().getConnection( "gamedb" )
					.prepareStatement( sql );
			rs = st.executeQuery();

			if ( rs.first() )
			{
				String list = rs.getString( "card_list" );
				String[] cardArray = list.split( "," );

				SoulCard soulCard;
				String[] cardTmp;
				int cardType;
				for ( int i = 0; i < cardArray.length; i++ )
				{
					if ( !cardArray[i].equals( "" ) )
					{
						cardTmp = cardArray[i].split( ":" );
						cardType = Integer.parseInt( cardTmp[0] );

						if ( cardType == 0 )
						{
							soulCard = SoulCardPool.getInstance().getObject();
							soulCard.loadInfo( cardTmp[1] );
							soulCardList.add( soulCard );
							cardMap.put( soulCard.getId(), soulCard );
						}
					}
				}
				Collections.shuffle( soulCardList );
			}
			else
			{
				log.error( "[loadFromDatabase] 没有已激活的卡组" + accountId );
				return false;
			}

			rs.close();
		}
		catch ( SQLException e )
		{
			e.printStackTrace();
		}
		return true;
	}

	public Card getCard( String id )
	{
		if ( cardMap.containsKey( id ) )
		{
			return cardMap.get( id );
		}
		log.error( "指定ID的卡牌不存在，id = " + id.toString() );
		return null;
	}

	public void update( long timeDiff )
	{

	}

	public void killPlayer()
	{
		// TODO 死亡处理

	}

	public int getCurrentCardGroup()
	{
		return currentCardGroup;
	}

	public void setCurrentCardGroup( int currentCardGroup )
	{
		this.currentCardGroup = currentCardGroup;
	}

	public SoulCard popSoulCardToHand()
	{
		if ( soulCardList.size() > 0 )
		{
			SoulCard card = soulCardList.remove( 0 );
			cardMap.remove( card.getId() );
			cardHand.add( card );
			handMap.put( card.getId(), card );

			return card;
		}
		return null;
	}

	public List< SoulCard > getSoulCardList()
	{
		return soulCardList;
	}

	public String getSoulCardString()
	{
		Card card;
		StringBuffer buf = new StringBuffer();
		if ( soulCardList.size() > 0 )
		{
			buf.append( soulCardList.get( 0 ).getId() );
			for ( int i = 1; i < soulCardList.size(); i++ )
			{
				card = soulCardList.get( i );
				buf.append( "," + card.getId() );
			}
		}
		return buf.toString();
	}

	// public SupplyCard popSupplyCardToHand()
	// {
	// SupplyCard card = (SupplyCard) supplyCardList.remove( 0 );
	// cardHand.add( card );
	//
	// return card;
	// }

	public List< Card > getSupplyCardList()
	{
		return supplyCardList;
	}

	public String getSupplyCardString()
	{
		Card card;
		StringBuffer buf = new StringBuffer();
		if ( supplyCardList.size() > 0 )
		{
			buf.append( supplyCardList.get( 0 ).getId() );
			for ( int i = 1; i < supplyCardList.size(); i++ )
			{
				card = supplyCardList.get( i );
				buf.append( "," + card.getId() );
			}
		}
		return buf.toString();
	}

	public List< Card > getCardHandList()
	{
		return cardHand;
	}

	public GameSession getSession()
	{
		return session;
	}

	public void setSession( GameSession session )
	{
		this.session = session;
	}

	public AsynchronousSocketChannel getChannel()
	{
		return channel;
	}

	public void setChannel( AsynchronousSocketChannel channel )
	{
		this.channel = channel;
	}

	public UUID getGuid()
	{
		return guid;
	}

	public void setGuid( UUID guid )
	{
		this.guid = guid;
	}

	public Room getCurrentRoom()
	{
		return currentRoom;
	}

	public void setCurrentRoom( Room currentRoom )
	{
		this.currentRoom = currentRoom;
	}

	public IHall getCurrentHall()
	{
		return currentHall;
	}

	public void setCurrentHall( IHall currentHall )
	{
		this.currentHall = currentHall;
	}

	public int getCurrentPosition()
	{
		return currentPosition;
	}

	public void setCurrentPosition( int currentPosition )
	{
		this.currentPosition = currentPosition;
	}

	public int getCurrentGroup()
	{
		return currentGroup;
	}

	public void setCurrentGroup( int currentGroup )
	{
		this.currentGroup = currentGroup;
	}

	public HeroCard getHeroCard()
	{
		return heroCard;
	}

	public void setHeroCard( HeroCard heroCard )
	{
		this.heroCard = heroCard;
	}

	public SoulCard getCardDefenser()
	{
		return cardDefenser;
	}

	public void setCardDefenser( String cardDefenser )
	{
		if ( handMap.containsKey( cardDefenser ) )
		{
			Card card = handMap.get( cardDefenser );
			if ( card instanceof SoulCard )
			{
				this.cardDefenser = (SoulCard) card;
			}
			else
			{
				// TODO 报错
				log.error( "CardDefenser不存在于手牌，或者指定的CardDefenser不是英灵卡牌, Id = "
						+ cardDefenser );
			}
		}

		if ( this.cardDefenser != null && this.cardAttacker1 != null
				&& this.cardAttacker2 != null && this.cardAttacker3 != null )
		{
			isDeploy = true;
		}
	}

	public SoulCard getCardAttacker1()
	{
		return cardAttacker1;
	}

	public void setCardAttacker1( String cardAttacker1 )
	{
		if ( handMap.containsKey( cardAttacker1 ) )
		{
			Card card = handMap.get( cardAttacker1 );
			if ( card instanceof SoulCard )
			{
				this.cardAttacker1 = (SoulCard) card;
			}
			else
			{
				// TODO 报错
				log.error( "CardDefenser不存在于手牌，或者指定的CardDefenser不是英灵卡牌, Id = "
						+ cardAttacker1 );
			}
		}

		if ( this.cardDefenser != null && this.cardAttacker1 != null
				&& this.cardAttacker2 != null && this.cardAttacker3 != null )
		{
			isDeploy = true;
		}
	}

	public SoulCard getCardAttacker2()
	{
		return cardAttacker2;
	}

	public void setCardAttacker2( String cardAttacker2 )
	{
		if ( handMap.containsKey( cardAttacker2 ) )
		{
			Card card = handMap.get( cardAttacker2 );
			if ( card instanceof SoulCard )
			{
				this.cardAttacker2 = (SoulCard) card;
			}
			else
			{
				// TODO 报错
				log.error( "CardDefenser不存在于手牌，或者指定的CardDefenser不是英灵卡牌, Id = "
						+ cardAttacker2 );
			}
		}

		if ( this.cardDefenser != null && this.cardAttacker1 != null
				&& this.cardAttacker2 != null && this.cardAttacker3 != null )
		{
			isDeploy = true;
		}
	}

	public SoulCard getCardAttacker3()
	{
		return cardAttacker3;
	}

	public void setCardAttacker3( String cardAttacker3 )
	{
		if ( handMap.containsKey( cardAttacker3 ) )
		{
			Card card = handMap.get( cardAttacker3 );
			if ( card instanceof SoulCard )
			{
				this.cardAttacker3 = (SoulCard) card;
			}
			else
			{
				// TODO 报错
				log.error( "CardDefenser不存在于手牌，或者指定的CardDefenser不是英灵卡牌, Id = "
						+ cardAttacker3 );
			}
		}

		if ( this.cardDefenser != null && this.cardAttacker1 != null
				&& this.cardAttacker2 != null && this.cardAttacker3 != null )
		{
			isDeploy = true;
		}
	}

	public SoulCard getFormationCard( int position )
	{
		if ( position == 0 )
		{
			return cardDefenser;
		}
		else if ( position == 1 )
		{
			return cardAttacker1;
		}
		else if ( position == 2 )
		{
			return cardAttacker2;
		}
		else if ( position == 3 )
		{
			return cardAttacker3;
		}
		return null;
	}

	public SoulCard getFormationCard( String cardId )
	{
		if ( cardDefenser.getId().equals( cardId ) )
		{
			return cardDefenser;
		}
		else if ( cardAttacker1.getId().equals( cardId ) )
		{
			return cardAttacker1;
		}
		else if ( cardAttacker2.getId().equals( cardId ) )
		{
			return cardAttacker2;
		}
		else if ( cardAttacker3.getId().equals( cardId ) )
		{
			return cardAttacker3;
		}
		return null;
	}

	public int getFormationCardPosition( String cardId )
	{
		if ( cardDefenser.getId().equals( cardId ) )
		{
			return 0;
		}
		else if ( cardAttacker1.getId().equals( cardId ) )
		{
			return 1;
		}
		else if ( cardAttacker2.getId().equals( cardId ) )
		{
			return 2;
		}
		else if ( cardAttacker3.getId().equals( cardId ) )
		{
			return 3;
		}
		return -1;
	}

	public boolean isDeploy()
	{
		return isDeploy;
	}
}
