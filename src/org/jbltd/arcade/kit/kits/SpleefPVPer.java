package org.jbltd.arcade.kit.kits;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jbltd.arcade.core.util.F;
import org.jbltd.arcade.core.util.ItemFactory;
import org.jbltd.arcade.kit.Kit;
import org.jbltd.arcade.managers.ArcadeManager;

public class SpleefPVPer extends Kit
{
	
	private ItemStack _sword = ItemFactory.createItem(Material.WOOD_SWORD, F.boldRed + "Nub Destroyer", null, false);
	
	public SpleefPVPer(ArcadeManager arcadeManager)
	{
		super("PVP'er", new String[]
		{ "REK! FIGHT! KILL!", "Eliminate all those n00bs" }, Material.WOOD_SWORD, 7, arcadeManager);
		
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void giveItems(Player player)
	{
		
		player.getInventory().addItem(this._sword);
		
	}
	
}
