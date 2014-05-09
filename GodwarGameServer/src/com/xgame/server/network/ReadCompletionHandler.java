package com.xgame.server.network;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.CompletionHandler;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xgame.server.game.ProtocolPackage;
import com.xgame.server.pool.BufferPool;

public class ReadCompletionHandler implements
		CompletionHandler< Integer, WorldSession >
{
	private static Log	log	= LogFactory.getLog( ReadCompletionHandler.class );

	public ReadCompletionHandler()
	{
	}

	@Override
	public void completed( Integer result, WorldSession attachment )
	{
		if ( result > 0 )
		{
			// ����buffer
			ByteBuffer buffer = attachment.getReadBuffer();
			buffer.flip();
			buffer.getInt();
			int protocolId = buffer.getInt();

			ProtocolPackage parameter = new ProtocolPackage();
			parameter.protocolId = protocolId;
			parameter.client = attachment.getChannel();
			parameter.receiveDataLength = result;
			parameter.receiveData = buffer.duplicate();
			parameter.offset = 8;

			attachment.addParameterQueue( parameter );

			BufferPool.getInstance().releaseBuffer( buffer );
			attachment.startRecv();
		}
		else
		{
			try
			{
				log.info( "disconnected from client, IP="
						+ attachment.getChannel().getRemoteAddress().toString()
						+ ", Player=" + attachment.getPlayer().name );
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
	public void failed( Throwable exc, WorldSession attachment )
	{
		try
		{
			log.info( "disconnected from client with error, IP="
					+ attachment.getChannel().getRemoteAddress().toString()
					+ ", Player=" + attachment.getPlayer().name );
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
