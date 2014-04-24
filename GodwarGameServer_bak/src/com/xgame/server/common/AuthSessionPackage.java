package com.xgame.server.common;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;

public class AuthSessionPackage
{
	public ByteBuffer					buffer;
	public AsynchronousSocketChannel	channel;

	public AuthSessionPackage( ByteBuffer buffer,
			AsynchronousSocketChannel channel )
	{
		this.buffer = buffer;
		this.channel = channel;
	}

	public AuthSessionPackage()
	{

	}

}
