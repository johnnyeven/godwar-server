package com.xgame.server.pool;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xgame.server.logic.Player;

public class PlayerPool implements IPool< Player >
{
	private static Log						log					= LogFactory
																		.getLog( PlayerPool.class );

	private static int						maxBufferPoolSize	= 5000;
	private static int						minBufferPoolSize	= 2000;

	private AtomicInteger					usableCount			= new AtomicInteger();
	private AtomicInteger					createCount			= new AtomicInteger();
	private ConcurrentLinkedQueue< Player >	queue				= new ConcurrentLinkedQueue< Player >();
	private static PlayerPool				instance			= new PlayerPool();

	private PlayerPool()
	{
		for ( int i = 0; i < minBufferPoolSize; ++i )
		{
			Player p = new Player();
			this.queue.add( p );
		}

		this.usableCount.set( minBufferPoolSize );
		this.createCount.set( minBufferPoolSize );
	}

	@Override
	public Player getObject()
	{
		Player p = this.queue.poll();

		if ( p == null )
		{
			p = new Player();
			this.createCount.incrementAndGet();
		}
		else
		{
			this.usableCount.decrementAndGet();
		}

		return p;
	}

	@Override
	public void returnObject( Player p )
	{
		if ( this.createCount.intValue() > maxBufferPoolSize
				&& ( this.usableCount.intValue() > ( this.createCount
						.intValue() / 2 ) ) )
		{
			p = null;
			this.createCount.decrementAndGet();
		}
		else
		{
			// «Â¿Ìp
			this.queue.add( p );
			this.usableCount.incrementAndGet();
		}
	}

	public static PlayerPool getInstance()
	{
		if ( instance == null )
		{
			instance = new PlayerPool();
		}
		return instance;
	}

}
