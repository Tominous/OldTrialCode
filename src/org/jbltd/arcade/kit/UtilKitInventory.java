package org.jbltd.arcade.kit;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jbltd.arcade.core.util.F;
import org.jbltd.arcade.core.util.ItemFactory;
import org.jbltd.arcade.managers.ArcadeManager;

public class UtilKitInventory
{
	
	public ArcadeManager _manager;
	
	public UtilKitInventory(ArcadeManager manager)
	{
		_manager = manager;
	}
	
	public void giveItem(Player player)
	{
		
		ItemStack compass = ItemFactory.createItem(Material.COMPASS, F.boldGreen + "Kits", null, false);
		
		player.getInventory().setItem(4, compass);
		
	}
	
	public void openInventory(Player player)
	{
		
		Inventory inv = Bukkit.getServer().createInventory(player, 9, F.underRed + "Kits");
		int space = 0;
		
		for (Kit kit : _manager.getGame().getAllKits())
		{
			
			ItemStack is = ItemFactory.createItem(kit.getDisplayMaterial(), F.boldAqua + kit.getName(), null, false);
			ItemMeta im = is.getItemMeta();
			im.setLore(Arrays.asList(F.GREEN + "This kit requires " + F.RED + "level " + kit.getRequired() + F.GREEN + " to use"));
			is.setItemMeta(im);
			inv.setItem(space++, is);
			continue;
		}
		
		player.openInventory(inv);
		
	}
	
}
