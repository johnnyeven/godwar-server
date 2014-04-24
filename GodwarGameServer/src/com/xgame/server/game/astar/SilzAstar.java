package com.xgame.server.game.astar;

import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * ...
 * 
 * @author sliz http://game-develop.net/
 */

public class SilzAstar
{
	private static int			WorkMode	= 8;
	private AstarGrid			_grid;
	private ArrayList< Node >	_path;
	private AStar				astar;
	private int					blockSizeWidth;
	private int					blockSizeHeight;

	private static Log			log			= LogFactory
													.getLog( SilzAstar.class );

	public SilzAstar( boolean[][] mapdata, int blockSizeWidth,
			int blockSizeHeight )
	{
		this.blockSizeWidth = blockSizeWidth;
		this.blockSizeHeight = blockSizeHeight;

		makeGrid( mapdata );
	}

	public ArrayList< Node > find( int xnow, int ynow, int xpos, int ypos )
	{
		xpos = xpos / blockSizeWidth;
		ypos = ypos / blockSizeHeight;
		xpos = Math.min( xpos, _grid.getNumCols() - 1 );
		ypos = Math.min( ypos, _grid.getNumRows() - 1 );
		_grid.setEndNode( xpos, ypos );

		xnow = xnow / blockSizeWidth;
		ynow = ynow / blockSizeHeight;

		_grid.setStartNode( xnow, ynow );

		if ( astar.findPath() )
		{
			astar.floyd();
			_path = astar.getFloydPath();

			return _path;
		}

		return null;
	}

	private void makeGrid( boolean[][] data )
	{
		int rows = data.length;
		int cols = data[0].length;
		_grid = new AstarGrid( cols, rows );

		int px;
		int py;

		for ( py = 0; py < rows; py++ )
		{
			for ( px = 0; px < cols; px++ )
			{
				_grid.setWalkable( px, py, data[py][px] );
			}
		}

		if ( WorkMode == 4 ) _grid.calculateLinks( 1 );
		else _grid.calculateLinks();

		astar = new AStar( _grid );
	}
}
