package com.xgame.server.objects;

import java.util.UUID;

import com.xgame.server.game.map.Grid;
import com.xgame.server.game.map.Map;
import com.xgame.server.game.map.MapManager;

public class WorldObject
{
	private UUID	guid;
	private int		mapId;
	private double	currentX;
	private double	currentY;
	private Grid	currentGrid;

	public WorldObject()
	{
		guid = UUID.randomUUID();
		currentX = Double.MIN_VALUE;
		currentY = Double.MIN_VALUE;
	}

	public Map getMap()
	{
		return MapManager.getInstance().getMap( getMapId() );
	}

	public UUID getGuid()
	{
		return guid;
	}

	public void setGuid( UUID guid )
	{
		this.guid = guid;
	}

	public int getMapId()
	{
		return mapId;
	}

	public void setMapId( int mapId )
	{
		this.mapId = mapId;
	}

	public double getX()
	{
		return currentX;
	}

	public void setX( double x )
	{
		currentX = x;
	}

	public double getY()
	{
		return currentY;
	}

	public void setY( double y )
	{
		currentY = y;
	}

	public Grid getCurrentGrid()
	{
		return currentGrid;
	}

	public void setCurrentGrid( Grid currentGrid )
	{
		this.currentGrid = currentGrid;
	}
	
	public void update( long timeDiff )
	{
		
	}

}
