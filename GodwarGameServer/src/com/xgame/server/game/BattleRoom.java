package com.xgame.server.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.xgame.server.CommandCenter;
import com.xgame.server.common.PackageItem;
import com.xgame.server.common.ServerPackage;
import com.xgame.server.common.protocol.EnumProtocol;
import com.xgame.server.pool.ServerPackagePool;

public class BattleRoom extends Room
{

	private List< Player >			group1;
	private List< Player >			group2;
	protected Map< Player, String >	heroGroup1;
	protected Map< Player, String >	heroGroup2;

	public BattleRoom()
	{
		super();
	}

	public void initialize()
	{
		super.initialize();

		if ( peopleLimit > 0 )
		{
			group1 = new ArrayList< Player >();
			group2 = new ArrayList< Player >();
			heroGroup1 = new HashMap< Player, String >();
			heroGroup2 = new HashMap< Player, String >();
		}
		else
		{
			log.fatal( "peopleLimit参数应大于0" );
			return;
		}
	}

	public void setPlayerHero( Player p, String hero )
	{
		int group = p.getCurrentGroup();

		if ( group == 1 )
		{
			if ( heroGroup1.containsValue( hero ) )
			{
				return;
			}
			else
			{
				heroGroup1.put( p, hero );
			}
		}
		else
		{
			if ( heroGroup2.containsValue( hero ) )
			{
				return;
			}
			else
			{
				heroGroup2.put( p, hero );
			}
		}
		super.setPlayerHero( p, hero );
	}

	public Boolean addPlayer( Player p )
	{
		if ( peopleCount >= peopleLimit )
		{
			log.error( "房间已满员" );
			return false;
		}
		if ( playerList.indexOf( p ) >= 0 )
		{
			log.error( "玩家已存在于该房间" );
			return false;
		}
		if ( group1.size() <= group2.size() )
		{
			p.setCurrentGroup( 1 );
			group1.add( p );
			p.setCurrentPosition( group1.size() - 1 );
		}
		else
		{
			p.setCurrentGroup( 2 );
			group2.add( p );
			p.setCurrentPosition( group2.size() - 1 );
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
			pack.protocolId = EnumProtocol.BATTLEROOM_PLAYER_ENTER_ROOM;

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

	protected void noticePlayerSelectedHero( Player p )
	{
		Iterator< Player > it = playerList.iterator();
		Player p1;
		while ( it.hasNext() )
		{
			p1 = it.next();

			if ( p1.getCurrentGroup() == p.getCurrentGroup() )
			{
				ServerPackage pack = ServerPackagePool.getInstance()
						.getObject();
				pack.success = EnumProtocol.ACK_CONFIRM;
				pack.protocolId = EnumProtocol.BATTLEROOM_PLAYER_SELECTED_HERO;
				String guid = p.getGuid().toString();
				pack.parameter.add( new PackageItem( guid.length(), guid ) );
				pack.parameter.add( new PackageItem( p.getLastHeroCardId()
						.length(), p.getLastHeroCardId() ) );
				pack.parameter.add( new PackageItem( p.getCurrentHeroCardId()
						.length(), p.getCurrentHeroCardId() ) );
				CommandCenter.send( p1.getChannel(), pack );
			}
		}
	}
}
