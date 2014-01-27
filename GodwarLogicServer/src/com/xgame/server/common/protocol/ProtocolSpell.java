package com.xgame.server.common.protocol;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import javax.script.ScriptException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xgame.server.CommandCenter;
import com.xgame.server.cards.SoulCard;
import com.xgame.server.common.PackageItem;
import com.xgame.server.common.ServerPackage;
import com.xgame.server.logic.Player;
import com.xgame.server.logic.ProtocolPackage;
import com.xgame.server.logic.Room;
import com.xgame.server.network.GameSession;
import com.xgame.server.pool.ServerPackagePool;
import com.xgame.server.skill.AttackInfo;
import com.xgame.server.skill.SkillManager;
import com.xgame.server.skill.SkillParameter;

public class ProtocolSpell implements IProtocol
{

	private static Log	log	= LogFactory.getLog( ProtocolSpell.class );

	@Override
	public void Execute( Object param1, Object param2 )
	{
		ProtocolPackage parameter = (ProtocolPackage) param1;
		GameSession session = (GameSession) param2;

		String attackerCard = null;
		String defenderGuid = null;
		String skillId = null;

		for ( int i = parameter.offset; i < parameter.receiveDataLength; )
		{
			int length = parameter.receiveData.getInt();
			int type = parameter.receiveData.get();
			switch ( type )
			{
				case EnumProtocol.TYPE_STRING:
					if ( attackerCard == null )
					{
						length = parameter.receiveData.getShort();
						byte[] dst = new byte[length];
						parameter.receiveData.get( dst );
						length += 2;
						try
						{
							attackerCard = new String( dst, "UTF-8" );
						}
						catch ( UnsupportedEncodingException e )
						{
							e.printStackTrace();
						}
					}
					else if ( defenderGuid == null )
					{
						length = parameter.receiveData.getShort();
						byte[] dst = new byte[length];
						parameter.receiveData.get( dst );
						length += 2;
						try
						{
							defenderGuid = new String( dst, "UTF-8" );
						}
						catch ( UnsupportedEncodingException e )
						{
							e.printStackTrace();
						}
					}
					else if ( skillId == null )
					{
						length = parameter.receiveData.getShort();
						byte[] dst = new byte[length];
						parameter.receiveData.get( dst );
						length += 2;
						try
						{
							skillId = new String( dst, "UTF-8" );
						}
						catch ( UnsupportedEncodingException e )
						{
							e.printStackTrace();
						}
					}
			}
			i += ( length + 5 );
		}

		log.info( "[Spell] player = " + session.getPlayer().name
				+ ", CardId = " + attackerCard + ", Defender = " + defenderGuid
				+ ", SpellId = " + skillId );

		if ( attackerCard != null && !attackerCard.equals( "" )
				&& skillId != null && !skillId.equals( "" ) )
		{
			Player player = session.getPlayer();
			Room room = player.getCurrentRoom();
			SoulCard card = player.getFormationCard( attackerCard );
			List< AttackInfo > info = null;
			if ( card != null )
			{
				Player defender = null;
				if ( !defenderGuid.equals( "" ) )
				{
					Map< String, Player > guidMap = room.getPlayerGuidMap();
					defender = guidMap.get( defenderGuid );
				}
				info = soulCardSpell( player, card, skillId, defender );
			}
			applyAttackInfo( skillId, info, room );
		}
	}

	private List< AttackInfo > soulCardSpell( Player attacker,
			SoulCard attackerCard, String skillId, Player defender )
	{
		List< String > list;
		SkillParameter skill;
		List< AttackInfo > info;

		list = attackerCard.getSkillList();
		if ( list.indexOf( skillId ) >= 0 )
		{
			skill = SkillManager.getInstance().get( skillId );
			if ( skill != null )
			{
				try
				{
					if ( skill.target.equals( "me" ) )
					{
						info = SkillManager.getInstance().execute( attacker,
								attacker, skillId, attackerCard, attackerCard );
						return info;
					}
					else if ( skill.target.equals( "enemy" ) )
					{
						int position = attacker
								.getFormationCardPosition( attackerCard.getId() );
						SoulCard defenderCard = defender
								.getFormationCard( position );
						info = SkillManager.getInstance().execute( attacker,
								defender, skillId, attackerCard, defenderCard );
						return info;
					}
				}
				catch ( ScriptException e )
				{
					e.printStackTrace();
					return null;
				}
			}
		}
		else
		{

		}

		return null;
	}

	private void applyAttackInfo( String spellId, List< AttackInfo > list,
			Room room )
	{
		if ( list != null )
		{
			AttackInfo info;
			Player attacker = null;
			Player defender = null;
			SoulCard attackerCard = null;
			SoulCard defenderCard = null;
			String guid = null;
			int position = Integer.MIN_VALUE;

			ServerPackage pack = ServerPackagePool.getInstance().getObject();
			pack.success = EnumProtocol.ACK_CONFIRM;
			pack.protocolId = EnumProtocol.BATTLEROOM_ROUND_ACTION_SPELL;

			for ( int i = 0; i < list.size(); i++ )
			{
				info = list.get( i );

				pack.parameter.add( new PackageItem( info.skillId.length(),
						info.skillId ) );
				if ( info.attacker != null )
				{
					attacker = info.attacker;
					guid = attacker.getGuid().toString();
					pack.parameter.add( new PackageItem( guid.length(), guid ) );
				}
				else
				{
					pack.parameter.add( new PackageItem( 0, "" ) );
				}
				if ( info.defender != null )
				{
					defender = info.defender;
					guid = defender.getGuid().toString();
					pack.parameter.add( new PackageItem( guid.length(), guid ) );
				}
				else
				{
					pack.parameter.add( new PackageItem( 0, "" ) );
				}

				if ( info.attackerCard instanceof SoulCard )
				{
					attackerCard = (SoulCard) info.attackerCard;
					pack.parameter.add( new PackageItem( attackerCard.getId()
							.length(), attackerCard.getId() ) );
					position = attacker.getFormationCardPosition( attackerCard
							.getId() );
					pack.parameter.add( new PackageItem( 4, position ) );
					if ( info.defenderCard == null )
					{
						pack.parameter.add( new PackageItem( 1, false ) );
					}
					else
					{
						pack.parameter.add( new PackageItem( 1, true ) );
					}
				}
				else
				{
					pack.parameter.add( new PackageItem( 0, "" ) );
					pack.parameter.add( new PackageItem( 4, -1 ) );
					pack.parameter.add( new PackageItem( 1, false ) );
				}
				if ( info.defenderCard instanceof SoulCard )
				{
					defenderCard = (SoulCard) info.defenderCard;
					pack.parameter.add( new PackageItem( defenderCard.getId()
							.length(), defenderCard.getId() ) );
					position = defender.getFormationCardPosition( defenderCard
							.getId() );
					pack.parameter.add( new PackageItem( 4, position ) );
					pack.parameter.add( new PackageItem( 1, false ) );
				}
				else
				{
					pack.parameter.add( new PackageItem( 0, "" ) );
					pack.parameter.add( new PackageItem( 4, -1 ) );
					pack.parameter.add( new PackageItem( 1, false ) );
				}

				if ( attackerCard != null )
				{
					if ( info.attackerAttackChange != 0 )
					{
						attackerCard.setLastAttack( attackerCard.getAttack() );
						attackerCard.setAttack( attackerCard.getAttack()
								+ info.attackerAttackChange );
					}
					pack.parameter.add( new PackageItem( 4,
							info.attackerAttackChange ) );
					pack.parameter.add( new PackageItem( 4, attackerCard
							.getAttack() ) );

					if ( info.attackerDefChange != 0 )
					{
						attackerCard.setLastDef( attackerCard.getDef() );
						attackerCard.setDef( attackerCard.getDef()
								+ info.attackerDefChange );
					}
					pack.parameter.add( new PackageItem( 4,
							info.attackerDefChange ) );
					pack.parameter.add( new PackageItem( 4, attackerCard
							.getDef() ) );

					if ( info.attackerMdefChange != 0 )
					{
						attackerCard.setLastMdef( attackerCard.getMdef() );
						attackerCard.setMdef( attackerCard.getMdef()
								+ info.attackerMdefChange );
					}
					pack.parameter.add( new PackageItem( 4,
							info.attackerMdefChange ) );
					pack.parameter.add( new PackageItem( 4, attackerCard
							.getMdef() ) );

					if ( info.attackerHealthChange != 0 )
					{
						attackerCard.setHealth( attackerCard.getHealth()
								+ info.attackerHealthChange );
						if ( attackerCard.getHealth() == 0 )
						{
							// 死亡处理
						}
					}
					pack.parameter.add( new PackageItem( 4,
							info.attackerHealthChange ) );
					pack.parameter.add( new PackageItem( 4, attackerCard
							.getHealth() ) );

					if ( info.attackerHealthMaxChange != 0 )
					{
						attackerCard.setLastHealthMax( attackerCard
								.getHealthMax() );
						attackerCard.setHealthMax( attackerCard.getHealthMax()
								+ info.attackerHealthMaxChange );
					}
					pack.parameter.add( new PackageItem( 4,
							info.attackerHealthMaxChange ) );
					pack.parameter.add( new PackageItem( 4, attackerCard
							.getHealthMax() ) );

					if ( info.attackerIsStatus )
					{

						if ( info.attackerRemainRound > 0 )
						{

						}
					}
					pack.parameter.add( new PackageItem( 1,
							info.attackerIsStatus ) );
					pack.parameter.add( new PackageItem( 4,
							info.attackerRemainRound ) );
				}
				else
				{
					pack.parameter.add( new PackageItem( 4, 0 ) );
					pack.parameter.add( new PackageItem( 4, 0 ) );
					pack.parameter.add( new PackageItem( 4, 0 ) );
					pack.parameter.add( new PackageItem( 4, 0 ) );
					pack.parameter.add( new PackageItem( 4, 0 ) );
					pack.parameter.add( new PackageItem( 4, 0 ) );
					pack.parameter.add( new PackageItem( 4, 0 ) );
					pack.parameter.add( new PackageItem( 4, 0 ) );
					pack.parameter.add( new PackageItem( 4, 0 ) );
					pack.parameter.add( new PackageItem( 4, 0 ) );
					pack.parameter.add( new PackageItem( 1, false ) );
					pack.parameter.add( new PackageItem( 4, 0 ) );
				}
				if ( defenderCard != null )
				{
					if ( info.defenderAttackChange != 0 )
					{
						defenderCard.setLastAttack( defenderCard.getAttack() );
						defenderCard.setAttack( defenderCard.getAttack()
								+ info.defenderAttackChange );
					}
					pack.parameter.add( new PackageItem( 4,
							info.defenderAttackChange ) );
					pack.parameter.add( new PackageItem( 4, defenderCard
							.getAttack() ) );

					if ( info.defenderDefChange != 0 )
					{
						defenderCard.setLastDef( defenderCard.getDef() );
						defenderCard.setDef( defenderCard.getDef()
								+ info.defenderDefChange );
					}
					pack.parameter.add( new PackageItem( 4,
							info.defenderDefChange ) );
					pack.parameter.add( new PackageItem( 4, defenderCard
							.getDef() ) );

					if ( info.defenderMdefChange != 0 )
					{
						defenderCard.setLastMdef( defenderCard.getMdef() );
						defenderCard.setMdef( defenderCard.getMdef()
								+ info.defenderMdefChange );
					}
					pack.parameter.add( new PackageItem( 4,
							info.defenderMdefChange ) );
					pack.parameter.add( new PackageItem( 4, defenderCard
							.getMdef() ) );

					if ( info.defenderHealthChange != 0 )
					{
						defenderCard.setHealth( defenderCard.getHealth()
								+ info.defenderHealthChange );
						if ( defenderCard.getHealth() == 0 )
						{
							// 死亡处理
						}
					}
					pack.parameter.add( new PackageItem( 4,
							info.defenderHealthChange ) );
					pack.parameter.add( new PackageItem( 4, defenderCard
							.getHealth() ) );

					if ( info.defenderHealthMaxChange != 0 )
					{
						defenderCard.setLastHealthMax( defenderCard
								.getHealthMax() );
						defenderCard.setHealthMax( defenderCard.getHealthMax()
								+ info.defenderHealthMaxChange );
					}
					pack.parameter.add( new PackageItem( 4,
							info.defenderHealthMaxChange ) );
					pack.parameter.add( new PackageItem( 4, defenderCard
							.getHealthMax() ) );

					if ( info.defenderIsStatus )
					{

						if ( info.defenderRemainRound > 0 )
						{

						}
					}
					pack.parameter.add( new PackageItem( 1,
							info.defenderIsStatus ) );
					pack.parameter.add( new PackageItem( 4,
							info.defenderRemainRound ) );
				}
				else
				{
					pack.parameter.add( new PackageItem( 4, 0 ) );
					pack.parameter.add( new PackageItem( 4, 0 ) );
					pack.parameter.add( new PackageItem( 4, 0 ) );
					pack.parameter.add( new PackageItem( 4, 0 ) );
					pack.parameter.add( new PackageItem( 4, 0 ) );
					pack.parameter.add( new PackageItem( 4, 0 ) );
					pack.parameter.add( new PackageItem( 4, 0 ) );
					pack.parameter.add( new PackageItem( 4, 0 ) );
					pack.parameter.add( new PackageItem( 4, 0 ) );
					pack.parameter.add( new PackageItem( 4, 0 ) );
					pack.parameter.add( new PackageItem( 1, false ) );
					pack.parameter.add( new PackageItem( 4, 0 ) );
				}
			}

			List< Player > playerList = room.getPlayerList();
			Player p;
			for ( int j = 0; j < playerList.size(); j++ )
			{
				p = playerList.get( j );
				CommandCenter.send( p.getChannel(), pack );
			}
		}
	}

}
