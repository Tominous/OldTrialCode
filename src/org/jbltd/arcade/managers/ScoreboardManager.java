package org.jbltd.arcade.managers;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.jbltd.arcade.core.sql.Manager;
import org.jbltd.arcade.core.util.F;
import org.jbltd.arcade.core.util.Module;
import org.jbltd.arcade.core.util.UtilLevel;

public class ScoreboardManager extends Module
{
	
	public ScoreboardManager(ArcadeManager manager)
	{
		
	}
	
	public void enable()
	{
		log("Enabled.");
	}
	
	public void updateLobbyScoreboard(String data) throws Exception
	{
		
		for (Player player : Bukkit.getOnlinePlayers())
		{
			
			Scoreboard sb = Bukkit.getScoreboardManager().getNewScoreboard();
			Objective obj = sb.registerNewObjective("info", "dummy");
			obj.setDisplaySlot(DisplaySlot.SIDEBAR);
			obj.setDisplayName(F.boldAqua + "Mineplex Trial");
			
			int lines = 15;
			
			Score white = obj.getScore(F.BLUE.toString());
			white.setScore(lines--);
			
			Score status = obj.getScore(F.boldGreen + "Next Game:");
			status.setScore(lines--);
			
			Score statusa = obj.getScore(F.GOLD + data);
			statusa.setScore(lines--);
			
			Score whites = obj.getScore(F.GREEN.toString());
			whites.setScore(lines--);
			
			Score rank = obj.getScore(F.boldBlue + "Rank:");
			rank.setScore(lines--);
			
			switch (Manager.getRank(player.getUniqueId().toString()))
			{
			
				case "DEFAULT":
				Score rankad = obj.getScore(F.boldWhite + "NO RANK");
				rankad.setScore(lines--);
					break;
				case "TESTER":
				Score rankat = obj.getScore(F.boldAqua + "TESTER");
				rankat.setScore(lines--);
					break;
				case "OWNER":
				Score rankao = obj.getScore(F.boldDR + "OWNER");
				rankao.setScore(lines--);
					break;
			
			}
			
			Score blank1 = obj.getScore(F.RED.toString());
			blank1.setScore(lines--);
			
			Score wins = obj.getScore(F.boldRed + "Total Wins:");
			wins.setScore(lines--);
			
			Score winsa = obj.getScore(F.GOLD + +Manager.getWins(player.getUniqueId().toString()));
			winsa.setScore(lines--);
			
			Score blank2 = obj.getScore(F.BLACK.toString());
			blank2.setScore(lines--);
			
			Score level = obj.getScore(F.boldPurple + "Level:");
			level.setScore(lines--);
			
			Score levela = obj.getScore(F.GOLD + UtilLevel.getLevel(player) + "");
			levela.setScore(lines--);
			
			Score blank3 = obj.getScore(F.boldBlack.toString());
			blank3.setScore(lines--);
			
			player.setScoreboard(sb);
			
		}
	}
	
	public void updateGameScoreboard() throws Exception
	{
		
		for (Player player : Bukkit.getOnlinePlayers())
		{
			Scoreboard sb = Bukkit.getScoreboardManager().getNewScoreboard();
			Objective obj = sb.registerNewObjective("info", "dummy");
			obj.setDisplaySlot(DisplaySlot.SIDEBAR);
			obj.setDisplayName(F.boldAqua + "Game");
			
			player.setScoreboard(sb);
		}
	}
}
