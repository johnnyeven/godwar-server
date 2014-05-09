package com.xgame.server.objects.hashmap;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import com.xgame.server.objects.NPC;

public class NPCMap
{
	private static NPCMap	instance;
	private Map< String, NPC >	hash	= new HashMap< String, NPC >();

	private NPCMap()
	{
	}

	public NPC get( String guid )
	{
		if ( hash.containsKey( guid ) )
		{
			return hash.get( guid );
		}
		return null;
	}

	public void add( NPC p )
	{
		hash.put( p.getGuid().toString(), p );
	}

	public Iterator< Entry< String, NPC >> getIterator()
	{
		return hash.entrySet().iterator();
	}

	public static NPCMap getInstance()
	{
		if ( instance == null )
		{
			instance = new NPCMap();
		}
		return instance;
	}
}
