package org.jbltd.arcade.kit.kits;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jbltd.arcade.core.util.F;
import org.jbltd.arcade.core.util.ItemFactory;
import org.jbltd.arcade.core.util.UtilPlayer;
import org.jbltd.arcade.kit.Kit;
import org.jbltd.arcade.managers.ArcadeManager;

public class CustomPumpkinKing extends Kit
{
	
	private ItemStack _shovel = ItemFactory.createItem(Material.WOOD_SPADE, F.GREEN + "Standard Shovel", null, false);
	private ItemStack _pumpkin = ItemFactory.createItem(Material.JACK_O_LANTERN, F.RED + "Pumpkin King Crown", null, false);
	
	public CustomPumpkinKing(ArcadeManager arcadeManager)
	{
		super("Pumpkin King", new String[]
		{ "Use your minions to help you win!", "Start with 2 pumpkins!", "And get a cool mask ;)" }, Material.PUMPKIN, 1,
				arcadeManager);
	}
	
	@Override
	public void giveItems(Player player)
	{
		player.getInventory().addItem(_shovel);
		player.getInventory().addItem(new ItemStack(Material.PUMPKIN, 2));
		player.getInventory().setHelmet(_pumpkin);
		
	}
	
	@EventHandler
	public void onInvClick(InventoryClickEvent e)
	{
		
		Player player = (Player) e.getWhoClicked();
		ItemStack is = e.getCurrentItem();
		
		if (is.getType() == Material.JACK_O_LANTERN && is.getItemMeta().hasDisplayName())
		{
			e.setCancelled(true);
			UtilPlayer.sendMessage(player, F.error("Game", "Why would you remove your crown?"));
			player.playSound(player.getLocation(), Sound.NOTE_BASS, 3F, 0F);
			player.closeInventory();
		}
		
	}
	
}
