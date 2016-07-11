package org.jbltd.arcade.games;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BlockIterator;
import org.jbltd.arcade.core.events.UpdateEvent;
import org.jbltd.arcade.core.sql.Manager;
import org.jbltd.arcade.core.util.F;
import org.jbltd.arcade.core.util.GameState;
import org.jbltd.arcade.core.util.GameType;
import org.jbltd.arcade.core.util.MapUtil;
import org.jbltd.arcade.core.util.UpdateType;
import org.jbltd.arcade.core.util.UtilLevel;
import org.jbltd.arcade.core.util.UtilMath;
import org.jbltd.arcade.game.SingleGame;
import org.jbltd.arcade.kit.Kit;
import org.jbltd.arcade.kit.kits.SpleefArcher;
import org.jbltd.arcade.kit.kits.SpleefPVPer;
import org.jbltd.arcade.kit.kits.SpleefSnowballer;
import org.jbltd.arcade.managers.ArcadeManager;

public class Spleef extends SingleGame
{
	
	private List<String> _alive = new ArrayList<String>();
	private List<String> _dead = new ArrayList<String>();
	
	private World _worldName;
	
	public Spleef(ArcadeManager arcadeManager)
	{
		super(arcadeManager, "Mineplex Spleef", new String[]
		{ "Punch blocks to break them!", "1 Hunger per block smashed!", "Last player alive wins!" }, new Kit[]
		{ new SpleefSnowballer(arcadeManager), new SpleefPVPer(arcadeManager), new SpleefArcher(arcadeManager) }, GameType.SPLEEF);
		
		arcadeManager.setGame(this);
		
		setDefaultKit(getAllKits()[0]);
		setLobbyCountdown(30);
		setCountdown(10);
	}
	
	@EventHandler
	public void onDeath(PlayerDeathEvent e)
	{
		
		Player player = (Player) e.getEntity();
		
		player.setAllowFlight(true);
		player.setFlying(true);
		player.setGameMode(GameMode.SPECTATOR);
		
		GetLivePlayers().remove(player.getName());
		
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
	public void blood(EntityDamageByEntityEvent e)
	{
		
		if (e.getDamager() instanceof Projectile)
		{
			e.setDamage(0.0D);
			e.setCancelled(true);
			return;
		}
		
		if (!e.isCancelled())
		{
			
			e.getEntity().getWorld().playEffect(e.getEntity().getLocation(), Effect.STEP_SOUND, Material.REDSTONE_BLOCK.getId());
			
		}
	}
	
	@EventHandler
	public void onThrowPreGame(ProjectileLaunchEvent e)
	{
		if (isPreparing() == true)
		{
			e.setCancelled(true);
			
			return;
		}
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onArrow(ProjectileHitEvent e)
	{
		if (isLive() == true)
		{
			
			if (e.getEntity() instanceof Snowball)
			{
				BlockIterator iterator = new BlockIterator(e.getEntity().getWorld(), e.getEntity().getLocation().toVector(), e
						.getEntity().getVelocity().normalize(), 0.0D, 4);
				
				Block hitBlock = null;
				
				while (iterator.hasNext())
				{
					hitBlock = iterator.next();
					
					if (hitBlock.getTypeId() != 0)
					{
						break;
					}
					
					if (hitBlock.isLiquid())
					{
						return;
					}
					
				}
				
				hitBlock.getWorld().playEffect(hitBlock.getLocation(), Effect.STEP_SOUND, hitBlock.getTypeId());
				hitBlock.setType(Material.AIR);
				e.getEntity().remove();
				
			}
			
			if (e.getEntity() instanceof Arrow)
			{
				
				BlockIterator iterator = new BlockIterator(e.getEntity().getWorld(), e.getEntity().getLocation().toVector(), e
						.getEntity().getVelocity().normalize(), 0.0D, 4);
				
				Block hitBlock = null;
				
				while (iterator.hasNext())
				{
					hitBlock = iterator.next();
					
					if (hitBlock.getTypeId() != 0)
					{
						break;
					}
					
					if (hitBlock.isLiquid())
					{
						return;
					}
					
				}
				
				for (Block other : UtilMath.getBlocksInRadius(hitBlock.getLocation().add(0.5D, 0.5D, 0.5D), 1, 100).keySet())
				{
					other.getWorld().playEffect(hitBlock.getLocation(), Effect.STEP_SOUND, hitBlock.getTypeId());
					other.setType(Material.AIR);
					hitBlock.getWorld().playEffect(hitBlock.getLocation(), Effect.STEP_SOUND, hitBlock.getTypeId());
					hitBlock.setType(Material.AIR);
					e.getEntity().remove();
					
				}
				
			}
			else
			{
				return;
			}
		}
		else
		{
			return;
		}
	}
	
	@EventHandler
	public void update(UpdateEvent e)
	{
		
		if (e.getType() != UpdateType.TICK)
		{
			return;
		}
		
		if (arcadeManager.getState() != GameState.INGAME)
		{
			return;
		}
		
		else
		{
			
			this.endCheck();
			
		}
		
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onInteract(PlayerInteractEvent e)
	{
		if (isLive() == true)
		{
			
			if (e.getAction() == Action.LEFT_CLICK_BLOCK)
			{
				
				Block block = e.getClickedBlock();
				block.getWorld().playEffect(block.getLocation(), Effect.STEP_SOUND, block.getTypeId());
				block.setType(Material.AIR);
				
				e.getPlayer().setFoodLevel(e.getPlayer().getFoodLevel() + 1);
				
				if (hasThisKit(e.getPlayer(), getDefaultKit()))
				{
					
					getDefaultKit().giveItems(e.getPlayer());
					
				}
				
			}
			else
			{
				return;
			}
		}
		else
		{
			return;
		}
	}
	
	@EventHandler
	public void cancelItems(PlayerPickupItemEvent e)
	{
		e.setCancelled(true);
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
		}.runTaskLater(arcadeManager.getPlugin(), 100L);
		arcadeManager.announceWinner();
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
			arcadeManager.setState(GameState.POSTGAME);
			
		}
		
	}
}
