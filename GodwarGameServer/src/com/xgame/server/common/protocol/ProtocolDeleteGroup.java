
package com.xgame.server.common.protocol;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xgame.server.CommandCenter;
import com.xgame.server.common.PackageItem;
import com.xgame.server.common.ServerPackage;
import com.xgame.server.common.database.DatabaseRouter;
import com.xgame.server.game.ProtocolPackage;
import com.xgame.server.network.WorldSession;
import com.xgame.server.pool.ServerPackagePool;

public class ProtocolDeleteGroup implements IProtocol
{

	private static Log	log	= LogFactory.getLog( ProtocolDeleteGroup.class );

	@Override
	public void Execute( Object param1, Object param2 )
	{
		ProtocolPackage parameter = ( ProtocolPackage ) param1;
		WorldSession session = ( WorldSession ) param2;

		int groupId = Integer.MIN_VALUE;
		long timestamp = Long.MIN_VALUE;

		for ( int i = parameter.offset; i < parameter.receiveDataLength; )
		{
			int length = parameter.receiveData.getInt();
			int type = parameter.receiveData.get();
			switch ( type )
			{
				case EnumProtocol.TYPE_INT:
					if ( groupId == Integer.MIN_VALUE )
					{
						groupId = parameter.receiveData.getInt();
					}
					break;
				case EnumProtocol.TYPE_LONG:
					if ( timestamp == Long.MIN_VALUE )
					{
						timestamp = parameter.receiveData.getLong();
					}
					break;
			}
			i += ( length + 5 );
		}
		log.info( "[DeleteGroup] RoleId=" + session.getPlayer().roleId
				+ ", GroupId=" + groupId );

		if ( groupId > 0 )
		{
			String sql = "DELETE FROM `game_card_group` WHERE `group_id`="
					+ groupId;
			try
			{
				PreparedStatement st = DatabaseRouter.getInstance()
						.getConnection( "gamedb" ).prepareStatement( sql );
				st.executeUpdate();

				ServerPackage pack = ServerPackagePool.getInstance()
						.getObject();
				pack.success = EnumProtocol.ACK_CONFIRM;
				pack.protocolId = EnumProtocol.INFO_DELETE_GROUP;
				pack.parameter.add( new PackageItem( 4, groupId ) );
				CommandCenter.send( parameter.client, pack );
			}
			catch ( SQLException e )
			{
				e.printStackTrace();
			}
		}
	}

}
