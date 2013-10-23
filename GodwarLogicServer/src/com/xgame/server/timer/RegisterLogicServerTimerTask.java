package com.xgame.server.timer;

import java.net.InetSocketAddress;
import java.util.TimerTask;

import com.xgame.server.network.GameServerConnector;

public class RegisterLogicServerTimerTask extends TimerTask
{

	private String				gameServerIp;
	private int					gameServerPort;
	private InetSocketAddress	add;

	public RegisterLogicServerTimerTask( String ip, int port )
	{
		gameServerIp = ip;
		gameServerPort = port;
		add = new InetSocketAddress( gameServerIp, gameServerPort );
	}

	@Override
	public void run()
	{
		GameServerConnector.getInstance().initialize( add );
	}

}
