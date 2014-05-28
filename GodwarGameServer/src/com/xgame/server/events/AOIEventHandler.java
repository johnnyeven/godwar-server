package com.xgame.server.events;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xgame.server.CommandCenter;
import com.xgame.server.common.PackageItem;
import com.xgame.server.common.ServerPackage;
import com.xgame.server.common.protocol.EnumProtocol;
import com.xgame.server.objects.InstancePortal;
import com.xgame.server.objects.MapPortal;
import com.xgame.server.objects.NPC;
import com.xgame.server.objects.Player;
import com.xgame.server.objects.Portal;
import com.xgame.server.objects.WorldObject;
import com.xgame.server.pool.ServerPackagePool;

public class AOIEventHandler implements IEventCallback
{
	private static Log				log	= LogFactory
												.getLog( AOIEventHandler.class );
	private static AOIEventHandler	instance;

	private AOIEventHandler()
	{

	}

	@Override
	public void execute( Event evt )
	{
		AOIEvent e = (AOIEvent) evt;
		WorldObject sender = e.getSender();

		if ( sender instanceof Player )
		{
			Player p = (Player) sender;

			ServerPackage pack = ServerPackagePool.getInstance().getObject();
			pack.success = EnumProtocol.ACK_CONFIRM;

			if ( e.who instanceof Player )
			{
				pack.protocolId = e.getName().equals( AOIEvent.AOI_ENTER ) ? EnumProtocol.SCENE_SHOW_PLAYER
						: EnumProtocol.SCENE_REMOVE_PLAYER;
			}
			else if ( e.who instanceof NPC )
			{
				pack.protocolId = e.getName().equals( AOIEvent.AOI_ENTER ) ? EnumProtocol.SCENE_SHOW_NPC
						: EnumProtocol.SCENE_REMOVE_NPC;
			}
			else if ( e.who instanceof InstancePortal )
			{
				pack.protocolId = e.getName().equals( AOIEvent.AOI_ENTER ) ? EnumProtocol.SCENE_SHOW_INSTANCE_PORTAL
						: EnumProtocol.SCENE_REMOVE_INSTANCE_PORTAL;
			}
			else if ( e.who instanceof MapPortal )
			{
				pack.protocolId = e.getName().equals( AOIEvent.AOI_ENTER ) ? EnumProtocol.SCENE_SHOW_MAP_PORTAL
						: EnumProtocol.SCENE_REMOVE_MAP_PORTAL;
			}

			if ( e.getName().equals( AOIEvent.AOI_ENTER ) )
			{
				String uuid = e.who.getGuid().toString();
				pack.parameter.add( new PackageItem( uuid.length(), uuid ) );

				if ( e.who instanceof Player )
				{
					Player p1 = (Player) e.who;
					pack.parameter.add( new PackageItem( 8, p1.accountId ) );
					pack.parameter.add( new PackageItem( 8, p1.roleId ) );
					pack.parameter.add( new PackageItem( p1.name.length(),
							p1.name ) );
					pack.parameter.add( new PackageItem( 8, p1.accountCash ) );
					pack.parameter.add( new PackageItem( 4, p1.direction ) );
					pack.parameter.add( new PackageItem( 4, p1.getSpeed() ) );
					pack.parameter.add( new PackageItem( 4, p1.energy ) );
					pack.parameter.add( new PackageItem( 4, p1.energyMax ) );
					pack.parameter.add( new PackageItem( 8, p1.getX() ) );
					pack.parameter.add( new PackageItem( 8, p1.getY() ) );
				}
				else if ( e.who instanceof NPC )
				{
					NPC n1 = (NPC) e.who;
					pack.parameter.add( new PackageItem( 4, n1.getId() ) );
					pack.parameter.add( new PackageItem( n1.getResource()
							.length(), n1.getResource() ) );
					pack.parameter.add( new PackageItem( n1.getPrependName()
							.length(), n1.getPrependName() ) );
					pack.parameter.add( new PackageItem( n1.getName().length(),
							n1.getName() ) );
					pack.parameter.add( new PackageItem( 4, n1.getLevel() ) );
					pack.parameter.add( new PackageItem( 4, n1.getHealth() ) );
					pack.parameter.add( new PackageItem( 4, n1.getMana() ) );
					pack.parameter
							.add( new PackageItem( 4, n1.getDirection() ) );
					pack.parameter.add( new PackageItem( 4, n1.getAction() ) );
					pack.parameter.add( new PackageItem( 8, n1.getX() ) );
					pack.parameter.add( new PackageItem( 8, n1.getY() ) );
				}
				else if ( e.who instanceof InstancePortal )
				{
					InstancePortal portal = (InstancePortal) e.who;
					pack.parameter.add( new PackageItem( 8, portal.getX() ) );
					pack.parameter.add( new PackageItem( 8, portal.getY() ) );
					pack.parameter.add( new PackageItem( portal.getResourceId()
							.length(), portal.getResourceId() ) );

					List< Integer > list = portal.getInstanceList();
					for ( int i = 0; i < list.size(); i++ )
					{
						pack.parameter
								.add( new PackageItem( 4, list.get( i ) ) );
					}
				}
				else if ( e.who instanceof MapPortal )
				{
					MapPortal portal = (MapPortal) e.who;
					pack.parameter.add( new PackageItem( 8, portal.getX() ) );
					pack.parameter.add( new PackageItem( 8, portal.getY() ) );
					pack.parameter.add( new PackageItem( portal.getResourceId()
							.length(), portal.getResourceId() ) );
					pack.parameter.add( new PackageItem( 4, portal
							.getDestinationMapId() ) );
					pack.parameter.add( new PackageItem( 4, portal
							.getDestinationX() ) );
					pack.parameter.add( new PackageItem( 4, portal
							.getDestinationY() ) );
				}
			}
			else
			{
				String uuid = e.who.getGuid().toString();
				pack.parameter.add( new PackageItem( uuid.length(), uuid ) );
			}

			CommandCenter.send( p.getChannel(), pack );
		}
	}

	public static AOIEventHandler getInstance()
	{
		if ( instance == null )
		{
			instance = new AOIEventHandler();
		}
		return instance;
	}

}
