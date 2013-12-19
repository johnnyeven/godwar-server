package com.xgame.server.pool;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xgame.server.cards.HeroCard;
import com.xgame.server.logic.Player;

public class HeroCardPool implements IPool< HeroCard >
{
	private static Log							log					= LogFactory
																			.getLog( HeroCardPool.class );

	private static int							maxBufferPoolSize	= 1000;
	private static int							minBufferPoolSize	= 500;

	private AtomicInteger						usableCount			= new AtomicInteger();
	private AtomicInteger						createCount			= new AtomicInteger();
	private ConcurrentLinkedQueue< HeroCard >	queue				= new ConcurrentLinkedQueue< HeroCard >();
	private static HeroCardPool					instance			= new HeroCardPool();

	private HeroCardPool()
	{
		for ( int i = 0; i < minBufferPoolSize; ++i )
		{
			HeroCard p = new HeroCard();
			this.queue.add( p );
		}

		this.usableCount.set( minBufferPoolSize );
		this.createCount.set( minBufferPoolSize );
	}

	@Override
	public HeroCard getObject()
	{
		HeroCard p = this.queue.poll();

		if ( p == null )
		{
			p = new HeroCard();
			this.createCount.incrementAndGet();
		}
		else
		{
			this.usableCount.decrementAndGet();
		}

		return p;
	}

	@Override
	public void returnObject( HeroCard p )
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

	public static HeroCardPool getInstance()
	{
		if ( instance == null )
		{
			instance = new HeroCardPool();
		}
		return instance;
	}

}
