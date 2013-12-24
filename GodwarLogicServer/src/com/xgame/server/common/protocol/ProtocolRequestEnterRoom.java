package com.xgame.server.common.protocol;

import java.util.Iterator;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xgame.server.CommandCenter;
import com.xgame.server.common.PackageItem;
import com.xgame.server.common.ServerPackage;
import com.xgame.server.logic.BattleHall;
import com.xgame.server.logic.BattleRoom;
import com.xgame.server.logic.Player;
import com.xgame.server.logic.ProtocolPackage;
import com.xgame.server.network.GameSession;
import com.xgame.server.pool.ServerPackagePool;
import com.xgame.server.timer.StartBattleGameTimerTask;
import com.xgame.server.timer.TimerManager;

public class ProtocolRequestEnterRoom implements IProtocol
{

	private static Log	log	= LogFactory
									.getLog( ProtocolRequestEnterRoom.class );

	@Override
	public void Execute( Object param1, Object param2 )
	{
		ProtocolPackage parameter = ( ProtocolPackage ) param1;
		GameSession session = ( GameSession ) param2;

		int roomType = Integer.MIN_VALUE;
		int id = Integer.MIN_VALUE;
		for ( int i = parameter.offset; i < parameter.receiveDataLength; )
		{
			int length = parameter.receiveData.getInt();
			int type = parameter.receiveData.get();
			switch ( type )
			{
				case EnumProtocol.TYPE_INT:
					if ( roomType == Integer.MIN_VALUE )
					{
						roomType = parameter.receiveData.getInt();
					}
					else if ( id == Integer.MIN_VALUE )
					{
						id = parameter.receiveData.getInt();
					}
					break;
			}
			i += ( length + 5 );
		}
		log.info( "[RequestEnterRoom] Room Type = " + roomType + ", Room id = "
				+ id + ", Player = " + session.getPlayer().name );

		List< Player > list;
		Iterator< Player > it;
		Player p;
		ServerPackage pack;

		BattleRoom room = BattleHall.getInstance().getRoom( id );
		if ( room != null )
		{
			if ( room.getPlayerList().size() >= room.getPeopleCount() )
			{
				log.info( "[RequestEnterRoom] 房间已满员" );

				pack = ServerPackagePool.getInstance().getObject();
				pack.success = EnumProtocol.ACK_CONFIRM;
				pack.protocolId = EnumProtocol.HALL_REQUEST_ENTER_ROOM_LOGICSERVER;
				pack.parameter.add( new PackageItem( 4, -1 ) );
				CommandCenter.send( parameter.client, pack );
			}
			else
			{
				room.addPlayer( session.getPlayer() );

				// 发送房间基本信息
				pack = ServerPackagePool.getInstance().getObject();
				pack.success = EnumProtocol.ACK_CONFIRM;
				pack.protocolId = EnumProtocol.BATTLEROOM_INIT_ROOM_LOGICSERVER;
				pack.parameter.add( new PackageItem( 4, room.getId() ) );
				pack.parameter
						.add( new PackageItem( 4, room.getPeopleCount() ) );
				// guid
				String guid = session.getPlayer().getGuid().toString();
				pack.parameter.add( new PackageItem( guid.length(), guid ) );
				// accountId
				pack.parameter.add( new PackageItem( 8,
						session.getPlayer().accountId ) );
				// level
				pack.parameter.add( new PackageItem( 4,
						session.getPlayer().level ) );
				// name
				String name = session.getPlayer().name;
				pack.parameter.add( new PackageItem( name.length(), name ) );
				// group
				pack.parameter.add( new PackageItem( 4, session.getPlayer()
						.getCurrentGroup() ) );
				// heroCard
				String heroCardId = session.getPlayer().getHeroCard().getId();
				pack.parameter.add( new PackageItem( heroCardId.length(),
						heroCardId ) );
				// soulCardCount
				pack.parameter.add( new PackageItem( 4, session.getPlayer()
						.getSoulCardList().size() ) );
				// supplyCardCount
				pack.parameter.add( new PackageItem( 4, session.getPlayer()
						.getSupplyCardList().size() ) );

				String soulCardString = session.getPlayer().getSoulCardString();
				pack.parameter.add( new PackageItem( soulCardString.length(),
						soulCardString ) );
				String supplyCardString = session.getPlayer().getSupplyCardString();
				pack.parameter.add( new PackageItem( supplyCardString.length(),
						supplyCardString ) );

				list = room.getPlayerList();
				it = list.iterator();
				String uuid;
				while ( it.hasNext() )
				{
					p = it.next();

					if ( p == session.getPlayer() )
					{
						continue;
					}

					uuid = p.getGuid().toString();
					pack.parameter.add( new PackageItem( uuid.length(), uuid ) );
					pack.parameter.add( new PackageItem( 8, p.accountId ) );
					pack.parameter.add( new PackageItem( p.name.length(),
							p.name ) );
					pack.parameter.add( new PackageItem( 4, p.level ) );
					heroCardId = p.getHeroCard().getId();
					pack.parameter.add( new PackageItem( heroCardId.length(),
							heroCardId ) );
					pack.parameter
							.add( new PackageItem( 4, p.getCurrentGroup() ) );
				}
				CommandCenter.send( parameter.client, pack );

				if ( room.getPlayerList().size() == room.getPeopleCount() )
				{
					list = room.getPlayerList();
					it = list.iterator();
					while ( it.hasNext() )
					{
						p = it.next();

						pack = ServerPackagePool.getInstance().getObject();
						pack.success = EnumProtocol.ACK_CONFIRM;
						pack.protocolId = EnumProtocol.BATTLEROOM_START_BATTLE_TIMER;
						CommandCenter.send( p.getChannel(), pack );
					}

					StartBattleGameTimerTask timer = new StartBattleGameTimerTask();
					timer.roomType = roomType;
					timer.roomId = id;
					TimerManager.getInstance().schedule( timer, 3000 );
				}
			}
		}
	}

}
