package com.xgame.server.game;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.xgame.server.common.database.DatabaseRouter;
import com.xgame.server.network.AIOSocketMgr;

public class GameServer
{

	public final static int	PORT				= 9050;
	public static String	initSoulCardConfig	= "";
	public static String	initHeroCardConfig	= "";
	public static String	freeHeroCardConfig	= "";

	public GameServer()
	{
		DatabaseRouter.getInstance();
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
		System.out.println( "GameServer\n\n" );

		try
		{
			loadConfig();
		}
		catch ( ParserConfigurationException | SAXException | IOException e )
		{
			e.printStackTrace();
		}

		BattleHall.getInstance().setInitialWorldSettings();
		MeleeHall.getInstance().setInitialWorldSettings();

		Hall.getInstance().startHall();

		Thread thLogicServerListen = new Thread( new LogicServerListenThread() );
		thLogicServerListen.setName( "LogicServerListenThread" );
		thLogicServerListen.start();

		startLogicServerHolderThread();

		AIOSocketMgr.getInstance().startCompletionPort();

		try
		{
			System.in.read();
		}
		catch ( IOException e )
		{
			e.printStackTrace();
		}
	}

	public void loadConfig() throws ParserConfigurationException, SAXException,
			IOException
	{
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dbBuilder = dbFactory.newDocumentBuilder();

		Document doc = dbBuilder.parse( "init_card_config.xml" );

		Node serverNode = doc.getElementsByTagName( "soul_cards" ).item( 0 );
		NodeList list = serverNode.getChildNodes();
		initSoulCardConfig = list.item( 0 ).getTextContent();

		serverNode = doc.getElementsByTagName( "hero_cards" ).item( 0 );
		list = serverNode.getChildNodes();
		initHeroCardConfig = list.item( 0 ).getTextContent();
		
		doc = dbBuilder.parse( "free_card_config.xml" );
		serverNode = doc.getElementsByTagName( "free_cards" ).item( 0 );
		list = serverNode.getChildNodes();
		freeHeroCardConfig = list.item( 0 ).getTextContent();
	}

	private void startLogicServerHolderThread()
	{
		ExecutorService exe = Executors.newFixedThreadPool( 2 );

		LogicServerHolderThread th1 = new LogicServerHolderThread();
		LogicServerHolderThread th2 = new LogicServerHolderThread();

		exe.execute( th1 );
		exe.execute( th2 );
	}

	public static void main( String[] args )
	{
		new GameServer().run();
	}

}