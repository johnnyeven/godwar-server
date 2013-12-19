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

		BattleRoom room = BattleHall.getInstance().getRoom( id );
		if ( room != null )
		{
			if ( room.getPlayerList().size() >= room.getPeopleCount() )
			{
				log.info( "[RequestEnterRoom] 房间已满员" );

				ServerPackage pack = ServerPackagePool.getInstance()
						.getObject();
				pack.success = EnumProtocol.ACK_CONFIRM;
				pack.protocolId = EnumProtocol.HALL_REQUEST_ENTER_ROOM_LOGICSERVER;
				pack.parameter.add( new PackageItem( 4, -1 ) );
				CommandCenter.send( parameter.client, pack );
			}
			else
			{
				room.addPlayer( session.getPlayer() );

				// 发送房间基本信息
				ServerPackage pack = ServerPackagePool.getInstance()
						.getObject();
				pack.success = EnumProtocol.ACK_CONFIRM;
				pack.protocolId = EnumProtocol.BATTLEROOM_INIT_ROOM_LOGICSERVER;
				pack.parameter.add( new PackageItem( 4, room.getId() ) );
				pack.parameter
						.add( new PackageItem( 4, room.getPeopleCount() ) );
				pack.parameter.add( new PackageItem( 4, session.getPlayer()
						.getCurrentGroup() ) );

				List< Player > list = room.getPlayerList();
				Iterator< Player > it = list.iterator();
				Player p;
				String uuid;
				String heroCardId;
				while ( it.hasNext() )
				{
					p = it.next();

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
			}
		}
	}

}
