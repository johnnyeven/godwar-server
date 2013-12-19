package com.xgame.server.cards;

public class SoulCard extends RoleCard
{
	private int	level;
	private int	race;

	public SoulCard()
	{
		super();
	}

	public SoulCard( String id )
	{
		super( id );
	}

	public int getLevel()
	{
		return level;
	}

	public void setLevel( int level )
	{
		this.level = level;
	}

	public int getRace()
	{
		return race;
	}

	public void setRace( int race )
	{
		this.race = race;
	}

	public void loadInfo()
	{
		super.loadInfo();

		SoulCardParameter param = (SoulCardParameter) parameter;
		if ( param != null )
		{
			level = param.level;
			race = param.race;
		}
	}

}
