package com.xgame.server.common;

public class IntervalTimer
{
	private long	interval;
	private long	current;

	public IntervalTimer()
	{
		interval = 0;
		current = 0;
	}

	public IntervalTimer( long i )
	{
		interval = i;
		current = 0;
	}

	public long getInterval()
	{
		return interval;
	}

	public void setInterval( long interval )
	{
		this.interval = interval;
	}

	public long getCurrent()
	{
		return current;
	}

	public void setCurrent( long current )
	{
		this.current = current;
	}

	public void update( long timeDiff )
	{
		current += timeDiff;
		if ( current < 0 )
		{
			current = 0;
		}
	}

	public boolean over()
	{
		return current >= interval;
	}

	public void reset()
	{
		if ( current >= interval )
		{
			current -= interval;
		}
	}

}
