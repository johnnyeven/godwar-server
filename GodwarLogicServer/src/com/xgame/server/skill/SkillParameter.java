package com.xgame.server.skill;

import java.io.Reader;

public class SkillParameter
{
	public String id;
	public int level;
	public String name;
	public String target;
	public Reader script;

	public SkillParameter()
	{
		id = null;
		level = Integer.MIN_VALUE;
		name = null;
		target = null;
		script = null;
	}

}
