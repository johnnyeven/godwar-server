package com.xgame.server.common.protocol;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xgame.server.CommandCenter;
import com.xgame.server.common.PackageItem;
import com.xgame.server.common.ServerPackage;
import com.xgame.server.game.BattleHall;
import com.xgame.server.game.BattleRoom;
import com.xgame.server.game.ProtocolPackage;
import com.xgame.server.network.GameSession;
import com.xgame.server.pool.ServerPackagePool;

class ProtocolRequestEnterRoom implements IProtocol
{

	private static Log	log	= LogFactory
									.getLog( ProtocolRequestEnterRoom.class );

	@Override
	public void Execute( Object param1, Object param2 )
	{
		ProtocolPackage parameter = (ProtocolPackage) param1;
		GameSession session = (GameSession) param2;

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
						break;
					}
					if ( id == Integer.MIN_VALUE )
					{
						id = parameter.receiveData.getInt();
						break;
					}
			}
			i += ( length + 5 );
		}
		log.info( "[RequestEnterRoom] Room Type = " + roomType + ", Room id = "
				+ id + ", Player = " + session.getPlayer().name );

		BattleRoom room = BattleHall.getInstance().getRoom( id );
		if ( room != null )
		{
			if(room.getPeopleCount() >= room.getPeopleLimit())
			{
				log.info( "[RequestEnterRoom] 房间已满员" );

				ServerPackage pack = ServerPackagePool.getInstance().getObject();
				pack.success = EnumProtocol.ACK_CONFIRM;
				pack.protocolId = EnumProtocol.HALL_REQUEST_ENTER_ROOM;
				pack.parameter.add( new PackageItem( 4, -1 ) );
				CommandCenter.send( parameter.client, pack );
			}
			else
			{
				room.addPlayer( session.getPlayer() );
			}
		}
	}

}
