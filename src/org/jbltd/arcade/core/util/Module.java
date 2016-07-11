package org.jbltd.arcade.core.util;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class Module implements Listener
{
	private String _name = "Modules";
	private double _version;
	private JavaPlugin _plugin;
	
	public Module(String name, double version, JavaPlugin plugin)
	{
		_plugin = plugin;
		_name = name;
		_version = version;
		
		onEnable();
		registerListener(this);
		
	}
	
	public Module()
	{
		
	}
	
	private void onEnable()
	{
		enable();
	}
	
	public JavaPlugin getPlugin()
	{
		return _plugin;
	}
	
	public double getVersion()
	{
		return _version;
	}
	
	public void setVersion(double version)
	{
		this._version = version;
	}
	
	public void enable()
	{
		
	}
	
	public void disable()
	{
		
	}
	
	public void registerListener(Listener listener)
	{
		_plugin.getServer().getPluginManager().registerEvents(listener, _plugin);
	}
	
	public void log(String message)
	{
		System.out.println(F.info(this._name, message));
		
	}
	
}
