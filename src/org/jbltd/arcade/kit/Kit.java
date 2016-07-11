package org.jbltd.arcade.kit;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.jbltd.arcade.core.util.F;
import org.jbltd.arcade.core.util.UtilPlayer;
import org.jbltd.arcade.managers.ArcadeManager;

public abstract class Kit implements Listener
{
	
	private String _displayName;
	private String[] _kitDescription;
	private Material _displayMaterial;
	private int _requiredLevel;
	
	// ONLY EVER MADE 1 ARCADE MANAGER, and thats in main, if you need it here
	// you need to pass it in
	
	private ArcadeManager _arcadeManager;
	
	public Kit(String _displayName, String[] _kitDescription, Material displayItem, int requiredLevel, ArcadeManager arcadeManager)
	{
		this._displayName = _displayName;
		this._kitDescription = _kitDescription;
		this._displayMaterial = displayItem;
		this._requiredLevel = requiredLevel;
		_arcadeManager = arcadeManager;
		
		Bukkit.getServer().getPluginManager().registerEvents(this, _arcadeManager.getPlugin());
	}
	
	public String getName()
	{
		return this._displayName;
	}
	
	public String[] getDescription()
	{
		return this._kitDescription;
	}
	
	public Material getDisplayMaterial()
	{
		return _displayMaterial;
	}
	
	public int getRequired()
	{
		return _requiredLevel;
	}
	
	public void displayDesciption(Player player)
	{
		
		for (int i = 0; i < (4 - getDescription().length); i++)
		{
			UtilPlayer.sendMessage(player, ("   \n"));
		}
		UtilPlayer.sendMessage(player, F.gameLine());
		UtilPlayer.sendMessage(player, (F.GREEN + "Kit " + "- " + F.boldWhite + getName()));
		UtilPlayer.sendMessage(player, ("   \n"));
		
		for (String s : getDescription())
		{
			UtilPlayer.sendMessage(player, F.GRAY + "  " + s);
		}
		UtilPlayer.sendMessage(player, "   \n");
		UtilPlayer.sendMessage(player, F.gameLine());
		
		UtilPlayer.sendMessage(player, F.info("Kit", "You equipped " + F.boldYellow + getName() + " Kit"));
		player.playSound(player.getLocation(), Sound.ORB_PICKUP, 5F, 1F);
		
	}
	
	public boolean hasKit(Player player)
	{
		return false;
		
	}
	
	public abstract void giveItems(Player player);
	
	public void assignKit(Player player)
	{
		
		giveItems(player);
		
	}
	
	// NMS Listeners
	
}
