package com.xgame.server.network;

import java.net.DatagramPacket;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class DatagramPacketQueue
{
	private LinkedBlockingQueue< DatagramPacket >	queue;

	private DatagramPacketQueue()
	{
		queue = new LinkedBlockingQueue< DatagramPacket >();
	}

	public void push( DatagramPacket p )
	{
		queue.add( p );
	}

	public synchronized DatagramPacket shift()
	{
		if ( queue.size() > 0 )
		{
			return queue.poll();
		}
		else
		{
			return null;
		}
	}
	
	public int size()
	{
		return queue.size();
	}

	public static DatagramPacketQueue getInstance()
	{
		return DatagramQueueHolder.instance;
	}

	private static class DatagramQueueHolder
	{
		private static DatagramPacketQueue	instance	= new DatagramPacketQueue();
	}

}
