package com.xgame.server.common.protocol;

public final class EnumProtocol
{
    public static final int CONTROLLER_INFO = 0;
    //INFO
	public static final int ACTION_LOGIN = 0;
	public static final int ACTION_LOGOUT = 1;
	public static final int ACTION_QUICK_START = 2;

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
    public static final short INFO_LOGIN = ACTION_LOGIN << 8 | CONTROLLER_INFO;
}
