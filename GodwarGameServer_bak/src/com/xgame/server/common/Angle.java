package com.xgame.server.common;

public class Angle
{

	public static double getAngle( double x, double y )
	{
		return Math.atan2( y, x );
	}

	public static double radian2Angle( double value )
	{
		return value * 180 / Math.PI;
	}

	public static double angle2Radian( double value )
	{
		return value * Math.PI / 180;
	}
}
