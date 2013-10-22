
package com.xgame.server.game;

import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.nio.ByteBuffer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xgame.server.common.protocol.EnumProtocol;
import com.xgame.server.network.DatagramPacketQueue;

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

				short flag = buffer.getShort();
				if ( flag == EnumProtocol.BASE_REGISTER_LOGIC_SERVER )
				{
					int type = Integer.MIN_VALUE;
					int length = Integer.MIN_VALUE;

					String ip = null;
					int port = 0;
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
								ip = new String( dst, "UTF-8" );
							}
							catch ( UnsupportedEncodingException e )
							{
								e.printStackTrace();
							}
						}
						else if ( type == EnumProtocol.TYPE_INT )
						{
							port = buffer.getInt();
						}
					}
				}
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
