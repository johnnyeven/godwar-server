package com.xgame.server.cards;


public class Card
{
	private String			id;
	private String			name;
	private int				energy;
	private String			series;
	protected CardParameter	parameter;

	public Card()
	{

	}

	public Card( String id )
	{
		this.id = id;
		loadInfo();
	}

	public void loadInfo( String id )
	{
		this.id = id;
		loadInfo();
	}

	public void loadInfo()
	{
		if ( id != null )
		{
			CardParameter param = CardParameterManager.getInstance().getCard(
					id );
			if ( param != null )
			{
				parameter = param;
				name = parameter.name;
				energy = parameter.energy;
				series = parameter.series;
			}
		}
	}

	public String getId()
	{
		return id;
	}

	public String getName()
	{
		return name;
	}

	public String getSeries()
	{
		return series;
	}

	public int getEnergy()
	{
		return energy;
	}

	public String toString()
	{
		return id;
	}

}
