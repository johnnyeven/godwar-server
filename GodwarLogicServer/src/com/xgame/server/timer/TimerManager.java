package com.xgame.server.timer;

import java.util.Timer;

public class TimerManager extends Timer
{

	private TimerManager()
	{
		super( true );
	}

	private TimerManager( String arg0 )
	{
		super( arg0, true );
	}
	
	public static TimerManager getInstance()
	{
		return TimerManagerHolder.instance;
	}
	
	private static class TimerManagerHolder
	{
		private static TimerManager instance = new TimerManager("TimerManager");
	}

}
