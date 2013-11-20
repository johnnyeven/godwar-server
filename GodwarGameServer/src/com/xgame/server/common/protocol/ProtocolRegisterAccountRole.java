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
import com.xgame.server.game.ObjectManager;
import com.xgame.server.game.Player;
import com.xgame.server.game.ProtocolPackage;
import com.xgame.server.network.GameSession;
import com.xgame.server.pool.PlayerPool;
import com.xgame.server.pool.ServerPackagePool;

public class ProtocolRegisterAccountRole implements IProtocol
{
	private static Log	log	= LogFactory
									.getLog( ProtocolRegisterAccountRole.class );

	@Override
	public void Execute( Object param1, Object param2 )
	{
		ProtocolPackage parameter = (ProtocolPackage) param1;
		GameSession session = (GameSession) param2;

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

		if ( guid != Long.MIN_VALUE && nickName != null )
		{
			try
			{
				String sql = "INSERT INTO game_account(account_guid, nick_name)VALUES";
				sql += "(" + guid + ", '" + nickName + "')";
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
					log.error( "[RegisterAccountRole] Player.loadFromDatabase()失败" );
					return;
				}
				initRoleDatabase( p );
				responseUserData( session );

				ObjectManager.getInstance().addPlayer( p );

				rs.close();
			}
			catch ( SQLException e )
			{
				e.printStackTrace();
			}
		}
	}

	private void initRoleDatabase( Player p )
	{
		String sql = "INSERT INTO `game_card_group`(`account_id`, `group_name`, `card_list`)VALUES";
		sql += "(" + p.accountId + ", '第一卡组', '')";
		try
		{
			PreparedStatement st = DatabaseRouter
					.getInstance()
					.getConnection( "gamedb" )
					.prepareStatement( sql, Statement.RETURN_GENERATED_KEYS );
			st.executeUpdate();
		}
		catch ( SQLException e )
		{
			e.printStackTrace();
		}
	}

	private void responseUserData( GameSession session )
	{
		Player p = session.getPlayer();
		ServerPackage pack = ServerPackagePool.getInstance().getObject();
		pack.success = EnumProtocol.ACK_CONFIRM;
		pack.protocolId = EnumProtocol.REGISTER_ACCOUNT_ROLE;
		String uuid = p.getGuid().toString();
		pack.parameter.add( new PackageItem( uuid.length(), uuid ) );
		pack.parameter.add( new PackageItem( 8, p.accountId ) );
		pack.parameter.add( new PackageItem( p.name.length(), p.name ) );
		pack.parameter.add( new PackageItem( 4, p.level ) );
		pack.parameter.add( new PackageItem( 8, p.accountCash ) );
		pack.parameter.add( new PackageItem( p.rolePicture.length(),
				p.rolePicture ) );
		CommandCenter.send( session.getChannel(), pack );
	}

}
