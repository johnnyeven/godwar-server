package com.xgame.server.common;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
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

public class ScriptManager
{
	private Map< String, Reader >	scriptList;
	private static Log				log	= LogFactory
												.getLog( ScriptManager.class );

	private ScriptManager()
	{
		scriptList = new HashMap< String, Reader >();
	}

	public void initialize() throws ParserConfigurationException, SAXException,
			IOException
	{
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dbBuilder = dbFactory.newDocumentBuilder();

		Document doc = dbBuilder.parse( "soul_card_config.xml" );

		NodeList list = doc.getElementsByTagName( "card" );
		Node node;
		NodeList children;
		Node child;
		NodeList skills;
		NodeList attributes;
		Node attribute;
		String id = null;
		String script = null;

		log.info( "[InitScript] 初始化脚本" );
		for ( int m = 0; m < list.getLength(); m++ )
		{
			node = list.item( m );
			children = node.getChildNodes();
			for ( int i = 0; i < children.getLength(); i++ )
			{
				child = children.item( i );
				if ( child.getNodeName() == "skills" )
				{
					skills = child.getChildNodes();
					for ( int j = 0; j < skills.getLength(); j++ )
					{
						attributes = skills.item( j ).getChildNodes();
						for ( int x = 0; x < attributes.getLength(); x++ )
						{
							attribute = attributes.item( x );
							if ( attribute.getNodeName() == "id" )
							{
								id = attribute.getTextContent().trim();
							}
							else if ( attribute.getNodeName() == "script" )
							{
								script = attribute.getTextContent().trim();
							}
						}
						if ( id != null && script != null )
						{
							add( id, script );
							id = null;
							script = null;
						}
					}
				}
			}
		}
		log.info( "[InitScriptComplete] 初始化脚本结束" );
	}

	public void add( String key, Reader reader )
	{
		if ( !scriptList.containsKey( key ) )
		{
			scriptList.put( key, reader );
			log.info( "[AddScript] Id = " + key + ", Script = "
					+ reader.toString() );
		}
	}

	public void add( String key, String script )
	{
		if ( !scriptList.containsKey( key ) )
		{
			try
			{
				Reader reader = new FileReader( script );
				scriptList.put( key, reader );
				log.info( "[AddScript] Id = " + key + ", Script = " + script );
			}
			catch ( FileNotFoundException e )
			{
				e.printStackTrace();
			}
		}
	}

	public Reader get( String key )
	{
		if ( scriptList.containsKey( key ) )
		{
			return scriptList.get( key );
		}
		return null;
	}

	public static ScriptManager getInstance()
	{
		return ScriptManagerHolder.instance;
	}

	private static class ScriptManagerHolder
	{
		private static ScriptManager	instance	= new ScriptManager();
	}

	// public static void main( String[] args )
	// {
	// ScriptEngineManager factory = new ScriptEngineManager();
	// ScriptEngine engine = factory.getEngineByName( "JavaScript" );
	// Room room = new Room();
	// Attacker att1 = new Attacker();
	// att1.setDef( 75 );
	// Attacker att2 = new Attacker();
	// att2.setDef( 98 );
	// Attacker att3 = new Attacker();
	// att3.setDef( 45 );
	// Attacker att4 = new Attacker();
	// att4.setDef( 435345 );
	// room.add( att1 );
	// room.add( att2 );
	// room.add( att3 );
	// room.add( att4 );
	// // engine.put("attacker", att1);
	// // engine.put("room", room);
	// Reader r;
	// try
	// {
	// r = new FileReader( "test.js" );
	// }
	// catch ( FileNotFoundException e1 )
	// {
	// e1.printStackTrace();
	// return;
	// }
	//
	// try
	// {
	// engine.eval( r );
	// Invocable inv = (Invocable) engine;
	// IScript s = inv.getInterface( IScript.class );
	// List< AttackLog > log = s.attackArea( att1, room );
	// for ( int i = 0; i < log.size(); i++ )
	// {
	// System.out.println( log.get( i ).def );
	// }
	// }
	// catch ( ScriptException e )
	// {
	// e.printStackTrace();
	// }
	// }

}
