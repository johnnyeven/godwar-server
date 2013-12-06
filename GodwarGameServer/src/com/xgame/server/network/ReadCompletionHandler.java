package com.xgame.server.network;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.CompletionHandler;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xgame.server.game.ProtocolPackage;
import com.xgame.server.pool.BufferPool;

public class ReadCompletionHandler implements
		CompletionHandler< Integer, GameSession >
{
	private static Log	log	= LogFactory.getLog( ReadCompletionHandler.class );

	public ReadCompletionHandler()
	{
	}

	@Override
	public void completed( Integer result, GameSession attachment )
	{
		if ( result > 0 )
		{
			// 处理buffer
			ByteBuffer buffer = attachment.getReadBuffer();
			buffer.flip();
			buffer.getInt();
			short protocolId = buffer.getShort();

			ProtocolPackage parameter = new ProtocolPackage();
			parameter.protocolId = protocolId;
			parameter.client = attachment.getChannel();
			parameter.receiveDataLength = result;
			parameter.receiveData = buffer.duplicate();
			parameter.offset = 6;

			attachment.addParameterQueue( parameter );

			BufferPool.getInstance().releaseBuffer( buffer );
			attachment.startRecv();
		}
		else
		{
			try
			{
				log.info( "断开连接 IP=" + attachment.getChannel().getRemoteAddress().toString() + ", Player=" + attachment.getPlayer().name );
			}
			catch ( IOException e )
			{
				e.printStackTrace();
			}
			finally
			{
				attachment.dispose();
			}
		}
	}

	@Override
	public void failed( Throwable exc, GameSession attachment )
	{
		try
		{
			log.info( "Socket错误读取数据失败 IP=" + attachment.getChannel().getRemoteAddress().toString() + ", Player=" + attachment.getPlayer().name );
		}
		catch ( IOException e )
		{
			e.printStackTrace();
		}
		finally
		{
			attachment.dispose();
		}
	}

}
