package com.xgame.server.network;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.ArrayList;
import java.util.List;

import com.xgame.server.common.protocol.ProtocolRouter;
import com.xgame.server.game.Player;
import com.xgame.server.game.ProtocolPackage;
import com.xgame.server.pool.BufferPool;

public class GameSession
{
	private long						accountId;
	private List< ProtocolPackage >		recvQueue;
	private Player						player;
	private AsynchronousSocketChannel	channel;
	private String						address;
	private long						generateTime;
	private ByteBuffer					readBuffer;

	public GameSession( long id, AsynchronousSocketChannel c, long time )
	{
		accountId = id;
		channel = c;
		try
		{
			address = c.getRemoteAddress().toString();
		}
		catch ( IOException e )
		{
			e.printStackTrace();
		}
		generateTime = time;
		recvQueue = new ArrayList< ProtocolPackage >();
	}

	public void startRecv()
	{
		if ( channel.isOpen() )
		{
			readBuffer = BufferPool.getInstance().getBuffer();
			channel.read( readBuffer, this, AIOSocketMgr.getInstance()
					.getReadHandler() );
		}
	}

	public void addParameterQueue( ProtocolPackage pack )
	{
		recvQueue.add( pack );
	}

	public void setPlayer( Player p )
	{
		if ( p != null )
		{
			player = p;
			player.setSession( this );
		}
	}

	public Player getPlayer()
	{
		return player;
	}

	public long getAccountId()
	{
		return accountId;
	}

	public AsynchronousSocketChannel getChannel()
	{
		return channel;
	}

	public ByteBuffer getReadBuffer()
	{
		return readBuffer;
	}

	public boolean update( long timeDiff )
	{
		if ( channel != null )
		{
			if ( !channel.isOpen() )
			{

			}
		}

		ProtocolPackage pack;
		while ( !recvQueue.isEmpty() )
		{
			pack = recvQueue.remove( 0 );
			ProtocolRouter.getInstance().Trigger( pack.protocolId, pack, this );
		}
		return true;
	}

	public void dispose()
	{
		if ( player != null )
		{
			player = null;
		}
		if ( channel != null )
		{
			try
			{
				channel.close();
			}
			catch ( IOException e )
			{
				e.printStackTrace();
			}
			finally
			{
				channel = null;
			}
		}

	}
}
