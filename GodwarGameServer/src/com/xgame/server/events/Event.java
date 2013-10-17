package com.xgame.server.events;

import com.xgame.server.game.Player;

public class Event
{
	private String		name;
	private Player	sender;

	public Event( String name )
	{
		this.name = name;
	}

	public String getName()
	{
		return name;
	}

	public void setSender( Player sender )
	{
		if ( this.sender == null )
		{
			this.sender = sender;
		}
	}

	public Player getSender()
	{
		return sender;
	}

}
