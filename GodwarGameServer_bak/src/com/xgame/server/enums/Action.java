package com.xgame.server.enums;

public class Action
{
	public static final int		RESURRECTION		= 8;
	public static final int		STOP				= 0;
	public static final int		MOVE				= 1;
	public static final int		ATTACK				= 2;
	public static final int		SIT					= 3;
	public static final int		DIE					= 4;
	public static final int		PICKUP				= 5;
	public static final int		BE_ATTACKED			= 6;
	public static final int		CAUTION				= 7;
	public static final int		CORPSE				= 8;
	public static final int[]	PlayOnce			=
													{ 2, 5, 6 };
	public static final int[]	PlayOnceToCaution	=
													{ 2, 6 };
}
