package com.xgame.server.common.protocol;

public final class EnumProtocol
{
    public static final int CONTROLLER_SCENE = 5;
    public static final int CONTROLLER_BASE = 4;
    public static final int CONTROLLER_BATTLE = 3;
    public static final int CONTROLLER_MSG = 2;
    public static final int CONTROLLER_MOVE = 1;
    public static final int CONTROLLER_INFO = 0;
    public static final int NPC_CONTROLLER_BATTLE = 13;
    public static final int NPC_CONTROLLER_MOVE = 11;
    //MOVE
    public static final int ACTION_MOVETO = 0;
    public static final int ACTION_MOVE = 1;
    //MSG
    public static final int ACTION_PUBLIC_MSG = 0;
    public static final int ACTION_PRIVATE_MSG = 1;
    //BATTLE
    public static final int ACTION_ATTACK = 0;
    public static final int ACTION_PREPARE_ATTACK = 2;
    public static final int ACTION_UNDERATTACK = 1;
    public static final int ACTION_SING = 3;
    //INFO
	public static final int ACTION_LOGIN = 0;
	public static final int ACTION_LOGOUT = 1;
	public static final int ACTION_QUICK_START = 2;
	public static final int ACTION_CHANGE_ACTION = 3;
	public static final int ACTION_REQUEST_CHARACTER = 4;
	public static final int ACTION_REGISTER_CHARACTER = 5;
	public static final int ACTION_REQUEST_HOTKEY = 6;
	public static final int ACTION_BIND_SESSION = 7;
    //BASE
    public static final int ACTION_VERIFY_MAP = 0;
	public static final int ACTION_UPDATE_STATUS = 1;
    //SCENE
    public static final int ACTION_SHOW_PLAYER = 0;

    public static final int ACK_CONFIRM = 1;
    public static final int ACK_ERROR = 0;
    public static final int ORDER_CONFIRM = 2;

    public static final int TYPE_INT = 0;
    public static final int TYPE_LONG = 1;
    public static final int TYPE_STRING = 2;
    public static final int TYPE_FLOAT = 3;
    public static final int TYPE_BOOL = 4;
    public static final int TYPE_DOUBLE = 5;
    //INFO
    public static final short QUICK_START = ACTION_QUICK_START << 8 | CONTROLLER_INFO;
    public static final short REQUEST_ACCOUNT_ROLE = ACTION_REQUEST_CHARACTER << 8 | CONTROLLER_INFO;
    public static final short REGISTER_ACCOUNT_ROLE = ACTION_REGISTER_CHARACTER << 8 | CONTROLLER_INFO;
    public static final short REQUEST_HOTKEY = ACTION_REQUEST_HOTKEY << 8 | CONTROLLER_INFO;
	public static final short INFO_BIND_SESSION = ACTION_BIND_SESSION << 8 | CONTROLLER_INFO;
	//BASE
	public static final short BASE_VERIFY_MAP = ACTION_VERIFY_MAP << 8 | CONTROLLER_BASE;
	public static final short BASE_UPDATE_STATUS = ACTION_UPDATE_STATUS << 8 | CONTROLLER_BASE;
	//SCENE
	public static final short SCENE_SHOW_PLAYER = ACTION_SHOW_PLAYER << 8 | CONTROLLER_SCENE;
}
