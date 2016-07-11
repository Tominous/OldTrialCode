package org.jbltd.arcade.core.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jbltd.arcade.core.util.GameState;
import org.jbltd.arcade.game.Game;

public class GameStateChangeEvent extends Event
{
	
	public static final HandlerList _handlers = new HandlerList();
	
	private GameState _toState;
	private Game _game;
	
	public GameStateChangeEvent(GameState tostate, Game game)
	{
		this._toState = tostate;
		this._game = game;
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
	
	public GameState getToState()
	{
		return _toState;
	}
	
	public void setToState(GameState _tostate)
	{
		this._toState = _tostate;
	}
	
	public Game getCurrentGame()
	{
		return _game;
	}
	
}
