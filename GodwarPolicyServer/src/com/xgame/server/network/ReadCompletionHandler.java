package com.xgame.server.network;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.CompletionHandler;
import java.nio.charset.Charset;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xgame.server.common.AuthSessionPackage;
import com.xgame.server.pool.BufferPool;
import com.xgame.server.pool.ServerPackagePool;

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

			byte[] dst = new byte[22];
			attachment.buffer.get( dst );
			String request = new String( dst );
			log.info( request );

			String policyFile = "<?xml version=\"1.0\"?><cross-domain-policy>"
					+ "<allow-access-from domain=\"*\" to-ports=\"9000-9999\"/>"
					+ "</cross-domain-policy>\0";
			byte[] strBytes = policyFile.getBytes();
			ByteBuffer buffer = BufferPool.getInstance().getBuffer();
			buffer.put( strBytes, 0, strBytes.length );
			buffer.flip();
			log.info( "写入策略文件: " + policyFile );
			
			Future< Integer > f = attachment.channel.write( buffer );

			try
			{
				int length = f.get();
				BufferPool.getInstance().releaseBuffer( buffer );
				log.debug( "send() Length=" + length );
			}
			catch ( InterruptedException | ExecutionException e )
			{
				e.printStackTrace();
			}
			finally
			{
				try
				{
					attachment.channel.close();
				}
				catch ( IOException e )
				{
					e.printStackTrace();
				}
			}
		}
		else
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

	@Override
	public void failed( Throwable exc, AuthSessionPackage attachment )
	{
		BufferPool.getInstance().releaseBuffer( attachment.buffer );
	}

}
