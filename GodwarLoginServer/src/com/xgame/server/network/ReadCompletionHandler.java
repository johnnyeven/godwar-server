package com.xgame.server.network;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.CompletionHandler;
import java.nio.charset.Charset;

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
		if ( result == 22 )
		{
			attachment.buffer.flip();
			byte[] dst = new byte[22];
			attachment.buffer.get( dst );
			try
			{
				String policy = new String( dst, "UTF-8" );
				if ( policy == "<policy-file-request/>" )
				{
					log.info( "捕获策略文件请求" );
					String policyFile = "<?xml version=\"1.0\"?><cross-domain-policy><site-control permitted-cross-domain-policies=\"all\"/><allow-access-from domain=\"*\" to-ports=\"9999\" /></cross-domain-policy>";
					byte[] strBytes = policyFile.getBytes( Charset
							.forName( "UTF-8" ) );
					ByteBuffer buffer = BufferPool.getInstance().getBuffer();
					buffer.put( strBytes, 0, strBytes.length );

					attachment.channel.write( buffer );
				}
			}
			catch ( UnsupportedEncodingException e )
			{
				e.printStackTrace();
			}
		}
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
