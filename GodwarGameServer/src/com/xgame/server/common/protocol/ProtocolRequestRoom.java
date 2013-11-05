package com.xgame.server.common.protocol;

import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xgame.server.CommandCenter;
import com.xgame.server.common.PackageItem;
import com.xgame.server.common.ServerPackage;
import com.xgame.server.game.BattleHall;
import com.xgame.server.game.BattleRoom;
import com.xgame.server.game.ProtocolPackage;
import com.xgame.server.network.GameSession;
import com.xgame.server.pool.BattleRoomPool;
import com.xgame.server.pool.ServerPackagePool;

/*
 * type
 * 0 = BattleRoom
 * 1 = MeleeRoom
 */

public class ProtocolRequestRoom implements IProtocol
{

	private static Log	log	= LogFactory.getLog( ProtocolRequestRoom.class );

	@Override
	public void Execute( Object param1, Object param2 )
	{
		ProtocolPackage parameter = (ProtocolPackage) param1;
		GameSession session = (GameSession) param2;

		int roomType = Integer.MIN_VALUE;
		int peopleLimit = Integer.MIN_VALUE;
		String title = null;
		for ( int i = parameter.offset; i < parameter.receiveDataLength; )
		{
			int length = parameter.receiveData.getInt();
			int type = parameter.receiveData.get();
			switch ( type )
			{
				case EnumProtocol.TYPE_INT:
					if ( roomType == Integer.MIN_VALUE )
					{
						roomType = parameter.receiveData.getInt();
					}
					else if ( peopleLimit == Integer.MIN_VALUE )
					{
						peopleLimit = parameter.receiveData.getInt();
					}
					break;
				case EnumProtocol.TYPE_STRING:
					if(title == null)
					{
						length = parameter.receiveData.getShort();
						byte[] dst = new byte[length];
						parameter.receiveData.get( dst );
						length += 2;
						try
						{
							title = new String( dst, "UTF-8" );
						}
						catch ( UnsupportedEncodingException e )
						{
							e.printStackTrace();
						}
					}
			}
			i += ( length + 5 );
		}
		log.info( "[RequestRoom] Room Type = " + roomType + ", Title = " + title + ", People limit = " + peopleLimit );

		int id = 0;
		BattleRoom room = null;
		if ( roomType == 0 )
		{
			room = BattleRoomPool.getInstance().getObject();
			room.setTitle( title );
			room.setOwner( session.getPlayer() );
			room.setPeopleLimit( peopleLimit );
			room.setPeopleCount( 0 );
			room.initialize();
			BattleHall.getInstance().addRoom( room );
			id = room.getId();
		}
		else if ( roomType == 1 )
		{

		}
		else
		{
			log.fatal( "Room Type ´íÎó£¬roomType = " + roomType );
			return;
		}

		if ( id > 0 )
		{
			ServerPackage pack = ServerPackagePool.getInstance().getObject();
			pack.success = EnumProtocol.ACK_CONFIRM;
			pack.protocolId = EnumProtocol.HALL_REQUEST_ROOM;
			pack.parameter.add( new PackageItem( 4, id ) );
			CommandCenter.send( parameter.client, pack );
			
			Iterator< Entry< Long, GameSession >> it = BattleHall.getInstance()
					.getSessionMapIterator();
			Entry< Long, GameSession > en;
			GameSession s;
			while ( it.hasNext() )
			{
				en = it.next();
				s = en.getValue();
				if ( s == session || s == null || !s.getChannel().isOpen() )
				{
					continue;
				}

				pack = ServerPackagePool.getInstance().getObject();
				pack.success = EnumProtocol.ACK_CONFIRM;
				pack.protocolId = EnumProtocol.HALL_ROOM_CREATED;
				pack.parameter.add( new PackageItem( 4, id ) );
				pack.parameter.add( new PackageItem( room.getTitle().length(),
						room.getTitle() ) );
				pack.parameter.add( new PackageItem( room.getOwner().name
						.length(), room.getOwner().name ) );
				pack.parameter
						.add( new PackageItem( 4, room.getPeopleCount() ) );
				pack.parameter
						.add( new PackageItem( 4, room.getPeopleLimit() ) );
				CommandCenter.send( s.getChannel(), pack );
			}
		}
	}

}
