package org.jbltd.arcade.core.listeners;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jbltd.arcade.core.sql.Manager;
import org.jbltd.arcade.core.util.F;
import org.jbltd.arcade.core.util.UtilLevel;

public class JoinQuit implements Listener
{
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) throws Exception
	{
		
		Player player = e.getPlayer();
		player.setHealth(20.0D);
		player.setMaxHealth(20.0D);
		player.setGameMode(GameMode.SURVIVAL);
		player.setFoodLevel(20);
		player.setSaturation(2F);
		player.setLevel(0);
		player.setTotalExperience(0);
		player.setFireTicks(0);
		
		e.setJoinMessage(F.info("Join", F.GRAY + player.getName()));
		
		if (!Manager.hasAccount(player.getUniqueId().toString()))
		{
			Manager.createAccount(player);
		}
		
		UtilLevel.calculateLevel(player);
		
		player.setPlayerListName(player.getDisplayName());
		
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e)
	{
		Player player = e.getPlayer();
		
		e.setQuitMessage(F.info("Quit", F.GRAY + player.getName()));
	}
	
}
