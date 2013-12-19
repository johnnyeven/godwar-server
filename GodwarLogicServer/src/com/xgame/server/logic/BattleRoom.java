package com.xgame.server.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.xgame.server.CommandCenter;
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

	public BattleRoom()
	{
		super();
		playerGroupMap = new HashMap< String, Integer >();
	}

	public void initialize()
	{
		super.initialize();

		if ( peopleCount > 0 )
		{
			group1 = new ArrayList< Player >();
			group2 = new ArrayList< Player >();
		}
		else
		{
			log.fatal( "peopleLimit参数应大于0" );
			return;
		}
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
			if ( group == 1 )
			{
				p.setCurrentGroup( 1 );
				group1.add( p );
			}
			else
			{
				p.setCurrentGroup( 2 );
				group2.add( p );
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
		while ( it.hasNext() )
		{
			p1 = it.next();
			if ( p1 == p )
			{
				continue;
			}

			ServerPackage pack = ServerPackagePool.getInstance().getObject();
			pack.success = EnumProtocol.ACK_CONFIRM;
			pack.protocolId = EnumProtocol.BATTLEROOM_PLAYER_ENTER_ROOM_LOGICSERVER;

			String uuid = p.getGuid().toString();
			pack.parameter.add( new PackageItem( uuid.length(), uuid ) );
			pack.parameter.add( new PackageItem( 8, p.accountId ) );
			pack.parameter.add( new PackageItem( p.name.length(), p.name ) );
			pack.parameter.add( new PackageItem( 4, p.level ) );
			pack.parameter.add( new PackageItem( p.rolePicture.length(),
					p.rolePicture ) );
			pack.parameter.add( new PackageItem( 8, p.accountCash ) );
			pack.parameter.add( new PackageItem( 4, p.winningCount ) );
			pack.parameter.add( new PackageItem( 4, p.battleCount ) );
			pack.parameter.add( new PackageItem( 4, p.honor ) );
			pack.parameter.add( new PackageItem( 4, 1 ) );
			pack.parameter.add( new PackageItem( 4, p.getCurrentGroup() ) );

			CommandCenter.send( p1.getChannel(), pack );
		}
	}

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

	public Boolean addHeroCardId( String guid, String id )
	{
		if ( heroCardIdMap.containsKey( guid ) )
		{
			log.error( "玩家英雄卡牌已存在于该房间" );
			return false;
		}
		heroCardIdMap.put( guid, id );
		return true;
	}

	public Boolean hasPlayerGuid( String guid )
	{
		if ( playerGuidList.indexOf( guid ) >= 0 )
		{
			return true;
		}
		return false;
	}
}
