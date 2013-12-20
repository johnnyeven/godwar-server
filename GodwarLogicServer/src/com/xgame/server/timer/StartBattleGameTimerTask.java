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
		List< Player > playerList;
		Player p;
		ServerPackage pack;
		if ( roomType == 0 )
		{
			room = BattleHall.getInstance().getRoom( roomId );
			if ( room != null )
			{
				playerList = room.getPlayerList();
				for ( int i = 0; i < playerList.size(); i++ )
				{
					p = playerList.get( i );

					pack = ServerPackagePool.getInstance().getObject();
					pack.success = EnumProtocol.ACK_CONFIRM;
					pack.protocolId = EnumProtocol.BATTLEROOM_REQUEST_START_BATTLE;

					CommandCenter.send( p.getChannel(), pack );
				}
			}
		}
		
		room = null;
		playerList = null;
		p = null;
		
		cancel();
	}

}
