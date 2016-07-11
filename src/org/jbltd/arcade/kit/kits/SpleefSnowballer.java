package org.jbltd.arcade.kit.kits;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jbltd.arcade.kit.Kit;
import org.jbltd.arcade.managers.ArcadeManager;

public class SpleefSnowballer extends Kit
{
	
	public SpleefSnowballer(ArcadeManager arcadeManager)
	{
		super("Snowballer", new String[]
		{ "Throw snowball to break blocks!", "Recieves a Snowball when you punch blocks (Max of 16)" }, Material.SNOW_BALL, 1,
				arcadeManager);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void giveItems(Player player)
	{
		
		if (!player.getInventory().contains(Material.SNOW_BALL, 16))
		{
			player.getInventory().addItem(new ItemStack(Material.SNOW_BALL, 1));
		}
		else
		{
			return;
		}
		
	}
	
}
