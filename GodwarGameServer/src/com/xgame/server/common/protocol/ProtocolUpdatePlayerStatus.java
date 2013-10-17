package com.xgame.server.common.protocol;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xgame.server.game.ProtocolPackage;
import com.xgame.server.network.GameSession;

public class ProtocolUpdatePlayerStatus implements IProtocol
{
	private static Log	log	= LogFactory
									.getLog( ProtocolUpdatePlayerStatus.class );

	@Override
	public void Execute( Object param1, Object param2 )
	{
		ProtocolPackage parameter = (ProtocolPackage) param1;
		GameSession session = (GameSession) param2;

		long accountId = Long.MIN_VALUE;

		for ( int i = parameter.offset; i < parameter.receiveDataLength; )
		{
			int length = parameter.receiveData.getInt();
			int type = parameter.receiveData.get();
			switch ( type )
			{
				case EnumProtocol.TYPE_LONG:
					if ( accountId == Long.MIN_VALUE )
					{
						accountId = parameter.receiveData.getLong();
					}
					break;
			}
			i += ( length + 5 );
		}
		log.info( "[UpdatePlayerStatus] AccountId=" + accountId );

//		if ( accountId == session.getPlayer().accountId )
//		{
//			session.getPlayer().getMap()
//					.updatePlayerStatus( session.getPlayer() );
//		}
//		else
//		{
//			log.error( "收到的AccountId与会话中保存的AccountId不符，现有的Id="
//					+ session.getPlayer().accountId + ", 收到的Id=" + accountId );
//		}
	}

}
