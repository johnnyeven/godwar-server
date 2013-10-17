package com.xgame.server.events;

import com.xgame.server.game.Player;

public class AOIEvent extends Event
{
	public final static String AOI_ENTER = "AOIEvent.Enter";
	public final static String AOI_LEAVE = "AOIEvent.Leave";
	public Player who;

	public AOIEvent( String name )
	{
		super( name );
	}

}
