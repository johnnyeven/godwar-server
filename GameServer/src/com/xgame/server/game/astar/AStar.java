package com.xgame.server.game.astar;

import java.util.ArrayList;

public class AStar
{
	private BinaryHeap			_open;
	private AstarGrid			_grid;
	private Node				_endNode;
	private Node				_startNode;
	private ArrayList< Node >	_path;
	private ArrayList< Node >	_floydPath;
	private double				_straightCost	= 1.0;
	private double				_diagCost		= Math.sqrt( 2 );
	private int					nowversion		= 1;
	private double				TwoOneTwoZero	= 2 * Math.cos( Math.PI / 3 );

	public AStar( AstarGrid grid )
	{
		this._grid = grid;
		_floydPath = new ArrayList< Node >();
	}

	public boolean findPath()
	{
		_endNode = _grid.getEndNode();
		nowversion++;
		_startNode = _grid.getStartNode();
		_open = new BinaryHeap();
		_startNode.g = 0;
		return search();
	}

	@SuppressWarnings( "unchecked" )
	public void floyd()
	{
		if ( _path == null ) return;
		_floydPath = (ArrayList< Node >) _path.clone();
		int len = _floydPath.size();
		if ( len > 2 )
		{
			Node vector = new Node( 0, 0 );
			Node tempVector = new Node( 0, 0 );
			floydVector( vector, _floydPath.get( len - 1 ),
					_floydPath.get( len - 2 ) );
			for ( int i = _floydPath.size() - 3; i >= 0; i-- )
			{
				floydVector( tempVector, _floydPath.get( i + 1 ),
						_floydPath.get( i ) );
				if ( vector.x == tempVector.x && vector.y == tempVector.y )
				{
					_floydPath.remove( i + 1 );
				}
				else
				{
					vector.x = tempVector.x;
					vector.y = tempVector.y;
				}
			}
		}
		len = _floydPath.size();
		for ( int i = len - 1; i >= 0; i-- )
		{
			for ( int j = 0; j <= i - 2; j++ )
			{
				if ( floydCrossAble( _floydPath.get( i ), _floydPath.get( j ) ) )
				{
					for ( int k = i - 1; k > j; k-- )
					{
						_floydPath.remove( k );
					}
					i = j;
					len = _floydPath.size();
					break;
				}
			}
		}
	}

	private boolean floydCrossAble( Node n1, Node n2 )
	{
		ArrayList< Point > ps = bresenhamNodes( new Point( n1.x, n1.y ),
				new Point( n2.x, n2.y ) );
		for ( int i = ps.size() - 2; i > 0; i-- )
		{
			if ( !_grid.getNode( (int) ps.get( i ).getX(), (int) ps.get( i )
					.getY() ).walkable )
			{
				return false;
			}
		}
		return true;
	}

	private ArrayList< Point > bresenhamNodes( Point p1, Point p2 )
	{
		boolean steep = Math.abs( p2.getY() - p1.getY() ) > Math.abs( p2.getX()
				- p1.getX() );
		if ( steep )
		{
			double temp = p1.getX();
			p1.setX( p1.getY() );
			p1.setY( temp );
			temp = p2.getX();
			p2.setX( p2.getY() );
			p2.setY( temp );
		}
		int stepX = p2.getX() > p1.getX() ? 1 : ( p2.getX() < p1.getX() ? -1
				: 0 );
		double deltay = (double) ( p2.getY() - p1.getY() )
				/ Math.abs( p2.getX() - p1.getX() );
		ArrayList< Point > ret = new ArrayList< Point >();
		double nowX = p1.getX() + stepX;
		double nowY = p1.getY() + deltay;
		if ( steep )
		{
			ret.add( new Point( p1.getY(), p1.getX() ) );
		}
		else
		{
			ret.add( new Point( p1.getX(), p1.getY() ) );
		}
		while ( nowX != p2.getX() )
		{
			int fy = (int) Math.floor( nowY );
			int cy = (int) Math.ceil( nowY );
			if ( steep )
			{
				ret.add( new Point( fy, nowX ) );
			}
			else
			{
				ret.add( new Point( nowX, fy ) );
			}
			if ( fy != cy )
			{
				if ( steep )
				{
					ret.add( new Point( cy, nowX ) );
				}
				else
				{
					ret.add( new Point( nowX, cy ) );
				}
			}
			nowX += stepX;
			nowY += deltay;
		}
		if ( steep )
		{
			ret.add( new Point( p2.getY(), p2.getX() ) );
		}
		else
		{
			ret.add( new Point( p2.getX(), p2.getY() ) );
		}
		return ret;
	}

	private void floydVector( Node target, Node n1, Node n2 )
	{
		target.x = n1.x - n2.x;
		target.y = n1.y - n2.y;
	}

	public boolean search()
	{
		Node node = _startNode;
		node.version = nowversion;
		while ( node != _endNode )
		{
			int len = node.links.size();
			for ( int i = 0; i < len; i++ )
			{
				Node test = node.links.get( i ).node;
				double cost = node.links.get( i ).cost;
				double g = node.g + cost;
				double h = euclidian2( test );
				double f = g + h;
				if ( test.version == nowversion )
				{
					if ( test.f > f )
					{
						test.f = f;
						test.g = g;
						test.h = h;
						test.parent = node;
					}
				}
				else
				{
					test.f = f;
					test.g = g;
					test.h = h;
					test.parent = node;
					_open.ins( test );
					test.version = nowversion;
				}
			}
			if ( _open.a.size() == 1 )
			{
				return false;
			}
			node = _open.pop();
		}
		buildPath();
		return true;
	}

	private void buildPath()
	{
		_path = new ArrayList< Node >();
		Node node = _endNode;
		_path.add( node );
		while ( node != _startNode )
		{
			node = node.parent;
			_path.add( 0, node );
		}
	}

	public ArrayList< Node > getPath()
	{
		return _path;
	}

	public ArrayList< Node > getFloydPath()
	{
		return _floydPath;
	}

	public double manhattan( Node node )
	{
		return Math.abs( node.x - _endNode.x ) + Math.abs( node.y - _endNode.y );
	}

	public double manhattan2( Node node )
	{
		double dx = Math.abs( node.x - _endNode.x );
		double dy = Math.abs( node.y - _endNode.y );
		return dx + dy + Math.abs( dx - dy ) / 1000;
	}

	public double euclidian( Node node )
	{
		double dx = node.x - _endNode.x;
		double dy = node.y - _endNode.y;
		return Math.sqrt( dx * dx + dy * dy );
	}

	public double chineseCheckersEuclidian2( Node node )
	{
		int y = (int) ( node.y / TwoOneTwoZero );
		int x = node.x + node.y / 2;
		double dx = x - _endNode.x - _endNode.y / 2;
		double dy = y - _endNode.y / TwoOneTwoZero;
		return sqrt( dx * dx + dy * dy );
	}

	private double sqrt( double x )
	{
		return Math.sqrt( x );
	}

	public double euclidian2( Node node )
	{
		double dx = node.x - _endNode.x;
		double dy = node.y - _endNode.y;
		return dx * dx + dy * dy;
	}

	public double diagonal( Node node )
	{
		double dx = Math.abs( node.x - _endNode.x );
		double dy = Math.abs( node.y - _endNode.y );
		double diag = Math.min( dx, dy );
		double straight = dx + dy;
		return _diagCost * diag + _straightCost * ( straight - 2 * diag );
	}
}