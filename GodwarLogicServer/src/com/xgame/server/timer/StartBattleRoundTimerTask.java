package com.xgame.server.timer;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.TimerTask;

import com.xgame.server.logic.BattleRoom;
import com.xgame.server.logic.Player;

public class StartBattleRoundTimerTask extends TimerTask
{
	private Map< String, Integer >	map1;
	private Map< String, Integer >	map2;
	private BattleRoom				room;
	private int						startGroup;
	private String					startGuid;

	public StartBattleRoundTimerTask( Map< String, Integer > map1,
			Map< String, Integer > map2, BattleRoom room )
	{
		this.map1 = map1;
		this.map2 = map2;
		this.room = room;

		initilization();
	}

	private void initilization()
	{
		Iterator< Entry< String, Integer >> it = map1.entrySet().iterator();
		Entry< String, Integer > en;
		int diceTotal1 = 0;
		int diceTotal2 = 0;
		int max1 = 0;
		int max2 = 0;
		String guid1 = null;
		String guid2 = null;
		Random rand = new Random();

		int tmp;
		int i;
		while ( it.hasNext() )
		{
			en = it.next();
			tmp = en.getValue();
			diceTotal1 += tmp;
			if ( tmp > max1 )
			{
				max1 = tmp;
				guid1 = en.getKey();
			}
			else if ( tmp == max1 )
			{
				i = Math.abs( rand.nextInt() % 100 );
				if ( i < 50 )
				{
					guid1 = en.getKey();
				}
			}
		}

		it = map2.entrySet().iterator();
		while ( it.hasNext() )
		{
			en = it.next();
			tmp = en.getValue();
			diceTotal2 += tmp;
			if ( tmp > max2 )
			{
				max2 = tmp;
				guid2 = en.getKey();
			}
			else if ( tmp == max2 )
			{
				i = Math.abs( rand.nextInt() % 100 );
				if ( i < 50 )
				{
					guid2 = en.getKey();
				}
			}
		}

		if ( diceTotal1 == diceTotal2 )
		{
			if ( max1 == max2 )
			{
				i = Math.abs( rand.nextInt() % 100 );
				if ( i < 50 )
				{
					startGroup = 1;
					startGuid = guid1;
				}
				else
				{
					startGroup = 2;
					startGuid = guid2;
				}
			}
			else if ( max1 > max2 )
			{
				startGroup = 1;
				startGuid = guid1;
			}
			else
			{
				startGroup = 2;
				startGuid = guid2;
			}
		}
		else if ( diceTotal1 > diceTotal2 )
		{
			startGroup = 1;
			startGuid = guid1;
		}
		else
		{
			startGroup = 2;
			startGuid = guid2;
		}
	}

	@Override
	public void run()
	{
		if ( startGroup > 0 )
		{
			List< Player > group;
			Player p;
			int i = 0;
			if ( startGroup == 1 )
			{
				group = room.getGroup1();
			}
			else
			{
				group = room.getGroup1();
			}

			for ( i = 0; i < group.size(); i++ )
			{
				p = group.get( i );
				if ( p.getGuid().toString().equals( startGuid ) )
				{
					break;
				}
			}

			room.resetPlayerSequence();
			room.setStartPosition( i, startGroup );
			room.startRound();
		}
	}

}
