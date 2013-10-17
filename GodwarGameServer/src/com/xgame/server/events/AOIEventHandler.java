package com.xgame.server.events;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class AOIEventHandler implements IEventCallback
{
	private static Log				log	= LogFactory
												.getLog( AOIEventHandler.class );
	private static AOIEventHandler	instance;

	private AOIEventHandler()
	{

	}

	@Override
	public void execute( Event evt )
	{
		AOIEvent e = (AOIEvent)evt;
		log.debug( evt.getName() + ": " + e.who.getGuid().toString() + ", sender: " + evt.getSender().getGuid().toString() );
	}

	public static AOIEventHandler getInstance()
	{
		if ( instance == null )
		{
			instance = new AOIEventHandler();
		}
		return instance;
	}

}
