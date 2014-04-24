package com.xgame.server.game;

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
	    
	    World.getInstance().setInitialWorldSettings();
	    
	    Thread wt = new Thread(new WorldThread());
	    wt.setPriority(10);
	    wt.start();
	    
		AIOSocketMgr.getInstance().startCompletionPort();
	}

	public static void main( String[] args )
	{
		new GameServer().run();
	}

}
