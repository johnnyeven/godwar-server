package com.xgame.server.common.protocol;

import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xgame.server.CommandCenter;
import com.xgame.server.common.PackageItem;
import com.xgame.server.common.ServerPackage;
import com.xgame.server.game.Player;
//import com.xgame.server.game.ProtocolPackage;
import com.xgame.server.game.Room;
import com.xgame.server.network.GameSession;
import com.xgame.server.pool.ServerPackagePool;

public class ProtocolUpdatePlayerReady implements IProtocol
{
	private static Log	log	= LogFactory
									.getLog( ProtocolUpdatePlayerReady.class );

	@Override
	public void Execute( Object param1, Object param2 )
	{
//		ProtocolPackage parameter = (ProtocolPackage) param1;
		GameSession session = (GameSession) param2;

		log.info( "[UpdatePlayerReady] Player Name = " + session.getPlayer().name );
		
		if(session.getPlayer().getCurrentRoom() != null)
		{
			Room room = session.getPlayer().getCurrentRoom();
			Iterator< Player > it = room.getPlayerList().iterator();
			Player p1;
			while ( it.hasNext() )
			{
				p1 = it.next();
				if ( p1 == session.getPlayer() )
				{
					continue;
				}
				
				ServerPackage pack = ServerPackagePool.getInstance().getObject();
				pack.success = EnumProtocol.ACK_CONFIRM;
				pack.protocolId = EnumProtocol.HALL_PLAYER_SELECTED_HERO;
				pack.parameter.add( new PackageItem( 8, session.getPlayer().accountId ) );
				CommandCenter.send( p1.getChannel(), pack );
			}
		}
	}

}
