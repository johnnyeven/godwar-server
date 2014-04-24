package com.xgame.server.game;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;

public class ProtocolPackage
{
	public short						protocolId;
	public AsynchronousSocketChannel	client;
	public ByteBuffer					receiveData;
	public int							receiveDataLength;
	public int							offset;

	public ProtocolPackage()
	{
		// TODO Auto-generated constructor stub
	}

}
