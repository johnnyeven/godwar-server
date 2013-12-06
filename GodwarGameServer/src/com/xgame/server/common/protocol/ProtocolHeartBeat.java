package com.xgame.server.common.protocol;

import java.util.Date;

import com.xgame.server.game.ProtocolPackage;
import com.xgame.server.network.GameSession;

public class ProtocolHeartBeat implements IProtocol
{

	@Override
	public void Execute( Object param1, Object param2 )
	{
		ProtocolPackage parameter = (ProtocolPackage) param1;
		GameSession session = (GameSession) param2;

		session.setHeartBeatTime( new Date().getTime() );
	}

}
