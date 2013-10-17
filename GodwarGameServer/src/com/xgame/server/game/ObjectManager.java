package com.xgame.server.game;

import java.util.Iterator;
import java.util.UUID;
import java.util.Map.Entry;

import com.xgame.server.cards.Player;
import com.xgame.server.hashmap.PlayerMap;

public class ObjectManager
{
	private static ObjectManager	instance;

	private ObjectManager()
	{

	}

	public static ObjectManager getInstance()
	{
		if ( instance == null )
		{
			instance = new ObjectManager();
		}
		return instance;
	}
	
	public void addPlayer(Player target)
	{
		PlayerMap.getInstance().add( target );
	}

	public Player getPlayer( UUID guid )
	{
		return PlayerMap.getInstance().get( guid );
	}

	public Player getPlayerByName( String name )
	{
		Iterator< Entry< UUID, Player >> it = PlayerMap.getInstance()
				.getIterator();
		Entry< UUID, Player > en;
		Player p;
		while ( it.hasNext() )
		{
			en = it.next();
			p = en.getValue();

			if ( p.name == name )
			{
				return p;
			}
		}
		return null;
	}

	public Player getObject( UUID guid, Player p )
	{
		return PlayerMap.getInstance().get( guid );
	}

	public void update( long timeDiff )
	{
		Iterator< Entry< UUID, Player >> it = PlayerMap.getInstance()
				.getIterator();
		Entry< UUID, Player > en;
		Player p;
		while ( it.hasNext() )
		{
			en = it.next();
			p = en.getValue();

			p.update( timeDiff );
		}
	}
}
