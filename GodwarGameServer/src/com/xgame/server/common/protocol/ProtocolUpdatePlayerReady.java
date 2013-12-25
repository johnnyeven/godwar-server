package com.xgame.server.common.protocol;

import java.net.DatagramPacket;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xgame.server.CommandCenter;
import com.xgame.server.common.LogicServerInfo;
import com.xgame.server.common.PackageItem;
import com.xgame.server.common.ServerPackage;
import com.xgame.server.game.BattleRoom;
import com.xgame.server.game.LogicServerManager;
import com.xgame.server.game.Player;
import com.xgame.server.game.ProtocolPackage;
//import com.xgame.server.game.ProtocolPackage;
import com.xgame.server.game.Room;
import com.xgame.server.network.GameSession;
import com.xgame.server.network.LogicServerConnector;
import com.xgame.server.pool.BufferPool;
import com.xgame.server.pool.DatagramPacketPool;
import com.xgame.server.pool.ServerPackagePool;
import com.xgame.server.timer.StartBattleGameTimerTask;
import com.xgame.server.timer.TimerManager;

public class ProtocolUpdatePlayerReady implements IProtocol
{

	private static Log	log	= LogFactory
									.getLog( ProtocolUpdatePlayerReady.class );

	@Override
	public void Execute( Object param1, Object param2 )
	{
		ProtocolPackage parameter = (ProtocolPackage) param1;
		GameSession session = (GameSession) param2;

		int ready = Integer.MIN_VALUE;
		for ( int i = parameter.offset; i < parameter.receiveDataLength; )
		{
			int length = parameter.receiveData.getInt();
			int type = parameter.receiveData.get();
			switch ( type )
			{
				case EnumProtocol.TYPE_INT:
					if ( ready == Integer.MIN_VALUE )
					{
						ready = parameter.receiveData.getInt();
					}
					break;
			}
			i += ( length + 5 );
		}
		log.info( "[UpdatePlayerReady] Player Name = "
				+ session.getPlayer().name + " Ready = " + ready );

		if ( session.getPlayer().getCurrentCardGroup() <= 0 )
		{
			ServerPackage pack = ServerPackagePool.getInstance().getObject();
			pack.success = EnumProtocol.ACK_CONFIRM;
			pack.protocolId = EnumProtocol.BATTLEROOM_PLAYER_READY_ERROR;

			pack.parameter.add( new PackageItem( 4, 500 ) );
			CommandCenter.send( session.getChannel(), pack );

			return;
		}
		if ( session.getPlayer().getCurrentHeroCardId() == null
				|| session.getPlayer().getCurrentHeroCardId().equals( "" ) )
		{
			ServerPackage pack = ServerPackagePool.getInstance().getObject();
			pack.success = EnumProtocol.ACK_CONFIRM;
			pack.protocolId = EnumProtocol.BATTLEROOM_PLAYER_READY_ERROR;

			pack.parameter.add( new PackageItem( 4, 400 ) );
			CommandCenter.send( session.getChannel(), pack );

			return;
		}

		if ( session.getPlayer().getCurrentRoom() != null )
		{
			Room room = session.getPlayer().getCurrentRoom();
			HashMap< Player, Boolean > statusMap = (HashMap< Player, Boolean >) room
					.getStatusMap();
			Iterator< Player > it = room.getPlayerList().iterator();
			Player p;
			boolean tmp = false;
			while ( it.hasNext() )
			{
				p = it.next();
				if ( p == session.getPlayer() )
				{
					if ( statusMap.containsKey( p ) )
					{
						tmp = ready == 1 ? true : false;
						statusMap.put( p, tmp );
					}
				}

				ServerPackage pack = ServerPackagePool.getInstance()
						.getObject();
				pack.success = EnumProtocol.ACK_CONFIRM;
				pack.protocolId = EnumProtocol.BATTLEROOM_PLAYER_READY;

				String guid = session.getPlayer().getGuid().toString();
				pack.parameter.add( new PackageItem( guid.length(), guid ) );
				pack.parameter.add( new PackageItem( 4, ready ) );
				CommandCenter.send( p.getChannel(), pack );
			}

			Iterator< Entry< Player, Boolean >> itStatus = statusMap.entrySet()
					.iterator();
			Entry< Player, Boolean > en;
			while ( itStatus.hasNext() )
			{
				en = itStatus.next();
				if ( !en.getValue() )
				{
					return;
				}
			}
			noticePlayerStartTimer( room );
		}
	}

	private void noticePlayerStartTimer( Room room )
	{
		if ( room.getPeopleLimit() == room.getPeopleCount() )
		{
			List< Player > list = room.getPlayerList();
			Iterator< Player > it = list.iterator();
			Player p;
			ServerPackage pack;
			while ( it.hasNext() )
			{
				p = it.next();

				pack = ServerPackagePool.getInstance().getObject();
				pack.success = EnumProtocol.ACK_CONFIRM;
				pack.protocolId = EnumProtocol.BATTLEROOM_START_ROOM_TIMER;
				CommandCenter.send( p.getChannel(), pack );
			}

			StartBattleGameTimerTask task = new StartBattleGameTimerTask();
			task.room = room;
			TimerManager.getInstance().schedule( task, 10000 );
		}
	}

}
