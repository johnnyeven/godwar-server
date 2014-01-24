package com.xgame.server.skill;

import java.util.List;

import com.xgame.server.cards.SoulCard;
import com.xgame.server.logic.Player;
import com.xgame.server.logic.Room;

public interface IScript
{
	List< AttackInfo > attack( String skillId, Player attacker, Player defender,
			SoulCard attackerCard, SoulCard defenderCard );

	List< AttackInfo > attackArea( String skillId, Player attacker, SoulCard attackerCard,
			Room room );
}
