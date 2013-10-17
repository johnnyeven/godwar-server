package com.xgame.server.game.astar;

public class Link
{

	public Node		node;
	public double	cost;

	public Link( Node node, double cost )
	{
		this.node = node;
		this.cost = cost;
	}

}
