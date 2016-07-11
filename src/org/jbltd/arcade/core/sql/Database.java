package org.jbltd.arcade.core.sql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.bukkit.plugin.Plugin;

public abstract class Database
{
	
	protected Connection _connection;
	
	protected Plugin _plugin;
	
	protected Database(Plugin _plugin)
	{
		this._plugin = _plugin;
		this._connection = null;
	}
	
	public abstract Connection openConnection() throws SQLException, ClassNotFoundException;
	
	public boolean checkConnection() throws SQLException
	{
		return _connection != null && !_connection.isClosed();
	}
	
	public Connection getConnection()
	{
		return _connection;
	}
	
	public boolean closeConnection() throws SQLException
	{
		if (_connection == null)
		{
			return false;
		}
		_connection.close();
		return true;
	}
	
	public ResultSet querySQL(String query) throws SQLException, ClassNotFoundException
	{
		if (!checkConnection())
		{
			openConnection();
		}
		
		Statement statement = _connection.createStatement();
		
		ResultSet result = statement.executeQuery(query);
		
		return result;
	}
	
	public int updateSQL(String query) throws SQLException, ClassNotFoundException
	{
		if (!checkConnection())
		{
			openConnection();
		}
		
		Statement statement = _connection.createStatement();
		
		int result = statement.executeUpdate(query);
		
		return result;
	}
}
