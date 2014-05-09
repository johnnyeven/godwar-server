package com.xgame.server.objects;

import java.util.Iterator;
import java.util.UUID;
import java.util.Map.Entry;

import com.xgame.server.events.AOIEvent;
import com.xgame.server.events.AOIEventHandler;
import com.xgame.server.events.EventManager;
import com.xgame.server.objects.hashmap.NPCMap;
import com.xgame.server.objects.hashmap.PlayerMap;

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

	public void addObject( WorldObject target )
	{
		if ( target instanceof InteractiveObject )
		{
			EventManager.getInstance().addEventListener(
					( (InteractiveObject) target ), AOIEvent.AOI_ENTER,
					AOIEventHandler.getInstance() );
			EventManager.getInstance().addEventListener(
					( (InteractiveObject) target ), AOIEvent.AOI_LEAVE,
					AOIEventHandler.getInstance() );
		}

		if ( target instanceof Player )
		{
			addPlayer( (Player) target );
		}
		else if ( target instanceof NPC )
		{
			addNPC( (NPC) target );
		}
	}

	public void addPlayer( Player target )
	{
		PlayerMap.getInstance().add( target );
	}

	public Player getPlayer( UUID guid )
	{
		return PlayerMap.getInstance().get( guid.toString() );
	}

	public Player getPlayer( String guid )
	{
		return PlayerMap.getInstance().get( guid );
	}

	public Player getPlayerByName( String name )
	{
		Iterator< Entry< String, Player >> it = PlayerMap.getInstance()
				.getIterator();
		Entry< String, Player > en;
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

	public void addNPC( NPC target )
	{
		NPCMap.getInstance().add( target );
	}

	public NPC getNPC( UUID guid )
	{
		return NPCMap.getInstance().get( guid.toString() );
	}

	public NPC getNPC( String guid )
	{
		return NPCMap.getInstance().get( guid );
	}

	public void update( long timeDiff )
	{
		Iterator< Entry< String, Player >> it = PlayerMap.getInstance()
				.getIterator();
		Entry< String, Player > en;
		Player p;
		while ( it.hasNext() )
		{
			en = it.next();
			p = en.getValue();

			p.update( timeDiff );
		}
	}
}
