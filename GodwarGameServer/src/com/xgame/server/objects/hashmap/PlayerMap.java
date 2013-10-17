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
	private Map< UUID, Player >	hash	= new HashMap< UUID, Player >();

	private PlayerMap()
	{
	}

	public Player get( UUID guid )
	{
		if ( hash.containsKey( guid ) )
		{
			return hash.get( guid );
		}
		return null;
	}

	public void add( Player p )
	{
		hash.put( p.getGuid(), p );
	}

	public Iterator< Entry< UUID, Player >> getIterator()
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
