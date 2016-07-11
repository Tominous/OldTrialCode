package org.jbltd.arcade.kit;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jbltd.arcade.core.events.GameStateChangeEvent;
import org.jbltd.arcade.core.util.F;
import org.jbltd.arcade.core.util.GameState;
import org.jbltd.arcade.core.util.UtilLevel;
import org.jbltd.arcade.core.util.UtilPlayer;
import org.jbltd.arcade.managers.ArcadeManager;

public class KitManager implements Listener
{
	
	private ArcadeManager _arcadeManager;
	private UtilKitInventory inv;
	
	public KitManager(ArcadeManager manager)
	{
		
		this._arcadeManager = manager;
		this.inv = new UtilKitInventory(_arcadeManager);
	}
	
	public Kit getKitByName(String input)
	{
		
		for (Kit kit : _arcadeManager.getGame().getAllKits())
		{
			
			if (kit.getName().equalsIgnoreCase(ChatColor.stripColor(input)))
			{
				return kit;
			}
		}
		return null;
		
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e)
	{
		
		e.getPlayer().getInventory().clear();
		e.getPlayer().getInventory().setArmorContents(null);
		inv.giveItem(e.getPlayer());
		
	}
	
	@EventHandler
	public void onGSChange(GameStateChangeEvent e)
	{
		
		if (e.getToState() == GameState.RECRUITING)
		{
			for (Player player : Bukkit.getOnlinePlayers())
			{
				player.getInventory().clear();
				player.getInventory().setArmorContents(null);
				inv.giveItem(player);
				player.updateInventory();
				
			}
		}
		
	}
	
	@EventHandler
	public void onInteractForKit(PlayerInteractEvent e)
	{
		
		if (_arcadeManager.getGame().isPreparing() == true)
		{
			return;
		}
		
		if (_arcadeManager.getGame().isLive() == true)
		{
			return;
		}
		
		if (e.getPlayer().getItemInHand().getType() != Material.COMPASS)
		{
			return;
		}
		
		if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK)
		{
			
			e.setCancelled(true);
			
			Player player = e.getPlayer();
			
			inv.openInventory(player);
			
		}
		else
		{
			return;
		}
	}
	
	@EventHandler
	public void onKitSelect(InventoryClickEvent e) throws Exception
	{
		
		if (!e.getInventory().getName().equals(F.underRed + "Kits"))
		{
			return;
		}
		
		if (e.getCurrentItem().getType() == Material.AIR)
		{
			return;
		}
		
		if (e.getCurrentItem() == null)
		{
			return;
		}
		
		e.setCancelled(true);
		
		Player player = (Player) e.getWhoClicked();
		String name = e.getCurrentItem().getItemMeta().getDisplayName();
		
		if (UtilLevel.getLevel(player) < getKitByName(name).getRequired())
		{
			
			UtilPlayer.sendMessage(player,
					F.error("Kit", "That kit requires level " + getKitByName(name).getRequired() + " to use!"));
			player.closeInventory();
			return;
		}
		
		else
		{
			this._arcadeManager.getGame().setKit(player, getKitByName(name));
			this._arcadeManager.getGame().getKit(player).displayDesciption(player);
			
			player.closeInventory();
			return;
		}
	}
	
}
