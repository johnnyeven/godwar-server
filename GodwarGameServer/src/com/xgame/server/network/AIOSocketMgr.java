package com.xgame.server.network;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.concurrent.Executors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xgame.server.common.protocol.EnumProtocol;
import com.xgame.server.common.protocol.ProtocolChooseHero;
import com.xgame.server.common.protocol.ProtocolCreateGroup;
import com.xgame.server.common.protocol.ProtocolRegisterAccountRole;
import com.xgame.server.common.protocol.ProtocolRequestAccountRole;
import com.xgame.server.common.protocol.ProtocolRequestCardGroup;
import com.xgame.server.common.protocol.ProtocolRequestCardList;
import com.xgame.server.common.protocol.ProtocolRequestRoom;
import com.xgame.server.common.protocol.ProtocolRouter;
import com.xgame.server.common.protocol.ProtocolShowRoomList;
import com.xgame.server.common.protocol.ProtocolRequestEnterRoom;
import com.xgame.server.common.protocol.ProtocolUpdatePlayerReady;

public class AIOSocketMgr
{
	private AsynchronousServerSocketChannel	server;
	private AcceptCompletionHandler			acceptHandler;
	private ReadCompletionHandler			readHandler;
	private AuthSessionCompletionHandler	authHandler;
	public final static String				HOST			= "127.0.0.1";
	public final static int					PORT			= 9050;
	public static int						counter			= 0;

	private static AIOSocketMgr				instance		= null;
	private static boolean					allowInstance	= false;
	private static Log						log				= LogFactory
																	.getLog( AIOSocketMgr.class );

	public AIOSocketMgr() throws Exception
	{
		if ( !allowInstance )
		{
			throw new Exception();
		}
		try
		{
			AsynchronousChannelGroup resourceGroup = AsynchronousChannelGroup
					.withCachedThreadPool( Executors.newCachedThreadPool(), 8 );
			server = AsynchronousServerSocketChannel.open( resourceGroup );
			server.bind( new InetSocketAddress( PORT ), 100 );

			acceptHandler = new AcceptCompletionHandler();
			readHandler = new ReadCompletionHandler();
			authHandler = new AuthSessionCompletionHandler();

			bindProtocol();
		}
		catch ( IOException e )
		{
			e.printStackTrace();
		}
	}

	public static AIOSocketMgr getInstance()
	{
		if ( instance == null )
		{
			allowInstance = true;
			try
			{
				instance = new AIOSocketMgr();
			}
			catch ( Exception e )
			{
				e.printStackTrace();
			}
			allowInstance = false;
		}
		return instance;
	}

	private void bindProtocol()
	{
		ProtocolRouter.getInstance().Bind( EnumProtocol.REQUEST_ACCOUNT_ROLE,
				ProtocolRequestAccountRole.class );
		ProtocolRouter.getInstance().Bind( EnumProtocol.REGISTER_ACCOUNT_ROLE,
				ProtocolRegisterAccountRole.class );
		ProtocolRouter.getInstance().Bind( EnumProtocol.HALL_SHOW_ROOM_LIST,
				ProtocolShowRoomList.class );
		ProtocolRouter.getInstance().Bind( EnumProtocol.HALL_REQUEST_ROOM,
				ProtocolRequestRoom.class );
		ProtocolRouter.getInstance().Bind(
				EnumProtocol.HALL_REQUEST_ENTER_ROOM,
				ProtocolRequestEnterRoom.class );
		ProtocolRouter.getInstance().Bind(
				EnumProtocol.INFO_CREATE_GROUP,
				ProtocolCreateGroup.class );
		ProtocolRouter.getInstance().Bind(
				EnumProtocol.INFO_REQUEST_CARD_GROUP,
				ProtocolRequestCardGroup.class );
		ProtocolRouter.getInstance().Bind(
				EnumProtocol.INFO_REQUEST_CARD_LIST,
				ProtocolRequestCardList.class );
		ProtocolRouter.getInstance().Bind(
				EnumProtocol.BATTLEROOM_PLAYER_SELECTED_HERO,
				ProtocolChooseHero.class );
		ProtocolRouter.getInstance().Bind(
				EnumProtocol.BATTLEROOM_PLAYER_READY,
				ProtocolUpdatePlayerReady.class );
	}

	public void startCompletionPort()
	{
		log.info( "服务器已启动" );
		server.accept( this, acceptHandler );

		try
		{
			System.in.read();
		}
		catch ( IOException e )
		{
			e.printStackTrace();
		}
	}

	public void stopCompletionPort()
	{
		try
		{
			server.close();
			log.info( "游戏服务器已关闭" );
		}
		catch ( IOException e )
		{
			e.printStackTrace();
		}
	}

	public AsynchronousServerSocketChannel getServer()
	{
		return server;
	}

	public void setServer( AsynchronousServerSocketChannel server )
	{
		this.server = server;
	}

	public ReadCompletionHandler getReadHandler()
	{
		return readHandler;
	}

	public AuthSessionCompletionHandler getAuthHandler()
	{
		return authHandler;
	}
}
