package com.xgame.server.network;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.concurrent.Executors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xgame.server.common.protocol.EnumProtocol;
import com.xgame.server.common.protocol.ProtocolRegisterAccountRole;
import com.xgame.server.common.protocol.ProtocolRequestAccountRole;
import com.xgame.server.common.protocol.ProtocolRequestFindPath;
import com.xgame.server.common.protocol.ProtocolRequestHotkey;
import com.xgame.server.common.protocol.ProtocolRouter;
import com.xgame.server.common.protocol.ProtocolUpdatePlayerStatus;

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
			server.bind( new InetSocketAddress( HOST, PORT ), 100 );

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
		ProtocolRouter.getInstance().Bind( EnumProtocol.REQUEST_HOTKEY,
				ProtocolRequestHotkey.class );
		ProtocolRouter.getInstance().Bind( EnumProtocol.BASE_UPDATE_STATUS,
				ProtocolUpdatePlayerStatus.class );
		ProtocolRouter.getInstance().Bind( EnumProtocol.REQUEST_FIND_PATH,
				ProtocolRequestFindPath.class );
	}

	public void startCompletionPort()
	{
		server.accept( this, acceptHandler );
		log.info( "服务器已启动" );

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
