package com.xgame.server.common;

public class CoordinatePair
{
	private double	x;
	private double	y;

	public CoordinatePair( double x, double y )
	{
		this.x = x;
		this.y = y;
	}

	public CoordinatePair()
	{
		x = 0.0;
		y = 0.0;
	}

	public double getX()
	{
		return x;
	}

	public void setX( double x )
	{
		this.x = x;
	}

	public double getY()
	{
		return y;
	}

	public void setY( double y )
	{
		this.y = y;
	}

}
