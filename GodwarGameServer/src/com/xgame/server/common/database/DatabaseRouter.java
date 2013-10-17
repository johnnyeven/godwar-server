package com.xgame.server.common.database;

import java.sql.Connection;
import java.sql.DriverManager;
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
				"pulse_db_platform", "root", "84@41%%wi96^4" ) );
		configSet.put( "gamedb", new DatabaseConfig( "accountdb",
				"com.mysql.jdbc.Driver", "jdbc:mysql://localhost/",
				"pulse_db_game", "root", "84@41%%wi96^4" ) );

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
								+ "?useUnicode=true&characterEncoding=UTF-8",
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
		return connectionSet.get( name );
	}

}
