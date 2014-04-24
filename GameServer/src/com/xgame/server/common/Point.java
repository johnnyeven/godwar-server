package com.xgame.server.common;

public class Point
{
	private double	x;
	private double	y;

	public Point( double x, double y )
	{
		this.x = x;
		this.y = y;
	}

	public Point()
	{
		this( 0, 0 );
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

	public double getLength()
	{
		return Math.sqrt( x * x + y * y );
	}

	public static double getDistance( Point p1, Point p2 )
	{
		double _x = Math.abs( p1.getX() - p2.getX() );
		double _y = Math.abs( p1.getY() - p2.getY() );
		return Math.sqrt( _x * _x + _y * _y );
	}
}
