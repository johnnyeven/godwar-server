package com.xgame.server.common.protocol;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xgame.server.CommandCenter;
import com.xgame.server.common.PackageItem;
import com.xgame.server.common.ServerPackage;
import com.xgame.server.common.database.DatabaseRouter;
import com.xgame.server.game.ProtocolPackage;
import com.xgame.server.network.WorldSession;
import com.xgame.server.pool.ServerPackagePool;

public class ProtocolRequestCardGroup implements IProtocol
{
	private static Log	log	= LogFactory
									.getLog( ProtocolRequestCardGroup.class );

	@Override
	public void Execute( Object param1, Object param2 )
	{
		ProtocolPackage parameter = (ProtocolPackage) param1;
		WorldSession session = (WorldSession) param2;

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
					}
					break;
			}
			i += ( length + 5 );
		}
		log.info( "[RequestCardGroup] AccountId = "
				+ session.getPlayer().accountId );

		if ( session.getPlayer().accountId > 0 )
		{
			String sql = "SELECT * FROM `game_card_group` WHERE `account_id`="
					+ session.getPlayer().accountId;

			PreparedStatement st;
			try
			{
				st = DatabaseRouter.getInstance().getConnection( "gamedb" )
						.prepareStatement( sql );
				ResultSet rs = st.executeQuery();

				ServerPackage pack = ServerPackagePool.getInstance()
						.getObject();
				pack.success = EnumProtocol.ACK_CONFIRM;
				pack.protocolId = EnumProtocol.INFO_REQUEST_CARD_GROUP;

				String groupName;
				String cardList;
				while ( rs.next() )
				{
					groupName = rs.getString( "group_name" );
					pack.parameter.add( new PackageItem( 4, rs
							.getInt( "group_id" ) ) );
					pack.parameter.add( new PackageItem( groupName.length(),
							groupName ) );
					cardList = rs.getString( "card_list" );
					pack.parameter.add( new PackageItem( cardList.length(),
							cardList ) );
				}
				CommandCenter.send( parameter.client, pack );
			}
			catch ( SQLException e )
			{
				e.printStackTrace();
			}
		}
	}

}
