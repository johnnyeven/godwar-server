package com.xgame.server.network;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.CompletionHandler;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xgame.server.CommandCenter;
import com.xgame.server.common.AuthSessionPackage;
import com.xgame.server.common.PackageItem;
import com.xgame.server.common.ServerPackage;
import com.xgame.server.common.database.DatabaseRouter;
import com.xgame.server.common.protocol.EnumProtocol;
import com.xgame.server.logic.ObjectManager;
import com.xgame.server.logic.Player;
import com.xgame.server.logic.BattleHall;
import com.xgame.server.pool.BufferPool;
import com.xgame.server.pool.PlayerPool;
import com.xgame.server.pool.ServerPackagePool;

public class AuthSessionCompletionHandler implements
		CompletionHandler< Integer, AuthSessionPackage >
{
	private static Log	log	= LogFactory
									.getLog( AuthSessionCompletionHandler.class );

	public AuthSessionCompletionHandler()
	{
	}

	@Override
	public void completed( Integer arg0, AuthSessionPackage arg1 )
	{
		ByteBuffer buffer = arg1.buffer;
		buffer.flip();
		int packageLength = buffer.getInt();
		short protocolId = buffer.getShort();

		if ( protocolId == EnumProtocol.INFO_LOGICSERVER_BIND_SESSION )
		{
			String guid = null;
			int roomType = Integer.MIN_VALUE;

			for ( int i = 6; i < arg0; )
			{
				int length = buffer.getInt();
				int type = buffer.get();
				switch ( type )
				{
					case EnumProtocol.TYPE_STRING:
						if ( guid == null )
						{
							length = buffer.getShort();
							byte[] dst = new byte[length];
							buffer.get( dst );
							length += 2;
							try
							{
								guid = new String( dst, "UTF-8" );
							}
							catch ( UnsupportedEncodingException e )
							{
								e.printStackTrace();
							}
						}
						break;
					case EnumProtocol.TYPE_INT:
						if ( roomType == Integer.MIN_VALUE )
						{
							roomType = buffer.getInt();
						}
						break;
				}
				i += ( length + 5 );
			}
			guid = guid.toUpperCase();
			log.info( "[AuthSession] Guid = " + guid );

			if ( guid != null )
			{
				try
				{
					String sql = "SELECT * FROM `game_account` WHERE `game_guid`='"
							+ guid + "'";
					PreparedStatement st = DatabaseRouter.getInstance()
							.getConnection( "gamedb" ).prepareStatement( sql );
					ResultSet rs = st.executeQuery();
					if ( rs.next() )
					{
						GameSession s = new GameSession( guid, arg1.channel,
								new Date().getTime() );

						Player p = PlayerPool.getInstance().getObject();
						p.accountId = rs.getLong( "account_id" );
						p.setChannel( arg1.channel );
						s.setPlayer( p );
						if ( !p.loadFromDatabase() )
						{

						}
						ObjectManager.getInstance().addPlayer( p );

						BattleHall.getInstance().addSessionQueue( s );
						s.startRecv();

						ServerPackage pack = ServerPackagePool.getInstance()
								.getObject();
						pack.success = EnumProtocol.ACK_CONFIRM;
						pack.protocolId = EnumProtocol.INFO_LOGICSERVER_BIND_SESSION;
						pack.parameter.add( new PackageItem( 4, 1 ) );
						CommandCenter.send( arg1.channel, pack );
					}

					rs.close();
				}
				catch ( SQLException e )
				{
					e.printStackTrace();
				}
			}

			BufferPool.getInstance().releaseBuffer( buffer );
		}
		else
		{
			log.error( "绑定Session协议号错误，收到的协议号为" + protocolId + ", 应该为"
					+ EnumProtocol.INFO_LOGICSERVER_BIND_SESSION );
		}
	}

	@Override
	public void failed( Throwable arg0, AuthSessionPackage arg1 )
	{
		log.error( arg0.getMessage() );
		ByteBuffer buffer = arg1.buffer;
		BufferPool.getInstance().releaseBuffer( buffer );
	}

}
