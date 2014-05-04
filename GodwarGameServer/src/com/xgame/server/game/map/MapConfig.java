package com.xgame.server.game.map;

import java.util.ArrayList;
import java.util.List;

import com.xgame.server.common.parameter.NPCParameter;

public class MapConfig
{
	public int					id;
	public int					width;
	public int					height;
	public int					blockNumWidth;
	public int					blockNumHeight;
	public int					blockSizeWidth;
	public int					blockSizeHeight;
	public List< NPCParameter >	npcList;

	public MapConfig()
	{
		npcList = new ArrayList< NPCParameter >();
	}

}
