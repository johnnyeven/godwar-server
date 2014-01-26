package com.xgame.server.cards;

import java.util.ArrayList;
import java.util.List;

public class SoulCard extends RoleCard
{
	private int				level;
	private int				race;
	private List< String >	skillList;

	public SoulCard()
	{
		super();
		skillList = new ArrayList< String >();
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

	public List< String > getSkillList()
	{
		return skillList;
	}

	public void loadInfo()
	{
		super.loadInfo();

		SoulCardParameter param = ( SoulCardParameter ) parameter;
		if ( param != null )
		{
			level = param.level;
			race = param.race;
			for ( int i = 0; i < param.skillList.size(); i++ )
			{
				skillList.add( param.skillList.get( i ) );
			}
		}
	}

}
