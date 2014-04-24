package com.xgame.server.objects;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.UUID;

import com.xgame.server.events.AOIEvent;
import com.xgame.server.events.EventManager;
import com.xgame.server.game.map.Grid;

public class InteractiveObject extends WorldObject
{
	private ArrayList< WorldObject >	lastInteractiveList;
	private ArrayList< WorldObject >	interactiveList;
	private ArrayList< WorldObject >	dropedInteractive;
	private ArrayList< WorldObject >	newInteractive;

	public InteractiveObject()
	{
		super();
		lastInteractiveList = new ArrayList< WorldObject >();
		interactiveList = new ArrayList< WorldObject >();
		dropedInteractive = new ArrayList< WorldObject >();
		newInteractive = new ArrayList< WorldObject >();
	}

	public void update( long timeDiff )
	{
		interactiveList.clear();
		Grid g = getCurrentGrid();

		ArrayList< Grid > glist = getMap().getViewGrid( g );
		Grid currentGrid;
		Iterator< Entry< UUID, WorldObject >> iten;
		Entry< UUID, WorldObject > en;
		WorldObject obj;
		int i = 0;
		for ( ; i < glist.size(); i++ )
		{
			currentGrid = glist.get( i );
			if ( currentGrid == null )
			{
				continue;
			}
			iten = currentGrid.getWorldObjectIterator();
			while ( iten.hasNext() )
			{
				en = iten.next();
				obj = en.getValue();
				if ( obj != this )
				{
					interactiveList.add( en.getValue() );
				}
			}
		}
		compareInteracting();
		
		lastInteractiveList.clear();
		lastInteractiveList.addAll( interactiveList );
	}

	private void compareInteracting()
	{
		diff( lastInteractiveList, interactiveList, dropedInteractive );
		diff( interactiveList, lastInteractiveList, newInteractive );

		WorldObject current;
		int i = 0;
		for ( ; i < newInteractive.size(); i++ )
		{
			current = newInteractive.get( i );
			AOIEvent evt = new AOIEvent(AOIEvent.AOI_ENTER);
			evt.who = current;
			EventManager.getInstance().dispatchEvent( this, evt );
		}
		for ( i = 0; i < dropedInteractive.size(); i++ )
		{
			current = dropedInteractive.get( i );
			AOIEvent evt = new AOIEvent(AOIEvent.AOI_LEAVE);
			evt.who = current;
			EventManager.getInstance().dispatchEvent( this, evt );
		}
	}

	private static ArrayList< WorldObject > diff( ArrayList< WorldObject > ls,
			ArrayList< WorldObject > ls2, ArrayList< WorldObject > target )
	{
		if ( target != null )
		{
			target.clear();
			target.addAll( ls );
		}
		else
		{
			target = new ArrayList< WorldObject >(ls);
		}
		
		target.removeAll( ls2 );
		return target;
	}

	public ArrayList< WorldObject > getDropedInteractive()
	{
		return dropedInteractive;
	}

	public ArrayList< WorldObject > getNewInteractive()
	{
		return newInteractive;
	}

}
