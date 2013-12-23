package com.xgame.server.timer;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class TimerManager extends Timer
{
	private Map< String, TimerTask >	timerContainer;

	private TimerManager()
	{
		super( true );
		timerContainer = new HashMap< String, TimerTask >();
	}

	private TimerManager( String arg0 )
	{
		super( arg0, true );
		timerContainer = new HashMap< String, TimerTask >();
	}

	public void schedule( String id, TimerTask task, long delay, long period )
	{
		if ( timerContainer.containsKey( id ) )
		{
			return;
		}
		timerContainer.put( id, task );
		super.schedule( task, delay, period );
	}

	public TimerTask getTask( String id )
	{
		return timerContainer.get( id );
	}

	public static TimerManager getInstance()
	{
		return TimerManagerHolder.instance;
	}

	private static class TimerManagerHolder
	{
		private static TimerManager	instance	= new TimerManager(
														"TimerManager" );
	}

}
