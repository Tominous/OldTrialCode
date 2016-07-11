package org.jbltd.arcade.kit.kits;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.jbltd.arcade.core.util.F;
import org.jbltd.arcade.core.util.ItemFactory;
import org.jbltd.arcade.kit.Kit;
import org.jbltd.arcade.managers.ArcadeManager;

public class CustomSnowballer extends Kit
{
	private ItemStack _shovel = ItemFactory.createItem(Material.IRON_SPADE, F.boldAqua + "Snow Destroyer", null, false);
	
	public CustomSnowballer(ArcadeManager arcadeManager)
	{
		super("Snow King", new String[]
		{ "Do you want to build a snowman? ♪", "Use your trusty iron shovel", "To build snowmen!" }, Material.IRON_SPADE, 3,
				arcadeManager);
		
	}
	
	@Override
	public void giveItems(Player player)
	{
		player.getInventory().addItem(_shovel);
		
	}
	
	@EventHandler
	public void pickupSnow(PlayerPickupItemEvent e)
	{
		
		if (e.getItem().getItemStack().getType() == Material.SNOW_BALL)
		{
			
			Player player = e.getPlayer();
			if (player.getInventory().contains(Material.SNOW_BALL, 4))
			{
				player.getInventory().remove(Material.SNOW_BALL);
				player.getInventory().addItem(new ItemStack(Material.SNOW_BLOCK));
				player.updateInventory();
			}
			
		}
		else
		{
			return;
		}
	}
	
}
