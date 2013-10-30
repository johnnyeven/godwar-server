package com.xgame.server.common;

import java.util.ArrayList;

public class ServerPackage
{
	public int success;
	public short protocolId;
	public ArrayList<PackageItem> parameter;
	
	public ServerPackage()
	{
		parameter = new ArrayList<PackageItem>();
	}

}
