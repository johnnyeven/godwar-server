package com.xgame.server.game;

import com.xgame.server.network.GameSession;

public interface IHall
{
	void addSessionQueue( GameSession session );
	void addSession( GameSession session );
	void removeSession( long id );
	GameSession getSession( long id );
	void updateSessions( long timeDiff );
	void kickAllPlayer();
}
