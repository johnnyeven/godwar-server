package com.xgame.server.policy;

import com.xgame.server.network.AIOSocketMgr;

public class PolicyServer
{
    
    public PolicyServer()
    {
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
	    System.out.println( "PolicyServer\n\n");
		
		AIOSocketMgr.getInstance().startCompletionPort();
		
		try
		{
			Thread.sleep( Long.MAX_VALUE );
		}
		catch ( InterruptedException e )
		{
			e.printStackTrace();
		}
    }
	
	public static void main(String[] args)
	{
		new PolicyServer().run();
	}
	
}
