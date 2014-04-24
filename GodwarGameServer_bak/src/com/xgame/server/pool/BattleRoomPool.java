package com.xgame.server.pool;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xgame.server.game.BattleRoom;

public class BattleRoomPool implements IPool< BattleRoom >
{
	private static Log							log					= LogFactory
																			.getLog( PlayerPool.class );

	private static int							maxBufferPoolSize	= 500;
	private static int							minBufferPoolSize	= 100;

	private AtomicInteger						usableCount			= new AtomicInteger();
	private AtomicInteger						createCount			= new AtomicInteger();
	private ConcurrentLinkedQueue< BattleRoom >	queue				= new ConcurrentLinkedQueue< BattleRoom >();
	private static BattleRoomPool				instance			= new BattleRoomPool();

	private BattleRoomPool()
	{
		for ( int i = 0; i < minBufferPoolSize; ++i )
		{
			BattleRoom r = new BattleRoom();
			this.queue.add( r );
		}

		this.usableCount.set( minBufferPoolSize );
		this.createCount.set( minBufferPoolSize );
	}

	@Override
	public BattleRoom getObject()
	{
		BattleRoom r = this.queue.poll();

		if ( r == null )
		{
			r = new BattleRoom();
			this.createCount.incrementAndGet();
		}
		else
		{
			this.usableCount.decrementAndGet();
		}

		return r;
	}

	@Override
	public void returnObject( BattleRoom r )
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

	public static BattleRoomPool getInstance()
	{
		if ( instance == null )
		{
			instance = new BattleRoomPool();
		}
		return instance;
	}

}
