
package com.xgame.server.common.protocol;

public final class EnumProtocol
{

	public static final int		CONTROLLER_HALL					= 5;
	public static final int		CONTROLLER_BASE					= 4;
	public static final int		CONTROLLER_BATTLE				= 3;
	public static final int		CONTROLLER_MSG					= 2;
	public static final int		CONTROLLER_MOVE					= 1;
	public static final int		CONTROLLER_INFO					= 0;
	public static final int		NPC_CONTROLLER_BATTLE			= 13;
	public static final int		NPC_CONTROLLER_MOVE				= 11;
	// MSG
	public static final int		ACTION_PUBLIC_MSG				= 0;
	public static final int		ACTION_PRIVATE_MSG				= 1;
	// INFO
	public static final int		ACTION_REQUEST_CHARACTER		= 4;
	public static final int		ACTION_REGISTER_CHARACTER		= 5;
	public static final int		ACTION_BIND_SESSION				= 7;
	// BASE
	public static final int		ACTION_REGISTER_LOGIC_SERVER	= 0;
	public static final int		ACTION_UPDATE_STATUS			= 1;
	// HALL
	public static final int		ACTION_REQUEST_ROOM				= 0;
	public static final int		ACTION_SHOW_ROOMLIST			= 1;
	public static final int		ACTION_ROOM_CREATED				= 2;
	public static final int		ACTION_REQUEST_ENTER_ROOM		= 3;
	public static final int		ACTION_PLAYER_ENTER_ROOM		= 4;
	public static final int		ACTION_PLAYER_SELETED_HERO		= 5;

	public static final int		ACK_CONFIRM						= 1;
	public static final int		ACK_ERROR						= 0;
	public static final int		ORDER_CONFIRM					= 2;

	public static final int		TYPE_INT						= 0;
	public static final int		TYPE_LONG						= 1;
	public static final int		TYPE_STRING						= 2;
	public static final int		TYPE_FLOAT						= 3;
	public static final int		TYPE_BOOL						= 4;
	public static final int		TYPE_DOUBLE						= 5;

	// INFO
	public static final short	REQUEST_ACCOUNT_ROLE			= ACTION_REQUEST_CHARACTER << 8
																		| CONTROLLER_INFO;
	public static final short	REGISTER_ACCOUNT_ROLE			= ACTION_REGISTER_CHARACTER << 8
																		| CONTROLLER_INFO;
	public static final short	INFO_BIND_SESSION				= ACTION_BIND_SESSION << 8
																		| CONTROLLER_INFO;
	// BASE
	public static final short	BASE_REGISTER_LOGIC_SERVER		= ACTION_REGISTER_LOGIC_SERVER << 8
																		| CONTROLLER_BASE;
	public static final short	BASE_UPDATE_STATUS				= ACTION_UPDATE_STATUS << 8
																		| CONTROLLER_BASE;
	// HALL
	public static final short	HALL_REQUEST_ROOM				= ACTION_REQUEST_ROOM << 8
																		| CONTROLLER_HALL;
	public static final short	HALL_SHOW_ROOM_LIST				= ACTION_SHOW_ROOMLIST << 8
																		| CONTROLLER_HALL;
	public static final short	HALL_ROOM_CREATED				= ACTION_ROOM_CREATED << 8
																		| CONTROLLER_HALL;
	public static final short	HALL_REQUEST_ENTER_ROOM			= ACTION_REQUEST_ENTER_ROOM << 8
																		| CONTROLLER_HALL;
	public static final short	HALL_PLAYER_ENTER_ROOM			= ACTION_PLAYER_ENTER_ROOM << 8
																		| CONTROLLER_HALL;
	public static final short	HALL_PLAYER_SELECTED_HERO		= ACTION_PLAYER_SELETED_HERO << 8
																		| CONTROLLER_HALL;
}
