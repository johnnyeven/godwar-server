
package com.xgame.server.game;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xgame.server.common.IntervalTimer;
import com.xgame.server.network.GameSession;

public class BattleHall implements IHall
{

	public static Map< Long, GameSession >	sessionMap		= new HashMap< Long, GameSession >();
	public static boolean					stop			= false;
	public static long						loopCounter		= 0;

	private static BattleHall				instance		= null;
	private static boolean					allowInstance	= false;

	private int								roomCount		= 0;
	private int								playerLimit;
	private long							serverStartTime;
	private List< GameSession >				sessionQueue;
	private IntervalTimer					timers[];
	private List< BattleRoom >				roomList;
	private static Log						log				= LogFactory
																	.getLog( BattleHall.class );

	public BattleHall() throws Exception
	{
		if ( !allowInstance )
		{
			throw new Exception();
		}
		playerLimit = 100;
		serverStartTime = new Date().getTime();
		sessionQueue = new ArrayList< GameSession >();
		roomList = new ArrayList< BattleRoom >();
		timers = new IntervalTimer[WorldTimers.TIMER_COUNT.ordinal()];
	}

	public static BattleHall getInstance()
	{
		if ( instance == null )
		{
			allowInstance = true;
			try
			{
				instance = new BattleHall();
			}
			catch ( Exception e )
			{
				e.printStackTrace();
			}
			allowInstance = false;
		}
		return instance;
	}

	public void addSessionQueue( GameSession session )
	{
		sessionQueue.add( session );
	}

	public void addSession( GameSession session )
	{
		GameSession old = sessionMap.get( session.getAccountId() );
		sessionMap.put( session.getAccountId(), session );
	}

	public void removeSession( long id )
	{

	}

	public GameSession getSession( long id )
	{
		return null;
	}

	public void updateSessions( long timeDiff )
	{
		while ( !sessionQueue.isEmpty() )
		{
			GameSession s = sessionQueue.remove( 0 );
			addSession( s );
		}

		Iterator< Entry< Long, GameSession >> it = sessionMap.entrySet()
				.iterator();
		Entry< Long, GameSession > e;
		GameSession s;
		while ( it.hasNext() )
		{
			e = it.next();
			s = e.getValue();
			if ( s == null )
			{
				continue;
			}

			if ( !s.update( timeDiff ) )
			{
				s.dispose();
				sessionMap.remove( e.getKey() );
				s = null;
			}
		}
	}

	public void setInitialWorldSettings()
	{
		timers[WorldTimers.TIMER_OBJECTS.ordinal()] = new IntervalTimer();
		timers[WorldTimers.TIMER_SESSIONS.ordinal()] = new IntervalTimer();
		// timers[WorldTimers.TIMER_EVENTS.ordinal()] = new IntervalTimer();
	}

	public void update( long timeDiff )
	{
		for ( int i = 0; i < WorldTimers.TIMER_COUNT.ordinal() - 1; i++ )
		{
			if ( timers[i] != null )
			{
				if ( timers[i].getCurrent() >= 0 )
				{
					timers[i].update( timeDiff );
				}
				else
				{
					timers[i].setCurrent( 0 );
				}
			}
		}

		if ( timers[WorldTimers.TIMER_SESSIONS.ordinal()].over() )
		{
			timers[WorldTimers.TIMER_SESSIONS.ordinal()].reset();

			updateSessions( timeDiff );

		}

		if ( timers[WorldTimers.TIMER_OBJECTS.ordinal()].over() )
		{
			timers[WorldTimers.TIMER_OBJECTS.ordinal()].reset();

			ObjectManager.getInstance().update( timeDiff );
		}
	}

	public void kickAllPlayer()
	{
		Iterator< Entry< Long, GameSession >> it = sessionMap.entrySet()
				.iterator();
		Entry< Long, GameSession > e;
		GameSession s;
		while ( it.hasNext() )
		{
			e = it.next();
			s = e.getValue();

			s.dispose();
		}
	}

	public void addRoom( BattleRoom room )
	{
		if ( roomList.indexOf( room ) > 0 )
		{
			log.error( "房间已存在" );
			return;
		}
		for ( int i = 0; i < roomList.size(); i++ )
		{
			if ( roomList.get( i ) == null )
			{
				roomList.set( i, room );
				room.setId( i + 1 );
				roomCount++;
				return;
			}
		}
		roomList.add( room );
		room.setId( roomList.size() );
		roomCount++;
	}

	public void removeRoom( BattleRoom room )
	{
		int index = roomList.indexOf( room );
		if ( index > 0 )
		{
			roomList.set( index, null );
			if ( roomCount > 0 )
			{
				roomCount--;
			}
		}
		else
		{
			log.error( "房间不存在" );
		}
	}

	public void removeRoom( int id )
	{
		if ( id > 0 )
		{
			roomList.set( id, null );
			if ( roomCount > 0 )
			{
				roomCount--;
			}
		}
	}

	public BattleRoom getRoom( int id )
	{
		if ( id >= roomList.size() )
		{
			log.error( "指定id的房间不存在" );
			return null;
		}
		else
		{
			return roomList.get( id );
		}
	}
}
