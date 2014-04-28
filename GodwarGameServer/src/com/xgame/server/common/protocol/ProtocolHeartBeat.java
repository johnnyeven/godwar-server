package com.xgame.server.common.protocol;

import com.xgame.server.CommandCenter;
import com.xgame.server.common.PackageItem;
import com.xgame.server.common.ServerPackage;
import com.xgame.server.game.ProtocolPackage;
import com.xgame.server.network.WorldSession;
import com.xgame.server.pool.ServerPackagePool;

public class ProtocolHeartBeat implements IProtocol
{

	@Override
	public void Execute( Object param1, Object param2 )
	{
		ProtocolPackage parameter = (ProtocolPackage) param1;
		WorldSession session = (WorldSession) param2;

		int flag = Integer.MIN_VALUE;
		long timestamp = Long.MIN_VALUE;
		for ( int i = parameter.offset; i < parameter.receiveDataLength; )
		{
			int length = parameter.receiveData.getInt();
			int type = parameter.receiveData.get();
			switch ( type )
			{
				case EnumProtocol.TYPE_INT:
					if ( flag == Integer.MIN_VALUE )
					{
						flag = parameter.receiveData.getInt();
					}
					break;
				case EnumProtocol.TYPE_LONG:
					if(timestamp == Long.MIN_VALUE)
					{
						timestamp = parameter.receiveData.getLong();
					}
					break;
					
			}
			i += ( length + 5 );
		}

		session.setHeartBeatTime( System.currentTimeMillis() );
		ServerPackage pack = ServerPackagePool.getInstance().getObject();
		pack.success = EnumProtocol.ACK_CONFIRM;
		pack.protocolId = EnumProtocol.INFO_HEART_BEAT_ECHO;
		pack.parameter.add( new PackageItem( 4, flag ) );
		pack.parameter.add( new PackageItem( 8, timestamp ) );

		CommandCenter.send( session.getChannel(), pack );
	}

}
