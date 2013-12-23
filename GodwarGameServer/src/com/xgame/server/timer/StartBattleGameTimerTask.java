package com.xgame.server.timer;

import java.net.DatagramPacket;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.List;
import java.util.TimerTask;
import java.util.Map.Entry;

import com.xgame.server.CommandCenter;
import com.xgame.server.common.LogicServerInfo;
import com.xgame.server.common.PackageItem;
import com.xgame.server.common.ServerPackage;
import com.xgame.server.common.protocol.EnumProtocol;
import com.xgame.server.game.BattleRoom;
import com.xgame.server.game.LogicServerManager;
import com.xgame.server.game.Player;
import com.xgame.server.game.Room;
import com.xgame.server.network.LogicServerConnector;
import com.xgame.server.pool.BufferPool;
import com.xgame.server.pool.DatagramPacketPool;
import com.xgame.server.pool.ServerPackagePool;

public class StartBattleGameTimerTask extends TimerTask
{
	public Room	room;

	public StartBattleGameTimerTask()
	{

	}

	@Override
	public void run()
	{
		if ( room != null )
		{
			responseStartGame();
		}
		cancel();
	}

	private void responseStartGame()
	{
		// 获取负载最低的LogicServer
		LogicServerInfo info = LogicServerManager.getInstance()
				.getLogicServer();
		// 通知LogicServer创建房间
		DatagramPacket pack = DatagramPacketPool.getInstance().getObject();
		pack.setSocketAddress( info.add );

		ByteBuffer bf = BufferPool.getInstance().getBuffer();
		String title = room.getTitle();
		int peopleCount = room.getPeopleCount();
		String ownerGuid = room.getOwner().getGuid().toString();
		Iterator< Entry< Player, String >> it = room.getHeroMap().entrySet()
				.iterator();
		Entry< Player, String > en;
		String heroCardId;

		bf.putShort( EnumProtocol.BASE_REQUEST_LOGIC_SERVER_ROOM );

		bf.putInt( 4 );
		bf.put( (byte) EnumProtocol.TYPE_INT );
		bf.putInt( room instanceof BattleRoom ? 0 : 1 );

		bf.putInt( 4 );
		bf.put( (byte) EnumProtocol.TYPE_INT );
		bf.putInt( room.getId() );

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
