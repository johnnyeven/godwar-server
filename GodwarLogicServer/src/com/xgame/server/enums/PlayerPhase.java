package com.xgame.server.enums;

public class PlayerPhase
{

	public static final int DEPLOY = 0; //部署阶段
	public static final int ROUND_START = 1; //回合开始阶段
	public static final int ROUND_STANDBY = 2; //回合摸牌阶段
	public static final int ROUND_ACTION = 3; //行动阶段
	public static final int ROUND_DISCARD = 4; //弃牌阶段
	public static final int ROUND_END = 5; //回合结束阶段
	public static final int DIEING = 6; //濒死阶段

}
