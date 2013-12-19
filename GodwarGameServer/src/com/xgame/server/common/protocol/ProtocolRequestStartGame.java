package com.xgame.server.common.protocol;

import java.net.DatagramPacket;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xgame.server.CommandCenter;
import com.xgame.server.common.LogicServerInfo;
import com.xgame.server.common.PackageItem;
import com.xgame.server.common.ServerPackage;
import com.xgame.server.game.BattleHall;
import com.xgame.server.game.BattleRoom;
import com.xgame.server.game.LogicServerManager;
import com.xgame.server.game.Player;
import com.xgame.server.game.ProtocolPackage;
import com.xgame.server.game.Room;
import com.xgame.server.network.GameSession;
import com.xgame.server.network.LogicServerConnector;
import com.xgame.server.pool.BufferPool;
import com.xgame.server.pool.DatagramPacketPool;
import com.xgame.server.pool.ServerPackagePool;

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

		// 获取负载最低的LogicServer
		LogicServerInfo info = LogicServerManager.getInstance()
				.getLogicServer();
		// 通知LogicServer创建房间
		DatagramPacket pack = DatagramPacketPool.getInstance().getObject();
		pack.setSocketAddress( info.add );

		String title = "";
		int peopleCount = 0;
		String ownerGuid = "";
		Iterator< Entry< Player, String >> it;
		Entry< Player, String > en;
		String heroCardId;
		ByteBuffer bf = BufferPool.getInstance().getBuffer();
		if ( roomType == 0 )
		{
			BattleRoom room = BattleHall.getInstance().getRoom( id );
			title = room.getTitle();
			peopleCount = room.getPeopleCount();
			ownerGuid = room.getOwner().getGuid().toString();
			it = room.getHeroMap().entrySet().iterator();

			bf.putShort( EnumProtocol.BASE_REQUEST_LOGIC_SERVER_ROOM );

			bf.putInt( 4 );
			bf.put( (byte) EnumProtocol.TYPE_INT );
			bf.putInt( roomType );

			bf.putInt( 4 );
			bf.put( (byte) EnumProtocol.TYPE_INT );
			bf.putInt( id );

			byte[] src = title.getBytes( Charset.forName( "UTF-8" ) );
			bf.putInt( src.length );
			bf.put( (byte) EnumProtocol.TYPE_STRING );
			bf.put( src );

			bf.putInt( 4 );
			bf.put( (byte) EnumProtocol.TYPE_INT );
			bf.putInt( peopleCount );

			src = ownerGuid.getBytes( Charset.forName( "UTF-8" ) );
			bf.putInt( src.length );
			bf.put( (byte) EnumProtocol.TYPE_STRING );
			bf.put( src );

			while ( it.hasNext() )
			{
				en = it.next();
				heroCardId = en.getValue();

				src = en.getKey().getGuid().toString()
						.getBytes( Charset.forName( "UTF-8" ) );
				bf.putInt( src.length );
				bf.put( (byte) EnumProtocol.TYPE_STRING );
				bf.put( src );

				bf.putInt( 4 );
				bf.put( (byte) EnumProtocol.TYPE_INT );
				bf.putInt( en.getKey().getCurrentGroup() );

				src = heroCardId.getBytes( Charset.forName( "UTF-8" ) );
				bf.putInt( src.length );
				bf.put( (byte) EnumProtocol.TYPE_STRING );
				bf.put( src );
			}

			noticePlayerLogicServer( room, info.ip, info.port );
		}
		bf.flip();

		byte[] dest = new byte[bf.remaining()];
		bf.get( dest, 0, dest.length );

		pack.setData( dest );

		LogicServerConnector.getInstance().send( pack );
	}

	private void noticePlayerLogicServer( Room room, String ip, int port )
	{
		List< Player > list = room.getPlayerList();
		Player p;
		ServerPackage pack;

		for ( int i = 0; i < list.size(); i++ )
		{
			p = list.get( i );

			pack = ServerPackagePool.getInstance().getObject();
			pack.success = EnumProtocol.ACK_CONFIRM;
			pack.protocolId = EnumProtocol.BASE_LOGIC_SERVER_INFO;
			pack.parameter.add( new PackageItem( ip.length(), ip ) );
			pack.parameter.add( new PackageItem( 4, port ) );
			CommandCenter.send( p.getChannel(), pack );
		}
	}
}
