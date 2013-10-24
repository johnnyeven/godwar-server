package com.xgame.server.game;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

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

	public LogicServerInfo getLogicServer()
	{
		if ( map.size() > 0 )
		{
			Iterator< LogicServerInfo > it = map.values().iterator();
			LogicServerInfo info;
			LogicServerInfo minInfo;

			minInfo = it.next();
			while ( it.hasNext() )
			{
				info = it.next();
				if ( info.load < minInfo.load )
				{
					minInfo = info;
				}
			}
			return minInfo;
		}
		return null;
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
