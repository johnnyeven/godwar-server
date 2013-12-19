package com.xgame.server.pool;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xgame.server.cards.HeroCard;
import com.xgame.server.cards.SoulCard;
import com.xgame.server.logic.Player;

public class SoulCardPool implements IPool< SoulCard >
{
	private static Log							log					= LogFactory
																			.getLog( SoulCardPool.class );

	private static int							maxBufferPoolSize	= 1000;
	private static int							minBufferPoolSize	= 500;

	private AtomicInteger						usableCount			= new AtomicInteger();
	private AtomicInteger						createCount			= new AtomicInteger();
	private ConcurrentLinkedQueue< SoulCard >	queue				= new ConcurrentLinkedQueue< SoulCard >();
	private static SoulCardPool					instance			= new SoulCardPool();

	private SoulCardPool()
	{
		for ( int i = 0; i < minBufferPoolSize; ++i )
		{
			SoulCard p = new SoulCard();
			this.queue.add( p );
		}

		this.usableCount.set( minBufferPoolSize );
		this.createCount.set( minBufferPoolSize );
	}

	@Override
	public SoulCard getObject()
	{
		SoulCard p = this.queue.poll();

		if ( p == null )
		{
			p = new SoulCard();
			this.createCount.incrementAndGet();
		}
		else
		{
			this.usableCount.decrementAndGet();
		}

		return p;
	}

	@Override
	public void returnObject( SoulCard p )
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

	public static SoulCardPool getInstance()
	{
		if ( instance == null )
		{
			instance = new SoulCardPool();
		}
		return instance;
	}

}
