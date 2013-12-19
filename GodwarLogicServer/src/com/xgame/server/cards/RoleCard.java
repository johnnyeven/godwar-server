package com.xgame.server.cards;

public class RoleCard extends Card implements IBattlable
{
	protected int	attack;
	protected int	def;
	protected int	mdef;
	protected int	health;
	protected int	healthMax;

	public RoleCard()
	{
		super();
	}
	
	public RoleCard( String id )
	{
		super( id );
	}

	@Override
	public void attack( IBattlable target )
	{
		AttackParameter parameter = new AttackParameter();
		target.underAttack( parameter );
	}

	@Override
	public void underAttack( AttackParameter parameter )
	{

	}

	public void loadInfo()
	{
		super.loadInfo();

		RoleCardParameter param = (RoleCardParameter) parameter;
		if ( param != null )
		{
			attack = param.attack;
			def = param.def;
			mdef = param.mdef;
			health = param.health;
			healthMax = param.health;
		}
	}

	public int getAttack()
	{
		return attack;
	}

	public void setAttack( int attack )
	{
		this.attack = attack;
	}

	public int getDef()
	{
		return def;
	}

	public void setDef( int def )
	{
		this.def = def;
	}

	public int getMdef()
	{
		return mdef;
	}

	public void setMdef( int mdef )
	{
		this.mdef = mdef;
	}

	public int getHealth()
	{
		return health;
	}

	public void setHealth( int health )
	{
		this.health = health;
	}

	public int getHealthMax()
	{
		return healthMax;
	}

	public void setHealthMax( int healthMax )
	{
		this.healthMax = healthMax;
	}

}
