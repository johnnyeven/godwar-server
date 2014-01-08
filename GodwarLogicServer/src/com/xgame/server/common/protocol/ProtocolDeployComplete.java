package com.xgame.server.common.protocol;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xgame.server.CommandCenter;
import com.xgame.server.common.PackageItem;
import com.xgame.server.common.ServerPackage;
import com.xgame.server.logic.BattleRoom;
import com.xgame.server.logic.Player;
import com.xgame.server.logic.ProtocolPackage;
import com.xgame.server.logic.Room;
import com.xgame.server.network.GameSession;
import com.xgame.server.pool.ServerPackagePool;
import com.xgame.server.timer.StartBattleRoundTimerTask;
import com.xgame.server.timer.TimerManager;

public class ProtocolDeployComplete implements IProtocol
{

	private static Log	log	= LogFactory.getLog( ProtocolDeployComplete.class );

	@Override
	public void Execute( Object param1, Object param2 )
	{
		ProtocolPackage parameter = (ProtocolPackage) param1;
		GameSession session = (GameSession) param2;

		String defenser = null;
		String attacker1 = null;
		String attacker2 = null;
		String attacker3 = null;

		for ( int i = parameter.offset; i < parameter.receiveDataLength; )
		{
			int length = parameter.receiveData.getInt();
			int type = parameter.receiveData.get();
			switch ( type )
			{
				case EnumProtocol.TYPE_STRING:
					if ( defenser == null )
					{
						length = parameter.receiveData.getShort();
						byte[] dst = new byte[length];
						parameter.receiveData.get( dst );
						length += 2;
						try
						{
							defenser = new String( dst, "UTF-8" );
						}
						catch ( UnsupportedEncodingException e )
						{
							e.printStackTrace();
						}
					}
					else if ( attacker1 == null )
					{
						length = parameter.receiveData.getShort();
						byte[] dst = new byte[length];
						parameter.receiveData.get( dst );
						length += 2;
						try
						{
							attacker1 = new String( dst, "UTF-8" );
						}
						catch ( UnsupportedEncodingException e )
						{
							e.printStackTrace();
						}
					}
					else if ( attacker2 == null )
					{
						length = parameter.receiveData.getShort();
						byte[] dst = new byte[length];
						parameter.receiveData.get( dst );
						length += 2;
						try
						{
							attacker2 = new String( dst, "UTF-8" );
						}
						catch ( UnsupportedEncodingException e )
						{
							e.printStackTrace();
						}
					}
					else if ( attacker3 == null )
					{
						length = parameter.receiveData.getShort();
						byte[] dst = new byte[length];
						parameter.receiveData.get( dst );
						length += 2;
						try
						{
							attacker3 = new String( dst, "UTF-8" );
						}
						catch ( UnsupportedEncodingException e )
						{
							e.printStackTrace();
						}
					}
					break;
			}
			i += ( length + 5 );
		}
		log.info( "[DeployComplete] AccountId=" + session.getPlayer().name
				+ ", Defenser=" + defenser + ", Attacker1=" + attacker1
				+ ", Attacker2=" + attacker2 + ", Attacker3=" + attacker3 );

		if ( !defenser.equals( "" ) && !attacker1.equals( "" )
				&& !attacker2.equals( "" ) && !attacker3.equals( "" ) )
		{
			Player player = session.getPlayer();
			player.setCardDefenser( defenser );
			player.setCardAttacker1( attacker1 );
			player.setCardAttacker2( attacker2 );
			player.setCardAttacker3( attacker3 );
			Room room = player.getCurrentRoom();
			List< Player > list = room.getPlayerList();
			Player p;
			ServerPackage pack;
			String guid;
			boolean isDeploy = true;
			for ( int i = 0; i < list.size(); i++ )
			{
				p = list.get( i );

				pack = ServerPackagePool.getInstance().getObject();
				pack.success = EnumProtocol.ACK_CONFIRM;
				pack.protocolId = EnumProtocol.BATTLEROOM_DEPLOY_COMPLETE;

				guid = player.getGuid().toString();
				pack.parameter.add( new PackageItem( guid.length(), guid ) );
				CommandCenter.send( p.getChannel(), pack );

				if ( !p.isDeploy() )
				{
					isDeploy = false;
				}
			}

			if ( isDeploy )
			{
				if ( room instanceof BattleRoom )
				{
					startBattleDice( (BattleRoom) room );
				}
			}
		}
	}

	private void startBattleDice( BattleRoom room )
	{
		Map< String, Integer > map1 = new HashMap< String, Integer >();
		Map< String, Integer > map2 = new HashMap< String, Integer >();
		List< Player > list = room.getPlayerList();
		List< Player > list1 = room.getGroup1();
		List< Player > list2 = room.getGroup2();
		Player p;
		ServerPackage pack;
		String guid;
		int value;
		Iterator< Entry< String, Integer >> it;
		Entry< String, Integer > en;
		Random random = new Random();

		for ( int i = 0; i < list1.size(); i++ )
		{
			p = list1.get( i );
			map1.put( p.getGuid().toString(),
					Math.abs( random.nextInt() ) % 6 + 1 );
		}

		for ( int i = 0; i < list2.size(); i++ )
		{
			p = list2.get( i );
			map2.put( p.getGuid().toString(),
					Math.abs( random.nextInt() ) % 6 + 1 );
		}

		for ( int i = 0; i < list.size(); i++ )
		{
			p = list.get( i );

			pack = ServerPackagePool.getInstance().getObject();
			pack.success = EnumProtocol.ACK_CONFIRM;
			pack.protocolId = EnumProtocol.BATTLEROOM_START_DICE;

			it = map1.entrySet().iterator();
			while ( it.hasNext() )
			{
				en = it.next();
				guid = en.getKey();
				value = en.getValue();
				pack.parameter.add( new PackageItem( guid.length(), guid ) );
				pack.parameter.add( new PackageItem( 4, value ) );
			}

			it = map2.entrySet().iterator();
			while ( it.hasNext() )
			{
				en = it.next();
				guid = en.getKey();
				value = en.getValue();
				pack.parameter.add( new PackageItem( guid.length(), guid ) );
				pack.parameter.add( new PackageItem( 4, value ) );
			}

			CommandCenter.send( p.getChannel(), pack );
		}

		StartBattleRoundTimerTask timer = new StartBattleRoundTimerTask(map1, map2, room);
		TimerManager.getInstance().schedule( timer, 5000 );
	}
}
