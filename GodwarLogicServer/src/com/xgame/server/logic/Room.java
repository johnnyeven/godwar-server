package com.xgame.server.logic;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xgame.server.CommandCenter;
import com.xgame.server.cards.Card;
import com.xgame.server.cards.HeroCard;
import com.xgame.server.common.PackageItem;
import com.xgame.server.common.ServerPackage;
import com.xgame.server.common.protocol.EnumProtocol;
import com.xgame.server.pool.HeroCardPool;
import com.xgame.server.pool.ServerPackagePool;

public abstract class Room
{

	protected int					id;
	protected String				title;
	protected int					peopleCount;

	protected String				ownerGuid;
	protected List< String >		playerGuidList;
	protected Map< String, String >	heroCardIdMap;

	protected Player				owner;
	protected List< Player >		playerList;
	protected Map< Player, Card >	heroMap;
	protected RoomStatus			status;
	protected int					rounds;
	protected Player				currentPlayer;
	protected long					createdTime;
	protected long					startTime;
	protected long					endTime;
	protected static Log			log	= LogFactory.getLog( Room.class );

	public Room()
	{
		
	}

	public void initialize()
	{
		if ( peopleCount > 0 )
		{
			playerGuidList = new ArrayList< String >();
			heroCardIdMap = new HashMap< String, String >();
			playerList = new ArrayList< Player >();
			heroMap = new HashMap< Player, Card >();
			createdTime = new Date().getTime();
		}
		else
		{
			log.fatal( "peopleLimit参数应大于0" );
			return;
		}
	}

	abstract public Boolean addPlayerGuid( String guid, int group );
	
	abstract public Boolean addPlayerPosition( String guid, int position );

	abstract public Boolean hasPlayerGuid( String guid );
	
	abstract public void start();
	
	public Boolean addHeroCardId( String guid, String id )
	{
		if ( heroCardIdMap.containsKey( guid ) )
		{
			log.error( "玩家英雄卡牌已存在于该房间" );
			return false;
		}
		heroCardIdMap.put( guid, id );
		return true;
	}

	public Boolean addPlayer( Player p )
	{
		if ( playerList.size() >= peopleCount )
		{
			log.error( "房间已满员" );
			return false;
		}
		if ( playerList.indexOf( p ) >= 0 )
		{
			log.error( "玩家已存在于该房间" );
			return false;
		}
		String guid = p.getGuid().toString();
		p.setCurrentRoom( this );
		if ( heroCardIdMap.containsKey( guid ) )
		{
			String heroCardId = heroCardIdMap.get( guid );
			HeroCard card = HeroCardPool.getInstance().getObject();
			card.loadInfo( heroCardId );
			p.setHeroCard( card );
		}
		else
		{
			log.error( "无法找到玩家的英雄数据" );
			return false;
		}
		for ( int i = 0; i < playerList.size(); i++ )
		{
			if ( playerList.get( i ) == null )
			{
				playerList.set( i, p );

				noticePlayerJoin( p );
				return true;
			}
		}
		playerList.add( p );

		noticePlayerJoin( p );
		return true;
	}

	public void removePlayer( Player p )
	{
		int index = playerList.indexOf( p );
		if ( index >= 0 )
		{
			playerList.remove( index );

			Player player;
			Iterator< Player > it = playerList.iterator();
			while ( it.hasNext() )
			{
				player = it.next();
				if ( player != p )
				{
					ServerPackage pack = ServerPackagePool.getInstance()
							.getObject();
					pack.success = EnumProtocol.ACK_CONFIRM;
					pack.protocolId = EnumProtocol.BATTLEROOM_PLAYER_LEAVE_ROOM;
					String guid = p.getGuid().toString();
					pack.parameter.add( new PackageItem( guid.length(), guid ) );
					CommandCenter.send( player.getChannel(), pack );
				}
			}
		}
		else
		{
			log.error( "玩家不存在" );
		}
	}

	public void removeAllPlayer()
	{
		owner = null;
		currentPlayer = null;

		playerList.clear();
		heroMap.clear();
		peopleCount = 0;
	}

	protected void noticePlayerJoin( Player p )
	{

	}

	public void setPlayerHero( Player p, Card hero )
	{
		if ( heroMap.containsKey( p ) )
		{
			log.error( "指定玩家已确定英雄卡牌" );
			return;
		}
		heroMap.put( p, hero );
		noticePlayerSelectedHero( p );
	}

	protected void noticePlayerSelectedHero( Player p )
	{
		Iterator< Player > it = playerList.iterator();
		Player p1;
		while ( it.hasNext() )
		{
			p1 = it.next();
			if ( p1 == p )
			{
				continue;
			}

			ServerPackage pack = ServerPackagePool.getInstance().getObject();
			pack.success = EnumProtocol.ACK_CONFIRM;
			pack.protocolId = EnumProtocol.BATTLEROOM_PLAYER_SELECTED_HERO;
			pack.parameter.add( new PackageItem( 8, p.accountId ) );
			CommandCenter.send( p.getChannel(), pack );
		}
	}

	public int getId()
	{
		return id;
	}

	public void setId( int id )
	{
		this.id = id;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle( String title )
	{
		this.title = title;
	}

	public Player getOwner()
	{
		return owner;
	}

	public void setOwner( Player owner )
	{
		this.owner = owner;
	}

	public List< Player > getPlayerList()
	{
		return playerList;
	}

	public RoomStatus getStatus()
	{
		return status;
	}

	public void setOwnerGuid( String ownerGuid )
	{
		this.ownerGuid = ownerGuid;
	}

	public void setStatus( RoomStatus status )
	{
		this.status = status;
	}

	public int getRounds()
	{
		return rounds;
	}

	public void setRounds( int rounds )
	{
		this.rounds = rounds;
	}

	public Player getCurrentPlayer()
	{
		return currentPlayer;
	}

	public void setCurrentPlayer( Player currentPlayer )
	{
		this.currentPlayer = currentPlayer;
	}

	public long getStartTime()
	{
		return startTime;
	}

	public void setStartTime( long startTime )
	{
		this.startTime = startTime;
	}

	public long getEndTime()
	{
		return endTime;
	}

	public void setEndTime( long endTime )
	{
		this.endTime = endTime;
	}

	public int getPeopleCount()
	{
		return peopleCount;
	}

	public void setPeopleCount( int peopleCount )
	{
		this.peopleCount = peopleCount;
	}

	public Map< Player, Card > getHeroMap()
	{
		return heroMap;
	}

	public long getCreatedTime()
	{
		return createdTime;
	}

	public void dispose()
	{
		removeAllPlayer();
		playerList = null;
		heroMap = null;
	}
}
