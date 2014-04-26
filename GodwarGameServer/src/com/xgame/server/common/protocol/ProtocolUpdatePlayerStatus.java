package com.xgame.server.common.protocol;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xgame.server.game.ProtocolPackage;
import com.xgame.server.network.WorldSession;

public class ProtocolUpdatePlayerStatus implements IProtocol
{
	private static Log	log	= LogFactory
									.getLog( ProtocolUpdatePlayerStatus.class );

	@Override
	public void Execute( Object param1, Object param2 )
	{
		ProtocolPackage parameter = (ProtocolPackage) param1;
		WorldSession session = (WorldSession) param2;

		long roleId = Long.MIN_VALUE;

		for ( int i = parameter.offset; i < parameter.receiveDataLength; )
		{
			int length = parameter.receiveData.getInt();
			int type = parameter.receiveData.get();
			switch ( type )
			{
				case EnumProtocol.TYPE_LONG:
					if ( roleId == Long.MIN_VALUE )
					{
						roleId = parameter.receiveData.getLong();
					}
					break;
			}
			i += ( length + 5 );
		}
		log.info( "[UpdatePlayerStatus] RoleId=" + roleId );

		if ( roleId == session.getPlayer().roleId )
		{
			session.getPlayer().getMap()
					.updatePlayerStatus( session.getPlayer() );
		}
		else
		{
			log.error( "�յ���RoleId��Ự�б����RoleId���������е�Id="
					+ session.getPlayer().roleId + ", �յ���Id=" + roleId );
		}
	}

}
