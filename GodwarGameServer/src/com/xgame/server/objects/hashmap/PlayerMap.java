package com.xgame.server.objects.hashmap;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import com.xgame.server.objects.Player;

public class PlayerMap
{
	private static PlayerMap	instance;
	private Map< String, Player >	hash	= new HashMap< String, Player >();

	private PlayerMap()
	{
	}

	public Player get( String guid )
	{
		if ( hash.containsKey( guid ) )
		{
			return hash.get( guid );
		}
		return null;
	}

	public void add( Player p )
	{
		hash.put( p.getGuid().toString(), p );
	}

	public Iterator< Entry< String, Player >> getIterator()
	{
		return hash.entrySet().iterator();
	}

	public static PlayerMap getInstance()
	{
		if ( instance == null )
		{
			instance = new PlayerMap();
		}
		return instance;
	}
}
