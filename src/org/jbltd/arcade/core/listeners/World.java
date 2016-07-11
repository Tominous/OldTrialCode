package org.jbltd.arcade.core.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

public class World implements Listener
{
	
	@EventHandler
	public void onFade(BlockFadeEvent e)
	{
		e.setCancelled(true);
	}
	
	@EventHandler
	public void onWeatherChange(WeatherChangeEvent e)
	{
		
		e.setCancelled(true);
	}
	
	@EventHandler
	public void onFreeze(BlockFormEvent e)
	{
		e.setCancelled(true);
	}
	
}
