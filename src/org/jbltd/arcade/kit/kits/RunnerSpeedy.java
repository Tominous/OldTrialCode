package org.jbltd.arcade.kit.kits;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jbltd.arcade.kit.Kit;
import org.jbltd.arcade.managers.ArcadeManager;

public class RunnerSpeedy extends Kit
{
	
	public RunnerSpeedy(ArcadeManager arcadeManager)
	{
		super("Speedy Gonzalez", new String[]
		{ "RUN RUN RUN!", "Permanent speed boost" }, Material.POTION, 5, arcadeManager);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void giveItems(Player player)
	{
		player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1));
		
	}
	
}
