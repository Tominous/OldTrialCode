package org.jbltd.arcade.core.listeners;

import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.jbltd.arcade.core.util.F;
import org.jbltd.arcade.core.util.UtilPlayer;

public class Chat implements Listener
{
	
	private HashMap<String, String> _playerLastMessage = new HashMap<String, String>();
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) throws Exception
	{
		
		Player player = e.getPlayer();
		
		e.setFormat(player.getDisplayName() + F.GRAY + ": " + F.RESET + e.getMessage());
		
		if (_playerLastMessage.containsKey(player.getName())
				&& _playerLastMessage.get(player.getName()).equalsIgnoreCase(e.getMessage()))
		{
			
			UtilPlayer.sendMessage(player, F.error("Chat", "You can't send the same message twice!"));
			e.setCancelled(true);
			
		}
		else
		{
			_playerLastMessage.put(player.getName(), e.getMessage());
		}
		
	}
	
}
