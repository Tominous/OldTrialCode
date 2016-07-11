package org.jbltd.arcade.core.util;

import org.bukkit.entity.Player;

public class UtilPlayer
{
	
	public static void sendMessage(Player player, String message)
	{
		
		player.sendMessage(message);
		
	}
	
	public static void sendPluginMessage(Player player, String module, String message)
	{
		
		player.sendMessage(F.info(module, message));
		
	}
	
}
