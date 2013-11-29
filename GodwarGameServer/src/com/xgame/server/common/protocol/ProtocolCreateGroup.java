
package com.xgame.server.common.protocol;

import java.io.UnsupportedEncodingException;
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
import com.xgame.server.network.GameSession;
import com.xgame.server.pool.ServerPackagePool;

public class ProtocolCreateGroup implements IProtocol
{

	private static Log	log	= LogFactory.getLog( ProtocolCreateGroup.class );

	@Override
	public void Execute( Object param1, Object param2 )
	{
		ProtocolPackage parameter = ( ProtocolPackage ) param1;
		GameSession session = ( GameSession ) param2;

		String groupName = null;
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
					if ( groupName == null )
					{
						length = parameter.receiveData.getShort();
						byte[] dst = new byte[length];
						parameter.receiveData.get( dst );
						length += 2;
						try
						{
							groupName = new String( dst, "UTF-8" );
						}
						catch ( UnsupportedEncodingException e )
						{
							e.printStackTrace();
						}
					}
			}
			i += ( length + 5 );
		}
		log.info( "[CreateGroup] AccountId=" + session.getPlayer().accountId
				+ ", GroupName=" + groupName );

		if ( groupName != "" )
		{
			String sql = "INSERT INTO `game_card_group`(`account_id`, `group_name`, `card_list`)VALUES";
			sql += "(" + session.getPlayer().accountId + ", '" + groupName
					+ "', '')";
			try
			{
				PreparedStatement st = DatabaseRouter
						.getInstance()
						.getConnection( "gamedb" )
						.prepareStatement( sql, Statement.RETURN_GENERATED_KEYS );
				st.executeUpdate();

				ResultSet rs = st.getGeneratedKeys();
				rs.first();
				int lastInsertId = rs.getInt( 1 );

				ServerPackage pack = ServerPackagePool.getInstance()
						.getObject();
				pack.success = EnumProtocol.ACK_CONFIRM;
				pack.protocolId = EnumProtocol.INFO_CREATE_GROUP;
				pack.parameter.add( new PackageItem( 4, lastInsertId ) );
				pack.parameter.add( new PackageItem( groupName.length(),
						groupName ) );
				CommandCenter.send( parameter.client, pack );
			}
			catch ( SQLException e )
			{
				e.printStackTrace();
			}
		}
	}

}
