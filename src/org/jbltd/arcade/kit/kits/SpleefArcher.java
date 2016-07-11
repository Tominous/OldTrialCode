package org.jbltd.arcade.kit.kits;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jbltd.arcade.core.util.F;
import org.jbltd.arcade.core.util.ItemFactory;
import org.jbltd.arcade.kit.Kit;
import org.jbltd.arcade.managers.ArcadeManager;

public class SpleefArcher extends Kit
{
	
	public SpleefArcher(ArcadeManager arcadeManager)
	{
		super("Archer", new String[]
		{ "Fire your arrows!", "Destroy a small section of land when you hit!" }, Material.BOW, 5, arcadeManager);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void giveItems(Player player)
	{
		ItemStack bow = ItemFactory.createItem(Material.BOW, F.boldGreen + "Natura Bow", null, false);
		ItemMeta bowMeta = bow.getItemMeta();
		bowMeta.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
		
		bow.setItemMeta(bowMeta);
		
		ItemStack arrow = ItemFactory.createItem(Material.ARROW, F.BLUE + "Eternal Arrow", null, false);
		
		player.getInventory().setItem(0, bow);
		player.getInventory().setItem(12, arrow);
		
	}
	
}
