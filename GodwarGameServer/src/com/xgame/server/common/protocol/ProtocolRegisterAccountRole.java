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
import com.xgame.server.game.map.MapManager;
import com.xgame.server.network.WorldSession;
import com.xgame.server.objects.ObjectManager;
import com.xgame.server.objects.Player;
import com.xgame.server.pool.PlayerPool;

public class ProtocolRegisterAccountRole implements IProtocol
{
	private static Log	log	= LogFactory
									.getLog( ProtocolRegisterAccountRole.class );

	@Override
	public void Execute( Object param1, Object param2 )
	{
		ProtocolPackage parameter = (ProtocolPackage) param1;
		WorldSession session = (WorldSession) param2;

		long guid = Long.MIN_VALUE;
		String nickName = null;
		long timestamp = Long.MIN_VALUE;

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
						break;
					}
					if ( timestamp == Long.MIN_VALUE )
					{
						timestamp = parameter.receiveData.getLong();
						break;
					}
				case EnumProtocol.TYPE_STRING:
					if ( nickName == null )
					{
						length = parameter.receiveData.getShort();
						byte[] dst = new byte[length];
						parameter.receiveData.get( dst );
						length += 2;
						try
						{
							nickName = new String( dst, "UTF-8" );
						}
						catch ( UnsupportedEncodingException e )
						{
							e.printStackTrace();
						}
					}
			}
			i += ( length + 5 );
		}
		log.info( "[RegisterAccountRole] Guid=" + guid + ", NickName="
				+ nickName );

		if ( guid != Integer.MIN_VALUE && nickName != null )
		{
			try
			{
				String sql = "INSERT INTO game_account(account_guid, nick_name, speed, current_health, max_health, current_mana, max_mana, current_energy, max_energy, current_x, current_y)values";
				sql += "(" + guid + ", '" + nickName
						+ "', 210.00, 200, 200, 85, 85, 100, 100, 700, 700)";
				PreparedStatement st = DatabaseRouter
						.getInstance()
						.getConnection( "gamedb" )
						.prepareStatement( sql, Statement.RETURN_GENERATED_KEYS );
				st.executeUpdate();

				ResultSet rs = st.getGeneratedKeys();
				rs.first();
				long lastInsertId = rs.getLong( 1 );

				// TODO 创建Player对象
				Player p = PlayerPool.getInstance().getObject();
				p.accountId = lastInsertId;
				p.setChannel( parameter.client );
				session.setPlayer( p );
				if ( !p.loadFromDatabase() )
				{

				}

				if ( !MapManager.getInstance().getMap( p.getMapId() ).add( p ) )
				{
					log.error( "Map::add() 失败，Player=" + p.name );
					return;
				}

				ObjectManager.getInstance().addObject( p );

				rs.close();
			}
			catch ( SQLException e )
			{
				e.printStackTrace();
			}
		}
	}

}
