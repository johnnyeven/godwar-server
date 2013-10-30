package com.xgame.server.common.protocol;

import java.util.HashMap;

public class ProtocolRouter
{
	private static ProtocolRouter		instance		= null;
	private static boolean				allowInstance	= false;

	private HashMap< Short, Class< ? >>	protocolList;

	public ProtocolRouter() throws Exception
	{
		if ( !allowInstance )
		{
			throw new Exception();
		}
		protocolList = new HashMap< Short, Class< ? >>();
	}

	public static ProtocolRouter getInstance()
	{
		if ( instance == null )
		{
			allowInstance = true;
			try
			{
				instance = new ProtocolRouter();
			}
			catch ( Exception e )
			{
				e.printStackTrace();
			}
			allowInstance = false;
		}
		return instance;
	}

	public void Bind( Short key, Class< ? > value )
	{
		protocolList.put( key, value );
	}

	public void UnBind( Short key )
	{
		if ( protocolList.containsKey( key ) )
		{
			protocolList.remove( key );
		}
	}

	public boolean HasBind( Short key )
	{
		return protocolList.containsKey( key );
	}

	public void Trigger( Short key, Object param1 )
	{
		if ( protocolList.containsKey( key ) )
		{
			Class< ? > reflection = protocolList.get( key );
			try
			{
				IProtocol protocol = (IProtocol) reflection.newInstance();
				protocol.Execute( param1 );
			}
			catch ( Exception e )
			{
				e.printStackTrace();
			}
		}
	}
}
