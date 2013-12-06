package com.xgame.server.network;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.CompletionHandler;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xgame.server.common.AuthSessionPackage;
import com.xgame.server.common.protocol.ProtocolRouter;
import com.xgame.server.login.ProtocolParam;
import com.xgame.server.pool.BufferPool;

public class ReadCompletionHandler implements
		CompletionHandler< Integer, AuthSessionPackage >
{
	private static Log	log	= LogFactory.getLog( ReadCompletionHandler.class );
	
	public ReadCompletionHandler()
	{
	}

	@Override
	public void completed( Integer result, AuthSessionPackage attachment )
	{
		if ( result > 0 )
		{
			attachment.buffer.flip();
			attachment.buffer.getInt();
			short protocolId = attachment.buffer.getShort();

			ProtocolParam parameter = new ProtocolParam();
			parameter.client = attachment.channel;
			parameter.receiveDataLength = result;
			parameter.receiveData = attachment.buffer;
			parameter.offset = 6;
			ProtocolRouter.getInstance().Trigger( protocolId, parameter );

			BufferPool.getInstance().releaseBuffer( attachment.buffer );

			ByteBuffer readBuffer = BufferPool.getInstance().getBuffer();
			attachment.channel.read( readBuffer, new AuthSessionPackage(
					readBuffer, attachment.channel ), AIOSocketMgr
					.getInstance().getReadHandler() );
		}
		else
		{
			try
			{
				log.info( "¶Ï¿ªÁ¬½Ó IP=" + attachment.channel.getRemoteAddress().toString() );
			}
			catch ( IOException e )
			{
				e.printStackTrace();
			}
			finally
			{
				BufferPool.getInstance().releaseBuffer( attachment.buffer );
			}
		}
	}

	@Override
	public void failed( Throwable exc, AuthSessionPackage attachment )
	{
		try
		{
			attachment.channel.close();
		}
		catch ( IOException e )
		{
			e.printStackTrace();
		}
		finally
		{
			BufferPool.getInstance().releaseBuffer( attachment.buffer );
		}
	}

}
