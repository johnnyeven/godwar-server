package com.xgame.server.pool;

import java.net.DatagramPacket;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DatagramPacketPool implements IPool< DatagramPacket >
{
	private static Log								log					= LogFactory
																				.getLog( PlayerPool.class );

	private static int								maxBufferPoolSize	= 500;
	private static int								minBufferPoolSize	= 100;
	private static int								datagramSize		= 10;

	private AtomicInteger							usableCount			= new AtomicInteger();
	private AtomicInteger							createCount			= new AtomicInteger();
	private ConcurrentLinkedQueue< DatagramPacket >	queue				= new ConcurrentLinkedQueue< DatagramPacket >();
	private static DatagramPacketPool				instance			= new DatagramPacketPool();

	private DatagramPacketPool()
	{
		for ( int i = 0; i < minBufferPoolSize; ++i )
		{
			byte[] dest = new byte[datagramSize * 1024];
			DatagramPacket r = new DatagramPacket( dest, dest.length );
			this.queue.add( r );
		}

		this.usableCount.set( minBufferPoolSize );
		this.createCount.set( minBufferPoolSize );
	}

	@Override
	public DatagramPacket getObject()
	{
		DatagramPacket r = this.queue.poll();

		if ( r == null )
		{
			byte[] dest = new byte[datagramSize * 1024];
			r = new DatagramPacket( dest, dest.length );
			this.createCount.incrementAndGet();
		}
		else
		{
			this.usableCount.decrementAndGet();
		}

		return r;
	}

	@Override
	public void returnObject( DatagramPacket r )
	{
		if ( this.createCount.intValue() > maxBufferPoolSize
				&& ( this.usableCount.intValue() > ( this.createCount
						.intValue() / 2 ) ) )
		{
			r = null;
			this.createCount.decrementAndGet();
		}
		else
		{
			// «Â¿Ìp
			this.queue.add( r );
			this.usableCount.incrementAndGet();
		}
	}

	public static DatagramPacketPool getInstance()
	{
		if ( instance == null )
		{
			instance = new DatagramPacketPool();
		}
		return instance;
	}

}
