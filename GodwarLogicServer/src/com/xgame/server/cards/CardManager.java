package com.xgame.server.cards;

import java.util.HashMap;
import java.util.Map;

public class CardManager
{
	private Map< String, Card >	cardMap;

	public CardManager()
	{
		cardMap = new HashMap< String, Card >();
	}

	public void initialize()
	{

	}

	public Card getCard( String cardName )
	{
		if ( cardMap.containsKey( cardName ) )
		{
			return cardMap.get( cardName );
		}
		return null;
	}

	public static CardManager getInstance()
	{
		return CardManagerHolder.instance;
	}

	private static class CardManagerHolder
	{
		private static CardManager	instance	= new CardManager();
	}

}
