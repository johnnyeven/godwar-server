package com.xgame.server.cards;

public interface IBattlable
{
	public void attack( IBattlable target );

	public void underAttack( AttackParameter parameter );
}
