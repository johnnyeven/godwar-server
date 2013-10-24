package com.xgame.server.logic;

import java.util.Date;

public class BattleHallThread implements Runnable
{
	public static final int	WORLD_SLEEP_TIME	= 200;

	@Override
	public void run()
	{
		long currentTime = 0;
		long prevTime = new Date().getTime();
		long timeDiff = 0;
		long prevSleepTime = 0;

		while ( !BattleHall.stop )
		{
			BattleHall.loopCounter++;
			currentTime = new Date().getTime();
			timeDiff = currentTime - prevTime;

			BattleHall.getInstance().update( timeDiff );
			prevTime = currentTime;

			if ( timeDiff <= WORLD_SLEEP_TIME + prevSleepTime )
			{
				prevSleepTime = WORLD_SLEEP_TIME + prevSleepTime - timeDiff;
				try
				{
					Thread.sleep( prevSleepTime );
				}
				catch ( InterruptedException e )
				{
					e.printStackTrace();
				}
			}
			else
			{
				prevSleepTime = 0;
			}
		}

		BattleHall.getInstance().kickAllPlayer();
		BattleHall.getInstance().updateSessions( 1 );
//		AIOSocketMgr.getInstance().stopCompletionPort();
	}

}
