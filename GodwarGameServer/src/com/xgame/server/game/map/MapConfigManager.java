package com.xgame.server.game.map;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.DOMReader;
import org.dom4j.io.SAXReader;

import com.xgame.server.common.parameter.InstancePortalParameter;
import com.xgame.server.common.parameter.MapPortalParameter;
import com.xgame.server.common.parameter.NPCParameter;
import com.xgame.server.common.parameter.PortalParameter;
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
			MapConfig c;
			try
			{
				c = loadMapConfig( id );
				configContainer.put( id, c );
				return c;
			}
			catch ( ParserConfigurationException | IOException
					| DocumentException e )
			{
				e.printStackTrace();
			}
			return null;
		}
	}

	private MapConfig loadMapConfig( int id )
			throws ParserConfigurationException, IOException, DocumentException
	{
		SAXReader reader = new SAXReader();
		Document doc = reader.read( GameServer.path + "data/map/" + id
				+ "/config.xml" );
		Element root = doc.getRootElement();

		MapConfig c = new MapConfig();
		c.id = Integer.parseInt( root.elementText( "id" ) );
		c.width = Integer.parseInt( root.elementText( "width" ) );
		c.height = Integer.parseInt( root.elementText( "height" ) );
		c.blockNumWidth = Integer
				.parseInt( root.elementText( "blockNumWidth" ) );
		c.blockNumHeight = Integer.parseInt( root
				.elementText( "blockNumHeight" ) );
		c.blockSizeWidth = (int) Math.floor( c.width / c.blockNumWidth );
		c.blockSizeHeight = (int) Math.floor( c.height / c.blockNumHeight );

		NPCParameter parameter;
		@SuppressWarnings( "rawtypes" )
		Iterator it;
		Element child;
		for ( it = root.element( "npcList" ).elementIterator( "npc" ); it
				.hasNext(); )
		{
			child = (Element) it.next();
			parameter = new NPCParameter();
			parameter.id = Integer.parseInt( child.elementText( "id" ) );
			parameter.prependName = child.elementText( "prependName" );
			parameter.name = child.elementText( "name" );
			parameter.level = Integer.parseInt( child.elementText( "level" ) );
			parameter.health = Integer.parseInt( child.elementText( "health" ) );
			parameter.mana = Integer.parseInt( child.elementText( "mana" ) );
			parameter.x = Integer.parseInt( child.elementText( "x" ) );
			parameter.y = Integer.parseInt( child.elementText( "y" ) );
			parameter.action = Integer.parseInt( child.elementText( "action" ) );
			parameter.direction = Integer.parseInt( child
					.elementText( "direction" ) );
			parameter.resource = child.elementText( "resource" );
			parameter.script = child.elementText( "script" );
			
			c.npcList.add( parameter );
		}

		PortalParameter portal;
		String type;
		Element child1;
		for ( it = root.element( "portalList" ).elementIterator( "portal" ); it
				.hasNext(); )
		{
			child = (Element) it.next();
			type = child.elementText( "type" );
			if ( type.equals( "instance" ) )
			{
				portal = new InstancePortalParameter();
			}
			else
			{
				portal = new MapPortalParameter();
			}
			portal.x = Integer.parseInt( child.elementText( "x" ) );
			portal.y = Integer.parseInt( child.elementText( "y" ) );
			portal.resourceId = child.elementText( "resource" );
			portal.rectX = Integer.parseInt( child.element( "rect" )
					.elementText( "x" ) );
			portal.rectY = Integer.parseInt( child.element( "rect" )
					.elementText( "y" ) );
			portal.rectWidth = Integer.parseInt( child.element( "rect" )
					.elementText( "width" ) );
			portal.rectHeight = Integer.parseInt( child.element( "rect" )
					.elementText( "height" ) );
			if ( type.equals( "instance" ) )
			{
				InstancePortalParameter instancePortal = (InstancePortalParameter) portal;
				for ( Iterator it1 = child.element( "instances" )
						.elementIterator( "instance" ); it1.hasNext(); )
				{
					child1 = (Element) it1.next();
					instancePortal.instanceList.add( Integer.parseInt( child1
							.getText() ) );
				}
			}
			else
			{
				portal = new MapPortalParameter();
			}
			
			c.portalList.add( portal );
		}

		return c;
	}
}
