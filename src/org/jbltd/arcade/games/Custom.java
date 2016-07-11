package org.jbltd.arcade.games;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowman;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.jbltd.arcade.core.sql.Manager;
import org.jbltd.arcade.core.util.F;
import org.jbltd.arcade.core.util.GameState;
import org.jbltd.arcade.core.util.GameType;
import org.jbltd.arcade.core.util.MapUtil;
import org.jbltd.arcade.core.util.UtilLevel;
import org.jbltd.arcade.core.util.UtilMath;
import org.jbltd.arcade.game.SingleGame;
import org.jbltd.arcade.kit.Kit;
import org.jbltd.arcade.kit.kits.CustomPumpkinKing;
import org.jbltd.arcade.kit.kits.CustomSnowballer;
import org.jbltd.arcade.managers.ArcadeManager;

public class Custom extends SingleGame
{
	
	private HashMap<String, Integer> _scores = new HashMap<String, Integer>();
	private UtilMath math = new UtilMath(arcadeManager);
	private World _worldName;
	
	public Custom(ArcadeManager arcadeManager)
	{
		super(arcadeManager, "Build a Snowman",
				new String[]
				{ "Collect snow pallets to build a snowman!", "Use your trusty shovel to help!",
						"First player to build 10 snowmen wins!" }, new Kit[]
				{ new CustomPumpkinKing(arcadeManager), new CustomSnowballer(arcadeManager), }, GameType.CUSTOM);
		
		setDefaultKit(getAllKits()[0]);
		setLobbyCountdown(30);
		setCountdown(10);
		
		arcadeManager.setGame(this);
	}
	
	@EventHandler
	public void onBreak(BlockBreakEvent e)
	{
		
		Block b = e.getBlock();
		if (b.getType() == Material.STONE)
		{
			e.setCancelled(true);
		}
		if (b.getType() == Material.GLASS)
		{
			e.setCancelled(true);
		}
		if (b.getType() == Material.WOOD)
		{
			e.setCancelled(true);
		}
		if (b.getType() == Material.ICE)
		{
			e.setCancelled(true);
		}
		if (b.getType() == Material.SNOW_BLOCK)
		{
			e.setCancelled(true);
		}
		if (b.getType() == Material.GOLD_BLOCK)
		{
			e.setCancelled(true);
		}
		
		final List<BlockState> blocks = new ArrayList<BlockState>();
		
		if (b.getType() == Material.PUMPKIN)
		{
			if (!blocks.contains(b.getState()))
			{
				blocks.add(b.getState());
			}
		}
		else if (b.getType() == Material.SNOW)
		{
			if (!blocks.contains(b.getState()))
			{
				blocks.add(b.getState());
			}
		}
		else
		{
			return;
		}
		
		new BukkitRunnable()
		{
			int i = 17;
			
			public void run()
			{
				if (i > 0)
				{
					i--;
				}
				else
				{
					math.regen(blocks, false, 6);
					this.cancel();
					
				}
			}
		}.runTaskTimer(arcadeManager.getPlugin(), 15, 15);
		
	}
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event)
	{
		
		if (isLive() == false)
		{
			event.setCancelled(true);
			return;
		}
		
		Player player = event.getPlayer();
		Location location = event.getBlock().getLocation();
		
		// Save the location of the last placed block
		UtilMath.lastPlaced.put(player, location);
	}
	
	@EventHandler
	public void onCreatureSpawn(CreatureSpawnEvent event)
	{
		
		if (isLive() == false)
		{
			return;
		}
		
		SpawnReason reason = event.getSpawnReason();
		
		if (reason == SpawnReason.BUILD_SNOWMAN)
		{
			
			Player responsible = UtilMath.findNearestPlayerPlacedBlock(event.getLocation(), 10);
			
			responsible.playSound(responsible.getLocation(), Sound.NOTE_PLING, 3F, 2F);
			
			String world = arcadeManager.getPlugin().getConfig().getString("game.build_a_snowman.gameworld");
			int x = arcadeManager.getPlugin().getConfig().getInt("game.build_a_snowman.snowmandisx");
			int y = arcadeManager.getPlugin().getConfig().getInt("game.build_a_snowman.snowmandisy");
			int z = arcadeManager.getPlugin().getConfig().getInt("game.build_a_snowman.snowmandisz");
			
			Location loc = new Location(Bukkit.getWorld(world), x, y, z);
			
			Snowman man = (Snowman) event.getEntity();
			man.teleport(loc);
			man.setHealth(0.0D);
			
			if (!_scores.containsKey(responsible.getName()))
			{
				_scores.put(responsible.getName(), 1);
			}
			else
			{
				_scores.put(responsible.getName(), _scores.get(responsible.getName()) + 1);
				responsible.setLevel(_scores.get(responsible.getName()));
				
				this.endCheck();
				
			}
			
		}
		else
		{
			event.setCancelled(true);
			return;
		}
		
	}
	
	@EventHandler
	public void onDamage(EntityDamageEvent e)
	{
		e.setDamage(0.0D);
		e.setCancelled(true);
	}
	
	@Override
	public void start()
	{
		try
		{
			this.startGameCountdown();
			
		}
		finally
		{
			for (Player player : Bukkit.getOnlinePlayers())
			{
				player.setGameMode(GameMode.SURVIVAL);
				player.setAllowFlight(false);
				player.setFlying(false);
			}
		}
	}
	
	@Override
	public void end()
	{
		HandlerList.unregisterAll(this);
		
		new BukkitRunnable()
		{
			
			@Override
			public void run()
			{
				
				arcadeManager.setState(GameState.RECRUITING);
				new BukkitRunnable()
				{
					
					@Override
					public void run()
					{
						MapUtil.unloadWorld(_worldName);
						MapUtil.deleteWorld(_worldName.getWorldFolder());
						
					}
				}.runTaskLater(arcadeManager.getPlugin(), 40L);
				
			}
		}.runTaskLater(arcadeManager.getPlugin(), 100L);
		
		arcadeManager.announceWinner();
		this.arcadeManager.continueCycle();
		
	}
	
	@Override
	public List<String> GetLivePlayers()
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public List<String> GetDeadPlayers()
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void endCheck()
	{
		
		if (_scores.values().contains(10))
		{
			
			for (Entry<String, Integer> entry : _scores.entrySet())
			{
				if (entry.getValue() >= 10)
				{
					
					Winner = entry.getKey();
					Player win = Bukkit.getPlayer(Winner);
					
					try
					{
						for (Player player : Bukkit.getOnlinePlayers())
						{
							if (player == win)
							{
								Manager.updateExperience(player.getUniqueId().toString(), UtilLevel.calculateNeeded());
								Manager.updateWins(player.getUniqueId().toString(), 1);
								player.sendMessage(F.boldGold + "You were awarded " + UtilLevel.calculateNeeded()
										+ " experience for: " + F.GREEN + "WINNING! :D");
							}
							else
							{
								Manager.updateExperience(player.getUniqueId().toString(), UtilLevel.calculateDefault());
								player.sendMessage(F.boldGold + "You were awarded " + UtilLevel.calculateDefault()
										+ " experience for: " + F.GREEN + "participation");
							}
						}
						UtilLevel.recalculatePlayerLevels();
					}
					catch (Exception ex)
					{
						
					}
					
					_worldName = win.getWorld();
					try
					{
						Manager.updateWins(win.getUniqueId().toString(), 1);
					}
					catch (Exception e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			
			end();
			this.arcadeManager.setState(GameState.POSTGAME);
			_scores.clear();
			
		}
		
	}
	
}
