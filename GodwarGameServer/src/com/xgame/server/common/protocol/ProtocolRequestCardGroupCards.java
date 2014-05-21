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
import com.xgame.server.common.parameter.SoulCardParameter;
import com.xgame.server.game.ProtocolPackage;
import com.xgame.server.network.WorldSession;
import com.xgame.server.objects.hashmap.CardConfigMap;
import com.xgame.server.pool.ServerPackagePool;

public class ProtocolRequestCardGroupCards implements IProtocol
{
	private static Log	log	= LogFactory
									.getLog( ProtocolRequestCardGroupCards.class );

	@Override
	public void Execute( Object param1, Object param2 )
	{
		ProtocolPackage parameter = (ProtocolPackage) param1;
		WorldSession session = (WorldSession) param2;

		int groupId = Integer.MIN_VALUE;

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
			}
			i += ( length + 5 );
		}
		log.info( "[RequestCardGroupCards] GroupId = " + groupId );

		if ( groupId > 0 )
		{
			String sql = "SELECT * FROM `game_card_group` WHERE `group_id`="
					+ groupId;

			PreparedStatement st;
			try
			{
				st = DatabaseRouter.getInstance().getConnection( "gamedb" )
						.prepareStatement( sql );
				ResultSet rs = st.executeQuery();

				ServerPackage pack = ServerPackagePool.getInstance()
						.getObject();
				pack.success = EnumProtocol.ACK_CONFIRM;
				pack.protocolId = EnumProtocol.INFO_REQUEST_CARD_GROUP_CARDS;
				
				pack.parameter.add( new PackageItem(4, groupId) );
				if ( rs.next() )
				{
					String cardList = rs.getString( "card_list" );
					rs.close();

					if ( !cardList.equals( "" ) )
					{
						sql = "SELECT * FROM `game_card` WHERE `card_id` IN ("
								+ cardList + ")";
						st = DatabaseRouter.getInstance()
								.getConnection( "gamedb" )
								.prepareStatement( sql );
						rs = st.executeQuery();
						
						long guid;
						String id;
						String name;
						int attack, def, mdef, health, energy, level, race;
						while(rs.next())
						{
							guid = rs.getLong( "card_id" );
							id = rs.getString( "resource_id" );
							name = rs.getString( "name" );
							attack = rs.getInt( "attack" );
							def = rs.getInt( "def" );
							mdef = rs.getInt( "mdef" );
							health = rs.getInt( "health" );
							energy = rs.getInt( "energy" );
							level = rs.getInt( "level" );
							race = rs.getInt( "race" );
							pack.parameter.add( new PackageItem( 8, guid ) );
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
						rs.close();
					}
				}
				CommandCenter.send( session.getChannel(), pack );
			}
			catch ( SQLException e )
			{
				e.printStackTrace();
			}
		}
	}

}
