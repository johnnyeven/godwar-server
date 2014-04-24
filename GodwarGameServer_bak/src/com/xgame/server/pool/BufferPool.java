package com.xgame.server.pool;

import java.nio.ByteBuffer;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class BufferPool
{
	private static Log							log					= LogFactory
																			.getLog( BufferPool.class );

	private static int							maxBufferPoolSize	= 1000;
	private static int							minBufferPoolSize	= 100;
	private static int							writeBufferSize		= 64;

	private static BufferPool					bufferPool			= new BufferPool();

	private AtomicInteger						usableCount			= new AtomicInteger();
	private AtomicInteger						createCount			= new AtomicInteger();
	private ConcurrentLinkedQueue< ByteBuffer >	queue				= new ConcurrentLinkedQueue< ByteBuffer >();

	static
	{
		Integer maxSize = 2000;
		if ( maxSize != null )
		{
			maxBufferPoolSize = maxSize;
		}

		Integer minSize = 2000;
		if ( minSize != null )
		{
			minBufferPoolSize = minSize;
		}

		Integer bufferSize = 10;
		if ( bufferSize != null )
		{
			writeBufferSize = bufferSize;
		}
	}

	private BufferPool()
	{
		for ( int i = 0; i < minBufferPoolSize; ++i )
		{
			ByteBuffer bb = ByteBuffer.allocateDirect( writeBufferSize * 1024 );
			this.queue.add( bb );
		}

		this.usableCount.set( minBufferPoolSize );
		this.createCount.set( minBufferPoolSize );
	}

	public ByteBuffer getBuffer()
	{
		ByteBuffer bb = this.queue.poll();

		if ( bb == null )
		{
			bb = ByteBuffer.allocateDirect( writeBufferSize * 1024 );
			this.createCount.incrementAndGet();
		}
		else
		{
			this.usableCount.decrementAndGet();
		}

		return bb;
	}

	public void releaseBuffer( ByteBuffer bb )
	{
		if ( this.createCount.intValue() > maxBufferPoolSize
				&& ( this.usableCount.intValue() > ( this.createCount
						.intValue() / 2 ) ) )
		{
			bb = null;
			this.createCount.decrementAndGet();
		}
		else
		{
			bb.clear();
			this.queue.add( bb );
			this.usableCount.incrementAndGet();
		}
	}

	public static BufferPool getInstance()
	{
		return bufferPool;
	}
}
