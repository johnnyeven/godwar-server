package com.xgame.server.cards;

import java.util.UUID;

public class Card
{
	private UUID id;
	private String name;
	private String series;
	private int level;
	private String comment;
	private String resource;
	
	public Card()
	{
		
	}

	public UUID getId()
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

	public int getLevel()
	{
		return level;
	}

	public String getComment()
	{
		return comment;
	}

	public String getResource()
	{
		return resource;
	}

}
