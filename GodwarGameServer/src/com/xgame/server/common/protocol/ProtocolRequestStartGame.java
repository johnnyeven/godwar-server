package com.xgame.server.common.protocol;

import java.net.DatagramPacket;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xgame.server.common.LogicServerInfo;
import com.xgame.server.game.BattleHall;
import com.xgame.server.game.BattleRoom;
import com.xgame.server.game.LogicServerManager;
import com.xgame.server.game.ProtocolPackage;
import com.xgame.server.network.GameSession;
import com.xgame.server.network.LogicServerConnector;
import com.xgame.server.pool.BufferPool;
import com.xgame.server.pool.DatagramPacketPool;

public class ProtocolRequestStartGame implements IProtocol
{

	private static Log	log	= LogFactory
									.getLog( ProtocolRequestStartGame.class );

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
		log.info( "[RequestStartGame] Room Type = " + roomType + ", Room id = "
				+ id );

		int peopleCount = 0;
		long ownerId = 0;
		if ( roomType == 0 )
		{
			BattleRoom room = BattleHall.getInstance().getRoom( id );
			peopleCount = room.getPeopleCount();
			ownerId = room.getOwner().accountId;
		}

		// 获取负载最低的LogicServer
		LogicServerInfo info = LogicServerManager.getInstance()
				.getLogicServer();
		// 通知LogicServer创建房间
		DatagramPacket pack = DatagramPacketPool.getInstance().getObject();
		pack.setSocketAddress( info.add );
		ByteBuffer bf = BufferPool.getInstance().getBuffer();

		bf.putShort( EnumProtocol.BASE_REQUEST_LOGIC_SERVER_ROOM );
		bf.put( (byte) EnumProtocol.TYPE_INT );
		bf.putInt( roomType );
		bf.put( (byte) EnumProtocol.TYPE_INT );
		bf.putInt( id );
		bf.put( (byte) EnumProtocol.TYPE_INT );
		bf.putInt( peopleCount );
		bf.put( (byte) EnumProtocol.TYPE_LONG );
		bf.putLong( ownerId );

		bf.flip();

		byte[] dest = new byte[bf.remaining()];
		bf.get( dest, 0, dest.length );

		pack.setData( dest );

		LogicServerConnector.getInstance().send( pack );
	}

}
