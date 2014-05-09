package com.xgame.server.game.map;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xgame.server.objects.WorldObject;

public class Grid
{
	private int								x;
	private int								y;
	private HashMap< UUID, WorldObject >	objectMap;

	private static Log						log	= LogFactory
														.getLog( Grid.class );

	public Grid( int x, int y )
	{
		this.x = x;
		this.y = y;
		objectMap = new HashMap< UUID, WorldObject >();
	}

	public Grid()
	{
		x = 0;
		y = 0;
		objectMap = new HashMap< UUID, WorldObject >();
	}

	public int getX()
	{
		return x;
	}

	public void setX( int x )
	{
		this.x = x;
	}

	public int getY()
	{
		return y;
	}

	public void setY( int y )
	{
		this.y = y;
	}

	public void addWorldObject( WorldObject o )
	{
		if ( objectMap.containsKey( o.getGuid() ) )
		{
			log.warn( "addWorldObject() Grid[x=" + x + ", y=" + y
					+ "]Key duplicated" );
			return;
		}
		o.setCurrentGrid( this );
		objectMap.put( o.getGuid(), o );
	}

	public WorldObject getWorldObject( UUID guid )
	{
		if ( objectMap.containsKey( guid ) )
		{
			return objectMap.get( guid );
		}
		log.warn( "getWorldObject() not exist UUID=" + guid.toString() );
		return null;
	}

	public void removeWorldObject( UUID guid )
	{
		if ( objectMap.containsKey( guid ) )
		{
			objectMap.remove( guid );
		}
		else
		{
			log.warn( "getWorldObject() �����ڣ�UUID=" + guid.toString() );
		}
	}

	public void removeAll()
	{
		objectMap.clear();
	}

	public Iterator< Entry< UUID, WorldObject >> getWorldObjectIterator()
	{
		return objectMap.entrySet().iterator();
	}

}
