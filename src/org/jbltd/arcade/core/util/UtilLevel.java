package org.jbltd.arcade.core.util;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jbltd.arcade.core.sql.Manager;

public class UtilLevel
{
	
	private static int _level = 1;
	private static int _exp = 50;
	
	public static int getLevel(Player player) throws Exception
	{
		_exp = Manager.getExperience(player.getUniqueId().toString());
		
		_level = (int) (Math.sqrt(625 + 100 * _exp) - 25) / 50;
		
		return _level;
	}
	
	public static int calculateNeeded()
	{
		
		int online = Bukkit.getOnlinePlayers().size();
		
		int needed = online * 20;
		
		return needed;
		
	}
	
	public static int calculateDefault()
	{
		
		int online = Bukkit.getOnlinePlayers().size();
		int needed = online * 5;
		
		return needed;
		
	}
	
	private static String getLevelFormat()
	{
		
		if (_level < 15)
		{
			return F.GRAY + _level;
		}
		else if (_level >= 15 && _level < 20)
		{
			return F.GREEN + _level;
		}
		else if (_level >= 20 && _level < 30)
		{
			return F.BLUE + _level;
		}
		else if (_level >= 30 && _level < 40)
		{
			return F.LIGHT_PURPLE + _level;
		}
		else if (_level >= 40 && _level < 50)
		{
			return F.RED + _level;
		}
		else
		{
			return F.GOLD + _level;
		}
	}
	
	public static void calculateLevel(Player player) throws Exception
	{
		
		_exp = Manager.getExperience(player.getUniqueId().toString());
		_level = (int) Math.floor((Math.sqrt(625 + 100 * _exp) - 25) / 50);
		
		switch (Manager.getRank(player.getUniqueId().toString()))
		{
		
			case "DEFAULT":
			player.setDisplayName(getLevelFormat() + " " + F.YELLOW + player.getName());
				break;
			case "TESTER":
			player.setDisplayName(getLevelFormat() + " " + F.boldAqua + "TESTER " + F.YELLOW + player.getName());
				break;
			case "OWNER":
			player.setDisplayName(getLevelFormat() + " " + F.boldDR + "OWNER " + F.YELLOW + player.getName());
				break;
		}
		player.setPlayerListName(player.getDisplayName());
		
	}
	
	public static void recalculatePlayerLevels() throws Exception
	{
		
		for (Player player : Bukkit.getOnlinePlayers())
		{
			calculateLevel(player);
			player.sendMessage(F.boldGold + "You are level " + getLevel(player));
			continue;
		}
		
	}
	
}
