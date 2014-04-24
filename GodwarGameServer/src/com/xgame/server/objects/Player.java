package com.xgame.server.objects;

import java.nio.channels.AsynchronousSocketChannel;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xgame.server.common.database.DatabaseRouter;
import com.xgame.server.enums.Action;
import com.xgame.server.enums.Direction;
import com.xgame.server.enums.PlayerStatus;
import com.xgame.server.game.WorldThread;
import com.xgame.server.network.WorldSession;

public class Player extends InteractiveObject
{
	private UUID						guid;
	public long							roleId				= Long.MIN_VALUE;
	public long							accountId			= Long.MIN_VALUE;
	public int							level				= 0;
	public String						name				= "";
	public long							accountCash			= Long.MIN_VALUE;
	public String						rolePicture			= "";
	private float						speed				= 0;
	private double						moveSpeed			= 0;
	public int							honor				= 0;
	public int							energy				= 0;
	public int							energyMax			= Integer.MIN_VALUE;
	public PlayerStatus					status				= PlayerStatus.NORMAL;
	public int							direction			= Direction.DOWN;
	public int							action				= Action.STOP;

	private Motion						motion;
	private AsynchronousSocketChannel	channel;
	private WorldSession				session;
	private int							currentGroup;											// 玩家阵营
																								// 1=红队
																								// 2=蓝队
	private int							currentPosition;
	private int							currentCardGroup;
	private String						lastHeroCardId		= "";
	private String						currentHeroCardId	= "";

	private static Log					log					= LogFactory
																	.getLog( Player.class );

	public Player()
	{
		guid = UUID.randomUUID();
		motion = new Motion( this );
	}

	public boolean loadFromDatabase()
	{
		if ( roleId == Long.MIN_VALUE )
		{
			log.error( "loadFromDatabase() accountId没有初始化" );
			return false;
		}
		try
		{
			String sql = "SELECT * FROM `role` WHERE `role_id`=" + roleId;
			PreparedStatement st = DatabaseRouter.getInstance()
					.getConnection( "gamedb" ).prepareStatement( sql );
			ResultSet rs = st.executeQuery();

			if ( rs.first() )
			{
				accountId = rs.getLong( "account_id" );
				level = rs.getInt( "level" );
				name = rs.getString( "nick_name" );
				accountCash = rs.getLong( "account_cash" );
				rolePicture = rs.getString( "role_picture" );
				direction = rs.getInt( "direction" );
				action = rs.getInt( "action" );
				setSpeed( rs.getFloat( "speed" ) );
				honor = rs.getInt( "honor" );
				energy = rs.getInt( "energy" );
				energyMax = rs.getInt( "max_energy" );
				setMapId( rs.getInt( "map_id" ) );
				setX( rs.getInt( "x" ) );
				setY( rs.getInt( "y" ) );
			}
			else
			{
				log.error( "[loadFromDatabase] 没有找到对应的角色数据 roleId=" + roleId );
				return false;
			}
			long accountGuid = rs.getLong( "account_id" );
			if ( accountGuid != session.getId() )
			{
				log.error( "[loadFromDatabase] accountId与WorldSession使用的accountId不匹配" );
				return false;
			}
			String gameGuid = rs.getString( "game_guid" );
			String guidSql = "";
			if ( !gameGuid.isEmpty() )
			{
				setGuid( UUID.fromString( gameGuid ) );
			}
			else
			{
				guidSql = "`game_guid`='" + getGuid().toString().toUpperCase()
						+ "', ";
			}

			sql = "UPDATE `role` SET " + guidSql + " `account_lastlogin`="
					+ new Date().getTime() + " WHERE `role_id`=" + roleId;
			st.executeUpdate( sql );

			rs.close();

			sql = "SELECT * FROM `game_card_group` WHERE `role_id`=" + roleId
					+ " AND `current`=1";
			st = DatabaseRouter.getInstance().getConnection( "gamedb" )
					.prepareStatement( sql );
			rs = st.executeQuery();

			if ( rs.first() )
			{
				int groupId = rs.getInt( "group_id" );
				setCurrentCardGroup( groupId );
			}

			rs.close();
		}
		catch ( SQLException e )
		{
			e.printStackTrace();
		}
		return true;
	}

	public void update( long timeDiff )
	{
		if ( action == Action.DIE )
		{
			killPlayer();
			return;
		}
		super.update( timeDiff );
		motion.update( timeDiff );
		getMap().check( this );
	}

	public void killPlayer()
	{
		// TODO 死亡处理

	}

	public int getCurrentCardGroup()
	{
		return currentCardGroup;
	}

	public void setCurrentCardGroup( int currentCardGroup )
	{
		this.currentCardGroup = currentCardGroup;
	}

	public WorldSession getSession()
	{
		return session;
	}

	public void setSession( WorldSession session )
	{
		this.session = session;
	}

	public AsynchronousSocketChannel getChannel()
	{
		return channel;
	}

	public void setChannel( AsynchronousSocketChannel channel )
	{
		this.channel = channel;
	}

	public UUID getGuid()
	{
		return guid;
	}

	public void setGuid( UUID guid )
	{
		this.guid = guid;
	}

	public int getCurrentPosition()
	{
		return currentPosition;
	}

	public void setCurrentPosition( int currentPosition )
	{
		this.currentPosition = currentPosition;
	}

	public int getCurrentGroup()
	{
		return currentGroup;
	}

	public void setCurrentGroup( int currentGroup )
	{
		this.currentGroup = currentGroup;
	}

	public String getCurrentHeroCardId()
	{
		return currentHeroCardId;
	}

	public void setCurrentHeroCardId( String currentHeroCardId )
	{
		lastHeroCardId = this.currentHeroCardId;
		this.currentHeroCardId = currentHeroCardId;
	}

	public String getLastHeroCardId()
	{
		return lastHeroCardId;
	}

	public Motion getMotion()
	{
		return motion;
	}

	public float getSpeed()
	{
		return speed;
	}

	public void setSpeed( float speed )
	{
		this.speed = speed;
		moveSpeed = speed * ( (double) WorldThread.WORLD_SLEEP_TIME / 1000 );
	}

	public double getMoveSpeed()
	{
		return moveSpeed;
	}
}
