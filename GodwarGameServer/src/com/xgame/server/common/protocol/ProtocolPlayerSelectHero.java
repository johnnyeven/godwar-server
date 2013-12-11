package com.xgame.server.common.protocol;

import java.io.UnsupportedEncodingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xgame.server.game.ProtocolPackage;
import com.xgame.server.network.GameSession;

public class ProtocolPlayerSelectHero implements IProtocol
{
	private static Log log = LogFactory.getLog(ProtocolPlayerSelectHero.class);

	@Override
	public void Execute(Object param1, Object param2)
	{
		ProtocolPackage parameter = (ProtocolPackage) param1;
		GameSession session = (GameSession) param2;

		String heroId = null;
		long timestamp = Long.MIN_VALUE;

		for (int i = parameter.offset; i < parameter.receiveDataLength;)
		{
			int length = parameter.receiveData.getInt();
			int type = parameter.receiveData.get();
			switch (type)
			{
				case EnumProtocol.TYPE_LONG:
					if (timestamp == Long.MIN_VALUE)
					{
						timestamp = parameter.receiveData.getLong();
						break;
					}
				case EnumProtocol.TYPE_STRING:
					if (heroId == null)
					{
						length = parameter.receiveData.getShort();
						byte[] dst = new byte[length];
						parameter.receiveData.get(dst);
						length += 2;
						try
						{
							heroId = new String(dst, "UTF-8");
						} catch (UnsupportedEncodingException e)
						{
							e.printStackTrace();
						}
					}
			}
			i += (length + 5);
		}
		log.info("[PlayerSelectHero] Player = " + session.getPlayer().name
				+ ", HeroCard = " + heroId);

	}

}
