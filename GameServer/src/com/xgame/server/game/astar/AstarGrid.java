package com.xgame.server.game.astar;

import java.util.ArrayList;

public class AstarGrid
{
	private Node		_startNode;
	private Node		_endNode;
	private Node[][]	_nodes;
	private int			_numCols;
	private int			_numRows;
	private int			type;
	private double		_straightCost	= 1.0;
	private double		_diagCost		= Math.sqrt( 2.0 );

	public AstarGrid( int numCols, int numRows )
	{
		_numCols = numCols;
		_numRows = numRows;
		_nodes = new Node[_numCols][_numRows];

		for ( int i = 0; i < _numCols; i++ )
		{
			for ( int j = 0; j < _numRows; j++ )
			{
				_nodes[i][j] = new Node( i, j );
			}
		}
	}

	/**
	 * 
	 * @param type
	 *            0八方向1四方向 2跳棋
	 */
	public void calculateLinks( int type )
	{
		this.type = type;
		for ( int i = 0; i < _numCols; i++ )
		{
			for ( int j = 0; j < _numRows; j++ )
			{
				initNodeLink( _nodes[i][j], type );
			}
		}
	}

	public void calculateLinks()
	{
		calculateLinks( 0 );
	}

	public int getType()
	{
		return type;
	}

	/**
	 * 
	 * @param node
	 * @param type
	 *            0八方向 1四方向 2跳棋
	 */
	private void initNodeLink( Node node, int type )
	{
		int startX = Math.max( 0, node.x - 1 );
		int endX = Math.min( _numCols - 1, node.x + 1 );
		int startY = Math.max( 0, node.y - 1 );
		int endY = Math.min( _numRows - 1, node.y + 1 );
		node.links = new ArrayList< Link >();
		for ( int i = startX; i <= endX; i++ )
		{
			for ( int j = startY; j <= endY; j++ )
			{
				Node test = getNode( i, j );
				if ( test == node || !test.walkable )
				{
					continue;
				}
				if ( type != 2 && i != node.x && j != node.y )
				{
					Node test2 = getNode( node.x, j );
					if ( !test2.walkable )
					{
						continue;
					}
					test2 = getNode( i, node.y );
					if ( !test2.walkable )
					{
						continue;
					}
				}
				double cost = _straightCost;
				if ( !( ( node.x == test.x ) || ( node.y == test.y ) ) )
				{
					if ( type == 1 )
					{
						continue;
					}
					if ( type == 2
							&& ( node.x - test.x ) * ( node.y - test.y ) == 1 )
					{
						continue;
					}
					if ( type == 2 )
					{
						cost = _straightCost;
					}
					else
					{
						cost = _diagCost;
					}
				}
				node.links.add( new Link( test, cost ) );
			}
		}
	}

	public Node getNode( int x, int y )
	{
		return _nodes[x][y];
	}

	public void setEndNode( int x, int y )
	{
		_endNode = _nodes[x][y];
	}

	public void setStartNode( int x, int y )
	{
		_startNode = _nodes[x][y];
	}

	public void setWalkable( int x, int y, boolean value )
	{
		_nodes[x][y].walkable = value;
	}

	public Node getEndNode()
	{
		return _endNode;
	}

	public int getNumCols()
	{
		return _numCols;
	}

	public int getNumRows()
	{
		return _numRows;
	}

	public Node getStartNode()
	{
		return _startNode;
	}

}
