package com.xgame.server.common.protocol;

import java.io.UnsupportedEncodingException;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xgame.server.common.database.DatabaseRouter;
import com.xgame.server.game.ProtocolPackage;
import com.xgame.server.network.WorldSession;

public class ProtocolSaveCardGroupCards implements IProtocol
{
	private static Log	log	= LogFactory
									.getLog( ProtocolSaveCardGroupCards.class );

	@Override
	public void Execute( Object param1, Object param2 )
	{
		ProtocolPackage parameter = (ProtocolPackage) param1;
		WorldSession session = (WorldSession) param2;

		int groupId = Integer.MIN_VALUE;
		String cardList = null;

		String sql = null;

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
				case EnumProtocol.TYPE_STRING:
					if ( cardList == null )
					{
						length = parameter.receiveData.getShort();
						byte[] dst = new byte[length];
						parameter.receiveData.get( dst );
						length += 2;
						try
						{
							cardList = new String( dst, "UTF-8" );
						}
						catch ( UnsupportedEncodingException e )
						{
							e.printStackTrace();
						}
					}
					break;
			}
			if ( groupId != Integer.MIN_VALUE && cardList != null )
			{
				sql = "UPDATE `game_card_group` SET `card_list`='" + cardList
						+ "' WHERE `group_id`=" + groupId;
				try
				{
					PreparedStatement st = DatabaseRouter.getInstance()
							.getConnection( "gamedb" ).prepareStatement( sql );
					st.executeUpdate();
				}
				catch ( SQLException e )
				{
					e.printStackTrace();
				}
				log.info( "[SaveCardGroupCards] GroupId = " + groupId );

				groupId = Integer.MIN_VALUE;
				cardList = null;
			}
			i += ( length + 5 );
		}
	}

}
