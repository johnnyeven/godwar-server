package com.xgame.server.timer;

import java.util.List;
import java.util.TimerTask;

import com.xgame.server.CommandCenter;
import com.xgame.server.common.ServerPackage;
import com.xgame.server.common.protocol.EnumProtocol;
import com.xgame.server.logic.BattleHall;
import com.xgame.server.logic.Player;
import com.xgame.server.logic.Room;
import com.xgame.server.pool.ServerPackagePool;

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
