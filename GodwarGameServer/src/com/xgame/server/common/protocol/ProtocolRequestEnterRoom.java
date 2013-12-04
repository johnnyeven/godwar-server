package com.xgame.server.common.protocol;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xgame.server.CommandCenter;
import com.xgame.server.common.PackageItem;
import com.xgame.server.common.ServerPackage;
import com.xgame.server.game.BattleHall;
import com.xgame.server.game.BattleRoom;
import com.xgame.server.game.Player;
import com.xgame.server.game.ProtocolPackage;
import com.xgame.server.network.GameSession;
import com.xgame.server.pool.ServerPackagePool;

public class ProtocolRequestEnterRoom implements IProtocol
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
			if ( room.getPeopleCount() >= room.getPeopleLimit() )
			{
				log.info( "[RequestEnterRoom] 房间已满员" );

				ServerPackage pack = ServerPackagePool.getInstance()
						.getObject();
				pack.success = EnumProtocol.ACK_CONFIRM;
				pack.protocolId = EnumProtocol.HALL_REQUEST_ENTER_ROOM;
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
				pack.protocolId = EnumProtocol.BATTLEROOM_INIT_ROOM;
				pack.parameter.add( new PackageItem( 4, room.getId() ) );
				pack.parameter.add( new PackageItem( room.getTitle().length(),
						room.getTitle() ) );
				String uuid = room.getOwner().getGuid().toString();
				pack.parameter.add( new PackageItem( uuid.length(), uuid ) );
				pack.parameter.add( new PackageItem( room.getOwner().name
						.length(), room.getOwner().name ) );
				pack.parameter
						.add( new PackageItem( 4, room.getPeopleCount() ) );
				pack.parameter
						.add( new PackageItem( 4, room.getPeopleLimit() ) );

				List< Player > list = room.getPlayerList();
				HashMap< Player, Boolean > statusMap = (HashMap< Player, Boolean >) room
						.getStatusMap();
				Iterator< Player > it = list.iterator();
				Player p;
				int status;
				while ( it.hasNext() )
				{
					p = it.next();
					if ( statusMap.containsKey( p ) )
					{
						status = statusMap.get( p ) ? 1 : 0;
					}
					else
					{
						status = 0;
					}
					uuid = p.getGuid().toString();
					pack.parameter.add( new PackageItem( uuid.length(), uuid ) );
					pack.parameter.add( new PackageItem( 8, p.accountId ) );
					pack.parameter.add( new PackageItem( p.name.length(),
							p.name ) );
					pack.parameter.add( new PackageItem( 4, p.level ) );
					pack.parameter.add( new PackageItem( p.rolePicture.length(), p.rolePicture ) );
					pack.parameter.add( new PackageItem( 8, p.accountCash ) );
					pack.parameter.add( new PackageItem( 4, p.winningCount ) );
					pack.parameter.add( new PackageItem( 4, p.battleCount ) );
					pack.parameter.add( new PackageItem( 4, p.honor ) );
					pack.parameter.add( new PackageItem( 4, status ) );
				}
				CommandCenter.send( parameter.client, pack );
			}
		}
	}

}
