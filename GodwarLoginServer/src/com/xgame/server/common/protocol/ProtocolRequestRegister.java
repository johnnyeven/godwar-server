package com.xgame.server.common.protocol;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import com.xgame.server.CommandCenter;
import com.xgame.server.common.PackageItem;
import com.xgame.server.common.ServerPackage;
import com.xgame.server.common.database.DatabaseRouter;
import com.xgame.server.login.ProtocolParam;
import com.xgame.server.pool.ServerPackagePool;

public class ProtocolRequestRegister implements IProtocol
{
	private static Log	log	= LogFactory.getLog( ProtocolRequestRegister.class );

	@Override
	public void Execute( Object param )
	{
		ProtocolParam parameter = (ProtocolParam) param;

		String name = null;
		String password = null;
		for ( int i = parameter.offset; i < parameter.receiveDataLength; )
		{
			int length = parameter.receiveData.getInt();
			int type = parameter.receiveData.get();
			switch ( type )
			{
				case EnumProtocol.TYPE_STRING:
					if ( name == null )
					{
						length = parameter.receiveData.getShort();
						byte[] dst = new byte[length];
						parameter.receiveData.get( dst );
						length += 2;
						try
						{
							name = new String( dst, "UTF-8" );
						}
						catch ( UnsupportedEncodingException e )
						{
							e.printStackTrace();
						}
					}
					else if ( password == null )
					{
						length = parameter.receiveData.getShort();
						byte[] dst = new byte[length];
						parameter.receiveData.get( dst );
						length += 2;
						try
						{
							password = new String( dst, "UTF-8" );
						}
						catch ( UnsupportedEncodingException e )
						{
							e.printStackTrace();
						}
					}
					break;
			}
			i += ( length + 5 );
		}

		if ( name != null && password != null )
		{
			log.info( "[Register] Name = " + name + ", Pass = " + password );

			password = encode( "SHA-1", encode( "MD5", password ) );
			String sql = "insert into accounts(name, pass, email, regtime, lasttime) values ('"
					+ name
					+ "', '"
					+ password
					+ "', '', "
					+ ( new Date().getTime() / 1000 ) + ", 0)";
			try
			{
				PreparedStatement st = DatabaseRouter
						.getInstance()
						.getDbConnection()
						.prepareStatement( sql, Statement.RETURN_GENERATED_KEYS );
				st.executeUpdate();
				ResultSet rs = st.getGeneratedKeys();
				rs.next();
				long insertId = rs.getLong( 1 );

				initPlayerDatabase( insertId );

				ServerPackage pack = ServerPackagePool.getInstance()
						.getObject();
				pack.success = EnumProtocol.ACK_CONFIRM;
				pack.protocolId = EnumProtocol.INFO_REGISTER;
				pack.parameter.add( new PackageItem( 8, insertId ) );
				pack.parameter.add( new PackageItem( name.length(), name ) );
				pack.parameter.add( new PackageItem( password.length(),
						password ) );

				CommandCenter.send( parameter.client, pack );
			}
			catch ( MySQLIntegrityConstraintViolationException e )
			{
				ServerPackage pack = ServerPackagePool.getInstance()
						.getObject();
				pack.success = EnumProtocol.ACK_CONFIRM;
				pack.protocolId = EnumProtocol.INFO_LOGIN;
				pack.parameter.add( new PackageItem( 4, -1 ) );

				CommandCenter.send( parameter.client, pack );
			}
			catch ( SQLException e )
			{
				e.printStackTrace();
			}
		}
	}

	private void initPlayerDatabase( long guid )
	{

	}

	private String encode( String algorithm, String str )
	{
		if ( str == null )
		{
			return null;
		}
		try
		{
			MessageDigest messageDigest = MessageDigest.getInstance( algorithm );
			messageDigest.update( str.getBytes() );
			return getFormattedText( messageDigest.digest() );
		}
		catch ( Exception e )
		{
			throw new RuntimeException( e );
		}
	}

	private String getFormattedText( byte[] digest )
	{
		StringBuffer md5StrBuff = new StringBuffer();

		for ( int i = 0; i < digest.length; i++ )
		{
			if ( Integer.toHexString( 0xFF & digest[i] ).length() == 1 )
			{
				md5StrBuff.append( "0" ).append(
						Integer.toHexString( 0xFF & digest[i] ) );
			}
			else
			{
				md5StrBuff.append( Integer.toHexString( 0xFF & digest[i] ) );
			}
		}
		return md5StrBuff.toString();
	}

}
