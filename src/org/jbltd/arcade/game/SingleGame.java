package org.jbltd.arcade.game;

import java.util.List;

import org.jbltd.arcade.core.util.GameType;
import org.jbltd.arcade.kit.Kit;
import org.jbltd.arcade.managers.ArcadeManager;

public abstract class SingleGame extends Game
{
	
	public SingleGame(ArcadeManager manager, String name, String[] description, Kit[] kits, GameType type)
	{
		super(manager, name, description, kits, type);
		// TODO Auto-generated constructor stub
	}
	
	public abstract List<String> GetLivePlayers();
	
	public abstract List<String> GetDeadPlayers();
	
	@Override
	public void start() {}
	
	@Override
	public void end() {}
	
}
