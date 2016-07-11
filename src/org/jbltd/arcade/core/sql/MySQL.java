package org.jbltd.arcade.core.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.bukkit.plugin.Plugin;

public class MySQL extends Database
{
	private final String _user;
	private final String _database;
	private final String _password;
	private final String _port;
	private final String _hostname;
	
	public MySQL(Plugin plugin, String _hostname, String _port, String _database, String username, String _password)
	{
		super(plugin);
		this._hostname = _hostname;
		this._port = _port;
		this._database = _database;
		this._user = username;
		this._password = _password;
	}
	
	@Override
	public Connection openConnection() throws SQLException, ClassNotFoundException
	{
		if (checkConnection())
		{
			return _connection;
		}
		Class.forName("com.mysql.jdbc.Driver");
		_connection = DriverManager.getConnection("jdbc:mysql://" + this._hostname + ":" + this._port + "/" + this._database,
				this._user, this._password);
		return _connection;
	}
}