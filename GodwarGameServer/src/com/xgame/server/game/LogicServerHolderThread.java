package com.xgame.server.game;

import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xgame.server.common.LogicServerInfo;
import com.xgame.server.common.protocol.EnumProtocol;
import com.xgame.server.network.DatagramPacketQueue;
import com.xgame.server.network.LogicServerConnector;
import com.xgame.server.pool.BufferPool;
import com.xgame.server.pool.DatagramPacketPool;

public class LogicServerHolderThread implements Runnable
{

	private boolean		stop	= false;
	private static Log	log		= LogFactory
										.getLog( LogicServerHolderThread.class );

	@Override
	public void run()
	{
		log.info( "LogicServerHolderThread线程已启动，ThreadName = "
				+ Thread.currentThread().getName() );
		while ( !stop )
		{
			DatagramPacket p = DatagramPacketQueue.getInstance().shift();
			if ( p != null )
			{
				ByteBuffer buffer = ByteBuffer.wrap( p.getData() );
				buffer.limit( p.getLength() );

				log.info( "线程捕获DatagramPacket, ip = "
						+ p.getAddress().getHostAddress() + ", length = "
						+ buffer.remaining() + ", ThreadName = "
						+ Thread.currentThread().getName() );

				short flag = buffer.getShort();
				if ( flag == EnumProtocol.BASE_REGISTER_LOGIC_SERVER )
				{
					registerLogicServer( buffer );

					DatagramPacket pack = DatagramPacketPool.getInstance()
							.getObject();
					pack.setSocketAddress( p.getSocketAddress() );
					ByteBuffer bf = BufferPool.getInstance().getBuffer();

					bf.putShort( EnumProtocol.BASE_REGISTER_LOGIC_SERVER_CONFIRM );
					bf.put( (byte) EnumProtocol.TYPE_INT );
					bf.putInt( 1 );

					bf.flip();

					byte[] dest = new byte[bf.remaining()];
					bf.get( dest, 0, dest.length );

					pack.setData( dest );

					LogicServerConnector.getInstance().send( pack );
				}
				else if ( flag == EnumProtocol.BASE_REQUEST_LOGIC_SERVER_ROOM_CONFIRM )
				{
					registerLogicServerRoomConfirm( buffer );
				}
			}

			try
			{
				Thread.sleep( 200 );
			}
			catch ( InterruptedException e )
			{
				e.printStackTrace();
			}
		}
	}

	private void registerLogicServer( ByteBuffer buffer )
	{
		int type = Integer.MIN_VALUE;
		int length = Integer.MIN_VALUE;

		String id = null;
		String ip = null;
		int port = 0;
		String udpIp = null;
		int udpPort = 0;
		while ( buffer.hasRemaining() )
		{
			type = buffer.get();
			if ( type == EnumProtocol.TYPE_STRING )
			{
				length = buffer.getInt();
				byte[] dst = new byte[length];
				buffer.get( dst );
				try
				{
					if ( id == null )
					{
						id = new String( dst, "UTF-8" );
						continue;
					}
					if ( ip == null )
					{
						ip = new String( dst, "UTF-8" );
						continue;
					}
					if ( udpIp == null )
					{
						udpIp = new String( dst, "UTF-8" );
						continue;
					}
				}
				catch ( UnsupportedEncodingException e )
				{
					e.printStackTrace();
				}
			}
			else if ( type == EnumProtocol.TYPE_INT )
			{
				if ( port == 0 )
				{
					port = buffer.getInt();
					continue;
				}
				if ( udpPort == 0 )
				{
					udpPort = buffer.getInt();
					continue;
				}
			}
		}
		LogicServerInfo info = new LogicServerInfo();
		info.id = id;
		info.ip = ip;
		info.port = port;
		info.add = new InetSocketAddress( udpIp, udpPort );
		info.load = 0;
		LogicServerManager.getInstance().addLogicServer( id, info );
		log.info( "LogicServer注册信息, id = " + id + ", ip = " + ip + ", port = "
				+ port + ", upd ip = " + udpIp + ", udp port = " + udpPort );
	}

	private void registerLogicServerRoomConfirm( ByteBuffer buffer )
	{
		int length = Integer.MIN_VALUE;
		int type = Integer.MIN_VALUE;

		int roomType = Integer.MIN_VALUE;
		int roomId = Integer.MIN_VALUE;

		while ( buffer.hasRemaining() )
		{
			length = buffer.getInt();
			type = buffer.get();
			if ( type == EnumProtocol.TYPE_INT )
			{
				if ( roomType == Integer.MIN_VALUE )
				{
					roomType = buffer.getInt();
				}
				else if ( roomId == Integer.MIN_VALUE )
				{
					roomId = buffer.getInt();
				}
			}
		}
		log.info( "LogicServer房间注册成功确认, room type = " + roomType + ", id = "
				+ roomId );
	}

	public void stop()
	{
		stop = true;
	}

}
