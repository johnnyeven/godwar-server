package com.xgame.server.scripts;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xgame.server.game.GameServer;

public class NPCScriptManager
{
	ScriptEngineManager					factory	= new ScriptEngineManager();
	private static Log					log		= LogFactory
														.getLog( NPCScriptManager.class );

	private Map< Integer, INPCScript >	scriptIndex;

	private NPCScriptManager()
	{
		scriptIndex = new HashMap< Integer, INPCScript >();
	}

	public void initialize() throws FileNotFoundException, ScriptException
	{
		File dir = new File( GameServer.path + "data/script/npc" );
		File[] list = dir.listFiles();
		File file;
		String name;
		String[] names;
		int id;
		Reader reader;
		ScriptEngine engine;
		Invocable inv;

		log.info( "[InitNPCScript] 初始化NPC脚本" );
		for ( int i = 0; i < list.length; i++ )
		{
			file = list[i];
			name = file.getName();
			names = name.split( ".js" );
			id = Integer.parseInt( names[0] );
			reader = new FileReader( file );
			engine = factory.getEngineByName( "JavaScript" );
			engine.eval( reader );
			inv = (Invocable) engine;
			scriptIndex.put( id, inv.getInterface( INPCScript.class ) );
			
			log.info( "[InitNPCScript] id = " + id );
		}
		log.info( "[InitNPCScript] 初始化NPC脚本结束" );
	}
	
	public INPCScript get(int id)
	{
		return scriptIndex.get( id );
	}

	public static NPCScriptManager getInstance()
	{
		return NPCScriptManagerHolder.instance;
	}

	private static class NPCScriptManagerHolder
	{
		private static NPCScriptManager	instance	= new NPCScriptManager();
	}
}
