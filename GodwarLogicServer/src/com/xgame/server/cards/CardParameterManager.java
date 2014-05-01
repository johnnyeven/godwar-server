package com.xgame.server.cards;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.xgame.server.logic.LogicServer;

public class CardParameterManager
{
	private static Log						log	= LogFactory
														.getLog( CardParameterManager.class );
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
		
		log.info( "-----------------------加载英灵卡牌数据开始-----------------------" );
		Document doc = dbBuilder.parse( LogicServer.path + "soul_card_config.xml" );

		NodeList list = doc.getElementsByTagName( "card" );
		NodeList children;
		Node node;
		Node child;
		NodeList skills;
		Node skill;
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
				else if ( child.getNodeName() == "skills" )
				{
					skills = child.getChildNodes();
					for ( int m = 0; m < skills.getLength(); m++ )
					{
						skill = skills.item( m );
						if ( skill.getNodeName() == "skill" )
						{
							parameter.skillList.add( skill.getTextContent()
									.trim() );
						}
					}
				}
			}
			cardMap.put( parameter.id, parameter );
			log.info( "[新增英灵] Id = " + parameter.id + ", Name = " + parameter.name );
		}
		log.info( "-----------------------加载英灵卡牌数据结束-----------------------" );

		log.info( "-----------------------加载英雄卡牌数据开始-----------------------" );
		doc = dbBuilder.parse( LogicServer.path + "hero_card_config.xml" );
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
			log.info( "[新增英雄] Id = " + parameter1.id + ", Name = " + parameter1.name );
		}
		log.info( "-----------------------加载英雄卡牌数据结束-----------------------" );
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
