package com.xgame.server.common.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class DatabaseRouter
{
	private static DatabaseRouter			instance;
	private static boolean					allowInstance	= false;

	private Map< String, DatabaseConfig >	configSet;
	private Map< String, Connection >		connectionSet;

	public DatabaseRouter()
	{
		try
		{
			if ( !allowInstance )
			{
				throw new Exception();
			}
		}
		catch ( Exception e )
		{
			System.out.println( "DatabaseRouter½ûÖ¹ÊµÀý»¯" );
			return;
		}

		configSet = new HashMap< String, DatabaseConfig >();
		configSet.put( "accountdb", new DatabaseConfig( "accountdb",
				"com.mysql.jdbc.Driver", "jdbc:mysql://localhost/",
				"pulse_platform_db", "root", "84@41%%wi96^4" ) );
		configSet.put( "gamedb", new DatabaseConfig( "gamedb",
				"com.mysql.jdbc.Driver", "jdbc:mysql://localhost/",
				"godwar_game_db", "root", "84@41%%wi96^4" ) );

		connectionSet = new HashMap< String, Connection >();

		Iterator< Entry< String, DatabaseConfig >> it = configSet.entrySet()
				.iterator();
		while ( it.hasNext() )
		{
			Entry< String, DatabaseConfig > e = it.next();
			DatabaseConfig config = e.getValue();

			try
			{
				Class.forName( config.driver );

				Connection c = DriverManager.getConnection(
						config.connectString + config.databaseName
								+ "?autoReconnect=true&failOverReadOnly=false&useUnicode=true&characterEncoding=UTF-8",
						config.username, config.password );
				if ( c.isClosed() )
				{
					throw new Exception();
				}
				connectionSet.put( e.getKey(), c );
			}
			catch ( Exception exp )
			{
				exp.printStackTrace();
			}
		}
	}

	public static DatabaseRouter getInstance()
	{
		if ( instance == null )
		{
			allowInstance = true;
			instance = new DatabaseRouter();
			allowInstance = false;
		}
		return instance;
	}

	public Connection getConnection( String name )
	{
		Connection c = connectionSet.get( name );
		try
		{
			if ( c.isClosed() )
			{
				DatabaseConfig config = configSet.get( name );
				if ( config != null )
				{
					Class.forName( config.driver );

					c = DriverManager.getConnection( config.connectString
							+ config.databaseName
							+ "?useUnicode=true&characterEncoding=UTF-8",
							config.username, config.password );
					connectionSet.put( name, c );
				}
			}
		}
		catch ( SQLException | ClassNotFoundException e )
		{
			e.printStackTrace();
		}
		return c;
	}

}
