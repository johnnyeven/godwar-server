package com.xgame.server.skill;

public class SkillParameter
{
	public String id;
	public int level;
	public String name;
	public String target;
	public IScript script;

	public SkillParameter()
	{
		id = null;
		level = Integer.MIN_VALUE;
		name = null;
		target = null;
		script = null;
	}

}
