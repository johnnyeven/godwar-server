package com.xgame.server.common;

import java.util.List;

import com.xgame.server.cards.SoulCard;
import com.xgame.server.logic.Room;

public interface IScript
{
	AttackLog attack( SoulCard attacker, SoulCard defender );

	List< AttackLog > attackArea( SoulCard attacker, Room room );
}
