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
import com.xgame.server.game.map.MapManager;
import com.xgame.server.network.WorldSession;
import com.xgame.server.objects.ObjectManager;
import com.xgame.server.objects.Player;
import com.xgame.server.pool.PlayerPool;
import com.xgame.server.pool.ServerPackagePool;

public class ProtocolRequestAccountRole implements IProtocol
{
	private static Log	log	= LogFactory
									.getLog( ProtocolRequestAccountRole.class );

	@Override
	public void Execute( Object param1, Object param2 )
	{
		ProtocolPackage parameter = (ProtocolPackage) param1;
		WorldSession session = (WorldSession) param2;

		long guid = Long.MIN_VALUE;
		for ( int i = parameter.offset; i < parameter.receiveDataLength; )
		{
			int length = parameter.receiveData.getInt();
			int type = parameter.receiveData.get();
			switch ( type )
			{
				case EnumProtocol.TYPE_LONG:
					if ( guid == Long.MIN_VALUE )
					{
						guid = parameter.receiveData.getLong();
					}
					break;
			}
			i += ( length + 5 );
		}
		log.info( "[RequestAccountRole] Guid=" + guid );

		if ( guid != Long.MIN_VALUE )
		{
			try
			{
				String sql = "SELECT *  FROM `role` WHERE `account_id` = "
						+ guid;
				PreparedStatement st = DatabaseRouter.getInstance()
						.getConnection( "gamedb" ).prepareStatement( sql );
				ResultSet rs = st.executeQuery();
				if ( rs.first() )
				{
					// TODO ����Player����
					Player p = PlayerPool.getInstance().getObject();
					p.roleId = rs.getLong( "role_id" );
					p.setChannel( parameter.client );
					session.setPlayer( p );
					if ( !p.loadFromDatabase() )
					{

					}

					if ( !MapManager.getInstance().getMap( p.getMapId() ).add( p ) )
					{
						log.error( "Map::add() ʧ�ܣ�Player=" + p.name );
						return;
					}
					
					responseUserData( session );

					ObjectManager.getInstance().addPlayer( p );

					rs.close();
				}
				else
				{
					ServerPackage pack = ServerPackagePool.getInstance()
							.getObject();
					pack.success = EnumProtocol.ACK_CONFIRM;
					pack.protocolId = EnumProtocol.REQUEST_ACCOUNT_ROLE;
					pack.parameter.add( new PackageItem( 8, (long) ( -1 ) ) );
					CommandCenter.send( parameter.client, pack );
				}
				rs.close();
			}
			catch ( SQLException e )
			{
				e.printStackTrace();
			}
		}
	}

	private void responseUserData( WorldSession session )
	{
		Player p = session.getPlayer();
		ServerPackage pack = ServerPackagePool.getInstance().getObject();
		pack.success = EnumProtocol.ACK_CONFIRM;
		pack.protocolId = EnumProtocol.REQUEST_ACCOUNT_ROLE;
		String uuid = p.getGuid().toString();
		pack.parameter.add( new PackageItem( uuid.length(), uuid ) );
		pack.parameter.add( new PackageItem( 8, p.accountId ) );
		pack.parameter.add( new PackageItem( p.name.length(), p.name ) );
		pack.parameter.add( new PackageItem( 4, p.level ) );
		pack.parameter.add( new PackageItem( 8, p.accountCash ) );
		pack.parameter.add( new PackageItem( 4, p.energy ) );
		pack.parameter.add( new PackageItem( p.rolePicture.length(),
				p.rolePicture ) );
		pack.parameter.add( new PackageItem( 4, p.getMapId() ) );
		pack.parameter.add( new PackageItem( 4, p.getX() ) );
		pack.parameter.add( new PackageItem( 4, p.getY() ) );
		CommandCenter.send( session.getChannel(), pack );
	}

}
