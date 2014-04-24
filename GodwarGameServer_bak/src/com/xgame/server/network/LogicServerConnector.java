package com.xgame.server.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

public class LogicServerConnector
{
	private DatagramSocket		server;
	private static final String	HOST	= "127.0.0.1";
	private static final int	PORT	= 9051;

	public LogicServerConnector()
	{
		try
		{
			server = new DatagramSocket( new InetSocketAddress( HOST, PORT ) );
		}
		catch ( SocketException e )
		{
			e.printStackTrace();
		}
	}
	
	public void receive(DatagramPacket p)
	{
		try
		{
			server.receive( p );
		}
		catch ( IOException e )
		{
			e.printStackTrace();
		}
	}
	
	public void send(DatagramPacket p)
	{
		try
		{
			server.send( p );
		}
		catch ( IOException e )
		{
			e.printStackTrace();
		}
	}

	public static LogicServerConnector getInstance()
	{
		return LogicServerConnectorHolder.instance;
	}

	private static class LogicServerConnectorHolder
	{
		private static LogicServerConnector	instance	= new LogicServerConnector();
	}

}
