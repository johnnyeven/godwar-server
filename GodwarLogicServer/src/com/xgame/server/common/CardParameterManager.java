package com.xgame.server.common;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.xgame.server.cards.CardParameter;
import com.xgame.server.cards.HeroCardParameter;
import com.xgame.server.cards.SoulCardParameter;

public class CardParameterManager
{
	private Map< String, CardParameter >	cardMap;

	public CardParameterManager()
	{
		cardMap = new HashMap< String, CardParameter >();
	}

	public void initialize() throws ParserConfigurationException, SAXException,
			IOException
	{
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dbBuilder = dbFactory.newDocumentBuilder();

		Document doc = dbBuilder.parse( "soul_card_config.xml" );

		NodeList list = doc.getElementsByTagName( "card" );
		NodeList children;
		Node node;
		Node child;
		int length = list.getLength();
		SoulCardParameter parameter;
		for ( int i = 0; i < length; i++ )
		{
			node = list.item( i );
			children = node.getChildNodes();
			parameter = new SoulCardParameter();
			for ( int j = 0; j < children.getLength(); j++ )
			{
				child = children.item( j );
				if ( child.getNodeName() == "id" )
				{
					parameter.id = child.getTextContent().trim();
				}
				else if ( child.getNodeName() == "name" )
				{
					parameter.name = child.getTextContent().trim();
				}
				else if ( child.getNodeName() == "attack" )
				{
					parameter.attack = Integer.parseInt( child.getTextContent()
							.trim() );
				}
				else if ( child.getNodeName() == "def" )
				{
					parameter.def = Integer.parseInt( child.getTextContent()
							.trim() );
				}
				else if ( child.getNodeName() == "mdef" )
				{
					parameter.mdef = Integer.parseInt( child.getTextContent()
							.trim() );
				}
				else if ( child.getNodeName() == "health" )
				{
					parameter.health = Integer.parseInt( child.getTextContent()
							.trim() );
				}
				else if ( child.getNodeName() == "energy" )
				{
					parameter.energy = Integer.parseInt( child.getTextContent()
							.trim() );
				}
				else if ( child.getNodeName() == "level" )
				{
					parameter.level = Integer.parseInt( child.getTextContent()
							.trim() );
				}
				else if ( child.getNodeName() == "race" )
				{
					parameter.race = Integer.parseInt( child.getTextContent()
							.trim() );
				}
			}
			cardMap.put( parameter.id, parameter );
		}

		doc = dbBuilder.parse( "hero_card_config.xml" );
		list = doc.getElementsByTagName( "card" );
		length = list.getLength();
		HeroCardParameter parameter1;
		for ( int i = 0; i < length; i++ )
		{
			node = list.item( i );
			children = node.getChildNodes();
			parameter1 = new HeroCardParameter();
			for ( int j = 0; j < children.getLength(); j++ )
			{
				child = children.item( j );
				if ( child.getNodeName() == "id" )
				{
					parameter1.id = child.getTextContent().trim();
				}
				else if ( child.getNodeName() == "name" )
				{
					parameter1.name = child.getTextContent().trim();
				}
				else if ( child.getNodeName() == "nickname" )
				{
					parameter1.nickname = child.getTextContent().trim();
				}
				else if ( child.getNodeName() == "attack" )
				{
					parameter1.attack = Integer.parseInt( child
							.getTextContent().trim() );
				}
				else if ( child.getNodeName() == "def" )
				{
					parameter1.def = Integer.parseInt( child.getTextContent()
							.trim() );
				}
				else if ( child.getNodeName() == "mdef" )
				{
					parameter1.mdef = Integer.parseInt( child.getTextContent()
							.trim() );
				}
				else if ( child.getNodeName() == "hit" )
				{
					parameter1.hit = Integer.parseInt( child.getTextContent()
							.trim() );
				}
				else if ( child.getNodeName() == "flee" )
				{
					parameter1.flee = Integer.parseInt( child.getTextContent()
							.trim() );
				}
				else if ( child.getNodeName() == "health" )
				{
					parameter1.health = Integer.parseInt( child
							.getTextContent().trim() );
				}
				else if ( child.getNodeName() == "race" )
				{
					parameter1.race = Integer.parseInt( child.getTextContent()
							.trim() );
				}
			}
			cardMap.put( parameter1.id, parameter1 );
		}
	}

	public CardParameter getCard( String id )
	{
		if ( cardMap.containsKey( id ) )
		{
			return cardMap.get( id );
		}
		return null;
	}

	public static CardParameterManager getInstance()
	{
		return CardManagerHolder.instance;
	}

	private static class CardManagerHolder
	{
		private static CardParameterManager	instance	= new CardParameterManager();
	}

}
