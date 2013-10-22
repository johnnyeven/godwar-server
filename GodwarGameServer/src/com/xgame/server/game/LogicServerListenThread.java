package com.xgame.server.game;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xgame.server.network.DatagramPacketQueue;
import com.xgame.server.pool.DatagramPacketPool;
import com.xgame.server.pool.PlayerPool;

public class LogicServerListenThread implements Runnable
{
	private static final String	HOST	= "127.0.0.1";
	private static final int	PORT	= 9051;
	private boolean				stop	= false;
	private static Log			log		= LogFactory
												.getLog( LogicServerListenThread.class );

	@Override
	public void run()
	{
		try
		{
			DatagramSocket server = new DatagramSocket( new InetSocketAddress(
					HOST, PORT ) );

			DatagramPacket p = DatagramPacketPool.getInstance().getObject();
			log.info( "LogicServerListenThread线程已启动，ThreadName = " + Thread.currentThread().getName() );
			while ( !stop )
			{
				try
				{
					server.receive( p );
					DatagramPacketQueue.getInstance().push( p );
				}
				catch ( IOException e )
				{
					e.printStackTrace();
				}
			}
		}
		catch ( SocketException e )
		{
			e.printStackTrace();
		}
	}

}
