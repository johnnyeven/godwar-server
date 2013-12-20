package com.xgame.server.logic;

import java.nio.channels.AsynchronousSocketChannel;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
	private int							currentCardGroup;

	private HeroCard					heroCard;
	private List< Card >				cardList;
	private Map< String, Card >			cardMap;

	private static Log					log				= LogFactory
																.getLog( Player.class );

	public Player()
	{
		cardList = new ArrayList< Card >();
		cardMap = new HashMap< String, Card >();
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
						
						if(cardType == 0)
						{
							soulCard = SoulCardPool.getInstance().getObject();
							soulCard.loadInfo( cardTmp[1] );
							cardList.add( soulCard );
							cardMap.put( soulCard.getId(), soulCard );
						}
					}
				}
			}
			else
			{
				log.error( "[loadFromDatabase] 没有已激活的卡组" + accountId );
				return false;
			}
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

	public List< Card > getCardList()
	{
		return cardList;
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
}
