package com.xgame.server.logic;

enum WorldTimers
{
	TIMER_OBJECTS, TIMER_SESSIONS, TIMER_EVENTS, TIMER_COUNT
}

public class Hall
{
	private static Hall	instance	= null;

	private Hall()
	{

	}

	public static Hall getInstance()
	{
		if ( instance == null )
		{
			instance = new Hall();
		}
		return instance;
	}

	public void startHall()
	{
		Thread battleThread = new Thread( new BattleHallThread() );
		battleThread.setPriority( 10 );
		battleThread.start();

		Thread meleeThread = new Thread( new MeleeHallThread() );
		meleeThread.setPriority( 10 );
		meleeThread.start();
	}

}
