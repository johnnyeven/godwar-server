package com.xgame.server.objects;

import com.xgame.server.common.Rectangle;

public class Portal extends WorldObject
{
	private String		resourceId;
	private Rectangle	range;

	public Portal()
	{
		range = new Rectangle();
	}

	public void setRange( double x, double y, double width, double height )
	{
		range.setX( x );
		range.setY( y );
		range.setWidth( width );
		range.setHeight( height );
	}

	public boolean reached( WorldObject obj )
	{
		return range.inArea( obj.getX(), obj.getY() );
	}

	public String getResourceId()
	{
		return resourceId;
	}

	public void setResourceId( String resourceId )
	{
		this.resourceId = resourceId;
	}

}
