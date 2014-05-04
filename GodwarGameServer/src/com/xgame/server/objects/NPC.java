package com.xgame.server.objects;

import com.xgame.server.enums.Action;
import com.xgame.server.enums.Direction;
import com.xgame.server.scripts.INPCScript;

public class NPC extends WorldObject
{
	private int			id;
	private String		prependName;
	private String		name;
	private int			level;
	private int			health;
	private int			mana;
	private int			direction	= Direction.DOWN;
	private int			action		= Action.STOP;
	private INPCScript	script;

	public NPC()
	{

	}

	public int getId()
	{
		return id;
	}

	public void setId( int id )
	{
		this.id = id;
	}

	public String getPrependName()
	{
		return prependName;
	}

	public void setPrependName( String prependName )
	{
		this.prependName = prependName;
	}

	public String getName()
	{
		return name;
	}

	public void setName( String name )
	{
		this.name = name;
	}

	public int getLevel()
	{
		return level;
	}

	public void setLevel( int level )
	{
		this.level = level;
	}

	public int getHealth()
	{
		return health;
	}

	public void setHealth( int health )
	{
		this.health = health;
	}

	public int getMana()
	{
		return mana;
	}

	public void setMana( int mana )
	{
		this.mana = mana;
	}

	public int getDirection()
	{
		return direction;
	}

	public void setDirection( int direction )
	{
		this.direction = direction;
	}

	public int getAction()
	{
		return action;
	}

	public void setAction( int action )
	{
		this.action = action;
	}

	public INPCScript getScript()
	{
		return script;
	}

	public void setScript( INPCScript script )
	{
		this.script = script;
	}

}
