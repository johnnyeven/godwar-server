package com.xgame.server.common;

import java.util.ArrayList;

public class ServerPackage
{
	public int success;
	public int protocolId;
	public ArrayList<PackageItem> parameter;
	
	public ServerPackage()
	{
		parameter = new ArrayList<PackageItem>();
	}

}
