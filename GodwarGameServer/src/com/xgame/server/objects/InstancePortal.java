package com.xgame.server.objects;

import java.util.ArrayList;
import java.util.List;

public class InstancePortal extends Portal
{
	private List< Integer >	instanceList;

	public InstancePortal()
	{
		instanceList = new ArrayList< Integer >();
	}

	public List< Integer > getInstanceList()
	{
		return instanceList;
	}

}
