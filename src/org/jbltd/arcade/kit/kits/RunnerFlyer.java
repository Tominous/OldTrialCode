package org.jbltd.arcade.kit.kits;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.jbltd.arcade.core.util.F;
import org.jbltd.arcade.core.util.GameType;
import org.jbltd.arcade.core.util.ItemFactory;
import org.jbltd.arcade.kit.Kit;
import org.jbltd.arcade.managers.ArcadeManager;

public class RunnerFlyer extends Kit
{
	private ArcadeManager _manager;
	
	public RunnerFlyer(ArcadeManager manager)
	{
		super("Flyer", new String[]
		{ "Fallen and can't get up?", "Give yourself a boost!", "Right click with axe to leap!" }, Material.STONE_AXE, 1, manager);
		
		_manager = manager;
	}
	
	ItemStack axe = ItemFactory.createItem(Material.STONE_AXE, F.boldYellow + "Leap - 1 Use", null, false);
	
	@Override
	public void giveItems(Player player)
	{
		player.getInventory().addItem(axe);
		
	}
	
	@EventHandler
	public void onUse(PlayerInteractEvent e)
	{
		
		if (_manager.getGame().getType() != GameType.RUNNER)
		{
			return;
		}
		
		if (_manager.getGame().isLive() == false)
		{
			return;
		}
		
		final Player player = e.getPlayer();
		
		if (e.getAction() != Action.RIGHT_CLICK_AIR)
		{
			return;
		}
		
		if (player.getItemInHand().getType() != Material.STONE_AXE)
		{
			return;
		}
		
		Location loc = player.getLocation().clone();
		loc.setPitch(0.0F);
		Vector vel = player.getVelocity().clone();
		
		int strength = 1;
		
		Vector jump = vel.multiply(0.1D).setY(0.20D * strength);
		Vector look = loc.getDirection().normalize().multiply(1.5D);
		
		player.setVelocity(jump.add(look));
		player.playSound(player.getLocation(), Sound.ITEM_BREAK, 1, 1);
		player.getInventory().remove(axe);
		
	}
	
}
