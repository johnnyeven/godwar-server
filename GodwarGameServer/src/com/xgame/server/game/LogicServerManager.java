package com.xgame.server.game;

import java.util.HashMap;
import java.util.Map;

import com.xgame.server.common.LogicServerInfo;

public class LogicServerManager
{
	private Map< String, LogicServerInfo >	map;

	private LogicServerManager()
	{
		map = new HashMap< String, LogicServerInfo >();
	}

	public void addLogicServer( String id, LogicServerInfo info )
	{
		if ( !map.containsKey( id ) )
		{
			map.put( id, info );
		}
	}

	public LogicServerInfo getLogicServer( String id )
	{
		if ( map.containsKey( id ) )
		{
			return map.get( id );
		}
		else
		{
			return null;
		}
	}

	public void addLoad( String id )
	{
		if ( map.containsKey( id ) )
		{
			LogicServerInfo info = map.get( id );
			info.load++;
		}
	}

	public static LogicServerManager getInstance()
	{
		return LogicServerManagerHolder.instance;
	}

	private static class LogicServerManagerHolder
	{
		private static LogicServerManager	instance	= new LogicServerManager();
	}

}
