package com.xgame.server.skill;

import com.xgame.server.cards.Card;
import com.xgame.server.logic.Player;

public class AttackInfo
{
	public String skillId;
	public Player attacker;
	public Player defender;
	
	public Card attackerCard;
	public boolean attackerCardUp = false;
	public boolean attackerCardDisabled = true;
	public Card defenderCard;
	public boolean defenderCardUp = false;
	public boolean defenderCardDisabled = true;
	
	public int attackerAttackChange;
	public int attackerDefChange;
	public int attackerMdefChange;
	public int attackerHealthChange;
	public int attackerHealthMaxChange;
	public Boolean attackerIsStatus = false;		//状态标记
	public int attackerRemainRound;					//状态持续时间
	
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
