package com.xgame.server;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.charset.Charset;
import java.util.Date;

import com.xgame.server.common.PackageItem;
import com.xgame.server.common.ServerPackage;
import com.xgame.server.common.protocol.EnumProtocol;

public class CommandCenter
{

	public static void send(AsynchronousSocketChannel channel, ServerPackage pack)
	{
		int dataLength = 0;
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		buffer.position(4);
		
		buffer.putShort(pack.protocolId);
		buffer.put((byte)(pack.success));
		dataLength += 3;
		
		String strVal;
		Integer intVal;
		Long longVal;
		for(int i = 0; i < pack.parameter.size(); i++)
		{
			PackageItem item = pack.parameter.get(i);
			buffer.putInt(item.length);
			dataLength += 4;
			
			if(item.item instanceof String)
			{
				buffer.put((byte)EnumProtocol.TYPE_STRING);
				dataLength += 1;
				
				strVal = (String)item.item;
				byte[] strBytes = strVal.getBytes(Charset.forName("UTF-8"));
				buffer.put(strBytes, 0, strBytes.length);
				dataLength += strVal.length();
			}
			else if(item.item instanceof Integer)
			{
				buffer.put((byte)EnumProtocol.TYPE_INT);
				dataLength += 1;
				
				intVal = (int)item.item;
				buffer.putInt(intVal);
				dataLength += 4;
			}
			else if(item.item instanceof Long)
			{
				buffer.put((byte)EnumProtocol.TYPE_LONG);
				dataLength += 1;
				
				longVal = (long)item.item;
				buffer.putLong(longVal);
				dataLength += 8;
			}
		}
		long timestamp = new Date().getTime();
		buffer.putLong( timestamp );
		dataLength += 8;
		
		buffer.putInt(0, dataLength);
		dataLength += 4;
		buffer.flip();
		buffer.limit(dataLength);
		
		channel.write(buffer);
	}

}
