package com.xgame.server.common.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseRouter
{
	private static DatabaseRouter instance;
	private static boolean allowInstance = false;
	
	private Connection dbConnection;
	
	private static final String DRIVER = "com.mysql.jdbc.Driver";
	private static final String CONNECT_STRING = "jdbc:mysql://localhost/";
	private static final String DATABASE_NAME = "pulse_platform_db";
	private static final String USERNAME = "root";
	private static final String PASSWORD = "84@41%%wi96^4";
	
	public DatabaseRouter()
	{
		try
		{
			if(!allowInstance)
			{
				throw new Exception();
			}
		}
		catch(Exception e)
		{
			System.out.println("DatabaseRouter½ûÖ¹ÊµÀý»¯");
			return;
		}
		
		try
		{
			Class.forName(DRIVER);
			
			dbConnection = DriverManager.getConnection(CONNECT_STRING + DATABASE_NAME + "?autoReconnect=true&failOverReadOnly=false&useUnicode=true&characterEncoding=UTF-8", USERNAME, PASSWORD);
			if(dbConnection.isClosed())
			{
				throw new Exception();
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public static DatabaseRouter getInstance()
	{
		if(instance == null)
		{
			allowInstance = true;
			instance = new DatabaseRouter();
			allowInstance = false;
		}
		return instance;
	}

	public Connection getDbConnection()
	{
		try
		{
			if(dbConnection.isClosed())
			{
				Class.forName(DRIVER);
				dbConnection = DriverManager.getConnection(CONNECT_STRING + DATABASE_NAME + "?useUnicode=true&characterEncoding=UTF-8", USERNAME, PASSWORD);
			}
		}
		catch ( SQLException | ClassNotFoundException e )
		{
			e.printStackTrace();
		}
		return dbConnection;
	}

}
