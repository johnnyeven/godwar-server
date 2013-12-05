
package com.xgame.server.common.protocol;

import java.util.HashMap;
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
		// ProtocolPackage parameter = (ProtocolPackage) param1;
		GameSession session = ( GameSession ) param2;

		log.info( "[UpdatePlayerReady] Player Name = "
				+ session.getPlayer().name );

		if ( session.getPlayer().getCurrentRoom() != null )
		{
			Room room = session.getPlayer().getCurrentRoom();
			HashMap< Player, Boolean > statusMap = ( HashMap< Player, Boolean > ) room
					.getStatusMap();
			Iterator< Player > it = room.getPlayerList().iterator();
			Player p;
			while ( it.hasNext() )
			{
				p = it.next();
				if ( p == session.getPlayer() )
				{
					if ( statusMap.containsKey( p ) )
					{
						statusMap.put( p, true );
					}
					continue;
				}

				ServerPackage pack = ServerPackagePool.getInstance()
						.getObject();
				pack.success = EnumProtocol.ACK_CONFIRM;
				pack.protocolId = EnumProtocol.BATTLEROOM_PLAYER_READY;

				String guid = session.getPlayer().getGuid().toString();
				pack.parameter.add( new PackageItem( guid.length(), guid ) );
				CommandCenter.send( p.getChannel(), pack );
			}
		}
	}

}
