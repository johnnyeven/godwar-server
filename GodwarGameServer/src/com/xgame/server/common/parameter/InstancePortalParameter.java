package com.xgame.server.common.parameter;

import java.util.ArrayList;
import java.util.List;

public class InstancePortalParameter extends PortalParameter
{
	public List< Integer >	instanceList;

	public InstancePortalParameter()
	{
		super();
		instanceList = new ArrayList< Integer >();
	}

}
