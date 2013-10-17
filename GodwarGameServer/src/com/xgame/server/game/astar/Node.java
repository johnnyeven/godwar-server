package com.xgame.server.game.astar;

import java.util.List;

public class Node
{

	public int			x;
	public int			y;
	public double		f;
	public double		g;
	public double		h;
	public boolean		walkable	= true;
	public Node			parent;
	public int			version		= 1;
	public List< Link >	links;

	public Node( int x, int y )
	{
		this.x = x;
		this.y = y;
	}

	public String toString()
	{
		return "x:" + x + " y:" + y;
	}

}
