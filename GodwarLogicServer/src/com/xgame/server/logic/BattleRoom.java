package com.xgame.server.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xgame.server.CommandCenter;
import com.xgame.server.cards.SoulCard;
import com.xgame.server.common.PackageItem;
import com.xgame.server.common.ServerPackage;
import com.xgame.server.common.protocol.EnumProtocol;
import com.xgame.server.logic.Player;
import com.xgame.server.pool.ServerPackagePool;

public class BattleRoom extends Room
{

	private List< Player >				group1;
	private List< Player >				group2;
	protected Map< String, Integer >	playerGroupMap;
	protected Map< String, Integer >	playerPositionMap;

	private static Log					log	= LogFactory
													.getLog( BattleRoom.class );

	public BattleRoom()
	{
		super();
	}

	public void initialize()
	{
		super.initialize();

		if ( peopleCount > 0 )
		{
			group1 = new ArrayList< Player >();
			group2 = new ArrayList< Player >();
			playerGroupMap = new HashMap< String, Integer >();
			playerPositionMap = new HashMap< String, Integer >();

			for ( int i = 0; i < peopleCount / 2; i++ )
			{
				group1.add( null );
				group2.add( null );
			}
		}
		else
		{
			log.fatal( "peopleLimit参数应大于0" );
			return;
		}
	}

	public List< Player > getGroup1()
	{
		return group1;
	}

	public List< Player > getGroup2()
	{
		return group2;
	}

	public Boolean addPlayer( Player p )
	{
		if ( playerList.size() == peopleCount )
		{
			log.error( "房间已满员" );
			return false;
		}
		if ( playerList.indexOf( p ) >= 0 )
		{
			log.error( "玩家已存在于该房间" );
			return false;
		}
		String guid = p.getGuid().toString();
		if ( playerGroupMap.containsKey( guid ) )
		{
			int group = playerGroupMap.get( guid );
			int position = playerPositionMap.get( guid );

			try
			{
				if ( group == 1 )
				{
					p.setCurrentGroup( 1 );
					group1.set( position, p );
					p.setCurrentPosition( group1.size() - 1 );
				}
				else
				{
					p.setCurrentGroup( 2 );
					group2.set( position, p );
					p.setCurrentPosition( group2.size() - 1 );
				}
			}
			catch ( IndexOutOfBoundsException e )
			{
				log.fatal( "group position不存在" );
			}
		}
		else
		{
			return false;
		}

		return super.addPlayer( p );
	}

	public void removePlayer( Player p )
	{
		if ( playerList.indexOf( p ) >= 0 )
		{
			int group = p.getCurrentGroup();
			if ( group == 1 )
			{
				group1.remove( p );
			}
			else
			{
				group2.remove( p );
			}
		}
		super.removePlayer( p );
	}

	protected void noticePlayerJoin( Player p )
	{
		Iterator< Player > it = playerList.iterator();
		Player p1;
		ServerPackage pack;
		String uuid;
		String heroCardId;
		while ( it.hasNext() )
		{
			p1 = it.next();
			if ( p1 == p )
			{
				continue;
			}

			pack = ServerPackagePool.getInstance().getObject();
			pack.success = EnumProtocol.ACK_CONFIRM;
			pack.protocolId = EnumProtocol.BATTLEROOM_PLAYER_ENTER_ROOM_LOGICSERVER;

			uuid = p.getGuid().toString();
			pack.parameter.add( new PackageItem( uuid.length(), uuid ) );
			pack.parameter.add( new PackageItem( 8, p.accountId ) );
			pack.parameter.add( new PackageItem( p.name.length(), p.name ) );

			heroCardId = p.getHeroCard().getId();
			pack.parameter.add( new PackageItem( heroCardId.length(),
					heroCardId ) );
			pack.parameter.add( new PackageItem( 4, p.level ) );
			pack.parameter.add( new PackageItem( 4, p.getCurrentGroup() ) );

			CommandCenter.send( p1.getChannel(), pack );

			log.debug( "通知" + p1.name + "新玩家" + p.name + "已加入" );
		}
	}

	@Override
	public Boolean addPlayerGuid( String guid, int group )
	{
		if ( playerGuidList.size() >= peopleCount )
		{
			log.error( "房间已满员" );
			return false;
		}
		if ( playerGuidList.indexOf( guid ) >= 0 )
		{
			log.error( "玩家Guid已存在于该房间" );
			return false;
		}
		playerGuidList.add( guid );
		playerGroupMap.put( guid, group );
		return true;
	}

	@Override
	public Boolean addPlayerPosition( String guid, int position )
	{
		playerPositionMap.put( guid, position );
		return true;
	}

	@Override
	public Boolean hasPlayerGuid( String guid )
	{
		if ( playerGuidList.indexOf( guid ) >= 0 )
		{
			return true;
		}
		return false;
	}

	@Override
	public void start()
	{
		List< Player > playerList;
		Player p;
		ServerPackage pack;
		playerList = getPlayerList();
		for ( int i = 0; i < playerList.size(); i++ )
		{
			p = playerList.get( i );

			pack = ServerPackagePool.getInstance().getObject();
			pack.success = EnumProtocol.ACK_CONFIRM;
			pack.protocolId = EnumProtocol.BATTLEROOM_REQUEST_START_BATTLE;

			CommandCenter.send( p.getChannel(), pack );
		}

		phaseDeploy();
	}

	private void phaseDeploy()
	{
		List< Player > playerList;
		Player p;
		ServerPackage pack;
		playerList = getPlayerList();
		SoulCard card;
		int j = 0;
		StringBuffer buf;
		String cardList;

		for ( int i = 0; i < playerList.size(); i++ )
		{
			p = playerList.get( i );

			pack = ServerPackagePool.getInstance().getObject();
			pack.success = EnumProtocol.ACK_CONFIRM;
			pack.protocolId = EnumProtocol.BATTLEROOM_FIRST_CHOUPAI;

			buf = new StringBuffer();
			card = p.popSoulCardToHand();
			buf.append( card.getId() );
			for ( j = 1; j < 5; j++ )
			{
				card = p.popSoulCardToHand();
				buf.append( "," + card.getId() );
			}
			cardList = buf.toString();
			pack.parameter.add( new PackageItem( cardList.length(), cardList ) );

			// for ( j = 0; j < 3; j++ )
			// {
			// card = p.popSupplyCardToHand();
			// pack.parameter.add( new PackageItem( 4, 1 ) );
			// pack.parameter.add( new PackageItem( card.getId().length(),
			// card.getId() ) );
			// }

			CommandCenter.send( p.getChannel(), pack );
		}
	}
}
