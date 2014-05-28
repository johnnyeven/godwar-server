package com.xgame.server.game.map;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;
import java.util.Map.Entry;

import javax.imageio.ImageIO;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xgame.server.CommandCenter;
import com.xgame.server.common.CoordinatePair;
import com.xgame.server.common.PackageItem;
import com.xgame.server.common.Point;
import com.xgame.server.common.ServerPackage;
import com.xgame.server.common.parameter.InstancePortalParameter;
import com.xgame.server.common.parameter.MapPortalParameter;
import com.xgame.server.common.parameter.NPCParameter;
import com.xgame.server.common.parameter.PortalParameter;
import com.xgame.server.common.protocol.EnumProtocol;
import com.xgame.server.enums.PlayerStatus;
import com.xgame.server.game.GameServer;
import com.xgame.server.game.astar.SilzAstar;
import com.xgame.server.objects.InstancePortal;
import com.xgame.server.objects.MapPortal;
import com.xgame.server.objects.NPC;
import com.xgame.server.objects.ObjectManager;
import com.xgame.server.objects.Player;
import com.xgame.server.objects.Portal;
import com.xgame.server.objects.WorldObject;
import com.xgame.server.pool.ServerPackagePool;

public class Map
{
	protected int				id;
	protected int				unloadTimer;
	private Grid[][]			gridContainer;
	private boolean[][]			negativePath;
	private int					gridX;
	private int					gridY;
	private MapConfig			config;
	private SilzAstar			astar;

	private static final int	GRID_WIDTH	= 500;
	private static final int	GRID_HEIGHT	= 500;
	private static Log			log			= LogFactory.getLog( Map.class );

	public Map( int id, MapConfig config )
	{
		this.id = id;
		this.config = config;

		gridX = (int) Math.ceil( (float) config.width / GRID_WIDTH );
		gridY = (int) Math.ceil( (float) config.height / GRID_HEIGHT );

		gridContainer = new Grid[gridX][gridY];
		negativePath = new boolean[config.blockNumHeight][config.blockNumWidth];
		loadMap();
		loadNPC();
		loadPortal();
	}

	private void loadMap()
	{
		try
		{
			BufferedImage img = ImageIO.read( new FileInputStream(
					GameServer.path + "data/map/" + id + "/road.png" ) );
			double roadScale = (double) img.getWidth() / config.width;
			long color;
			long alpha;

			for ( int x = 0; x < config.blockNumWidth; x++ )
			{
				for ( int y = 0; y < config.blockNumHeight; y++ )
				{
					color = img.getRGB(
							(int) ( config.blockSizeWidth * x * roadScale ),
							(int) ( config.blockSizeHeight * y * roadScale ) );
					alpha = color >> 24;
					if ( alpha != 0 )
					{
						negativePath[y][x] = true;
					}
				}
			}

			astar = new SilzAstar( negativePath, config.blockSizeWidth,
					config.blockSizeHeight );
			// String logString;
			// for(int i = 0; i < negativePath.length; i++)
			// {
			// logString = "";
			// for(int j = 0; j < negativePath[i].length; j++)
			// {
			// if(negativePath[i][j])
			// {
			// logString += "��";
			// }
			// else
			// {
			// logString += "��";
			// }
			// }
			// log.debug(logString);
			// }
		}
		catch ( IOException e )
		{
			e.printStackTrace();
			return;
		}
	}

	private void loadNPC()
	{
		Iterator< NPCParameter > it = config.npcList.iterator();
		NPCParameter parameter;
		CoordinatePair coordinate;
		Grid g;
		while ( it.hasNext() )
		{
			parameter = it.next();
			NPC n = new NPC();
			n.setId( parameter.id );
			n.setResource( parameter.resource );
			n.setPrependName( parameter.prependName );
			n.setName( parameter.name );
			n.setLevel( parameter.level );
			n.setHealth( parameter.health );
			n.setMana( parameter.mana );
			n.setX( parameter.x );
			n.setY( parameter.y );
			n.setAction( parameter.action );
			n.setDirection( parameter.direction );
			// n.setScript( parameter.script );

			coordinate = getCoordinatePair( n.getX(), n.getY() );

			g = getGrid( (int) coordinate.getX(), (int) coordinate.getY() );
			if ( g == null )
			{
				g = new Grid( (int) coordinate.getX(), (int) coordinate.getY() );
				setGrid( g, (int) coordinate.getX(), (int) coordinate.getY() );
			}
			g.addWorldObject( n );
			ObjectManager.getInstance().addObject( n );

			log.info( "create NPC - <" + n.getPrependName() + ">" + n.getName() );
		}
	}

	private void loadPortal()
	{
		Iterator< PortalParameter > it = config.portalList.iterator();
		PortalParameter parameter;
		InstancePortalParameter instanceParameter;
		MapPortalParameter mapParameter;
		InstancePortal instancePortal;
		MapPortal mapPortal;
		while ( it.hasNext() )
		{
			parameter = it.next();
			if ( parameter instanceof InstancePortalParameter )
			{
				instanceParameter = (InstancePortalParameter) parameter;
				instancePortal = new InstancePortal();
				instancePortal.setX( instanceParameter.x );
				instancePortal.setY( instanceParameter.y );
				instancePortal.setResourceId( instanceParameter.resourceId );
				instancePortal.setRange( instanceParameter.rectX,
						instanceParameter.rectY, instanceParameter.rectWidth,
						instanceParameter.rectHeight );
				add( instancePortal );
			}
			else if ( parameter instanceof MapPortalParameter )
			{
				mapParameter = (MapPortalParameter) parameter;
				mapPortal = new MapPortal();
				mapPortal.setX( mapParameter.x );
				mapPortal.setY( mapParameter.y );
				mapPortal.setResourceId( mapParameter.resourceId );
				mapPortal.setDestinationMapId( mapParameter.destinationMapId );
				mapPortal.setDestinationX( mapParameter.destinationX );
				mapPortal.setDestinationY( mapParameter.destinationY );
				add( mapPortal );
			}
		}
	}

	public static CoordinatePair getCoordinatePair( double x, double y )
	{
		return new CoordinatePair( Math.floor( x / GRID_WIDTH ), Math.floor( y
				/ GRID_HEIGHT ) );
	}

	private void setGrid( Grid g, int x, int y )
	{
		if ( x >= gridX || y >= gridY )
		{
			log.error( "setGrid() coordinate error x=" + x + ", y=" + y
					+ ", GridX=" + gridX + ", GridY=" + gridY );
			return;
		}
		gridContainer[x][y] = g;
	}

	public Grid getGrid( int x, int y )
	{
		if ( x >= gridX || y >= gridY )
		{
			log.error( "getGrid() coordinate error x=" + x + ", y=" + y
					+ ", GridX=" + gridX + ", GridY=" + gridY );
			return null;
		}
		return gridContainer[x][y];
	}

	public boolean add( Player p )
	{
		CoordinatePair coordinate = getCoordinatePair( p.getX(), p.getY() );

		Grid g = getGrid( (int) coordinate.getX(), (int) coordinate.getY() );
		if ( g == null )
		{
			g = new Grid( (int) coordinate.getX(), (int) coordinate.getY() );
			setGrid( g, (int) coordinate.getX(), (int) coordinate.getY() );
		}
		g.addWorldObject( p );

		sendPlayerData( p );
		return true;
	}

	public boolean add( Portal p )
	{
		CoordinatePair coordinate = getCoordinatePair( p.getX(), p.getY() );

		Grid g = getGrid( (int) coordinate.getX(), (int) coordinate.getY() );
		if ( g == null )
		{
			g = new Grid( (int) coordinate.getX(), (int) coordinate.getY() );
			setGrid( g, (int) coordinate.getX(), (int) coordinate.getY() );
		}
		g.addWorldObject( p );
		return true;
	}

	public boolean remove( Player p )
	{
		p.getCurrentGrid().removeWorldObject( p.getGuid() );
		updatePlayerStatus( p, false );

		return true;
	}

	public boolean check( Player p )
	{
		Grid g1 = p.getCurrentGrid();
		if ( checkPortal( g1, p ) )
		{
			return true;
		}
		CoordinatePair coordinate = getCoordinatePair( p.getX(), p.getY() );
		if ( g1.getX() == coordinate.getX() && g1.getY() == coordinate.getY() )
		{
			return false;
		}

		Grid g = getGrid( (int) coordinate.getX(), (int) coordinate.getY() );
		if ( g == null )
		{
			g = new Grid( (int) coordinate.getX(), (int) coordinate.getY() );
			setGrid( g, (int) coordinate.getX(), (int) coordinate.getY() );
		}
		g1.removeWorldObject( p.getGuid() );
		g.addWorldObject( p );
		log.debug( p.name + " change grid to x=" + g.getX() + ", y=" + g.getY() );
		return true;
	}

	private boolean checkPortal( Grid g, Player p )
	{
		Iterator< Entry< UUID, Portal >> it = g.getPortalIterator();
		Entry< UUID, Portal > en;
		Portal portal;
		while ( it.hasNext() )
		{
			en = it.next();
			portal = en.getValue();

			if ( portal.reached( p ) )
			{
				transferPortal( portal, p );
				return true;
			}
		}
		return false;
	}

	private void transferPortal( Portal portal, Player p )
	{
		if ( portal instanceof MapPortal )
		{

		}
		else if ( portal instanceof InstancePortal )
		{

		}
	}

	public boolean updatePlayerStatus( Player p, boolean show )
	{
		if ( p.getMapId() != id )
		{
			log.error( "updatePlayerStatus() Player not match" );
			return false;
		}
		CoordinatePair coordinate = getCoordinatePair( p.getX(), p.getY() );
		Grid g = getGrid( (int) coordinate.getX(), (int) coordinate.getY() );
		if ( g == null )
		{
			return false;
		}

		if ( show )
		{
			updateVisibilityShow( p, g );
			// updateOtherVisibility(p, g);

			p.status = PlayerStatus.NORMAL;
		}
		else
		{
			updateVisibilityRemove( p, g );
		}

		return true;
	}

	private void sendPlayerData( Player p )
	{
		ServerPackage verifyMap = ServerPackagePool.getInstance().getObject();
		verifyMap.success = EnumProtocol.ACK_CONFIRM;
		verifyMap.protocolId = EnumProtocol.BASE_VERIFY_MAP;
		verifyMap.parameter.add( new PackageItem( 4, p.getMapId() ) );
		verifyMap.parameter.add( new PackageItem( 4, p.direction ) );
		CommandCenter.send( p.getChannel(), verifyMap );

		// ServerPackage pack = ServerPackagePool.getInstance().getObject();
		// pack.success = EnumProtocol.ACK_CONFIRM;
		// pack.protocolId = EnumProtocol.REGISTER_ACCOUNT_ROLE;
		// pack.parameter.add( new PackageItem( 8, p.accountId ) );
		// pack.parameter.add( new PackageItem( p.name.length(), p.name ) );
		// pack.parameter.add( new PackageItem( 8, p.accountCash ) );
		// pack.parameter.add( new PackageItem( 4, p.direction ) );
		// pack.parameter.add( new PackageItem( 4, p.getSpeed() ) );
		// pack.parameter.add( new PackageItem( 4, p.energy ) );
		// pack.parameter.add( new PackageItem( 4, p.energyMax ) );
		// pack.parameter.add( new PackageItem( 8, p.getX() ) );
		// pack.parameter.add( new PackageItem( 8, p.getY() ) );
		// CommandCenter.send( p.getChannel(), pack );
	}

	private void updateVisibilityShow( Player p, Grid g )
	{
		ArrayList< Grid > list = getViewGrid( g );
		Iterator< Grid > it = list.iterator();
		Grid currentGrid;
		Entry< UUID, WorldObject > en;
		Player currentPlayer;
		String uuid;
		while ( it.hasNext() )
		{
			currentGrid = it.next();

			if ( currentGrid == null )
			{
				continue;
			}

			Iterator< Entry< UUID, WorldObject >> gridIt = currentGrid
					.getWorldObjectIterator();
			while ( gridIt.hasNext() )
			{
				en = gridIt.next();
				if ( en.getValue() instanceof Player && en.getValue() != p )
				{
					currentPlayer = (Player) en.getValue();
					if ( !currentPlayer.getChannel().isOpen() )
					{
						continue;
					}

					ServerPackage pack = ServerPackagePool.getInstance()
							.getObject();
					pack.success = EnumProtocol.ACK_CONFIRM;
					pack.protocolId = EnumProtocol.SCENE_SHOW_PLAYER;
					uuid = p.getGuid().toString();
					pack.parameter.add( new PackageItem( uuid.length(), uuid ) );
					pack.parameter.add( new PackageItem( 8, p.accountId ) );
					pack.parameter.add( new PackageItem( 8, p.roleId ) );
					pack.parameter.add( new PackageItem( p.name.length(),
							p.name ) );
					pack.parameter.add( new PackageItem( 8, p.accountCash ) );
					pack.parameter.add( new PackageItem( 4, p.direction ) );
					pack.parameter.add( new PackageItem( 4, p.getSpeed() ) );
					pack.parameter.add( new PackageItem( 4, p.energy ) );
					pack.parameter.add( new PackageItem( 4, p.energyMax ) );
					pack.parameter.add( new PackageItem( 8, p.getX() ) );
					pack.parameter.add( new PackageItem( 8, p.getY() ) );
					CommandCenter.send( currentPlayer.getChannel(), pack );

					pack = ServerPackagePool.getInstance().getObject();
					pack.success = EnumProtocol.ACK_CONFIRM;
					pack.protocolId = EnumProtocol.SCENE_SHOW_PLAYER;
					uuid = currentPlayer.getGuid().toString();
					pack.parameter.add( new PackageItem( uuid.length(), uuid ) );
					pack.parameter.add( new PackageItem( 8,
							currentPlayer.accountId ) );
					pack.parameter.add( new PackageItem( 8,
							currentPlayer.roleId ) );
					pack.parameter.add( new PackageItem( currentPlayer.name
							.length(), currentPlayer.name ) );
					pack.parameter.add( new PackageItem( 8,
							currentPlayer.accountCash ) );
					pack.parameter.add( new PackageItem( 4,
							currentPlayer.direction ) );
					pack.parameter.add( new PackageItem( 4, currentPlayer
							.getSpeed() ) );
					pack.parameter.add( new PackageItem( 4,
							currentPlayer.energy ) );
					pack.parameter.add( new PackageItem( 4,
							currentPlayer.energyMax ) );
					pack.parameter.add( new PackageItem( 8, currentPlayer
							.getX() ) );
					pack.parameter.add( new PackageItem( 8, currentPlayer
							.getY() ) );
					CommandCenter.send( p.getChannel(), pack );
				}
			}
		}
	}

	private void updateVisibilityRemove( Player p, Grid g )
	{
		ArrayList< Grid > list = getViewGrid( g );
		Iterator< Grid > it = list.iterator();
		Grid currentGrid;
		Entry< UUID, WorldObject > en;
		Player currentPlayer;
		String uuid;
		while ( it.hasNext() )
		{
			currentGrid = it.next();

			if ( currentGrid == null )
			{
				continue;
			}

			Iterator< Entry< UUID, WorldObject >> gridIt = currentGrid
					.getWorldObjectIterator();
			while ( gridIt.hasNext() )
			{
				en = gridIt.next();
				if ( en.getValue() instanceof Player )
				{
					currentPlayer = (Player) en.getValue();
					if ( !currentPlayer.getChannel().isOpen() )
					{
						continue;
					}

					ServerPackage pack = ServerPackagePool.getInstance()
							.getObject();
					pack.success = EnumProtocol.ACK_CONFIRM;
					pack.protocolId = EnumProtocol.SCENE_REMOVE_PLAYER;
					uuid = p.getGuid().toString();
					pack.parameter.add( new PackageItem( uuid.length(), uuid ) );
					CommandCenter.send( currentPlayer.getChannel(), pack );
				}
			}
		}
	}

	@SuppressWarnings( "unused" )
	private void updateOtherVisibility( Player p, Grid g )
	{
		ArrayList< Grid > list = getViewGrid( g );
		Iterator< Grid > it = list.iterator();
		Grid currentGrid;
		Entry< UUID, WorldObject > en;
		Player currentPlayer;
		while ( it.hasNext() )
		{
			currentGrid = it.next();

			if ( currentGrid == null )
			{
				continue;
			}

			Iterator< Entry< UUID, WorldObject >> gridIt = currentGrid
					.getWorldObjectIterator();
			while ( gridIt.hasNext() )
			{
				en = gridIt.next();
				if ( en.getValue() instanceof Player )
				{
					currentPlayer = (Player) en.getValue();

					if ( currentPlayer == p )
					{
						continue;
					}
					if ( !p.getChannel().isOpen() )
					{
						continue;
					}

					ServerPackage pack = ServerPackagePool.getInstance()
							.getObject();
					pack.success = EnumProtocol.ACK_CONFIRM;
					pack.protocolId = EnumProtocol.SCENE_SHOW_PLAYER;
					pack.parameter.add( new PackageItem( 8,
							currentPlayer.accountId ) );
					pack.parameter.add( new PackageItem( currentPlayer.name
							.length(), currentPlayer.name ) );
					pack.parameter.add( new PackageItem( 8,
							currentPlayer.accountCash ) );
					pack.parameter.add( new PackageItem( 4,
							currentPlayer.direction ) );
					pack.parameter.add( new PackageItem( 4, currentPlayer
							.getSpeed() ) );
					pack.parameter.add( new PackageItem( 4,
							currentPlayer.energy ) );
					pack.parameter.add( new PackageItem( 4,
							currentPlayer.energyMax ) );
					pack.parameter.add( new PackageItem( 8, currentPlayer
							.getX() ) );
					pack.parameter.add( new PackageItem( 8, currentPlayer
							.getY() ) );
					CommandCenter.send( p.getChannel(), pack );
				}
			}
		}
	}

	public ArrayList< Grid > getViewGrid( Grid g )
	{
		int x = g.getX();
		int y = g.getY();
		int startX = x - 1 < 0 ? 0 : x - 1;
		int startY = y - 1 < 0 ? 0 : y - 1;
		int endX = startX + 3 > gridX ? gridX : startX + 3;
		int endY = startY + 3 > gridY ? gridY : startY + 3;
		ArrayList< Grid > list = new ArrayList< Grid >();

		for ( x = startX; x < endX; x++ )
		{
			for ( y = startY; y < endY; y++ )
			{
				list.add( gridContainer[x][y] );
			}
		}
		return list;
	}

	public SilzAstar getAstar()
	{
		return astar;
	}

	public Point block2WorldPosition( int x, int y )
	{
		Point p = new Point();
		p.setX( ( x + .5 ) * config.blockSizeWidth );
		p.setY( ( y + .5 ) * config.blockSizeHeight );
		return p;
	}

	public void unloadMap()
	{
		negativePath = null;
		config = null;
		astar = null;

		for ( int i = 0; i < gridContainer.length; i++ )
		{
			for ( int j = 0; j < gridContainer[i].length; j++ )
			{
				gridContainer[i][j].removeAll();
				gridContainer[i][j] = null;
			}
		}
	}

}
