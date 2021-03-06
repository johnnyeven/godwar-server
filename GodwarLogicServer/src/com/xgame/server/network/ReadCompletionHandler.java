package com.xgame.server.network;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.CompletionHandler;

import com.xgame.server.logic.ProtocolPackage;
import com.xgame.server.pool.BufferPool;

public class ReadCompletionHandler implements
		CompletionHandler< Integer, GameSession >
{

	public ReadCompletionHandler()
	{
	}

	@Override
	public void completed( Integer result, GameSession attachment )
	{
		if ( result > 0 )
		{
			// ����buffer
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
				attachment.getChannel().close();
			}
			catch ( IOException e )
			{
				e.printStackTrace();
			}
			finally
			{
				BufferPool.getInstance().releaseBuffer(
						attachment.getReadBuffer() );
			}
		}
	}

	@Override
	public void failed( Throwable exc, GameSession attachment )
	{
		BufferPool.getInstance().releaseBuffer( attachment.getReadBuffer() );
	}

}
