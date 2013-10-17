package com.xgame.server.events;

import com.xgame.server.objects.WorldObject;

public class Event
{
	private String		name;
	private WorldObject	sender;

	public Event( String name )
	{
		this.name = name;
	}

	public String getName()
	{
		return name;
	}

	public void setSender( WorldObject sender )
	{
		if ( this.sender == null )
		{
			this.sender = sender;
		}
	}

	public WorldObject getSender()
	{
		return sender;
	}

}
