package com.xgame.server.objects;

public class MapPortal extends Portal
{
	private int destinationMapId;
	private int destinationX;
	private int destinationY;

	public MapPortal()
	{
		super();
	}

	public int getDestinationMapId()
	{
		return destinationMapId;
	}

	public void setDestinationMapId( int destinationMapId )
	{
		this.destinationMapId = destinationMapId;
	}

	public int getDestinationX()
	{
		return destinationX;
	}

	public void setDestinationX( int destinationX )
	{
		this.destinationX = destinationX;
	}

	public int getDestinationY()
	{
		return destinationY;
	}

	public void setDestinationY( int destinationY )
	{
		this.destinationY = destinationY;
	}

}
