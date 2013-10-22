package com.xgame.server.game;

import java.net.DatagramPacket;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xgame.server.network.DatagramPacketQueue;

public class LogicServerHolderThread implements Runnable
{
	private boolean		stop	= false;
	private static Log	log		= LogFactory
										.getLog( LogicServerHolderThread.class );

	@Override
	public void run()
	{
		log.info( "LogicServerHolderThread线程已启动，ThreadName = " + Thread.currentThread().getName() );
		while ( !stop )
		{
			DatagramPacket p = DatagramPacketQueue.getInstance().shift();
			if ( p != null )
			{

			}
			else
			{
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
	}

	public void stop()
	{
		stop = true;
	}

}
