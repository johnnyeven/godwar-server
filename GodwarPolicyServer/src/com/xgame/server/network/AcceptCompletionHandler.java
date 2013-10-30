package com.xgame.server.network;

import java.io.IOException;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xgame.server.common.AuthSessionPackage;
import com.xgame.server.pool.BufferPool;

public class AcceptCompletionHandler implements
		CompletionHandler< AsynchronousSocketChannel, AIOSocketMgr >
{
	private static Log	log	= LogFactory.getLog( AcceptCompletionHandler.class );

	@Override
	public void completed( AsynchronousSocketChannel socketChannel,
			AIOSocketMgr param )
	{
		try
		{
			log.info( "接受远程主机连接，IP: "
					+ socketChannel.getRemoteAddress().toString() );
			socketChannel.setOption( StandardSocketOptions.TCP_NODELAY, true );
			socketChannel
					.setOption( StandardSocketOptions.SO_SNDBUF, 10 * 1024 );
			socketChannel
					.setOption( StandardSocketOptions.SO_RCVBUF, 10 * 1024 );

			ByteBuffer buffer = BufferPool.getInstance().getBuffer();

			socketChannel.read( buffer, new AuthSessionPackage(buffer, socketChannel) , param.getReadHandler() );
		}
		catch ( IOException e )
		{
			e.printStackTrace();
		}
		finally
		{
			param.getServer().accept( param, this );
		}
	}

	@Override
	public void failed( Throwable arg0, AIOSocketMgr arg1 )
	{
		log.error( arg0.getMessage() );
	}

}
