package com.xgame.server.network;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.ArrayList;
import java.util.List;

import com.xgame.server.common.protocol.ProtocolRouter;
import com.xgame.server.game.IHall;
import com.xgame.server.game.Player;
import com.xgame.server.game.ProtocolPackage;
import com.xgame.server.pool.BufferPool;

public class GameSession
{
	private long						id;
	private List< ProtocolPackage >		recvQueue;
	private Player						player;
	private AsynchronousSocketChannel	channel;
	private String						address;
	private long						generateTime;
	private long						heartBeatTime;
	private ByteBuffer					readBuffer;
	private IHall						currentHall;
	private Boolean						isDispose;

	public GameSession( long id, AsynchronousSocketChannel c, long time )
	{
		this.id = id;
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
		heartBeatTime = time;
		recvQueue = new ArrayList< ProtocolPackage >();
		isDispose = false;
	}

	public Boolean getIsDispose()
	{
		return isDispose;
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
			
			if(currentHall != null)
			{
				player.setCurrentHall( currentHall );
			}
		}
	}

	public Player getPlayer()
	{
		return player;
	}

	public long getId()
	{
		return this.id;
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
		if ( !isDispose )
		{
			isDispose = true;
		}
		if ( player != null )
		{
			if ( player.getCurrentHall() != null )
			{
				player.getCurrentHall().kickPlayer( this );
			}
			player = null;
		}
		if ( readBuffer != null )
		{
			BufferPool.getInstance().releaseBuffer( readBuffer );
			readBuffer = null;
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

	public long getHeartBeatTime()
	{
		return heartBeatTime;
	}

	public void setHeartBeatTime( long heartBeatTime )
	{
		this.heartBeatTime = heartBeatTime;
	}

	public IHall getCurrentHall()
	{
		return currentHall;
	}

	public void setCurrentHall( IHall currentHall )
	{
		this.currentHall = currentHall;
		
		if(player != null)
		{
			player.setCurrentHall( currentHall );
		}
	}
}
