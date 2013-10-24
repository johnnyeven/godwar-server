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
	private DatagramSocket		socket;
	private String				id;
	private String				ip;
	private int					port;
	private static final String	HOST	= "127.0.0.1";
	private static final int	PORT	= 9061;

	private GameServerConnector()
	{
		try
		{
			socket = new DatagramSocket( new InetSocketAddress( HOST, PORT ) );
		}
		catch ( SocketException e )
		{
			e.printStackTrace();
		}
	}

	public void initialize( InetSocketAddress add )
	{
		DatagramPacket p = DatagramPacketPool.getInstance().getObject();
		p.setSocketAddress( add );

		ByteBuffer bf = BufferPool.getInstance().getBuffer();

		bf.putShort( EnumProtocol.BASE_REGISTER_LOGIC_SERVER );

		bf.put( (byte) EnumProtocol.TYPE_STRING );
		byte[] str = id.getBytes( Charset.forName( "UTF-8" ) );
		bf.putInt( str.length );
		bf.put( str );

		bf.put( (byte) EnumProtocol.TYPE_STRING );
		str = ip.getBytes( Charset.forName( "UTF-8" ) );
		bf.putInt( str.length );
		bf.put( str );

		bf.put( (byte) EnumProtocol.TYPE_INT );
		bf.putInt( port );

		bf.put( (byte) EnumProtocol.TYPE_STRING );
		str = HOST.getBytes( Charset.forName( "UTF-8" ) );
		bf.putInt( str.length );
		bf.put( str );

		bf.put( (byte) EnumProtocol.TYPE_INT );
		bf.putInt( PORT );

		bf.flip();

		byte[] dest = new byte[bf.remaining()];
		bf.get( dest, 0, dest.length );

		p.setData( dest );

		send( p );
	}

	public void receive( DatagramPacket p )
	{
		try
		{
			socket.receive( p );
		}
		catch ( IOException e )
		{
			e.printStackTrace();
		}
	}

	public void send( DatagramPacket p )
	{
		try
		{
			socket.send( p );
		}
		catch ( IOException e )
		{
			e.printStackTrace();
		}
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

	public String getId()
	{
		return id;
	}

	public void setId( String id )
	{
		this.id = id;
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
