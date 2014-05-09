package com.xgame.server.common.protocol;

import java.io.UnsupportedEncodingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import sun.org.mozilla.javascript.internal.NativeObject;

import com.xgame.server.CommandCenter;
import com.xgame.server.common.PackageItem;
import com.xgame.server.common.ServerPackage;
import com.xgame.server.game.ProtocolPackage;
import com.xgame.server.network.WorldSession;
import com.xgame.server.objects.NPC;
import com.xgame.server.objects.ObjectManager;
import com.xgame.server.pool.ServerPackagePool;
import com.xgame.server.scripts.INPCScript;
import com.xgame.server.scripts.NPCScriptAnswerParameter;
import com.xgame.server.scripts.NPCScriptContentParameter;
import com.xgame.server.scripts.NPCScriptManager;

public class ProtocolTriggerNPC implements IProtocol
{

	private static Log	log	= LogFactory.getLog( ProtocolTriggerNPC.class );

	@Override
	public void Execute( Object param1, Object param2 )
	{
		ProtocolPackage parameter = (ProtocolPackage) param1;
		WorldSession session = (WorldSession) param2;

		String guid = null;
		int step = Integer.MIN_VALUE;

		for ( int i = parameter.offset; i < parameter.receiveDataLength; )
		{
			int length = parameter.receiveData.getInt();
			int type = parameter.receiveData.get();
			switch ( type )
			{
				case EnumProtocol.TYPE_STRING:
					if ( guid == null )
					{
						length = parameter.receiveData.getShort();
						byte[] dst = new byte[length];
						parameter.receiveData.get( dst );
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
					if ( step == Integer.MIN_VALUE )
					{
						step = parameter.receiveData.getInt();
					}
					break;
			}
			i += ( length + 5 );
		}
		log.info( "[TriggerNPC] guid=" + guid + ", step=" + step );

		NPC n = ObjectManager.getInstance().getNPC( guid );
		if ( n != null )
		{
			int id = n.getId();
			INPCScript s = NPCScriptManager.getInstance().get( id );

			if ( s != null )
			{
				NativeObject obj = s.dialogue( step );
				NPCScriptContentParameter p = NPCScriptManager.getInstance()
						.getScriptUtil().convertDialogueContent( obj );
				NPCScriptAnswerParameter a;

				ServerPackage pack = ServerPackagePool.getInstance()
						.getObject();
				pack.success = EnumProtocol.ACK_CONFIRM;
				pack.protocolId = EnumProtocol.SCENE_TRIGGER_NPC;
				pack.parameter.add( new PackageItem( guid.length(), guid ) );
				pack.parameter
						.add( new PackageItem( p.title.length(), p.title ) );
				pack.parameter.add( new PackageItem( p.content.length(),
						p.content ) );
				for ( int i = 0; i < p.answer.size(); i++ )
				{
					a = p.answer.get( i );
					pack.parameter.add( new PackageItem( a.content.length(),
							a.content ) );
					pack.parameter.add( new PackageItem( a.action.length(),
							a.action ) );
					pack.parameter.add( new PackageItem( 4, a.position ) );
					pack.parameter.add( new PackageItem( a.command.length(),
							a.command ) );
				}

				CommandCenter.send( session.getChannel(), pack );
			}
		}
	}

}
