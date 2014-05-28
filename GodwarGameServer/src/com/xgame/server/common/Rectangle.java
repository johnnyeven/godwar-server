package com.xgame.server.common;

public class Rectangle
{
	private double x;
	private double y;
	private double width;
	private double height;

	public Rectangle()
	{
		this(0, 0, 1, 1);
	}
	
	public Rectangle(double x, double y, double width, double height)
	{
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	public boolean inArea(double x, double y)
	{
		if(x >= this.x && x <= this.x + width && y >= this.y && y <= this.y + height)
		{
			return true;
		}
		return false;
	}
	
	public boolean inArea(Point p)
	{
		return inArea(p.getX(), p.getY());
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

	public double getWidth()
	{
		return width;
	}

	public void setWidth( double width )
	{
		this.width = width;
	}

	public double getHeight()
	{
		return height;
	}

	public void setHeight( double height )
	{
		this.height = height;
	}

}
