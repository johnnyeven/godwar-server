package com.xgame.server.common.protocol;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xgame.server.CommandCenter;
import com.xgame.server.common.PackageItem;
import com.xgame.server.common.ServerPackage;
import com.xgame.server.logic.Player;
import com.xgame.server.logic.ProtocolPackage;
import com.xgame.server.logic.Room;
import com.xgame.server.network.GameSession;
import com.xgame.server.pool.ServerPackagePool;

public class ProtocolChangeFormation implements IProtocol
{

	private static Log	log	= LogFactory.getLog( ProtocolChangeFormation.class );

	@Override
	public void Execute( Object param1, Object param2 )
	{
		ProtocolPackage parameter = (ProtocolPackage) param1;
		GameSession session = (GameSession) param2;

		String cardIn = null;
		String cardOut = null;

		for ( int i = parameter.offset; i < parameter.receiveDataLength; )
		{
			int length = parameter.receiveData.getInt();
			int type = parameter.receiveData.get();
			switch ( type )
			{
				case EnumProtocol.TYPE_STRING:
					if ( cardOut == null )
					{
						length = parameter.receiveData.getShort();
						byte[] dst = new byte[length];
						parameter.receiveData.get( dst );
						length += 2;
						try
						{
							cardOut = new String( dst, "UTF-8" );
						}
						catch ( UnsupportedEncodingException e )
						{
							e.printStackTrace();
						}
					}
					else if ( cardIn == null )
					{
						length = parameter.receiveData.getShort();
						byte[] dst = new byte[length];
						parameter.receiveData.get( dst );
						length += 2;
						try
						{
							cardIn = new String( dst, "UTF-8" );
						}
						catch ( UnsupportedEncodingException e )
						{
							e.printStackTrace();
						}
					}
			}
			i += ( length + 5 );
		}

		log.info( "[ChangeFormation] player = " + session.getPlayer().name
				+ ", CardIn = " + cardIn + ", CardOut = " + cardOut );

		if ( !cardIn.equals( "" ) && !cardOut.equals( "" ) )
		{
			Player player = session.getPlayer();
			String guid = player.getGuid().toString();
			int position = Integer.MIN_VALUE;
			if ( player.getCardDefenser().getId().equals( cardOut ) )
			{
				position = 0;
				player.setCardDefenser( cardIn );
			}
			else if ( player.getCardAttacker1().getId().equals( cardOut ) )
			{
				position = 1;
				player.setCardAttacker1( cardIn );
			}
			else if ( player.getCardAttacker2().getId().equals( cardOut ) )
			{
				position = 2;
				player.setCardAttacker2( cardIn );
			}
			else if ( player.getCardAttacker3().getId().equals( cardOut ) )
			{
				position = 3;
				player.setCardAttacker3( cardIn );
			}

			Room room = session.getPlayer().getCurrentRoom();
			List< Player > list = room.getPlayerList();
			Player p;
			ServerPackage pack;
			for ( int i = 0; i < list.size(); i++ )
			{
				p = list.get( i );

				pack = ServerPackagePool.getInstance().getObject();
				pack.success = EnumProtocol.ACK_CONFIRM;
				pack.protocolId = EnumProtocol.BATTLEROOM_ROUND_STANDBY_CHANGE_FORMATION;

				pack.parameter.add( new PackageItem( guid.length(), guid ) );
				pack.parameter.add( new PackageItem( 4, position ) );

				if ( p == player )
				{
					pack.parameter.add( new PackageItem( cardOut.length(),
							cardOut ) );
					pack.parameter.add( new PackageItem( cardIn.length(),
							cardIn ) );
				}
				CommandCenter.send( p.getChannel(), pack );
			}
		}
	}

}
