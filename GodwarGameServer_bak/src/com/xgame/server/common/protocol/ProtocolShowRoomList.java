package com.xgame.server.common.protocol;

import java.util.ArrayList;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xgame.server.CommandCenter;
import com.xgame.server.common.PackageItem;
import com.xgame.server.common.ServerPackage;
import com.xgame.server.game.BattleHall;
import com.xgame.server.game.BattleRoom;
import com.xgame.server.game.ProtocolPackage;
import com.xgame.server.network.GameSession;
import com.xgame.server.pool.BattleRoomPool;
import com.xgame.server.pool.ServerPackagePool;

/*
 * type
 * 0 = BattleRoom
 * 1 = MeleeRoom
 */

public class ProtocolShowRoomList implements IProtocol
{

	private static Log	log	= LogFactory.getLog( ProtocolShowRoomList.class );

	@Override
	public void Execute( Object param1, Object param2 )
	{
		ProtocolPackage parameter = (ProtocolPackage) param1;
		GameSession session = (GameSession) param2;

		int roomType = Integer.MIN_VALUE;
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
					break;
			}
			i += ( length + 5 );
		}
		log.info( "[ShowRoomList] Room Type = " + roomType );

		if ( roomType == 0 )
		{
			ArrayList< BattleRoom > roomList = BattleHall.getInstance()
					.getRooms();

			ServerPackage pack = ServerPackagePool.getInstance().getObject();
			pack.success = EnumProtocol.ACK_CONFIRM;
			pack.protocolId = EnumProtocol.HALL_SHOW_ROOM_LIST;

			Iterator< BattleRoom > it = roomList.iterator();
			BattleRoom room;
			while ( it.hasNext() )
			{
				room = it.next();
				if ( room != null )
				{
					pack.parameter.add( new PackageItem( 4, room.getId() ) );
					pack.parameter.add( new PackageItem( room.getTitle()
							.length(), room.getTitle() ) );
					pack.parameter.add( new PackageItem( room.getOwner().name
							.length(), room.getOwner().name ) );
					pack.parameter.add( new PackageItem( 4, room
							.getPeopleCount() ) );
					pack.parameter.add( new PackageItem( 4, room
							.getPeopleLimit() ) );
					// pack.parameter.add( new PackageItem( 4, room.getStatus()
					// ) );
				}
			}
			CommandCenter.send( parameter.client, pack );
		}
		else if ( roomType == 1 )
		{

		}
		else
		{
			log.fatal( "Room Type ´íÎó£¬roomType = " + roomType );
			return;
		}

	}

}
