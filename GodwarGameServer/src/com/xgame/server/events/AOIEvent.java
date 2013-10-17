package com.xgame.server.events;

import com.xgame.server.objects.WorldObject;

public class AOIEvent extends Event
{
	public final static String AOI_ENTER = "AOIEvent.Enter";
	public final static String AOI_LEAVE = "AOIEvent.Leave";
	public WorldObject who;

	public AOIEvent( String name )
	{
		super( name );
	}

}
