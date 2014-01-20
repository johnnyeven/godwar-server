package com.xgame.server.timer;

import java.util.TimerTask;

import com.xgame.server.logic.BattleHall;
import com.xgame.server.logic.Room;

public class StartBattleGameTimerTask extends TimerTask
{
	public int	roomType;
	public int	roomId;

	public StartBattleGameTimerTask()
	{

	}

	@Override
	public void run()
	{
		Room room;
		if ( roomType == 0 )
		{
			room = BattleHall.getInstance().getRoom( roomId );
			if ( room != null )
			{
				room.start();
			}
		}
		
		room = null;
		
		cancel();
	}

}
