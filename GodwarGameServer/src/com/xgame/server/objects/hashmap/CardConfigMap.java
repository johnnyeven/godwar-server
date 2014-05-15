package com.xgame.server.objects.hashmap;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.xgame.server.common.parameter.SoulCardParameter;

public class CardConfigMap
{
	private static CardConfigMap				instance;
	private Map< String, SoulCardParameter >	hash	= new HashMap< String, SoulCardParameter >();

	private CardConfigMap()
	{
	}

	public SoulCardParameter get( String id )
	{
		if ( hash.containsKey( id ) )
		{
			return hash.get( id );
		}
		return null;
	}

	public void add( SoulCardParameter p )
	{
		hash.put( p.id, p );
	}

	public Iterator< Entry< String, SoulCardParameter >> getIterator()
	{
		return hash.entrySet().iterator();
	}

	public static CardConfigMap getInstance()
	{
		if ( instance == null )
		{
			instance = new CardConfigMap();
		}
		return instance;
	}
}
