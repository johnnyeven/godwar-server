package com.xgame.server.pool;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xgame.server.common.ServerPackage;

public class ServerPackagePool implements IPool< ServerPackage >
{
	private static Log								log					= LogFactory
																				.getLog( ServerPackagePool.class );

	private static int								maxBufferPoolSize	= 5000;
	private static int								minBufferPoolSize	= 2000;

	private AtomicInteger							usableCount			= new AtomicInteger();
	private AtomicInteger							createCount			= new AtomicInteger();
	private ConcurrentLinkedQueue< ServerPackage >	queue				= new ConcurrentLinkedQueue< ServerPackage >();
	private static ServerPackagePool				instance			= new ServerPackagePool();

	public ServerPackagePool()
	{
		for ( int i = 0; i < minBufferPoolSize; ++i )
		{
			ServerPackage p = new ServerPackage();
			this.queue.add( p );
		}

		this.usableCount.set( minBufferPoolSize );
		this.createCount.set( minBufferPoolSize );
	}

	@Override
	public ServerPackage getObject()
	{
		ServerPackage p = this.queue.poll();

		if ( p == null )
		{
			p = new ServerPackage();
			this.createCount.incrementAndGet();
		}
		else
		{
			this.usableCount.decrementAndGet();
		}

		return p;
	}

	@Override
	public void returnObject( ServerPackage p )
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

	public static ServerPackagePool getInstance()
	{
		if ( instance == null )
		{
			instance = new ServerPackagePool();
		}
		return instance;
	}

}
