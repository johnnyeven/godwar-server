package com.xgame.server.cards;

public class Card
{
	private String			id;
	private String			name;
	private String			series;
	protected CardParameter	parameter;

	public Card( String id )
	{
		this.id = id;
		loadInfo();
	}

	protected void loadInfo()
	{
		if ( id != null )
		{
			CardParameter param = CardParameterManager.getInstance().getCard(
					id );
			if ( param != null )
			{
				parameter = param;
				name = parameter.name;
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

}
