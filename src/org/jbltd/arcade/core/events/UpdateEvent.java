package org.jbltd.arcade.core.events;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jbltd.arcade.core.util.UpdateType;

public class UpdateEvent extends Event implements Cancellable
{
	
	private final UpdateType _ut;
	
	private static final HandlerList _handlers = new HandlerList();
	
	public UpdateEvent(UpdateType type)
	{
		_ut = type;
	}
	
	@Override
	public boolean isCancelled()
	{
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public void setCancelled(boolean arg0)
	{
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public HandlerList getHandlers()
	{
		// TODO Auto-generated method stub
		return _handlers;
	}
	
	public static HandlerList getHandlerList()
	{
		return _handlers;
	}
	
	public UpdateType getType()
	{
		return this._ut;
		
	}
	
}
