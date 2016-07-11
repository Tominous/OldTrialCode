package org.jbltd.arcade.game;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.jbltd.arcade.core.util.CountdownTask;
import org.jbltd.arcade.core.util.GameState;
import org.jbltd.arcade.core.util.GameType;
import org.jbltd.arcade.kit.Kit;
import org.jbltd.arcade.managers.ArcadeManager;

public abstract class Game implements Listener
{
	
	public ArcadeManager arcadeManager;
	private String _name;
	private String[] _description;
	private GameType _type;
	public Kit[] Kits;
	public String Winner = "Nobody";
	private int _cd;
	private int _lcd;
	public boolean Countdown = false;
	public int cdId;
	public HashMap<Player, Kit> PlayerKits = new HashMap<Player, Kit>();
	private Kit _defaultKit;
	
	public Game(ArcadeManager manager, String name, String[] description, Kit[] kits, GameType type)
	{
		
		this.arcadeManager = manager;
		this.setName(name);
		this.setDescription(description);
		this.setType(type);
		this.Kits = kits;
		this.setDefaultKit(_defaultKit);
		this.setCountdown(_cd);
		this.setLobbyCountdown(_lcd);
		
		registerListener(this);
		
	}
	
	// Util
	public void registerListener(Listener listener)
	{
		Bukkit.getServer().getPluginManager().registerEvents(listener, arcadeManager.getPlugin());
	}
	
	// Game info
	public String getName()
	{
		return _name;
	}
	
	public void setName(String _name)
	{
		this._name = _name;
	}
	
	public String[] getDescription()
	{
		return _description;
	}
	
	public void setDescription(String[] _description)
	{
		this._description = _description;
	}
	
	public GameType getType()
	{
		return _type;
	}
	
	public void setType(GameType _type)
	{
		this._type = _type;
	}
	
	public boolean Countdown()
	{
		return Countdown;
	}
	
	public void setCanCountdown(boolean endis)
	{
		this.Countdown = endis;
	}
	
	public int getLobbyCountdown()
	{
		return _lcd * 20;
	}
	
	public void setLobbyCountdown(int countdown)
	{
		this._lcd = countdown * 20;
	}
	
	public int getCountdown()
	{
		return _cd;
	}
	
	public void setCountdown(int countdown)
	{
		this._cd = countdown;
	}
	
	public boolean isLive()
	{
		if (arcadeManager.getState() == GameState.INGAME)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public boolean isPreparing()
	{
		if (arcadeManager.getState() == GameState.PREGAME)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	// Kits
	
	public boolean hasAKit(Player player)
	{
		if (PlayerKits.containsKey(player))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public boolean hasThisKit(Player player, Kit kit)
	{
		if (PlayerKits.get(player) == kit)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public Kit[] getAllKits()
	{
		return Kits;
	}
	
	public void registerKits()
	{
		
		for (int i = 0; i < Kits.length; i++)
		{
			Kit kit = Kits[i];
			
			Bukkit.getServer().getPluginManager().registerEvents(kit, arcadeManager.getPlugin());
		}
		
	}
	
	public void unregisterKits()
	{
		Kit[] arrayOfKit;
		int j = (arrayOfKit = this.Kits).length;
		for (int i = 0; i < j; i++)
		{
			Kit kit = arrayOfKit[i];
			
			HandlerList.unregisterAll(kit);
			
		}
	}
	
	public Kit getKit(Player player)
	{
		return PlayerKits.get(player);
	}
	
	public void setKit(Player player, Kit kit)
	{
		
		PlayerKits.put(player, kit);
		
	}
	
	public Kit getDefaultKit()
	{
		return _defaultKit;
	}
	
	public void setDefaultKit(Kit _defaultKit)
	{
		this._defaultKit = _defaultKit;
	}
	
	public void removeKit(Player player)
	{
		if (PlayerKits.containsKey(player))
		{
			PlayerKits.remove(player);
		}
	}
	
	// Countdown
	
	public void startLobbyCountdown()
	{
		
		if (Countdown == false)
		{
			return;
		}
		
		else
		{
			
			_lcd--;
			
			if (getLobbyCountdown() == 0)
			{
				arcadeManager.setState(GameState.PREGAME);
			}
		}
	}
	
	public void startGameCountdown()
	{
		CountdownTask _cd = new CountdownTask(arcadeManager);
		_cd.start();
	}
	
	public void forceStopCountdown()
	{
		CountdownTask _cd = new CountdownTask(arcadeManager);
		
		if (_cd.isRunning())
		{
			_cd.stop();
			_cd.reset();
		}
	}
	
	public abstract void start();
	
	public abstract void end();
	
	public abstract void endCheck();
	
}
