package com.xgame.server.common.protocol;

import java.io.UnsupportedEncodingException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xgame.server.CommandCenter;
import com.xgame.server.common.PackageItem;
import com.xgame.server.common.ServerPackage;
import com.xgame.server.common.database.DatabaseRouter;
import com.xgame.server.common.parameter.CardGroupParameter;
import com.xgame.server.game.ProtocolPackage;
import com.xgame.server.network.WorldSession;
import com.xgame.server.pool.ServerPackagePool;

public class ProtocolSaveCardConfig implements IProtocol
{

	private static Log log = LogFactory.getLog(ProtocolSaveCardConfig.class);

	@Override
	public void Execute(Object param1, Object param2)
	{
		ProtocolPackage parameter = (ProtocolPackage) param1;
		WorldSession session = (WorldSession) param2;

		int groupId = Integer.MIN_VALUE;
		List<CardGroupParameter> list = new ArrayList<CardGroupParameter>();
		CardGroupParameter param = new CardGroupParameter();

		for (int i = parameter.offset; i < parameter.receiveDataLength;)
		{
			int length = parameter.receiveData.getInt();
			int type = parameter.receiveData.get();
			switch (type)
			{
				case EnumProtocol.TYPE_INT:
					if (groupId == Integer.MIN_VALUE)
					{
						groupId = parameter.receiveData.getInt();
					} else if (param.groupId == Integer.MIN_VALUE)
					{
						param.groupId = parameter.receiveData.getInt();
					}
					break;
				case EnumProtocol.TYPE_STRING:
					if (param.cardList == null)
					{
						length = parameter.receiveData.getShort();
						byte[] dst = new byte[length];
						parameter.receiveData.get(dst);
						length += 2;
						try
						{
							param.cardList = new String(dst, "UTF-8");
						} catch (UnsupportedEncodingException e)
						{
							e.printStackTrace();
						}
					}
			}
			i += (length + 5);

			if (param.groupId > 0 && param.cardList != null)
			{
				list.add(param);
				param = new CardGroupParameter();
			}
		}
		log.info("[SaveCardConfig] AccountId=" + session.getPlayer().accountId);
		
		if(groupId > 0)
		{
			session.getPlayer().setCurrentCardGroup( groupId );
			
			String sql = "UPDATE `game_card_group` SET `current`=0 WHERE `account_id`=" + session.getPlayer().accountId;
			try
			{
				PreparedStatement st = DatabaseRouter.getInstance()
						.getConnection( "gamedb" ).prepareStatement( sql );
				st.executeUpdate();
				
				sql = "UPDATE `game_card_group` SET `current`=1 WHERE `group_id`=" + groupId;
				st.executeUpdate( sql );
				
				CardGroupParameter it;
				for(int i = 0; i<list.size(); i++)
				{
					it = list.get( i );
					sql = "UPDATE `game_card_group` SET `card_list`='" + it.cardList + "' WHERE `group_id`=" + it.groupId;
					st.executeUpdate( sql );
				}

				ServerPackage pack = ServerPackagePool.getInstance()
						.getObject();
				pack.success = EnumProtocol.ACK_CONFIRM;
				pack.protocolId = EnumProtocol.INFO_SAVE_CARD_GROUP;
				CommandCenter.send( parameter.client, pack );
			}
			catch ( SQLException e )
			{
				e.printStackTrace();
			}
		}
	}

}
