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
		log.info( "[RequestCardList] AccountId = "
				+ session.getPlayer().accountId );

		if ( session.getPlayer().accountId > 0 )
		{
			String sql = "SELECT * FROM `game_card` WHERE `account_id`="
					+ session.getPlayer().accountId;

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

				if ( rs.next() )
				{
					String cardList = rs.getString( "card_list" );
					String heroCardList = rs.getString( "hero_card_list" );
					String freeCardList = GameServer.freeHeroCardConfig;
					heroCardList = mergeHeroCard( heroCardList, freeCardList );
					pack.parameter.add( new PackageItem( cardList.length(),
							cardList ) );
					pack.parameter.add( new PackageItem( heroCardList.length(),
							heroCardList ) );
				}
				CommandCenter.send( parameter.client, pack );
			}
			catch ( SQLException e )
			{
				e.printStackTrace();
			}
		}
	}

	private String mergeHeroCard( String list1, String list2 )
	{
		String[] strList1 = list1.split( "," );
		String[] strList2 = list2.split( "," );

		List< String > l1 = Arrays.asList( strList1 );
		List< String > l2 = Arrays.asList( strList2 );
		Set<String> s = new TreeSet<String>();
		
		s.addAll( l1 );
		s.addAll( l2 );
		
		Iterator<String> it = s.iterator();
		String tmp;
		
		if(it.hasNext())
		{
			StringBuffer buf = new StringBuffer();
			tmp = it.next();
			buf.append( tmp );
			
			while(it.hasNext())
			{
				tmp = it.next();
				buf.append( "," + tmp );
			}
			
			return buf.toString();
		}
		
		return "";
	}

}
