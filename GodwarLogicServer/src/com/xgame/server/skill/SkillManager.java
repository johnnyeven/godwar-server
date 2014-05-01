package com.xgame.server.skill;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.xgame.server.cards.SoulCard;
import com.xgame.server.logic.LogicServer;
import com.xgame.server.logic.Player;

public class SkillManager
{
	ScriptEngineManager						factory	= new ScriptEngineManager();
	private Map< String, SkillParameter >	scriptList;
	private static Log						log		= LogFactory
															.getLog( SkillManager.class );

	private SkillManager()
	{
		scriptList = new HashMap< String, SkillParameter >();
	}

	public void initialize() throws ParserConfigurationException, SAXException,
			IOException, ScriptException
	{
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dbBuilder = dbFactory.newDocumentBuilder();

		Document doc = dbBuilder.parse( LogicServer.path + "conf/skill_config.xml" );

		NodeList list = doc.getElementsByTagName( "skill" );
		Node node;
		NodeList children;
		Node child;
		Reader reader;
		ScriptEngine engine;
		Invocable inv;
		String path;
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
					path = child.getTextContent();
					if(path != null && !path.equals( "" ))
					{
						reader = new FileReader( path.trim() );
						engine = factory.getEngineByName( "JavaScript" );
						engine.eval( reader );
						inv = (Invocable) engine;
						parameter.script = inv.getInterface( IScript.class );
					}
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

	public List< AttackInfo > execute( Player attacker, Player defender,
			String skillId, SoulCard attackerCard, SoulCard defenderCard )
			throws ScriptException
	{
		if ( scriptList.containsKey( skillId ) )
		{
			SkillParameter skill = scriptList.get( skillId );
			if ( skill != null && skill.script != null )
			{
				List< AttackInfo > info = skill.script.attack( skillId, attacker,
						defender, attackerCard, defenderCard );
				return info;
			}
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

}
