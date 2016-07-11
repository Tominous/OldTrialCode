package org.jbltd.arcade.managers;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jbltd.arcade.core.events.GameStateChangeEvent;
import org.jbltd.arcade.core.events.UpdateEvent;
import org.jbltd.arcade.core.util.CountdownTask;
import org.jbltd.arcade.core.util.F;
import org.jbltd.arcade.core.util.GameState;
import org.jbltd.arcade.core.util.GameType;
import org.jbltd.arcade.core.util.MapUtil;
import org.jbltd.arcade.core.util.Module;
import org.jbltd.arcade.core.util.UpdateType;
import org.jbltd.arcade.core.util.UtilPacket;
import org.jbltd.arcade.core.util.UtilPlayer;
import org.jbltd.arcade.game.Game;
import org.jbltd.arcade.games.Custom;
import org.jbltd.arcade.games.Runner;
import org.jbltd.arcade.games.Spleef;

public class ArcadeManager extends Module
{
	private GameState _state = GameState.LOADING;
	private Game _game = null;
	public List<GameType> QueuedGames = new ArrayList<GameType>();
	private ScoreboardManager sbm;
	
	public ArcadeManager(JavaPlugin plugin)
	{
		super("Arcade", 0.1, plugin);
		
		sbm = new ScoreboardManager(this);
		
		QueuedGames.add(GameType.RUNNER);
		QueuedGames.add(GameType.SPLEEF);
		QueuedGames.add(GameType.CUSTOM);
	}
	
	public void enable()
	{
		log("Arcade ready.");
	}
	
	public GameState getState()
	{
		return _state;
	}
	
	public void setState(GameState _state)
	{
		this._state = _state;
		Bukkit.getServer().getPluginManager().callEvent(new GameStateChangeEvent(getState(), getGame()));
	}
	
	public Game getGame()
	{
		return _game;
	}
	
	public void setGame(Game _game)
	{
		this._game = _game;
	}
	
	public void announceGame()
	{
		
		if (getGame() == null)
		{
			return;
		}
		
		String mapname = this.getPlugin().getConfig()
				.getString("game." + getGame().getName().replace(" ", "_").toLowerCase() + ".mapname");
		String mapauthor = this.getPlugin().getConfig()
				.getString("game." + getGame().getName().replace(" ", "_").toLowerCase() + ".mapauthor");
		
		for (Player player : Bukkit.getOnlinePlayers())
		{
			for (int i = 0; i < (4 - getGame().getDescription().length); i++)
			{
				UtilPlayer.sendMessage(player, ("   \n"));
			}
			UtilPlayer.sendMessage(player, F.gameLine());
			UtilPlayer.sendMessage(player, (F.GREEN + "Game " + "- " + F.boldYellow + getGame().getName()));
			UtilPlayer.sendMessage(player, ("   \n"));
			for (String s : getGame().getDescription())
			{
				UtilPlayer.sendMessage(player, ("  " + s));
			}
			UtilPlayer.sendMessage(player, ("   \n"));
			
			System.out.println(mapname);
			UtilPlayer.sendMessage(player, F.GREEN + "Map - " + F.boldYellow + mapname + " " + F.GRAY + "created by "
					+ F.boldYellow + mapauthor);
			
			UtilPlayer.sendMessage(player, F.gameLine());
			player.playSound(player.getLocation(), Sound.LEVEL_UP, 5F, 1F);
		}
	}
	
	public void announceWinner()
	{
		
		if (getGame() == null)
		{
			return;
		}
		
		if (getGame().isLive() == false)
		{
			return;
		}
		
		String mapname = this.getPlugin().getConfig()
				.getString("game." + getGame().getName().replace(" ", "_").toLowerCase() + ".mapname");
		String mapauthor = this.getPlugin().getConfig()
				.getString("game." + getGame().getName().replace(" ", "_").toLowerCase() + ".mapauthor");
		
		for (Player player : Bukkit.getOnlinePlayers())
		{
			UtilPlayer.sendMessage(player, F.gameLine());
			UtilPlayer.sendMessage(player, (F.GREEN + "Game " + "- " + F.boldYellow + getGame().getName()));
			UtilPlayer.sendMessage(player, ("   \n"));
			UtilPlayer.sendMessage(player, ("   \n"));
			UtilPlayer.sendMessage(player, F.boldAqua + _game.Winner + F.boldGold + " Won the game!");
			UtilPlayer.sendMessage(player, ("   \n"));
			UtilPlayer.sendMessage(player, ("   \n"));
			UtilPlayer.sendMessage(player, F.GREEN + "Map - " + F.boldYellow + mapname + " " + F.GRAY + "created by "
					+ F.boldYellow + mapauthor);
			
			UtilPlayer.sendMessage(player, F.gameLine());
			
			UtilPacket.sendTitleBarMessage(player, F.boldGreen + _game.Winner, F.BLUE + "Won the game");
			player.playSound(player.getLocation(), Sound.LEVEL_UP, 5F, 1F);
		}
		
	}
	
	// Game Util
	
	public Game createGame(GameType type)
	{
		
		if (type == GameType.RUNNER)
		{
			setGame(new Runner(this));
			
			MapUtil.copyWorld(new File(this.getPlugin().getDataFolder() + "/maps/RunnerMaster/"), new File(this.getPlugin()
					.getServer().getWorldContainer()
					+ "/" + this.getPlugin().getConfig().getString("game.mineplex_runner.gameworld")));
			
			MapUtil.loadWorld(this.getPlugin().getConfig().getString("game.mineplex_runner.gameworld"));
			
			return getGame();
		}
		if (type == GameType.SPLEEF)
		{
			setGame(new Spleef(this));
			
			MapUtil.copyWorld(new File(this.getPlugin().getDataFolder() + "/maps/SpleefMaster/"), new File(this.getPlugin()
					.getServer().getWorldContainer().getAbsolutePath()
					+ "/" + this.getPlugin().getConfig().getString("game.mineplex_spleef.gameworld")));
			
			MapUtil.loadWorld(this.getPlugin().getConfig().getString("game.mineplex_spleef.gameworld"));
			
			return getGame();
		}
		else
		{
			setGame(new Custom(this));
			
			MapUtil.copyWorld(new File(this.getPlugin().getDataFolder() + "/maps/SnowmanMaster/"), new File(this.getPlugin()
					.getServer().getWorldContainer().getAbsolutePath()
					+ "/" + this.getPlugin().getConfig().getString("game.build_a_snowman.gameworld")));
			
			MapUtil.loadWorld(this.getPlugin().getConfig().getString("game.build_a_snowman.gameworld"));
			
			return getGame();
		}
		
	}
	
	public Game pickRandomGame()
	{
		
		if (QueuedGames.isEmpty())
		{
			QueuedGames.add(GameType.RUNNER);
			QueuedGames.add(GameType.SPLEEF);
			QueuedGames.add(GameType.CUSTOM);
		}
		
		switch (QueuedGames.size())
		{
			case 1:
			QueuedGames.remove(GameType.RUNNER);
			System.out.println("Created Runner");
			return createGame(GameType.RUNNER);
			
			case 2:
			System.out.println("Created Spleef");
			QueuedGames.remove(GameType.SPLEEF);
			return createGame(GameType.SPLEEF);
			
			case 3:
			System.out.println("Created Custom");
			QueuedGames.remove(GameType.CUSTOM);
			return createGame(GameType.CUSTOM);
		}
		return null;
		
	}
	
	public void continueCycle()
	{
		
		_game = pickRandomGame();
		_game.setCanCountdown(true);
		_game.start();
		
		setState(GameState.INGAME);
		
	}
	
	public void sendToLobbySpawn(Player player)
	{
		
		String world = getPlugin().getConfig().getString("game.lobbyworld");
		int x = getPlugin().getConfig().getInt("game.lobbyspawnx");
		int y = getPlugin().getConfig().getInt("game.lobbyspawny");
		int z = getPlugin().getConfig().getInt("game.lobbyspawnz");
		
		Location location = new Location(Bukkit.getWorld(world), x, y, z);
		
		player.teleport(location);
		
	}
	
	public void sendToGameSpawn(Player player)
	{
		
		String key = "game." + getGame().getName().toLowerCase().replace(" ", "_");
		
		String world = getPlugin().getConfig().getString(key + ".gameworld");
		int x = getPlugin().getConfig().getInt(key + ".gamespawnx");
		int y = getPlugin().getConfig().getInt(key + ".gamespawny");
		int z = getPlugin().getConfig().getInt(key + ".gamespawnz");
		
		Location location = new Location(Bukkit.getWorld(world), x, y, z);
		
		player.teleport(location);
		
	}
	
	public void sendToSpectatorSpawn(Player player)
	{
		String key = "game." + getGame().getName().toLowerCase().replace(" ", "_");
		
		String world = getPlugin().getConfig().getString(key + ".gameworld");
		int x = getPlugin().getConfig().getInt(key + ".specspawnx");
		int y = getPlugin().getConfig().getInt(key + ".specspawny");
		int z = getPlugin().getConfig().getInt(key + ".specspawnz");
		
		Location location = new Location(Bukkit.getWorld(world), x, y, z);
		
		player.teleport(location);
		
	}
	
	// listeners
	
	@EventHandler
	public void joinStarting(PlayerLoginEvent e)
	{
		
		if (getState() == GameState.PREGAME)
		{
			e.disallow(Result.KICK_OTHER, F.error("Game", "Sorry! You missed the game! Join back next round."));
		}
		
		if (getState() == GameState.INGAME)
		{
			e.disallow(Result.KICK_OTHER, F.error("Game", "Sorry! You missed the game! Join back next round."));
		}
		
		if (getState() == GameState.POSTGAME)
		{
			
			e.disallow(Result.KICK_OTHER, F.error("Game", "This game is currently restarting!"));
		}
	}
	
	@EventHandler
	public void onJoinEnough(PlayerJoinEvent e) throws Exception
	{
		if (_game.isLive() == true)
		{
			return;
		}
		
		sbm.updateLobbyScoreboard(getGame().getName());
		sendToLobbySpawn(e.getPlayer());
		
		if (Bukkit.getOnlinePlayers().size() == getPlugin().getConfig().getInt("game.minstart"))
		{
			for (Player all : Bukkit.getOnlinePlayers())
			{
				if (_game.Countdown() == false)
				{
					UtilPlayer.sendMessage(all, F.boldGold + "Game starting in 30 seconds");
					_game.setCanCountdown(true);
				}
			}
			
		}
		if (Bukkit.getOnlinePlayers().size() == getPlugin().getConfig().getInt("game.maxstart"))
		{
			
			_game.setCountdown(10);
			for (Player all : Bukkit.getOnlinePlayers())
			{
				UtilPlayer.sendMessage(all, F.GOLD + "Max players reached. Countdown shortened");
			}
		}
		
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e)
	{
		
		if (Bukkit.getOnlinePlayers().size() == 0)
		{
			if (_game.isLive() == true)
			{
				Bukkit.shutdown();
			}
			else
			{
				return;
			}
		}
		
	}
	
	@EventHandler
	public void onGSChange(GameStateChangeEvent e) throws Exception
	{
		
		if (Bukkit.getOnlinePlayers().size() <= 1)
		{
			for (Player all : Bukkit.getOnlinePlayers())
			{
				
				try
				{
					all.kickPlayer(F.error("Game", "Not enough players to continue game cycle. Server restarting..."));
				}
				finally
				{
					Bukkit.shutdown();
				}
				
			}
		}
		
		if (e.getToState() == GameState.RECRUITING)
		{
			
			sbm.updateLobbyScoreboard(getGame().getName());
			for (Player player : Bukkit.getOnlinePlayers())
			{
				player.setFireTicks(0);
				player.setFallDistance(0F);
				player.removePotionEffect(PotionEffectType.SPEED);
				sendToLobbySpawn(player);
			}
		}
		else if (e.getToState() == GameState.PREGAME)
		{
			sbm.updateGameScoreboard();
			for (Player player : Bukkit.getOnlinePlayers())
			{
				
				player.getInventory().clear();
				player.setLevel(0);
				sendToGameSpawn(player);
				
				if (_game.hasAKit(player))
				{
					_game.getKit(player).assignKit(player);
				}
				else
				{
					_game.setKit(player, _game.getDefaultKit());
					_game.getKit(player).assignKit(player);
				}
				
			}
			announceGame();
			CountdownTask cd = new CountdownTask(this);
			cd.start();
		}
		else if (e.getToState() == GameState.INGAME)
		{
			getGame().registerKits();
		}
		else if (e.getToState() == GameState.POSTGAME)
		{
			for (Player player : Bukkit.getOnlinePlayers())
			{
				player.setHealth(20.0D);
				player.setFoodLevel(20);
			}
			getGame().unregisterKits();
			
		}
		else
		{
			return;
		}
		
	}
	
	@EventHandler
	public void onBreak(BlockBreakEvent e)
	{
		if (this.getGame().isLive() == false)
		{
			e.setCancelled(true);
		}
		else
		{
			return;
		}
	}
	
	@EventHandler
	public void onPlace(BlockPlaceEvent e)
	{
		if (this.getGame().isLive() == false)
		{
			e.setCancelled(true);
		}
		else
		{
			return;
		}
	}
	
	@EventHandler
	public void onDrop(PlayerDropItemEvent e)
	{
		e.setCancelled(true);
	}
	
	@EventHandler
	public void onPickup(PlayerPickupItemEvent e)
	{
		
		if (this.getGame().isLive() == false)
		{
			e.setCancelled(true);
		}
		
	}
	
	@EventHandler
	public void onMobSpawn(CreatureSpawnEvent e)
	{
		if (e.getSpawnReason() != SpawnReason.BUILD_SNOWMAN)
		{
			e.setCancelled(true);
		}
		
	}
	
	@EventHandler
	public void onDeath(PlayerDeathEvent e) throws Exception
	{
		
		Player player = (Player) e.getEntity();
		
		e.setDeathMessage(F.info("Death", F.GREEN + player.getName() + F.GRAY + " met their doom."));
		
		player.setHealth(20.0D);
		player.setFireTicks(0);
		player.setFoodLevel(20);
		player.setSaturation(2F);
		player.getActivePotionEffects().clear();
		player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 30, 0));
		sendToSpectatorSpawn(player);
		
		sbm.updateGameScoreboard();
		
	}
	
	@EventHandler
	public void onPing(ServerListPingEvent e)
	{
		
		if (this.getGame() == null)
		{
			return;
		}
		
		String motdstat1 = "";
		String motdstat2 = "";
		if (this.getState() == GameState.LOADING)
		{
			motdstat1 = F.BLUE + "Loading...";
		}
		else if (this.getState() == GameState.RECRUITING)
		{
			motdstat1 = F.GREEN + "Recruiting | Click to Join";
		}
		else if (this.getState() == GameState.STARTING)
		{
			motdstat1 = F.YELLOW + "Starting | Better Hurry!";
		}
		else if (this.getState() == GameState.PREGAME)
		{
			motdstat1 = F.YELLOW + "Pre-Game | Too late!";
		}
		else if (this.getState() == GameState.INGAME)
		{
			motdstat1 = F.RED + "In-Game | Not Joinable";
		}
		else if (this.getState() == GameState.POSTGAME)
		{
			motdstat1 = F.RED + "Restarting | Not Joinable";
		}
		else
		{
			motdstat1 = "Null";
		}
		
		// then...
		
		if (this.getGame().getType() == GameType.SPLEEF)
		{
			motdstat2 = "Spleef";
		}
		else if (this.getGame().getType() == GameType.RUNNER)
		{
			motdstat2 = "Runner";
		}
		else if (this.getGame().getType() == GameType.CUSTOM)
		{
			motdstat2 = "Build a Snowman";
		}
		else
		{
			motdstat2 = "No Game";
		}
		
		String finalMotd = "";
		StringBuilder sb = new StringBuilder();
		sb.append(motdstat1 + " | " + motdstat2);
		finalMotd = sb.toString();
		
		e.setMotd(finalMotd);
		
	}
	
	@EventHandler
	public void updateCountdown(UpdateEvent e)
	{
		
		if (getGame() == null)
		{
			return;
		}
		
		if (e.getType() == UpdateType.SECOND)
		{
			
			if (getGame().Countdown() == false)
			{
				return;
			}
			
			int realCount = (getGame().getLobbyCountdown() / 20);
			
			if (getGame().getLobbyCountdown() <= 0)
			{
				getGame().setCanCountdown(false);
				return;
			}
			
			if (getGame().Countdown() == true)
			{
				
				getGame().startLobbyCountdown();
			}
			
			if ((realCount % 20) == 0)
			{
				for (Player player : Bukkit.getOnlinePlayers())
				{
					player.setLevel((realCount / 20));
					if (realCount <= 200)
					{
						UtilPacket.sendTitleB7arMessage(player, F.GOLD + "Game Starts In", F.RED + (realCount / 20));
						player.playSound(player.getLocation(), Sound.NOTE_PLING, 5F, 1F);
						
					}
				}
			}
		}
		else
		{
			return;
		}
		
	}
	
	// Game Options
	
	@EventHandler
	public void onVoidDamage(EntityDamageEvent e)
	{
		
		if (this.getGame().isPreparing() == true)
		{
			return;
		}
		
		if (this.getGame().isLive() == true)
		{
			return;
		}
		
		if (e.getCause() == DamageCause.VOID)
		{
			e.setDamage(22.0D);
		}
		
		String world = this.getPlugin().getConfig().getString("lobbyworld");
		
		e.setDamage(0.0D);
		e.setCancelled(true);
		if (e.getEntity().getWorld().equals(world))
		{
			sendToLobbySpawn((Player) e.getEntity());
		}
		return;
		
	}
	
	@EventHandler
	public void onPVP(EntityDamageByEntityEvent e)
	{
		
		if (!(e.getEntity() instanceof Player))
		{
			return;
		}
		if (!(e.getDamager() instanceof Player))
		{
			return;
		}
		
		if (this.getGame().isLive() == false)
		{
			
			e.setDamage(0.0D);
			e.setCancelled(true);
			return;
			
		}
		
	}
	
	@EventHandler
	public void onChatSilenced(AsyncPlayerChatEvent e)
	{
		
		Player player = e.getPlayer();
		if (getState() == GameState.PREGAME)
		{
			e.setCancelled(true);
			UtilPlayer.sendMessage(player, F.error("Chat", "You may not speak now."));
		}
		if (getState() == GameState.POSTGAME)
		{
			e.setCancelled(true);
			UtilPlayer.sendMessage(player, F.error("Chat", "You may not speak now."));
		}
		else
		{
			return;
		}
		
	}
	
}