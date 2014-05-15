package com.xgame.server.common.parameter;

import java.util.ArrayList;
import java.util.List;

public class SoulCardParameter extends RoleCardParameter
{
	public int level;
	public int race;
	public List<String> skillList;
	
	public SoulCardParameter()
	{
		super();
		skillList = new ArrayList<String>();
	}
}
