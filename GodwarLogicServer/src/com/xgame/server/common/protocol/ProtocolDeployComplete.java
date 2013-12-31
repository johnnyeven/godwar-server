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

public class ProtocolDeployComplete implements IProtocol
{

	private static Log	log	= LogFactory.getLog( ProtocolDeployComplete.class );

	@Override
	public void Execute( Object param1, Object param2 )
	{
		ProtocolPackage parameter = ( ProtocolPackage ) param1;
		GameSession session = ( GameSession ) param2;

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
			for ( int i = 0; i < list.size(); i++ )
			{
				p = list.get( i );

				pack = ServerPackagePool.getInstance().getObject();
				pack.success = EnumProtocol.ACK_CONFIRM;
				pack.protocolId = EnumProtocol.BATTLEROOM_DEPLOY_COMPLETE;

				guid = player.getGuid().toString();
				pack.parameter.add( new PackageItem( guid.length(), guid ) );
				CommandCenter.send( p.getChannel(), pack );
			}
		}
	}

}
