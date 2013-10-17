package com.xgame.server.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xgame.server.common.IntervalTimer;

public class BattleRoom
{
	private int								id;
	private String							title;
	private int								peopleLimit;
	private int								peopleCount;
	private Player							owner;
	private List< Player >					playerList;
	private Map< Player, IntervalTimer >	timerMap;
	private RoomStatus						status;
	private int								rounds;
	private Player							currentPlayer;
	private long							startTime;
	private long							endTime;
	private static Log						log	= LogFactory
														.getLog( BattleRoom.class );

	public BattleRoom()
	{

	}

	public void initialize()
	{
		if ( peopleLimit > 0 )
		{
			playerList = new ArrayList< Player >();
			timerMap = new HashMap< Player, IntervalTimer >();
		}
		else
		{
			log.fatal( "peopleLimit参数应大于0" );
		}
	}

	public void addPlayer( Player p )
	{
		if ( peopleCount >= peopleLimit )
		{
			log.error( "房间已满员" );
			return;
		}
		if ( playerList.indexOf( p ) > 0 )
		{
			log.error( "玩家已存在于该房间" );
			return;
		}
		for ( int i = 0; i < playerList.size(); i++ )
		{
			if ( playerList.get( i ) == null )
			{
				playerList.set( i, p );
				peopleCount++;
				return;
			}
		}
		playerList.add( p );
		peopleCount++;
	}

	public void removePlayer( Player p )
	{
		int index = playerList.indexOf( p );
		if ( index > 0 )
		{
			playerList.set( index, null );
			peopleCount--;
		}
		else
		{
			log.error( "玩家不存在" );
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

}
