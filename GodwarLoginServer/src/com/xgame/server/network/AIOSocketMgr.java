package com.xgame.server.network;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.concurrent.Executors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xgame.server.common.protocol.EnumProtocol;
import com.xgame.server.common.protocol.ProtocolRequestLogin;
import com.xgame.server.common.protocol.ProtocolRequestQuickStart;
import com.xgame.server.common.protocol.ProtocolRequestRegister;
import com.xgame.server.common.protocol.ProtocolRouter;

public class AIOSocketMgr
{
	private AsynchronousServerSocketChannel	server;
	private AcceptCompletionHandler			acceptHandler;
	private ReadCompletionHandler			readHandler;
	public final static String				HOST			= "127.0.0.1";
	public final static int					PORT			= 9040;
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
		ProtocolRouter.getInstance().Bind(EnumProtocol.QUICK_START, ProtocolRequestQuickStart.class);
		ProtocolRouter.getInstance().Bind(EnumProtocol.INFO_LOGIN, ProtocolRequestLogin.class);
		ProtocolRouter.getInstance().Bind(EnumProtocol.INFO_REGISTER, ProtocolRequestRegister.class);
	}

	public void startCompletionPort()
	{
		server.accept( this, acceptHandler );
		log.info( "login server started" );
	}

	public void stopCompletionPort()
	{
		try
		{
			server.close();
			log.info( "login server stopped" );
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
}
