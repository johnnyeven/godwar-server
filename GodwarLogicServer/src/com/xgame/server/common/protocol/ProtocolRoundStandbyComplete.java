package com.xgame.server.common.protocol;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xgame.server.CommandCenter;
import com.xgame.server.cards.SoulCard;
import com.xgame.server.common.PackageItem;
import com.xgame.server.common.ServerPackage;
import com.xgame.server.logic.ProtocolPackage;
import com.xgame.server.network.GameSession;
import com.xgame.server.pool.ServerPackagePool;

public class ProtocolRoundStandbyComplete implements IProtocol
{

	private static Log	log	= LogFactory
									.getLog( ProtocolRoundStandbyComplete.class );

	@Override
	public void Execute( Object param1, Object param2 )
	{
		ProtocolPackage parameter = (ProtocolPackage) param1;
		GameSession session = (GameSession) param2;

		int soulCount = Integer.MIN_VALUE;
		int supplyCount = Integer.MIN_VALUE;

		for ( int i = parameter.offset; i < parameter.receiveDataLength; )
		{
			int length = parameter.receiveData.getInt();
			int type = parameter.receiveData.get();
			switch ( type )
			{
				case EnumProtocol.TYPE_INT:
					if ( soulCount == Integer.MIN_VALUE )
					{
						soulCount = parameter.receiveData.getInt();
					}
					else if ( supplyCount == Integer.MIN_VALUE )
					{
						supplyCount = parameter.receiveData.getInt();
					}
					break;
			}
			i += ( length + 5 );
		}
		log.info( "[RoundStandbyComplete] AccountId="
				+ session.getPlayer().name + ", Soul Count = " + soulCount
				+ ", Supply Count = " + supplyCount );

		if ( soulCount >= 0 && supplyCount >= 0 )
		{
			ServerPackage pack = ServerPackagePool.getInstance().getObject();
			pack.success = EnumProtocol.ACK_CONFIRM;
			pack.protocolId = EnumProtocol.BATTLEROOM_ROUND_STANDBY_CONFIRM;

			StringBuffer buf = new StringBuffer();
			
			SoulCard card = session.getPlayer().popSoulCardToHand();
			if ( card != null )
			{
				buf.append( card.getId() );
				int j;
				for ( j = 1; j < soulCount; j++ )
				{
					card = session.getPlayer().popSoulCardToHand();
					if ( card != null )
					{
						buf.append( "," + card.getId() );
					}
					else
					{
						break;
					}
				}
			}
			String cardList = buf.toString();
			pack.parameter.add( new PackageItem( cardList.length(), cardList ) );

//			buf = new StringBuffer();
//			SupplyCard supplyCard = session.getPlayer().popSupplyCardToHand();
//			if ( supplyCard != null )
//			{
//				buf.append( supplyCard.getId() );
//				int j;
//				for ( j = 1; j < supplyCount; j++ )
//				{
//					supplyCard = session.getPlayer().popSoulCardToHand();
//					if ( supplyCard != null )
//					{
//						buf.append( "," + supplyCard.getId() );
//					}
//					else
//					{
//						break;
//					}
//				}
//			}
//			cardList = buf.toString();
//			pack.parameter.add( new PackageItem( cardList.length(), cardList ) );

			CommandCenter.send( session.getChannel(), pack );
		}
	}
}
