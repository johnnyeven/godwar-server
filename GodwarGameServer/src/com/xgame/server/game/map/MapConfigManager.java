package com.xgame.server.game.map;

import java.io.IOException;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.xgame.server.common.parameter.NPCParameter;
import com.xgame.server.game.GameServer;

public class MapConfigManager
{
	private static MapConfigManager			instance		= new MapConfigManager();
	private HashMap< Integer, MapConfig >	configContainer	= new HashMap< Integer, MapConfig >();

	private MapConfigManager()
	{

	}

	public static MapConfigManager getInstance()
	{
		if ( instance == null )
		{
			instance = new MapConfigManager();
		}
		return instance;
	}

	public MapConfig getConfig( int id )
	{
		if ( configContainer.containsKey( id ) )
		{
			return configContainer.get( id );
		}
		else
		{
			try
			{
				MapConfig c = loadMapConfig( id );
				configContainer.put( id, c );
				return c;
			}
			catch ( ParserConfigurationException | SAXException | IOException e )
			{
				e.printStackTrace();
			}
			return null;
		}
	}

	private MapConfig loadMapConfig( int id )
			throws ParserConfigurationException, SAXException, IOException
	{
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dbBuilder = dbFactory.newDocumentBuilder();
		Document doc = dbBuilder.parse( GameServer.path + "data/map/" + id
				+ "/config.xml" );

		MapConfig c = new MapConfig();
		c.id = Integer.parseInt( doc.getElementsByTagName( "id" ).item( 0 )
				.getTextContent() );
		c.width = Integer.parseInt( doc.getElementsByTagName( "width" )
				.item( 0 ).getTextContent() );
		c.height = Integer.parseInt( doc.getElementsByTagName( "height" )
				.item( 0 ).getTextContent() );
		c.blockNumWidth = Integer.parseInt( doc
				.getElementsByTagName( "blockNumWidth" ).item( 0 )
				.getTextContent() );
		c.blockNumHeight = Integer.parseInt( doc
				.getElementsByTagName( "blockNumHeight" ).item( 0 )
				.getTextContent() );
		c.blockSizeWidth = (int) Math.floor( c.width / c.blockNumWidth );
		c.blockSizeHeight = (int) Math.floor( c.height / c.blockNumHeight );

		NodeList childNodes = doc.getElementsByTagName( "npcList" ).item( 0 )
				.getChildNodes();
		NodeList propertyList;
		Node child, property;
		NPCParameter parameter;

		for ( int i = 0; i < childNodes.getLength(); i++ )
		{
			child = childNodes.item( i );

			if ( child.getNodeName() == "npc" )
			{
				propertyList = child.getChildNodes();
				parameter = new NPCParameter();

				for ( int j = 0; j < propertyList.getLength(); j++ )
				{
					property = propertyList.item( j );
					if ( property.getNodeName().equals( "id" ) )
					{
						parameter.id = Integer.parseInt( property
								.getTextContent().trim() );
					}
					else if ( property.getNodeName().equals( "prependName" ) )
					{
						parameter.prependName = property.getTextContent()
								.trim();
					}
					else if ( property.getNodeName().equals( "name" ) )
					{
						parameter.name = property.getTextContent().trim();
					}
					else if ( property.getNodeName().equals( "level" ) )
					{
						parameter.level = Integer.parseInt( property
								.getTextContent().trim() );
					}
					else if ( property.getNodeName().equals( "health" ) )
					{
						parameter.health = Integer.parseInt( property
								.getTextContent().trim() );
					}
					else if ( property.getNodeName().equals( "mana" ) )
					{
						parameter.mana = Integer.parseInt( property
								.getTextContent().trim() );
					}
					else if ( property.getNodeName().equals( "x" ) )
					{
						parameter.x = Integer.parseInt( property
								.getTextContent().trim() );
					}
					else if ( property.getNodeName().equals( "y" ) )
					{
						parameter.y = Integer.parseInt( property
								.getTextContent().trim() );
					}
					else if ( property.getNodeName().equals( "action" ) )
					{
						parameter.action = Integer.parseInt( property
								.getTextContent().trim() );
					}
					else if ( property.getNodeName().equals( "direction" ) )
					{
						parameter.direction = Integer.parseInt( property
								.getTextContent().trim() );
					}
					else if ( property.getNodeName().equals( "resource" ) )
					{
						parameter.resource = property.getTextContent().trim();
					}
					else if ( property.getNodeName().equals( "script" ) )
					{
						parameter.script = property.getTextContent().trim();
					}
				}
				c.npcList.add( parameter );
			}
		}

		return c;
	}
}
