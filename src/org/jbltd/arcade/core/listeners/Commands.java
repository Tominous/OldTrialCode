package org.jbltd.arcade.core.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.jbltd.arcade.core.util.F;
import org.jbltd.arcade.core.util.UtilPlayer;

public class Commands implements Listener
{
	
	@EventHandler
	public void onCommand(PlayerCommandPreprocessEvent e)
	{
		
		Player player = e.getPlayer();
		
		if (e.getMessage().startsWith("/me") || e.getMessage().startsWith("/say"))
		{
			
			e.setCancelled(true);
			UtilPlayer.sendMessage(player, F.error("Permissions", "No permission!"));
			
		}
		
		if (e.getMessage().startsWith("/bukkit"))
		{
			e.setCancelled(true);
			UtilPlayer.sendMessage(player, F.error("Chat", "Don't be so nosy!"));
			
		}
		
	}
	
}
