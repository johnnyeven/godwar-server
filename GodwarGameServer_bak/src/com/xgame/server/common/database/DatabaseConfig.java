package com.xgame.server.common.database;

public class DatabaseConfig
{
	public String	name			= null;
	public String	driver			= null;
	public String	connectString	= null;
	public String	databaseName	= null;
	public String	username		= null;
	public String	password		= null;

	public DatabaseConfig( String name, String driver, String connectString,
			String databaseName, String username, String password )
	{
		this.name = name;
		this.driver = driver;
		this.connectString = connectString;
		this.databaseName = databaseName;
		this.username = username;
		this.password = password;
	}

	public DatabaseConfig()
	{
	}

}
