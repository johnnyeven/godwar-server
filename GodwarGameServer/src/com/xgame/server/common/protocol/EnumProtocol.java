package com.xgame.server.common.protocol;

public final class EnumProtocol
{

	public static final int		CONTROLLER_HALL								= 5;
	public static final int		CONTROLLER_BASE								= 4;
	public static final int		CONTROLLER_BATTLEROOM						= 3;
	public static final int		CONTROLLER_MSG								= 2;
	public static final int		CONTROLLER_MOVE								= 1;
	public static final int		CONTROLLER_INFO								= 0;
	public static final int		NPC_CONTROLLER_BATTLE						= 13;
	public static final int		NPC_CONTROLLER_MOVE							= 11;
	// MSG
	public static final int		ACTION_PUBLIC_MSG							= 0;
	public static final int		ACTION_PRIVATE_MSG							= 1;
	// INFO
	public static final int		ACTION_REQUEST_CHARACTER					= 4;
	public static final int		ACTION_REGISTER_CHARACTER					= 5;
	public static final int		ACTION_LOGICSERVER_BIND_SESSION				= 6;
	public static final int		ACTION_BIND_SESSION							= 7;
	public static final int		ACTION_REQUEST_CARD_GROUP					= 8;
	public static final int		ACTION_REQUEST_CARD_LIST					= 9;
	public static final int		ACTION_CREATE_GROUP							= 10;
	public static final int		ACTION_DELETE_GROUP							= 11;
	public static final int		ACTION_SAVE_CARD_GROUP						= 12;
	public static final int		ACTION_HEART_BEAT							= 127;
	// BASE
	public static final int		ACTION_REGISTER_LOGIC_SERVER				= 0;
	public static final int		ACTION_REGISTER_LOGIC_SERVER_CONFIRM		= 1;
	public static final int		ACTION_REQUEST_LOGIC_SERVER_ROOM			= 2;
	public static final int		ACTION_REQUEST_LOGIC_SERVER_ROOM_CONFIRM	= 3;
	public static final int		ACTION_LOGIC_SERVER_INFO					= 4;
	public static final int		ACTION_CONNECT_LOGIC_SERVER					= 5;
	// HALL
	public static final int		ACTION_REQUEST_ROOM							= 0;
	public static final int		ACTION_SHOW_ROOMLIST						= 1;
	public static final int		ACTION_ROOM_CREATED							= 2;
	public static final int		ACTION_REQUEST_ENTER_ROOM					= 3;
	// battle ROOM
	public static final int		ACTION_INIT_ROOM_DATA						= 0;
	public static final int		ACTION_PLAYER_ENTER_ROOM_NOTICE				= 1;
	public static final int		ACTION_PLAYER_SELETED_HERO					= 2;
	public static final int		ACTION_PLAYER_READY							= 3;
	public static final int		ACTION_PLAYER_LEAVE_ROOM_NOTICE				= 4;
	public static final int		ACTION_REQUEST_START_BATTLE					= 5;

	public static final int		ACK_CONFIRM									= 1;
	public static final int		ACK_ERROR									= 0;
	public static final int		ORDER_CONFIRM								= 2;

	public static final int		TYPE_INT									= 0;
	public static final int		TYPE_LONG									= 1;
	public static final int		TYPE_STRING									= 2;
	public static final int		TYPE_FLOAT									= 3;
	public static final int		TYPE_BOOL									= 4;
	public static final int		TYPE_DOUBLE									= 5;

	// INFO
	public static final short	REQUEST_ACCOUNT_ROLE						= ACTION_REQUEST_CHARACTER << 8
																					| CONTROLLER_INFO;
	public static final short	REGISTER_ACCOUNT_ROLE						= ACTION_REGISTER_CHARACTER << 8
																					| CONTROLLER_INFO;
	public static final short	INFO_LOGICSERVER_BIND_SESSION				= ACTION_LOGICSERVER_BIND_SESSION << 8
																					| CONTROLLER_INFO;
	public static final short	INFO_BIND_SESSION							= ACTION_BIND_SESSION << 8
																					| CONTROLLER_INFO;
	public static final short	INFO_REQUEST_CARD_GROUP						= ACTION_REQUEST_CARD_GROUP << 8
																					| CONTROLLER_INFO;
	public static final short	INFO_REQUEST_CARD_LIST						= ACTION_REQUEST_CARD_LIST << 8
																					| CONTROLLER_INFO;
	public static final short	INFO_CREATE_GROUP							= ACTION_CREATE_GROUP << 8
																					| CONTROLLER_INFO;
	public static final short	INFO_DELETE_GROUP							= ACTION_DELETE_GROUP << 8
																					| CONTROLLER_INFO;
	public static final short	INFO_SAVE_CARD_GROUP						= ACTION_SAVE_CARD_GROUP << 8
																					| CONTROLLER_INFO;
	public static final short	INFO_HEART_BEAT								= ACTION_HEART_BEAT << 8
																					| CONTROLLER_INFO;
	// BASE
	public static final short	BASE_REGISTER_LOGIC_SERVER					= ACTION_REGISTER_LOGIC_SERVER << 8
																					| CONTROLLER_BASE;
	public static final short	BASE_REGISTER_LOGIC_SERVER_CONFIRM			= ACTION_REGISTER_LOGIC_SERVER_CONFIRM << 8
																					| CONTROLLER_BASE;
	public static final short	BASE_REQUEST_LOGIC_SERVER_ROOM				= ACTION_REQUEST_LOGIC_SERVER_ROOM << 8
																					| CONTROLLER_BASE;
	public static final short	BASE_REQUEST_LOGIC_SERVER_ROOM_CONFIRM		= ACTION_REQUEST_LOGIC_SERVER_ROOM_CONFIRM << 8
																					| CONTROLLER_BASE;
	public static final short	BASE_LOGIC_SERVER_INFO						= ACTION_LOGIC_SERVER_INFO << 8
																					| CONTROLLER_BASE;
	public static final short	BASE_CONNECT_LOGIC_SERVER					= ACTION_CONNECT_LOGIC_SERVER << 8
																					| CONTROLLER_BASE;
	// HALL
	public static final short	HALL_REQUEST_ROOM							= ACTION_REQUEST_ROOM << 8
																					| CONTROLLER_HALL;
	public static final short	HALL_SHOW_ROOM_LIST							= ACTION_SHOW_ROOMLIST << 8
																					| CONTROLLER_HALL;
	public static final short	HALL_ROOM_CREATED							= ACTION_ROOM_CREATED << 8
																					| CONTROLLER_HALL;
	public static final short	HALL_REQUEST_ENTER_ROOM						= ACTION_REQUEST_ENTER_ROOM << 8
																					| CONTROLLER_HALL;
	// battle ROOM
	public static final short	BATTLEROOM_INIT_ROOM						= ACTION_INIT_ROOM_DATA << 8
																					| CONTROLLER_BATTLEROOM;
	public static final short	BATTLEROOM_PLAYER_ENTER_ROOM				= ACTION_PLAYER_ENTER_ROOM_NOTICE << 8
																					| CONTROLLER_BATTLEROOM;
	public static final short	BATTLEROOM_PLAYER_SELECTED_HERO				= ACTION_PLAYER_SELETED_HERO << 8
																					| CONTROLLER_BATTLEROOM;
	public static final short	BATTLEROOM_PLAYER_READY						= ACTION_PLAYER_READY << 8
																					| CONTROLLER_BATTLEROOM;
	public static final short	BATTLEROOM_PLAYER_LEAVE_ROOM				= ACTION_PLAYER_LEAVE_ROOM_NOTICE << 8
																					| CONTROLLER_BATTLEROOM;
	public static final short	BATTLEROOM_REQUEST_START_BATTLE				= ACTION_REQUEST_START_BATTLE << 8
																					| CONTROLLER_BATTLEROOM;
}
