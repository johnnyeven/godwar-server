package com.xgame.server.skill;

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

public class SkillManager
{
	private Map< String, SkillParameter >	scriptList;
	private static Log						log	= LogFactory
														.getLog( SkillManager.class );

	private SkillManager()
	{
		scriptList = new HashMap< String, SkillParameter >();
	}

	public void initialize() throws ParserConfigurationException, SAXException,
			IOException
	{
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dbBuilder = dbFactory.newDocumentBuilder();

		Document doc = dbBuilder.parse( "conf/skill_config.xml" );

		NodeList list = doc.getElementsByTagName( "skill" );
		Node node;
		NodeList children;
		Node child;
		SkillParameter parameter = new SkillParameter();

		log.info( "[InitSkill] 初始化技能" );
		for ( int m = 0; m < list.getLength(); m++ )
		{
			node = list.item( m );
			children = node.getChildNodes();
			for ( int i = 0; i < children.getLength(); i++ )
			{
				child = children.item( i );
				if ( child.getNodeName() == "id" )
				{
					parameter.id = child.getTextContent().trim();
				}
				else if ( child.getNodeName() == "level" )
				{
					parameter.level = Integer.parseInt( child.getTextContent()
							.trim() );
				}
				else if ( child.getNodeName() == "name" )
				{
					parameter.name = child.getTextContent().trim();
				}
				else if ( child.getNodeName() == "target" )
				{
					parameter.target = child.getTextContent().trim();
				}
				else if ( child.getNodeName() == "script" )
				{
					parameter.script = new FileReader( child.getTextContent()
							.trim() );
				}

				if ( parameter.id != null && parameter.level > 0
						&& parameter.name != null && parameter.target != null
						&& parameter.script != null )
				{
					add( parameter.id, parameter );
					parameter = new SkillParameter();
				}
			}
		}
		log.info( "[InitSkillComplete] 初始化技能结束" );
	}

	public void add( String key, SkillParameter parameter )
	{
		if ( !scriptList.containsKey( key ) )
		{
			scriptList.put( key, parameter );
			log.info( "[AddSkill] Id = " + key + ", Script = "
					+ parameter.script.toString() );
		}
	}

	public SkillParameter get( String key )
	{
		if ( scriptList.containsKey( key ) )
		{
			return scriptList.get( key );
		}
		return null;
	}

	public static SkillManager getInstance()
	{
		return ScriptManagerHolder.instance;
	}

	private static class ScriptManagerHolder
	{
		private static SkillManager	instance	= new SkillManager();
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
