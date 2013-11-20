package com.xgame.server.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xgame.server.CommandCenter;
import com.xgame.server.cards.Card;
import com.xgame.server.common.IntervalTimer;
import com.xgame.server.common.PackageItem;
import com.xgame.server.common.ServerPackage;
import com.xgame.server.common.protocol.EnumProtocol;
import com.xgame.server.pool.ServerPackagePool;

public class Room
{

	private int						id;
	private String					title;
	private int						peopleLimit;
	private int						peopleCount;
	private Player					owner;
	private List< Player >			playerList;
	private Map< Player, Boolean >	statusMap;
	private Map< Player, Card >		heroMap;
	private RoomStatus				status;
	private int						rounds;
	private Player					currentPlayer;
	private long					startTime;
	private long					endTime;
	private static Log				log	= LogFactory.getLog( Room.class );

	public Room()
	{

	}

	public void initialize()
	{
		if ( peopleLimit > 0 )
		{
			playerList = new ArrayList< Player >();
			statusMap = new HashMap< Player, Boolean >();
			heroMap = new HashMap< Player, Card >();
		}
		else
		{
			log.fatal( "peopleLimit����Ӧ����0" );
		}
	}

	public Boolean addPlayer( Player p )
	{
		if ( peopleCount >= peopleLimit )
		{
			log.error( "��������Ա" );
			return false;
		}
		if ( playerList.indexOf( p ) > 0 )
		{
			log.error( "����Ѵ����ڸ÷���" );
			return false;
		}
		p.setCurrentRoom( this );
		for ( int i = 0; i < playerList.size(); i++ )
		{
			if ( playerList.get( i ) == null )
			{
				playerList.set( i, p );
				peopleCount++;

				noticePlayerJoin( p );
				return true;
			}
		}
		playerList.add( p );
		statusMap.put( p, false );
		peopleCount++;

		noticePlayerJoin( p );
		return true;
	}

	public void removePlayer( Player p )
	{
		int index = playerList.indexOf( p );
		if ( index > 0 )
		{
			playerList.set( index, null );
			statusMap.remove( p );
			peopleCount--;
		}
		else
		{
			log.error( "��Ҳ�����" );
		}
	}

	private void noticePlayerJoin( Player p )
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
			pack.protocolId = EnumProtocol.BATTLEROOM_PLAYER_ENTER_ROOM;
			pack.parameter.add( new PackageItem( 8, p.accountId ) );
			pack.parameter.add( new PackageItem( p.name.length(), p.name ) );
			CommandCenter.send( p.getChannel(), pack );
		}
	}

	public void setPlayerHero( Player p, Card hero )
	{
		if ( heroMap.containsKey( p ) )
		{
			log.error( "ָ�������ȷ��Ӣ�ۿ���" );
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

	public void startGame()
	{

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

	public int getPeopleLimit()
	{
		return peopleLimit;
	}

	public void setPeopleLimit( int peopleLimit )
	{
		this.peopleLimit = peopleLimit;
	}

	public int getPeopleCount()
	{
		return peopleCount;
	}

	public void setPeopleCount( int peopleCount )
	{
		this.peopleCount = peopleCount;
	}

	public Map< Player, Boolean > getStatusMap()
	{
		return statusMap;
	}

	public Map< Player, Card > getHeroMap()
	{
		return heroMap;
	}

}
