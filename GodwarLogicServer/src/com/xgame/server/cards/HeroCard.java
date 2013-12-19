package com.xgame.server.cards;

public class HeroCard extends RoleCard
{
	private String	nickname;
	private int		hit;
	private int		flee;
	private int		race;

	public HeroCard()
	{
		super();
	}

	public HeroCard( String id )
	{
		super( id );
	}

	public String getNickname()
	{
		return nickname;
	}

	public void setNickname( String nickname )
	{
		this.nickname = nickname;
	}

	public int getHit()
	{
		return hit;
	}

	public void setHit( int hit )
	{
		this.hit = hit;
	}

	public int getFlee()
	{
		return flee;
	}

	public void setFlee( int flee )
	{
		this.flee = flee;
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

		HeroCardParameter param = (HeroCardParameter) parameter;
		if ( param != null )
		{
			nickname = param.nickname;
			hit = param.hit;
			flee = param.flee;
			race = param.race;
		}
	}

}
