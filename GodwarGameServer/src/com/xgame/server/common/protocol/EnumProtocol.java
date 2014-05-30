package com.xgame.server.common.protocol;

public final class EnumProtocol
{
	public static final int	CONTROLLER_SCENE							= 5;
	public static final int	CONTROLLER_BASE								= 4;
	public static final int	CONTROLLER_BATTLEROOM						= 3;
	public static final int	CONTROLLER_MSG								= 2;
	public static final int	CONTROLLER_MOVE								= 1;
	public static final int	CONTROLLER_INFO								= 0;
	// MOVE
	public static final int	ACTION_REQUEST_FINDPATH						= 0;
	public static final int	ACTION_SYNC_MOVE							= 1;
	public static final int	ACTION_SEND_PATH							= 2;
	// INFO
	public static final int	ACTION_LOGIN								= 0;
	public static final int	ACTION_LOGOUT								= 1;
	public static final int	ACTION_QUICK_START							= 2;
	public static final int	ACTION_REGISTER								= 3;
	public static final int	ACTION_REQUEST_CHARACTER					= 4;
	public static final int	ACTION_REGISTER_CHARACTER					= 5;
	public static final int	ACTION_LOGICSERVER_BIND_SESSION				= 6;
	public static final int	ACTION_BIND_SESSION							= 7;
	public static final int	ACTION_REQUEST_CARD_GROUP					= 8;
	public static final int	ACTION_REQUEST_CARD_LIST					= 9;
	public static final int	ACTION_CREATE_GROUP							= 10;
	public static final int	ACTION_DELETE_GROUP							= 11;
	public static final int	ACTION_SAVE_CARD_GROUP						= 12;
	public static final int	ACTION_REQUEST_CARD_GROUP_CARDS				= 13;
	public static final int	ACTION_SAVE_CARD_GROUP_CARDS				= 14;
	public static final int	ACTION_HEART_BEAT							= 126;
	public static final int	ACTION_HEART_BEAT_ECHO						= 127;
	// MSG
	public static final int	ACTION_SEND_PUBLIC							= 0;								// ����
	public static final int	ACTION_SEND_TEAM							= 1;								// ���
	public static final int	ACTION_SEND_PRIVATE							= 2;								// ����
	public static final int	ACTION_SEND_WORLD							= 3;								// ����
	// BASE
	public static final int	ACTION_REGISTER_LOGIC_SERVER				= 0;
	public static final int	ACTION_REGISTER_LOGIC_SERVER_CONFIRM		= 1;
	public static final int	ACTION_REQUEST_LOGIC_SERVER_ROOM			= 2;
	public static final int	ACTION_REQUEST_LOGIC_SERVER_ROOM_CONFIRM	= 3;
	public static final int	ACTION_LOGIC_SERVER_INFO					= 4;
	public static final int	ACTION_CONNECT_LOGIC_SERVER					= 5;
	public static final int	ACTION_VERIFY_MAP							= 6;
	public static final int	ACTION_UPDATE_STATUS						= 7;
	// SCENE
	public static final int	ACTION_REQUEST_ROOM							= 0;
	public static final int	ACTION_SHOW_ROOMLIST						= 1;
	public static final int	ACTION_ROOM_CREATED							= 2;
	public static final int	ACTION_REQUEST_ENTER_ROOM					= 3;
	public static final int	ACTION_REQUEST_ENTER_ROOM_LOGICSERVER		= 4;
	public static final int	ACTION_SHOW_PLAYER							= 5;
	public static final int	ACTION_REMOVE_PLAYER						= 6;
	public static final int	ACTION_SHOW_NPC								= 7;
	public static final int	ACTION_REMOVE_NPC							= 8;
	public static final int	ACTION_TRIGGER_NPC							= 9;
	public static final int	ACTION_SHOW_INSTANCE_PORTAL					= 10;
	public static final int	ACTION_REMOVE_INSTANCE_PORTAL				= 11;
	public static final int	ACTION_SHOW_MAP_PORTAL						= 12;
	public static final int	ACTION_REMOVE_MAP_PORTAL					= 13;
	public static final int	ACTION_TRIGGER_MAP_PORTAL					= 14;
	public static final int	ACTION_TRIGGER_INSTANCE_PORTAL				= 15;
	// BATTLE ROOM
	public static final int	ACTION_INIT_ROOM_DATA						= 0;
	public static final int	ACTION_PLAYER_ENTER_ROOM_NOTICE				= 1;
	public static final int	ACTION_PLAYER_SELETED_HERO					= 2;
	public static final int	ACTION_PLAYER_READY							= 3;
	public static final int	ACTION_PLAYER_LEAVE_ROOM_NOTICE				= 4;
	public static final int	ACTION_REQUEST_START_BATTLE					= 5;
	public static final int	ACTION_INIT_ROOM_DATA_LOGICSERVER			= 6;
	public static final int	ACTION_PLAYER_ENTER_ROOM_NOTICE_LOGICSERVER	= 7;
	public static final int	ACTION_START_BATTLE_TIMER					= 8;
	public static final int	ACTION_START_ROOM_TIMER						= 9;
	public static final int	ACTION_FIRST_CHOUPAI						= 10;
	public static final int	ACTION_PLAYER_READY_ERROR					= 11;
	public static final int	ACTION_DEPLOY_COMPLETE						= 12;
	public static final int	ACTION_START_DICE							= 13;
	public static final int	ACTION_ROUND_STANDBY						= 14;
	public static final int	ACTION_ROUND_STANDBY_CONFIRM				= 15;
	public static final int	ACTION_ROUND_STANDBY_CHANGE_FORMATION		= 16;
	public static final int	ACTION_ROUND_STANDBY_EQUIP					= 17;
	public static final int	ACTION_ROUND_ACTION							= 25;
	public static final int	ACTION_ROUND_ACTION_ATTACK					= 26;
	public static final int	ACTION_ROUND_ACTION_SPELL					= 27;
	public static final int	ACTION_ROUND_ACTION_REST					= 28;

	// MOVE
	public static final int	REQUEST_FIND_PATH							= ACTION_REQUEST_FINDPATH << 8
																				| CONTROLLER_MOVE;
	public static final int	SYNC_MOVE									= ACTION_SYNC_MOVE << 8
																				| CONTROLLER_MOVE;
	public static final int	SEND_PATH									= ACTION_SEND_PATH << 8
																				| CONTROLLER_MOVE;
	// INFO
	public static final int	QUICK_START									= ACTION_QUICK_START << 8
																				| CONTROLLER_INFO;
	public static final int	INFO_LOGIN									= ACTION_LOGIN << 8
																				| CONTROLLER_INFO;
	public static final int	INFO_LOGOUT									= ACTION_LOGOUT << 8
																				| CONTROLLER_INFO;
	public static final int	INFO_REGISTER								= ACTION_REGISTER << 8
																				| CONTROLLER_INFO;
	public static final int	REQUEST_ACCOUNT_ROLE						= ACTION_REQUEST_CHARACTER << 8
																				| CONTROLLER_INFO;
	public static final int	REGISTER_ACCOUNT_ROLE						= ACTION_REGISTER_CHARACTER << 8
																				| CONTROLLER_INFO;
	public static final int	INFO_LOGICSERVER_BIND_SESSION				= ACTION_LOGICSERVER_BIND_SESSION << 8
																				| CONTROLLER_INFO;
	public static final int	INFO_BIND_SESSION							= ACTION_BIND_SESSION << 8
																				| CONTROLLER_INFO;
	public static final int	INFO_REQUEST_CARD_GROUP						= ACTION_REQUEST_CARD_GROUP << 8
																				| CONTROLLER_INFO;
	public static final int	INFO_REQUEST_CARD_LIST						= ACTION_REQUEST_CARD_LIST << 8
																				| CONTROLLER_INFO;
	public static final int	INFO_CREATE_GROUP							= ACTION_CREATE_GROUP << 8
																				| CONTROLLER_INFO;
	public static final int	INFO_DELETE_GROUP							= ACTION_DELETE_GROUP << 8
																				| CONTROLLER_INFO;
	public static final int	INFO_SAVE_CARD_GROUP						= ACTION_SAVE_CARD_GROUP << 8
																				| CONTROLLER_INFO;
	public static final int	INFO_HEART_BEAT								= ACTION_HEART_BEAT << 8
																				| CONTROLLER_INFO;
	public static final int	INFO_HEART_BEAT_ECHO						= ACTION_HEART_BEAT_ECHO << 8
																				| CONTROLLER_INFO;
	public static final int	INFO_REQUEST_CARD_GROUP_CARDS				= ACTION_REQUEST_CARD_GROUP_CARDS << 8
																				| CONTROLLER_INFO;
	public static final int	INFO_SAVE_CARD_GROUP_CARDS					= ACTION_SAVE_CARD_GROUP_CARDS << 8
																				| CONTROLLER_INFO;
	// MSG
	public static final int	MSG_SEND_PUBLIC								= ACTION_SEND_PUBLIC << 8
																				| CONTROLLER_MSG;
	public static final int	MSG_SEND_TEAM								= ACTION_SEND_TEAM << 8
																				| CONTROLLER_MSG;
	public static final int	MSG_SEND_PRIVATE							= ACTION_SEND_PRIVATE << 8
																				| CONTROLLER_MSG;
	public static final int	MSG_SEND_WORLD								= ACTION_SEND_WORLD << 8
																				| CONTROLLER_MSG;
	// BASE
	public static final int	BASE_VERIFY_MAP								= ACTION_VERIFY_MAP << 8
																				| CONTROLLER_BASE;
	public static final int	BASE_REGISTER_LOGIC_SERVER					= ACTION_REGISTER_LOGIC_SERVER << 8
																				| CONTROLLER_BASE;
	public static final int	BASE_REGISTER_LOGIC_SERVER_CONFIRM			= ACTION_REGISTER_LOGIC_SERVER_CONFIRM << 8
																				| CONTROLLER_BASE;
	public static final int	BASE_REQUEST_LOGIC_SERVER_ROOM				= ACTION_REQUEST_LOGIC_SERVER_ROOM << 8
																				| CONTROLLER_BASE;
	public static final int	BASE_REQUEST_LOGIC_SERVER_ROOM_CONFIRM		= ACTION_REQUEST_LOGIC_SERVER_ROOM_CONFIRM << 8
																				| CONTROLLER_BASE;
	public static final int	BASE_LOGIC_SERVER_INFO						= ACTION_LOGIC_SERVER_INFO << 8
																				| CONTROLLER_BASE;
	public static final int	BASE_CONNECT_LOGIC_SERVER					= ACTION_CONNECT_LOGIC_SERVER << 8
																				| CONTROLLER_BASE;
	public static final int	BASE_UPDATE_STATUS							= ACTION_UPDATE_STATUS << 8
																				| CONTROLLER_BASE;
	// SCENE
	public static final int	HALL_REQUEST_ROOM							= ACTION_REQUEST_ROOM << 8
																				| CONTROLLER_SCENE;
	public static final int	HALL_SHOW_ROOM_LIST							= ACTION_SHOW_ROOMLIST << 8
																				| CONTROLLER_SCENE;
	public static final int	HALL_ROOM_CREATED							= ACTION_ROOM_CREATED << 8
																				| CONTROLLER_SCENE;
	public static final int	HALL_REQUEST_ENTER_ROOM						= ACTION_REQUEST_ENTER_ROOM << 8
																				| CONTROLLER_SCENE;
	public static final int	HALL_REQUEST_ENTER_ROOM_LOGICSERVER			= ACTION_REQUEST_ENTER_ROOM_LOGICSERVER << 8
																				| CONTROLLER_SCENE;
	public static final int	SCENE_SHOW_PLAYER							= ACTION_SHOW_PLAYER << 8
																				| CONTROLLER_SCENE;
	public static final int	SCENE_REMOVE_PLAYER							= ACTION_REMOVE_PLAYER << 8
																				| CONTROLLER_SCENE;
	public static final int	SCENE_SHOW_NPC								= ACTION_SHOW_NPC << 8
																				| CONTROLLER_SCENE;
	public static final int	SCENE_REMOVE_NPC							= ACTION_REMOVE_NPC << 8
																				| CONTROLLER_SCENE;
	public static final int	SCENE_TRIGGER_NPC							= ACTION_TRIGGER_NPC << 8
																				| CONTROLLER_SCENE;
	public static final int	SCENE_SHOW_INSTANCE_PORTAL					= ACTION_SHOW_INSTANCE_PORTAL << 8
																				| CONTROLLER_SCENE;
	public static final int	SCENE_REMOVE_INSTANCE_PORTAL				= ACTION_REMOVE_INSTANCE_PORTAL << 8
																				| CONTROLLER_SCENE;
	public static final int	SCENE_SHOW_MAP_PORTAL						= ACTION_SHOW_MAP_PORTAL << 8
																				| CONTROLLER_SCENE;
	public static final int	SCENE_REMOVE_MAP_PORTAL						= ACTION_REMOVE_MAP_PORTAL << 8
																				| CONTROLLER_SCENE;
	public static final int	SCENE_TRIGGER_MAP_PORTAL					= ACTION_TRIGGER_MAP_PORTAL << 8
																				| CONTROLLER_SCENE;
	public static final int	SCENE_TRIGGER_INSTANCE_PORTAL				= ACTION_TRIGGER_INSTANCE_PORTAL << 8
																				| CONTROLLER_SCENE;
	// BATTLE ROOM
	public static final int	BATTLEROOM_INIT_ROOM						= ACTION_INIT_ROOM_DATA << 8
																				| CONTROLLER_BATTLEROOM;
	public static final int	BATTLEROOM_PLAYER_ENTER_ROOM				= ACTION_PLAYER_ENTER_ROOM_NOTICE << 8
																				| CONTROLLER_BATTLEROOM;
	public static final int	BATTLEROOM_PLAYER_SELECTED_HERO				= ACTION_PLAYER_SELETED_HERO << 8
																				| CONTROLLER_BATTLEROOM;
	public static final int	BATTLEROOM_PLAYER_READY						= ACTION_PLAYER_READY << 8
																				| CONTROLLER_BATTLEROOM;
	public static final int	BATTLEROOM_PLAYER_READY_ERROR				= ACTION_PLAYER_READY_ERROR << 8
																				| CONTROLLER_BATTLEROOM;
	public static final int	BATTLEROOM_PLAYER_LEAVE_ROOM				= ACTION_PLAYER_LEAVE_ROOM_NOTICE << 8
																				| CONTROLLER_BATTLEROOM;
	public static final int	BATTLEROOM_REQUEST_START_BATTLE				= ACTION_REQUEST_START_BATTLE << 8
																				| CONTROLLER_BATTLEROOM;
	public static final int	BATTLEROOM_INIT_ROOM_LOGICSERVER			= ACTION_INIT_ROOM_DATA_LOGICSERVER << 8
																				| CONTROLLER_BATTLEROOM;
	public static final int	BATTLEROOM_PLAYER_ENTER_ROOM_LOGICSERVER	= ACTION_PLAYER_ENTER_ROOM_NOTICE_LOGICSERVER << 8
																				| CONTROLLER_BATTLEROOM;
	public static final int	BATTLEROOM_START_BATTLE_TIMER				= ACTION_START_BATTLE_TIMER << 8
																				| CONTROLLER_BATTLEROOM;
	public static final int	BATTLEROOM_START_ROOM_TIMER					= ACTION_START_ROOM_TIMER << 8
																				| CONTROLLER_BATTLEROOM;
	public static final int	BATTLEROOM_FIRST_CHOUPAI					= ACTION_FIRST_CHOUPAI << 8
																				| CONTROLLER_BATTLEROOM;
	public static final int	BATTLEROOM_DEPLOY_COMPLETE					= ACTION_DEPLOY_COMPLETE << 8
																				| CONTROLLER_BATTLEROOM;
	public static final int	BATTLEROOM_START_DICE						= ACTION_START_DICE << 8
																				| CONTROLLER_BATTLEROOM;
	public static final int	BATTLEROOM_ROUND_STANDBY					= ACTION_ROUND_STANDBY << 8
																				| CONTROLLER_BATTLEROOM;
	public static final int	BATTLEROOM_ROUND_STANDBY_CONFIRM			= ACTION_ROUND_STANDBY_CONFIRM << 8
																				| CONTROLLER_BATTLEROOM;
	public static final int	BATTLEROOM_ROUND_STANDBY_CHANGE_FORMATION	= ACTION_ROUND_STANDBY_CHANGE_FORMATION << 8
																				| CONTROLLER_BATTLEROOM;
	public static final int	BATTLEROOM_ROUND_STANDBY_EQUIP				= ACTION_ROUND_STANDBY_EQUIP << 8
																				| CONTROLLER_BATTLEROOM;
	public static final int	BATTLEROOM_ROUND_ACTION						= ACTION_ROUND_ACTION << 8
																				| CONTROLLER_BATTLEROOM;
	public static final int	BATTLEROOM_ROUND_ACTION_ATTACK				= ACTION_ROUND_ACTION_ATTACK << 8
																				| CONTROLLER_BATTLEROOM;
	public static final int	BATTLEROOM_ROUND_ACTION_SPELL				= ACTION_ROUND_ACTION_SPELL << 8
																				| CONTROLLER_BATTLEROOM;
	public static final int	BATTLEROOM_ROUND_ACTION_REST				= ACTION_ROUND_ACTION_REST << 8
																				| CONTROLLER_BATTLEROOM;

	public static final int	ACK_CONFIRM									= 1;
	public static final int	ACK_ERROR									= 0;
	public static final int	ORDER_CONFIRM								= 2;

	public static final int	TYPE_INT									= 0;
	public static final int	TYPE_LONG									= 1;
	public static final int	TYPE_STRING									= 2;
	public static final int	TYPE_FLOAT									= 3;
	public static final int	TYPE_BOOL									= 4;
	public static final int	TYPE_DOUBLE									= 5;
}
