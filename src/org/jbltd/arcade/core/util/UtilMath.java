package org.jbltd.arcade.core.util;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentMap;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.jbltd.arcade.managers.ArcadeManager;

import com.google.common.collect.MapMaker;

public class UtilMath
{
	
	private ArcadeManager _manager;
	
	public UtilMath(ArcadeManager manager)
	{
		_manager = manager;
	}
	
	public static HashMap<Block, Double> getBlocksInRadius(Location loc, double dR, double heightLimit)
	{
		HashMap<Block, Double> blocks = new HashMap<Block, Double>();
		int iR = (int) dR + 1;
		for (int x = -iR; x <= iR; x++)
		{
			for (int z = -iR; z <= iR; z++)
			{
				for (int y = -iR; y <= iR; y++)
				{
					if (Math.abs(y) <= heightLimit)
					{
						Block block = loc.getWorld().getBlockAt((int) (loc.getX() + x), (int) (loc.getY() + y),
								(int) (loc.getZ() + z));
						
						double offset = UtilMath.offset(loc, block.getLocation().add(0.5D, 0.5D, 0.5D));
						if (offset <= dR)
						{
							blocks.put(block, Double.valueOf(1.0D - offset / dR));
						}
					}
				}
			}
		}
		return blocks;
	}
	
	public static ConcurrentMap<Player, Location> lastPlaced = new MapMaker().weakKeys().weakValues().makeMap();
	
	public static Player findNearestPlayerPlacedBlock(Location location, int maximumDistance)
	{
		Player best = null;
		double bestDistance = Double.MAX_VALUE;
		
		for (Player player : location.getWorld().getPlayers())
		{
			Location lastPlacedBlock = lastPlaced.get(player);
			
			if (lastPlacedBlock != null)
			{
				double distance = location.distanceSquared(lastPlacedBlock);
				
				if (distance < bestDistance && distance < maximumDistance)
				{
					best = player;
					distance = bestDistance;
				}
			}
		}
		return best;
	}
	
	public void regen(final List<BlockState> blocks, final boolean effect, final int speed)
	{
		
		new BukkitRunnable()
		{
			int i = -1;
			
			@SuppressWarnings("deprecation")
			public void run()
			{
				if (i != blocks.size() - 1)
				{
					i++;
					BlockState bs = blocks.get(i);
					bs.getBlock().setType(bs.getType());
					bs.getBlock().setData(bs.getBlock().getData());
					if (effect)
						bs.getBlock().getWorld().playEffect(bs.getLocation(), Effect.STEP_SOUND, bs.getBlock().getType());
				}
				else
				{
					for (BlockState bs : blocks)
					{
						bs.getBlock().setType(bs.getType());
						bs.getBlock().setData(bs.getBlock().getData());
					}
					blocks.clear();
					this.cancel();
				}
			}
		}.runTaskTimer(this._manager.getPlugin(), speed, speed);
	}
	
	public static double offset(Location a, Location b)
	{
		return offset(a.toVector(), b.toVector());
	}
	
	public static double offset(Vector a, Vector b)
	{
		return a.subtract(b).length();
	}
	
}
