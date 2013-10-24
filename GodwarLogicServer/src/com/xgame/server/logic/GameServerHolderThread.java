package com.xgame.server.logic;

import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.nio.ByteBuffer;
import java.util.TimerTask;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xgame.server.common.protocol.EnumProtocol;
import com.xgame.server.network.DatagramPacketQueue;
import com.xgame.server.timer.TimerManager;

public class GameServerHolderThread implements Runnable
{

	private boolean		stop	= false;
	private static Log	log		= LogFactory
										.getLog( GameServerHolderThread.class );

	@Override
	public void run()
	{
		log.info( "GameServerHolderThread线程已启动，ThreadName = "
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
				if ( flag == EnumProtocol.BASE_REGISTER_LOGIC_SERVER_CONFIRM )
				{
					cancelConnectorInitialization( buffer );
				}
				else if ( flag == EnumProtocol.BASE_REQUEST_LOGIC_SERVER_ROOM )
				{
					requestLogicServerRoom( buffer );
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

	private void cancelConnectorInitialization( ByteBuffer buffer )
	{
		TimerTask t = TimerManager.getInstance().getTask(
				"GameServerConnectorInitilization" );
		t.cancel();

		log.info( "注册信息对方已确认收到，取消持续发送，改为发送心跳包" );
	}

	private void requestLogicServerRoom( ByteBuffer buffer )
	{
		int type = Integer.MIN_VALUE;
		int length = Integer.MIN_VALUE;

		int roomType = Integer.MIN_VALUE;
		int roomId = Integer.MIN_VALUE;
		int peopleCount = Integer.MIN_VALUE;
		long ownerId = Long.MIN_VALUE;
		while ( buffer.hasRemaining() )
		{
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
				else if ( peopleCount == Integer.MIN_VALUE )
				{
					peopleCount = buffer.getInt();
				}
			}
			else if ( type == EnumProtocol.TYPE_LONG )
			{
				ownerId = buffer.getLong();
			}
		}

		if ( roomType == 0 )
		{
			if ( BattleHall.getInstance().addRoom( roomId ) )
			{
				log.info( "[RequestRoom] 房间创建成功，等待客户端连接, room id = " + roomId );
				// TODO 通知GameServer创建房间成功
			}
			else
			{
				log.error( "[RequestRoom] 创建房间失败, room id = " + roomId );
				// TODO 通知GameServer创建房间失败
			}
		}
	}

}
