package com.xgame.server.policy;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;

public class ProtocolParam
{
    public AsynchronousSocketChannel client;
    public ByteBuffer receiveData;
    public int receiveDataLength;
    public int offset;
    
	public ProtocolParam()
	{
		// TODO Auto-generated constructor stub
	}

}
