package org.jbltd.arcade.core;

import java.sql.SQLException;

import org.bukkit.plugin.java.JavaPlugin;
import org.jbltd.arcade.core.listeners.Chat;
import org.jbltd.arcade.core.listeners.Commands;
import org.jbltd.arcade.core.listeners.JoinQuit;
import org.jbltd.arcade.core.listeners.World;
import org.jbltd.arcade.core.sql.Manager;
import org.jbltd.arcade.core.util.F;
import org.jbltd.arcade.core.util.GameState;
import org.jbltd.arcade.core.util.Updater;
import org.jbltd.arcade.kit.KitManager;
import org.jbltd.arcade.managers.ArcadeManager;

public class Main extends JavaPlugin
{
	private ArcadeManager _manager;
	public Manager MySQL = new Manager(this);
	
	@Override
	public void onEnable()
	{
		
		_manager = new ArcadeManager(this);
		_manager.setState(GameState.LOADING);
		
		new Updater(this);
		
		try
		{
			this.MySQL.setupDB();
			getLogger().info(org.jbltd.arcade.core.util.F.database("Connected!"));
		}
		catch (ClassNotFoundException | SQLException e)
		{
			getLogger().info(F.databaseError("Unable to connect to MySQL database."));
			e.printStackTrace();
		}
		
		_manager.pickRandomGame();
		_manager.QueuedGames.remove(_manager.getGame());
		
		registerListeners();
		getConfig().addDefault("game.lobbyworld", "mineplex");
		getConfig().addDefault("game.lobbyspawnx", 12);
		getConfig().addDefault("game.lobbyspawny", 103);
		getConfig().addDefault("game.lobbyspawnz", -23);
		getConfig().addDefault("game.minstart", 2);
		getConfig().addDefault("game.maxstart", 5);
		getConfig().options().copyDefaults(true);
		saveConfig();
		getLogger().info("Plugin enabled.");
		_manager.setState(GameState.RECRUITING);
	}
	
	public void onDisable()
	{
		saveDefaultConfig();
		
		getLogger().info("Plugin disabled.");
	}
	
	public void registerListeners()
	{
		
		getServer().getPluginManager().registerEvents(new JoinQuit(), this);
		getServer().getPluginManager().registerEvents(new Chat(), this);
		getServer().getPluginManager().registerEvents(new World(), this);
		getServer().getPluginManager().registerEvents(new Commands(), this);
		getServer().getPluginManager().registerEvents(new KitManager(this._manager), this);
	}
	
}
