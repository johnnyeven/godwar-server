package com.xgame.server.game;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.xgame.server.common.database.DatabaseRouter;
import com.xgame.server.network.AIOSocketMgr;

public class GameServer
{
    public final static int PORT = 9050;

	public GameServer()
	{
		DatabaseRouter.getInstance();
	}
	
	public void run()
	{
	    System.out.println( "                MMMM");
	    System.out.println( " MMM    MMM    MMMMMMM                          MMMMMMMM");
	    System.out.println( " MMM    MMM   MMMMMMMMM                         MMMMMMMM");
	    System.out.println( "  MMM  MMM   MMMM   MMM                         MM");
	    System.out.println( "  MMM  MMM   MMM    MMM    MMM    MM MMM  MMM   MM");
	    System.out.println( "   MMMMMM    MMM     MM   MMMMMM  MMMMMMMMMMMMM MM");
	    System.out.println( "   MMMMMM    MMM         MMMMMMM  MMMMMMMMMMMMM MM");
	    System.out.println( "    MMMM     MM          MM   MM  MM   MMM  MMM MMMMMMMM");
	    System.out.println( "    MMMM     MM   MMMMM       MM  MM   MMM   MM MMMMMMMM");
	    System.out.println( "    MMMM     MM   MMMMM    MMMMM  MM   MMM   MM MM");
	    System.out.println( "   MMMMMM    MMM     MM  MMMMMMM  MM   MMM   MM MM");
	    System.out.println( "   MMMMMM    MMM     MM  MMMM MM  MM   MMM   MM MM");
	    System.out.println( "  MMM  MMM   MMM     MM  MM   MM  MM   MMM   MM MM");
	    System.out.println( " MMMM  MMMM  MMM    MMM  MM   MM  MM   MMM   MM MM");
	    System.out.println( " MMM    MMM   MMMMMMMMM  MMMMMMMM MM   MMM   MM MMMMMMMM");
	    System.out.println( "MMMM    MMMM   MMMMMMMM  MMMMMMMM MM   MMM   MM MMMMMMMM");
	    System.out.println( "                 MMM MM    MM\n");
	    System.out.println( "GameServer\n\n");
	    
	    BattleHall.getInstance().setInitialWorldSettings();
	    MeleeHall.getInstance().setInitialWorldSettings();
	    
	    Hall.getInstance().startHall();
		
		Thread thLogicServerListen = new Thread(new LogicServerListenThread());
		thLogicServerListen.start();
		
		startLogicServerHolderThread();
	    
		AIOSocketMgr.getInstance().startCompletionPort();
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
