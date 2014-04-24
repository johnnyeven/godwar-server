package com.xgame.server.game.map;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class MapManager
{
	private static MapManager		instance		= new MapManager();
	private HashMap< Integer, Map >	mapContainer	= new HashMap< Integer, Map >();
	private static Log				log				= LogFactory
															.getLog( MapManager.class );

	private MapManager()
	{

	}

	public static MapManager getInstance()
	{
		if ( instance == null )
		{
			instance = new MapManager();
		}
		return instance;
	}

	public Map getMap( int id )
	{
		Map m = getBaseMap( id );
		return m;
	}

	private Map getBaseMap( int id )
	{
		Map m = findMap( id );
		if ( m == null )
		{
			MapConfig config = MapConfigManager.getInstance().getConfig( id );
			if ( config != null )
			{
				m = new Map( id, config );
				log.info( "Created new Map, id=" + id );
			}
			mapContainer.put( id, m );
		}
		return m;
	}

	private Map findMap( int id )
	{
		if ( mapContainer.containsKey( id ) )
		{
			return mapContainer.get( id );
		}
		return null;
	}

	public void unloadAllMaps()
	{
		Set< Entry< Integer, Map >> s = mapContainer.entrySet();
		Iterator< Entry< Integer, Map >> i = s.iterator();
		Entry< Integer, Map > en;
		Map m;
		while ( i.hasNext() )
		{
			en = i.next();
			m = en.getValue();
			m.unloadMap();
		}
	}
}
