package com.xgame.server.timer;

import java.util.Date;
import java.util.Iterator;
import java.util.TimerTask;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xgame.server.logic.BattleHall;
import com.xgame.server.logic.BattleRoom;

public class CheckBattleRoomTimerTask extends TimerTask
{
	Iterator< BattleRoom >	it;
	private static Log		log	= LogFactory
										.getLog( CheckBattleRoomTimerTask.class );

	public CheckBattleRoomTimerTask()
	{
		it = BattleHall.getInstance().getRooms();
	}

	@Override
	public void run()
	{
		BattleRoom room;
		long currentTime = new Date().getTime();

		while ( it.hasNext() )
		{
			room = it.next();

			if ( room.getPlayerList().size() == 0
					&& room.getCreatedTime() + 10000 <= currentTime )
			{
				log.warn( "[Timer-CheckBattleRoom] �Ӵ�����ĿǰΪֹ�ѳ���10���������ӣ����䱻�ͷţ�room id = " + room.getId() + ", room title = " + room.getTitle() );
				BattleHall.getInstance().removeRoom( room );
				room.dispose();
				room = null;
			}
		}
	}

}
