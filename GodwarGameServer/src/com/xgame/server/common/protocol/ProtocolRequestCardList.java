package com.xgame.server.common.protocol;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xgame.server.CommandCenter;
import com.xgame.server.common.PackageItem;
import com.xgame.server.common.ServerPackage;
import com.xgame.server.common.database.DatabaseRouter;
import com.xgame.server.game.GameServer;
import com.xgame.server.game.ProtocolPackage;
import com.xgame.server.network.WorldSession;
import com.xgame.server.pool.ServerPackagePool;

public class ProtocolRequestCardList implements IProtocol
{
	private static Log	log	= LogFactory.getLog( ProtocolRequestCardList.class );

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
		log.info( "[RequestCardList] RoleId = " + session.getPlayer().roleId );

		if ( session.getPlayer().accountId > 0 )
		{
			String sql = "SELECT * FROM `game_card` WHERE `role_id`="
					+ session.getPlayer().roleId;

			PreparedStatement st;
			try
			{
				st = DatabaseRouter.getInstance().getConnection( "gamedb" )
						.prepareStatement( sql );
				ResultSet rs = st.executeQuery();

				ServerPackage pack = ServerPackagePool.getInstance()
						.getObject();
				pack.success = EnumProtocol.ACK_CONFIRM;
				pack.protocolId = EnumProtocol.INFO_REQUEST_CARD_LIST;

				String id;
				String name;
				int attack, def, mdef, health, energy, level, race;
				while ( rs.next() )
				{
					id = rs.getString( "resource_id" );
					name = rs.getString( "name" );
					attack = rs.getInt( "attack" );
					def = rs.getInt( "def" );
					mdef = rs.getInt( "mdef" );
					health = rs.getInt( "health" );
					energy = rs.getInt( "energy" );
					level = rs.getInt( "level" );
					race = rs.getInt( "race" );
					pack.parameter.add( new PackageItem( id.length(), id ) );
					pack.parameter.add( new PackageItem( name.length(), name ) );
					pack.parameter.add( new PackageItem( 4, attack ) );
					pack.parameter.add( new PackageItem( 4, def ) );
					pack.parameter.add( new PackageItem( 4, mdef ) );
					pack.parameter.add( new PackageItem( 4, health ) );
					pack.parameter.add( new PackageItem( 4, energy ) );
					pack.parameter.add( new PackageItem( 4, level ) );
					pack.parameter.add( new PackageItem( 4, race ) );
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
