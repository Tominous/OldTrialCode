package org.jbltd.arcade.core.sql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.bukkit.entity.Player;
import org.jbltd.arcade.core.Main;

public class Manager
{
	
	protected Main _main;
	public static MySQL DB;
	
	public Manager(Main h)
	{
		this._main = h;
	}
	
	public void setupDB() throws SQLException, ClassNotFoundException
	{
		Manager.DB = new MySQL(_main, "69.175.126.226", "3306", "mc85490", "mc85490", "221cb17d61");
		Manager.DB.openConnection();
		
		Statement a = DB.getConnection().createStatement();
		a.executeUpdate("CREATE TABLE IF NOT EXISTS `MPAccounts` (`id` INT NOT NULL AUTO_INCREMENT, `Name` MEDIUMTEXT,`UUID` MEDIUMTEXT, `Rank` varchar(32), `Wins` INT, `Exp` INT,PRIMARY KEY (`id`));");
		
	}
	
	public static boolean hasAccount(String uuid) throws Exception
	{
		
		if (!DB.checkConnection())
		{
			DB.openConnection();
		}
		
		Statement s = DB.getConnection().createStatement();
		ResultSet rs = s.executeQuery("SELECT * FROM `MPAccounts` WHERE `UUID`='" + uuid + "';");
		
		if (!rs.next())
		{
			return false;
		}
		else
		{
			return true;
		}
		
	}
	
	public static void createAccount(Player player) throws Exception
	{
		
		if (!DB.checkConnection())
		{
			DB.openConnection();
		}
		
		String name = player.getName();
		String uuid = player.getUniqueId().toString();
		
		Statement s = DB.getConnection().createStatement();
		s.executeUpdate("INSERT INTO `MPAccounts` (`Name`,`UUID`,`Rank`,`Wins`,`Exp`) VALUES ('" + name + "','" + uuid
				+ "','DEFAULT','0','50');");
		
	}
	
	public static String getRank(String uuid) throws Exception
	{
		
		if (!DB.checkConnection())
		{
			DB.openConnection();
		}
		
		Statement s = DB.getConnection().createStatement();
		ResultSet rs = s.executeQuery("SELECT * FROM `MPAccounts` WHERE `UUID`='" + uuid + "';");
		
		if (!rs.next())
		{
			return "DEFAULT";
		}
		else
		{
			return rs.getString("Rank");
		}
		
	}
	
	public static int getWins(String uuid) throws Exception
	{
		
		if (!DB.checkConnection())
		{
			DB.openConnection();
		}
		
		Statement s = DB.getConnection().createStatement();
		ResultSet rs = s.executeQuery("SELECT * FROM `MPAccounts` WHERE `UUID`='" + uuid + "';");
		
		if (!rs.next())
		{
			return 0;
		}
		else
		{
			return rs.getInt("Wins");
		}
		
	}
	
	public static void updateWins(String uuid, int increment) throws Exception
	{
		
		if (!DB.checkConnection())
		{
			DB.openConnection();
		}
		
		Statement s = DB.getConnection().createStatement();
		s.executeUpdate("UPDATE `MPAccounts` SET `Wins`='" + (getWins(uuid) + increment) + "' WHERE `UUID`='" + uuid + "';");
		
	}
	
	public static int getExperience(String uuid) throws Exception
	{
		
		if (!DB.checkConnection())
		{
			DB.openConnection();
		}
		
		Statement s = DB.getConnection().createStatement();
		ResultSet rs = s.executeQuery("SELECT * FROM `MPAccounts` WHERE `UUID`='" + uuid + "';");
		
		if (!rs.next())
		{
			return 0;
		}
		else
		{
			return rs.getInt("Exp");
		}
		
	}
	
	public static void updateExperience(String uuid, int increment) throws Exception
	{
		
		if (!DB.checkConnection())
		{
			DB.openConnection();
		}
		
		Statement s = DB.getConnection().createStatement();
		s.executeUpdate("UPDATE `MPAccounts` SET `Exp`='" + (getExperience(uuid) + increment) + "' WHERE `UUID`='" + uuid + "';");
		
	}
	
}
