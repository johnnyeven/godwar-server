package com.xgame.server.game.astar;

import java.util.ArrayList;
import java.util.List;

public class BinaryHeap
{

	public List< Node >	a	= new ArrayList< Node >();

	public BinaryHeap()
	{
		a.add( new Node( -1, -1 ) );
	}

	public boolean justMinFun( Node x, Node y )
	{
		return x.f < y.f;
	}

	public void ins( Node value )
	{
		int p = a.size();
		a.add( p, value );
		int pp = p >> 1;
		while ( p > 1 && justMinFun( a.get( p ), a.get( pp ) ) )
		{
			Node temp = a.get( p );
			a.set( p, a.get( pp ) );
			a.set( pp, temp );
			p = pp;
			pp = p >> 1;
		}
	}

	public Node pop()
	{
		Node min = a.get( 1 );
		a.set( 1, a.get( a.size() - 1 ) );
		a.remove( a.size() - 1 );
		int p = 1;
		int l = a.size();
		int sp1 = p << 1;
		int sp2 = sp1 + 1;
		int minp;
		while ( sp1 < l )
		{
			if ( sp2 < l )
			{
				minp = justMinFun( a.get( sp2 ), a.get( sp1 ) ) ? sp2 : sp1;
			}
			else
			{
				minp = sp1;
			}
			if ( justMinFun( a.get( minp ), a.get( p ) ) )
			{
				Node temp = a.get( p );
				a.set( p, a.get( minp ) );
				a.set( minp, temp );
				p = minp;
				sp1 = p << 1;
				sp2 = sp1 + 1;
			}
			else
			{
				break;
			}
		}
		return min;
	}

}
