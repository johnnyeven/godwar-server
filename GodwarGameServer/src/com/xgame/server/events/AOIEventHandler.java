package com.xgame.server.events;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xgame.server.common.ServerPackage;
import com.xgame.server.common.protocol.EnumProtocol;
import com.xgame.server.objects.NPC;
import com.xgame.server.objects.Player;
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
			
			if(e.who instanceof Player)
			{
				pack.protocolId = e.getName().equals( AOIEvent.AOI_ENTER ) ? EnumProtocol.SCENE_SHOW_PLAYER : EnumProtocol.SCENE_REMOVE_PLAYER;
			}
			else if(e.who instanceof NPC)
			{
				pack.protocolId = e.getName().equals( AOIEvent.AOI_ENTER ) ? EnumProtocol.SCENE_SHOW_NPC : EnumProtocol.SCENE_REMOVE_NPC;
			}
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
