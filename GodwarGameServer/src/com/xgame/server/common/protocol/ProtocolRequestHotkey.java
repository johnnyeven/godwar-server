package com.xgame.server.common.protocol;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
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

public class ProtocolRequestHotkey implements IProtocol
{
	private static Log	log	= LogFactory.getLog( ProtocolRequestHotkey.class );

	@Override
	public void Execute( Object param1, Object param2 )
	{
		ProtocolPackage parameter = (ProtocolPackage) param1;
		WorldSession session = (WorldSession) param2;

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
		log.info( "[RequestHotkey] AccountId=" + accountId );

		if ( accountId != Long.MIN_VALUE )
		{
			try
			{
				String sql = "SELECT *  FROM `game_hotkey_config` WHERE `account_id` = "
						+ accountId;
				PreparedStatement st = DatabaseRouter.getInstance()
						.getConnection( "gamedb" ).prepareStatement( sql );
				ResultSet rs = st.executeQuery();
				ServerPackage pack = ServerPackagePool.getInstance()
						.getObject();
				pack.success = EnumProtocol.ACK_CONFIRM;
				pack.protocolId = EnumProtocol.REQUEST_HOTKEY;
				if ( rs.first() )
				{
					String config = rs.getString( "config" );
					pack.parameter.add( new PackageItem( config.length(),
							config ) );
				}
				else
				{
					String initConfig = "<skill><hotkey code=\"112\" class=\"skill.Skill1\" /><hotkey code=\"113\" class=\"skill.Sheild1\" /></skill>";
					sql = "INSERT INTO `game_hotkey_config`(`config`)VALUES('"
							+ initConfig + "')";
					PreparedStatement st1 = DatabaseRouter.getInstance()
							.getConnection( "gamedb" ).prepareStatement( sql );
					st1.executeUpdate();
					pack.parameter.add( new PackageItem( initConfig.length(),
							initConfig ) );
				}
				rs.close();
				CommandCenter.send( parameter.client, pack );
			}
			catch ( SQLException e )
			{
				e.printStackTrace();
			}
		}
	}

}
