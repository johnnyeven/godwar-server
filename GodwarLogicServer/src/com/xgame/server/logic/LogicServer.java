package com.xgame.server.logic;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.TimerTask;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.xgame.server.cards.CardParameterManager;
import com.xgame.server.common.database.DatabaseRouter;
import com.xgame.server.network.AIOSocketMgr;
import com.xgame.server.network.GameServerConnector;
import com.xgame.server.pool.BufferPool;
import com.xgame.server.pool.DatagramPacketPool;
import com.xgame.server.pool.HeroCardPool;
import com.xgame.server.pool.PlayerPool;
import com.xgame.server.pool.ServerPackagePool;
import com.xgame.server.pool.SoulCardPool;
import com.xgame.server.timer.CheckBattleRoomTimerTask;
import com.xgame.server.timer.RegisterLogicServerTimerTask;
import com.xgame.server.timer.TimerManager;

public class LogicServer
{
	public static String			gameServerIp	= null;
	public static int				gameServerPort	= 0;
	public static InetSocketAddress	gameServerAdd;

	public LogicServer()
	{
		DatabaseRouter.getInstance();
	}

	private void loadConfig() throws ParserConfigurationException,
			SAXException, IOException
	{
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dbBuilder = dbFactory.newDocumentBuilder();

		Document doc = dbBuilder.parse( "config.xml" );

		Node serverNode = doc.getElementsByTagName( "GameServer" ).item( 0 );
		NodeList list = serverNode.getChildNodes();
		int length = list.getLength();
		for ( int i = 0; i < length; i++ )
		{
			if ( list.item( i ).getNodeName() == "ip" )
			{
				gameServerIp = list.item( i ).getTextContent().trim();
			}
			else if ( list.item( i ).getNodeName() == "port" )
			{
				gameServerPort = Integer.parseInt( list.item( i )
						.getTextContent().trim() );
			}
		}
		gameServerAdd = new InetSocketAddress( gameServerIp, gameServerPort );

		serverNode = doc.getElementsByTagName( "LogicServer" ).item( 0 );
		list = serverNode.getChildNodes();
		String logicServerId = null;
		String logicServerIp = null;
		int logicServerPort = 0;
		length = list.getLength();
		for ( int i = 0; i < length; i++ )
		{
			if ( list.item( i ).getNodeName() == "id" )
			{
				logicServerId = list.item( i ).getTextContent().trim();
			}
			else if ( list.item( i ).getNodeName() == "ip" )
			{
				logicServerIp = list.item( i ).getTextContent().trim();
			}
			else if ( list.item( i ).getNodeName() == "port" )
			{
				logicServerPort = Integer.parseInt( list.item( i )
						.getTextContent().trim() );
			}
		}

		GameServerConnector.getInstance().setId( logicServerId );
		GameServerConnector.getInstance().setIp( logicServerIp );
		GameServerConnector.getInstance().setPort( logicServerPort );

		TimerTask task = new RegisterLogicServerTimerTask( gameServerIp,
				gameServerPort );
		TimerManager.getInstance().schedule(
				"GameServerConnectorInitilization", task, 0, 5000 );

		task = new CheckBattleRoomTimerTask();
		TimerManager.getInstance().schedule( "BattleRoomValidation", task, 0,
				10000 );
	}

	public void run()
	{
		System.out.println( "                MMMM" );
		System.out
				.println( " MMM    MMM    MMMMMMM                          MMMMMMMM" );
		System.out
				.println( " MMM    MMM   MMMMMMMMM                         MMMMMMMM" );
		System.out
				.println( "  MMM  MMM   MMMM   MMM                         MM" );
		System.out
				.println( "  MMM  MMM   MMM    MMM    MMM    MM MMM  MMM   MM" );
		System.out
				.println( "   MMMMMM    MMM     MM   MMMMMM  MMMMMMMMMMMMM MM" );
		System.out
				.println( "   MMMMMM    MMM         MMMMMMM  MMMMMMMMMMMMM MM" );
		System.out
				.println( "    MMMM     MM          MM   MM  MM   MMM  MMM MMMMMMMM" );
		System.out
				.println( "    MMMM     MM   MMMMM       MM  MM   MMM   MM MMMMMMMM" );
		System.out
				.println( "    MMMM     MM   MMMMM    MMMMM  MM   MMM   MM MM" );
		System.out
				.println( "   MMMMMM    MMM     MM  MMMMMMM  MM   MMM   MM MM" );
		System.out
				.println( "   MMMMMM    MMM     MM  MMMM MM  MM   MMM   MM MM" );
		System.out
				.println( "  MMM  MMM   MMM     MM  MM   MM  MM   MMM   MM MM" );
		System.out
				.println( " MMMM  MMMM  MMM    MMM  MM   MM  MM   MMM   MM MM" );
		System.out
				.println( " MMM    MMM   MMMMMMMMM  MMMMMMMM MM   MMM   MM MMMMMMMM" );
		System.out
				.println( "MMMM    MMMM   MMMMMMMM  MMMMMMMM MM   MMM   MM MMMMMMMM" );
		System.out.println( "                 MMM MM    MM\n" );
		System.out.println( "LogicServer\n\n" );

		BattleHall.getInstance().setInitialWorldSettings();
		MeleeHall.getInstance().setInitialWorldSettings();

		Hall.getInstance().startHall();

		Thread thGameServerListen = new Thread( new GameServerListenThread() );
		thGameServerListen.setName( "GameServerListenThread" );
		thGameServerListen.start();

		Thread thGameServerHolder = new Thread( new GameServerHolderThread() );
		thGameServerHolder.setName( "GameServerHolderThread" );
		thGameServerHolder.start();

		AIOSocketMgr.getInstance().startCompletionPort();
	}
	
	private void initPool()
	{
		BufferPool.getInstance();
		ServerPackagePool.getInstance();
		DatagramPacketPool.getInstance();
		PlayerPool.getInstance();
		HeroCardPool.getInstance();
		SoulCardPool.getInstance();
	}

	public static void main( String[] args )
	{
		LogicServer me = new LogicServer();
		try
		{
			me.loadConfig();
			me.initPool();
			CardParameterManager.getInstance().initialize();
		}
		catch ( ParserConfigurationException | SAXException | IOException e )
		{
			e.printStackTrace();
		}
		me.run();
	}

}
