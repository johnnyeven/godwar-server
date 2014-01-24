package com.xgame.server;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xgame.server.common.PackageItem;
import com.xgame.server.common.ServerPackage;
import com.xgame.server.common.protocol.EnumProtocol;
import com.xgame.server.pool.BufferPool;
import com.xgame.server.pool.ServerPackagePool;

public class CommandCenter
{
	private static Log	log	= LogFactory.getLog( CommandCenter.class );

	public static void send( AsynchronousSocketChannel channel,
			ServerPackage pack )
	{
		int dataLength = 0;
		ByteBuffer buffer = BufferPool.getInstance().getBuffer();
		buffer.position( 4 );

		buffer.putShort( pack.protocolId );
		buffer.put( (byte) ( pack.success ) );
		dataLength += 3;

		String strVal;
		Integer intVal;
		Long longVal;
		Double doubleVal;
		Float floatVal;
		byte byteVal;
		for ( int i = 0; i < pack.parameter.size(); i++ )
		{
			PackageItem item = pack.parameter.get( i );
			if ( item.item instanceof String )
			{
				byte[] str = ( (String) item.item ).getBytes( Charset
						.forName( "UTF-8" ) );
				buffer.putInt( str.length );
			}
			else
			{
				buffer.putInt( item.length );
			}
			dataLength += 4;

			if ( item.item instanceof String )
			{
				buffer.put( (byte) EnumProtocol.TYPE_STRING );
				dataLength += 1;

				strVal = (String) item.item;
				byte[] strBytes = strVal.getBytes( Charset.forName( "UTF-8" ) );
				buffer.put( strBytes, 0, strBytes.length );
				dataLength += strBytes.length;
			}
			else if ( item.item instanceof Integer )
			{
				buffer.put( (byte) EnumProtocol.TYPE_INT );
				dataLength += 1;

				intVal = (int) item.item;
				buffer.putInt( intVal );
				dataLength += 4;
			}
			else if ( item.item instanceof Long )
			{
				buffer.put( (byte) EnumProtocol.TYPE_LONG );
				dataLength += 1;

				longVal = (long) item.item;
				buffer.putLong( longVal );
				dataLength += 8;
			}
			else if ( item.item instanceof Double )
			{
				buffer.put( (byte) EnumProtocol.TYPE_DOUBLE );
				dataLength += 1;

				doubleVal = (double) item.item;
				buffer.putDouble( doubleVal );
				dataLength += 8;
			}
			else if ( item.item instanceof Float )
			{
				buffer.put( (byte) EnumProtocol.TYPE_FLOAT );
				dataLength += 1;

				floatVal = (float) item.item;
				buffer.putFloat( floatVal );
				dataLength += 4;
			}
			else if ( item.item instanceof Boolean )
			{
				buffer.put( (byte) EnumProtocol.TYPE_BOOL );
				dataLength += 1;

				byteVal = (byte) ( (boolean) item.item ? 0x1 : 0x0 );
				buffer.put( byteVal );
				dataLength += 1;
			}
		}
		long timestamp = new Date().getTime();
		buffer.putLong( timestamp );
		dataLength += 8;

		buffer.putInt( 0, dataLength );
		dataLength += 4;
		buffer.flip();
		buffer.limit( dataLength );

		Future< Integer > f = channel.write( buffer );

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
			ServerPackagePool.getInstance().returnObject( pack );
		}
	}
}
