package org.jbltd.arcade.games;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.jbltd.arcade.core.events.UpdateEvent;
import org.jbltd.arcade.core.sql.Manager;
import org.jbltd.arcade.core.util.F;
import org.jbltd.arcade.core.util.GameState;
import org.jbltd.arcade.core.util.GameType;
import org.jbltd.arcade.core.util.MapUtil;
import org.jbltd.arcade.core.util.UpdateType;
import org.jbltd.arcade.core.util.UtilLevel;
import org.jbltd.arcade.core.util.UtilPlayer;
import org.jbltd.arcade.game.SingleGame;
import org.jbltd.arcade.kit.Kit;
import org.jbltd.arcade.kit.kits.RunnerFlyer;
import org.jbltd.arcade.kit.kits.RunnerSpeedy;
import org.jbltd.arcade.managers.ArcadeManager;

public class Runner extends SingleGame
{
	
	private HashMap<Block, Integer> _blocks = new HashMap<Block, Integer>();
	
	private List<String> _alive = new ArrayList<String>();
	private List<String> _dead = new ArrayList<String>();
	
	private World _worldName;
	
	public Runner(ArcadeManager manager)
	{
		super(manager, "Mineplex Runner", new String[]
		{ "Blocks fall from underneath you", "Keep running to stay alive", "Avoid falling blocks from above",
				"Last player alive wins!" }, new Kit[]
		{ new RunnerFlyer(manager), new RunnerSpeedy(manager) }, GameType.RUNNER);
		
		setDefaultKit(getAllKits()[0]);
		setLobbyCountdown(30);
		setCountdown(10);
		this.arcadeManager.setGame(this);
		
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent e)
	{
		
		if (!GetLivePlayers().contains(e.getPlayer().getName()))
		{
			return;
		}
		
		if (isLive() == true)
		{
			
			Location from = e.getFrom();
			Location to = e.getTo();
			
			if (from.getBlockX() == to.getBlockX() && from.getBlockY() == to.getBlockY() && from.getBlockZ() == to.getBlockZ())
				return;
			
			Block block = to.subtract(0, 1, 0).getBlock();
			
			if (block.getType() == Material.getMaterial(98))
			{
				e.getPlayer().setHealth(0);
				UtilPlayer.sendMessage(e.getPlayer(), F.error("Game", "Don't try to cheat!"));
				return;
			}
			
			if (block.getType() != Material.AIR && !block.isLiquid())
			{
				addBlock(block);
			}
		}
		else
		{
			return;
		}
		
	}
	
	@EventHandler
	public void blockForm(EntityChangeBlockEvent event)
	{
		smash(event.getEntity());
		
		event.setCancelled(true);
	}
	
	public void smash(Entity ent)
	{
		if (!(ent instanceof FallingBlock))
		{
			return;
		}
		FallingBlock block = (FallingBlock) ent;
		
		@SuppressWarnings("deprecation")
		int id = block.getBlockId();
		if ((id == 35) || (id == 159))
		{
			id = 152;
		}
		block.getWorld().playEffect(block.getLocation(), Effect.STEP_SOUND, id);
		
		ent.remove();
	}
	
	@EventHandler
	public void onHunger(FoodLevelChangeEvent e)
	{
		e.setFoodLevel(20);
		e.setCancelled(true);
	}
	
	@EventHandler
	public void onPickup(PlayerPickupItemEvent e)
	{
		e.setCancelled(true);
	}
	
	@EventHandler
	public void onDamage(EntityDamageByEntityEvent e)
	{
		
		if (isLive() == false)
		{
			e.setCancelled(true);
		}
		
		if (!(e.getDamager() instanceof Player))
		{
			return;
		}
		if (!(e.getEntity() instanceof Player))
		{
			return;
		}
		
		Player damager = (Player) e.getDamager();
		
		if (_dead.contains(damager.getName()))
		{
			e.setDamage(0.0D);
			e.setCancelled(true);
		}
		
	}
	
	@EventHandler
	public void onDeath(PlayerDeathEvent e)
	{
		
		Player player = (Player) e.getEntity();
		player.setAllowFlight(true);
		player.setFlying(true);
		player.setGameMode(GameMode.SPECTATOR);
		if (GetLivePlayers().contains(player.getName()))
		{
			GetLivePlayers().remove(player.getName());
		}
		_dead.add(player.getName());
		
	}
	
	@EventHandler
	public void onLeave(PlayerQuitEvent e)
	{
		
		if (GetLivePlayers().contains(e.getPlayer().getName()))
		{
			GetLivePlayers().remove(e.getPlayer().getName());
		}
		
		if (GetDeadPlayers().contains(e.getPlayer().getName()))
		{
			GetDeadPlayers().remove(e.getPlayer().getName());
		}
		
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void update(UpdateEvent e)
	{
		
		if (e.getType() != UpdateType.TICK)
		{
			return;
		}
		
		if (this.arcadeManager.getState() != GameState.INGAME)
		{
			return;
			
		}
		else
		{
			
			this.endCheck();
			
			try
			{
				for (Block block : _blocks.keySet())
				{
					int state = _blocks.get(block);
					if (state == 0)
					{
						block.setType(Material.STAINED_CLAY);
						block.setData(DyeColor.BLUE.getData());
						_blocks.put(block, 1);
					}
					if (state == 1)
					{
						block.setType(Material.STAINED_CLAY);
						block.setData(DyeColor.RED.getData());
						_blocks.put(block, 2);
					}
					if (state == 2)
					{
						
						block.getWorld().spawnFallingBlock(block.getLocation(), block.getType(), block.getData());
						block.setType(Material.AIR);
						removeBlock(block);
						
					}
					
				}
			}
			catch (ConcurrentModificationException ex)
			{
				return;
			}
		}
		
	}
	
	public void addBlock(Block block)
	{
		_blocks.put(block, 0);
	}
	
	public void removeBlock(Block block)
	{
		_blocks.remove(block);
	}
	
	public HashMap<Block, Integer> getBlocks()
	{
		return _blocks;
	}
	
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
				if (!_alive.contains(player.getName()))
				{
					_alive.add(player.getName());
				}
				player.setGameMode(GameMode.SURVIVAL);
				player.setAllowFlight(false);
				player.setFlying(false);
			}
		}
	}
	
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
		}.runTaskLater(this.arcadeManager.getPlugin(), 100L);
		
		this.arcadeManager.announceWinner();
		
		this.arcadeManager.continueCycle();
		
	}
	
	@Override
	public List<String> GetLivePlayers()
	{
		// TODO Auto-generated method stub
		return _alive;
	}
	
	@Override
	public List<String> GetDeadPlayers()
	{
		// TODO Auto-generated method stub
		return _dead;
	}
	
	@Override
	public void endCheck()
	{
		
		if (GetLivePlayers().size() == 1)
		{
			Winner = GetLivePlayers().get(0);
			Player win = Bukkit.getPlayer(Winner);
			_worldName = win.getWorld();
			
			try
			{
				for (Player player : Bukkit.getOnlinePlayers())
				{
					if (player == win)
					{
						Manager.updateExperience(player.getUniqueId().toString(), UtilLevel.calculateNeeded());
						Manager.updateWins(player.getUniqueId().toString(), 1);
						player.sendMessage(F.boldGold + "You were awarded " + UtilLevel.calculateNeeded() + " experience for: "
								+ F.GREEN + "WINNING! :D");
					}
					else
					{
						Manager.updateExperience(player.getUniqueId().toString(), UtilLevel.calculateDefault());
						player.sendMessage(F.boldGold + "You were awarded " + UtilLevel.calculateDefault() + " experience for: "
								+ F.GREEN + "participation");
					}
				}
				UtilLevel.recalculatePlayerLevels();
			}
			catch (Exception ex)
			{
				
			}
			end();
			GetLivePlayers().clear();
			GetDeadPlayers().clear();
			this.arcadeManager.setState(GameState.POSTGAME);
			
		}
		
	}
	
}
