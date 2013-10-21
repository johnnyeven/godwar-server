package com.xgame.server.common.protocol;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xgame.server.cards.Card;
import com.xgame.server.game.ProtocolPackage;
import com.xgame.server.game.Room;
import com.xgame.server.network.GameSession;

public class ProtocolChooseHero implements IProtocol
{

	private static Log	log	= LogFactory.getLog( ProtocolChooseHero.class );

	@Override
	public void Execute( Object param1, Object param2 )
	{
		ProtocolPackage parameter = (ProtocolPackage) param1;
		GameSession session = (GameSession) param2;

		String id = null;
		for ( int i = parameter.offset; i < parameter.receiveDataLength; )
		{
			int length = parameter.receiveData.getInt();
			int type = parameter.receiveData.get();
			switch ( type )
			{
				case EnumProtocol.TYPE_STRING:
					if ( id == null )
					{
						length = parameter.receiveData.getShort();
						byte[] dst = new byte[length];
						parameter.receiveData.get( dst );
						length += 2;
						try
						{
							id = new String( dst, "UTF-8" );
						}
						catch ( UnsupportedEncodingException e )
						{
							e.printStackTrace();
						}
					}
			}
			i += ( length + 5 );
		}
		log.info( "[ChooseHero] Card Id = " + id + ", Player = "
				+ session.getPlayer().name );

		if ( id != null && session.getPlayer().getCurrentRoom() != null )
		{
			Room room = session.getPlayer().getCurrentRoom();
			UUID uid = UUID.fromString( id );
			Card c = session.getPlayer().getCard( uid );

			if ( c != null )
			{
				room.setPlayerHero( session.getPlayer(), c );
			}
		}
	}

}
