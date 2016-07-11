package org.jbltd.arcade.core.util;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.jbltd.arcade.managers.ArcadeManager;

public class CountdownTask implements Runnable
{
	
	private ArcadeManager _manager;
	private int _time;
	
	private int _id;
	
	public CountdownTask(ArcadeManager manager)
	{
		
		_manager = manager;
		
	}
	
	@Override
	public void run()
	{
		if (_manager.getState() == GameState.RECRUITING)
		{
			return;
		}
		if (_manager.getState() == GameState.INGAME)
		{
			stop();
			return;
		}
		if (_manager.getState() == GameState.POSTGAME)
		{
			stop();
			return;
		}
		_time = _manager.getGame().getCountdown();
		_time--;
		_manager.getGame().setCountdown(_time);
		for (Player player : Bukkit.getOnlinePlayers())
		{
			UtilPacket.sendActionBarMessage(player, F.GREEN + "Game Start > " + F.RED + _time);
			player.playSound(player.getLocation(), Sound.WOOD_CLICK, 3F, 3F);
		}
		if (_time <= 0)
		{
			stop();
			reset();
			_manager.getGame().start();
			_manager.setState(GameState.INGAME);
			for (Player player : Bukkit.getOnlinePlayers())
			{
				player.playSound(player.getLocation(), Sound.NOTE_PLING, 5F, 1F);
			}
		}
	}
	
	public void stop()
	{
		if (isRunning())
		{
			Bukkit.getScheduler().cancelTask(_id);
		}
	}
	
	public void start()
	{
		if (!isRunning())
		{
			this._id = Bukkit.getScheduler().runTaskTimer(_manager.getPlugin(), this, 1, 20).getTaskId();
		}
	}
	
	public boolean isRunning()
	{
		return Bukkit.getScheduler().isQueued(_id);
	}
	
	public void reset()
	{
		_time = 0;
	}
	
}
