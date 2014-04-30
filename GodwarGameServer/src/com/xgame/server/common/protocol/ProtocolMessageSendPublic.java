package com.xgame.server.common.protocol;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xgame.server.CommandCenter;
import com.xgame.server.common.CoordinatePair;
import com.xgame.server.common.PackageItem;
import com.xgame.server.common.ServerPackage;
import com.xgame.server.game.ProtocolPackage;
import com.xgame.server.game.map.Grid;
import com.xgame.server.game.map.Map;
import com.xgame.server.network.WorldSession;
import com.xgame.server.objects.Player;
import com.xgame.server.objects.WorldObject;
import com.xgame.server.pool.ServerPackagePool;

public class ProtocolMessageSendPublic implements IProtocol
{
	private static Log	log	= LogFactory.getLog( ProtocolCreateGroup.class );

	@Override
	public void Execute(Object param1, Object param2)
	{
		ProtocolPackage parameter = ( ProtocolPackage ) param1;
		WorldSession session = ( WorldSession ) param2;

		String message = null;
		long timestamp = Long.MIN_VALUE;

		for ( int i = parameter.offset; i < parameter.receiveDataLength; )
		{
			int length = parameter.receiveData.getInt();
			int type = parameter.receiveData.get();
			switch ( type )
			{
				case EnumProtocol.TYPE_LONG:
					if ( timestamp == Long.MIN_VALUE )
					{
						timestamp = parameter.receiveData.getLong();
						break;
					}
				case EnumProtocol.TYPE_STRING:
					if ( message == null )
					{
						length = parameter.receiveData.getShort();
						byte[] dst = new byte[length];
						parameter.receiveData.get( dst );
						length += 2;
						try
						{
							message = new String( dst, "UTF-8" );
						}
						catch ( UnsupportedEncodingException e )
						{
							e.printStackTrace();
						}
					}
			}
			i += ( length + 5 );
		}
		log.info( "[MessageSendPublic] RoleId=" + session.getPlayer().roleId );
		
		if(message != null && !message.equals(""))
		{

			// 告诉视野内其他玩家
			Map m = session.getPlayer().getMap();
			CoordinatePair coordinate = Map.getCoordinatePair( session
					.getPlayer().getX(), session.getPlayer().getY() );
			Grid g = m.getGrid( (int) coordinate.getX(),
					(int) coordinate.getY() );

			ArrayList< Grid > list = m.getViewGrid( g );
			Iterator< Grid > itG = list.iterator();
			Iterator< Entry< UUID, WorldObject >> gridIt;
			Grid currentGrid;
			Entry< UUID, WorldObject > en;
			Player currentPlayer;

			ServerPackage pack = ServerPackagePool.getInstance()
					.getObject();
			pack.success = EnumProtocol.ACK_CONFIRM;
			pack.protocolId = EnumProtocol.MSG_SEND_PUBLIC;
			
			String uuid = session.getPlayer().getGuid().toString();
			pack.parameter.add( new PackageItem( uuid.length(), uuid ) );
			pack.parameter.add( new PackageItem( message.length(), message ) );
			
			while ( itG.hasNext() )
			{
				currentGrid = itG.next();

				if ( currentGrid == null )
				{
					continue;
				}

				gridIt = currentGrid.getWorldObjectIterator();
				while ( gridIt.hasNext() )
				{
					en = gridIt.next();
					
					currentPlayer = (Player) en.getValue();
					if ( !currentPlayer.getChannel().isOpen() )
					{
						continue;
					}
					CommandCenter.send( currentPlayer.getChannel(), pack );
				}
			}
		}
	}

}
