package com.xgame.server.skill;

import com.xgame.server.cards.Card;
import com.xgame.server.logic.Player;

public class AttackInfo
{
	public String skillId;
	public Player attacker;
	public Player defender;
	
	public Card attackerCard;
	public Card defenderCard;
	
	public int attackerAttackChange;
	public int attackerDefChange;
	public int attackerMdefChange;
	public int attackerHealthChange;
	public int attackerHealthMaxChange;
	public Boolean attackerIsStatus = false;
	public int attackerRemainRound;
	
	public int defenderAttackChange;
	public int defenderDefChange;
	public int defenderMdefChange;
	public int defenderHealthChange;
	public int defenderHealthMaxChange;
	public Boolean defenderIsStatus = false;
	public int defenderRemainRound;

	public AttackInfo()
	{
		
	}

}
