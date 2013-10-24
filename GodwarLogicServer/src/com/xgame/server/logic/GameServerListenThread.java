package com.xgame.server.logic;

import java.net.DatagramPacket;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xgame.server.network.DatagramPacketQueue;
import com.xgame.server.network.GameServerConnector;
import com.xgame.server.pool.DatagramPacketPool;

public class GameServerListenThread implements Runnable
{
	private boolean				stop	= false;
	private static Log			log		= LogFactory
												.getLog( GameServerListenThread.class );

	@Override
	public void run()
	{
		GameServerConnector server = GameServerConnector.getInstance();

		DatagramPacket p = null;
		log.info( "GameServerListenerThread线程已启动，ThreadName = " + Thread.currentThread().getName() );
		while ( !stop )
		{
			p = DatagramPacketPool.getInstance().getObject();
			server.receive( p );
			DatagramPacketQueue.getInstance().push( p );
			log.info( "DatagramSocket收到数据，ip = " + p.getAddress().getHostAddress() + ", length = " + p.getLength() );
		}
	}

}
