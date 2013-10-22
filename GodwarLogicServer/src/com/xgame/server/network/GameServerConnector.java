package com.xgame.server.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import com.xgame.server.common.protocol.EnumProtocol;
import com.xgame.server.pool.BufferPool;
import com.xgame.server.pool.DatagramPacketPool;

public class GameServerConnector
{
	private DatagramSocket	socket;
	private String			ip;
	private int				port;

	private GameServerConnector()
	{

	}

	public void initialize( InetSocketAddress add ) throws IOException
	{
		socket = new DatagramSocket( add );

		DatagramPacket p = DatagramPacketPool.getInstance().getObject();
		p.setSocketAddress( add );

		ByteBuffer bf = BufferPool.getInstance().getBuffer();

		bf.put( (byte) EnumProtocol.TYPE_STRING );
		byte[] str = ip.getBytes( Charset.forName( "UTF-8" ) );
		bf.putInt( str.length );
		bf.put( str );

		bf.put( (byte) EnumProtocol.TYPE_INT );
		bf.putInt( port );

		bf.flip();

		p.setData( bf.array() );

		socket.send( p );
	}

	public String getIp()
	{
		return ip;
	}

	public void setIp( String ip )
	{
		this.ip = ip;
	}

	public int getPort()
	{
		return port;
	}

	public void setPort( int port )
	{
		this.port = port;
	}

	public static GameServerConnector getInstance()
	{
		return GameServerConnectorHolder.instance;
	}

	private static class GameServerConnectorHolder
	{
		private static GameServerConnector	instance	= new GameServerConnector();
	}

}
