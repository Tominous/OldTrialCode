package org.jbltd.arcade.core.util;

import net.minecraft.server.v1_8_R1.ChatSerializer;
import net.minecraft.server.v1_8_R1.EnumTitleAction;
import net.minecraft.server.v1_8_R1.IChatBaseComponent;
import net.minecraft.server.v1_8_R1.PacketPlayOutChat;
import net.minecraft.server.v1_8_R1.PacketPlayOutTitle;

import org.bukkit.craftbukkit.v1_8_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class UtilPacket
{
	
	public static void sendActionBarMessage(Player player, String message)
	{
		
		IChatBaseComponent cbc = ChatSerializer.a("{\"text\": \"" + message + "\"}");
		
		PacketPlayOutChat ppoc = new PacketPlayOutChat(cbc, (byte) 2);
		
		((CraftPlayer) player).getHandle().playerConnection.sendPacket(ppoc);
		
	}
	
	public static void sendTitleBarMessage(Player player, String title, String sub)
	{
		
		IChatBaseComponent chatTitle = ChatSerializer.a("{\"text\": \"" + title + "\"}");
		IChatBaseComponent chatSub = ChatSerializer.a("{\"text\": \"" + sub + "\"}");
		
		PacketPlayOutTitle pt = new PacketPlayOutTitle(EnumTitleAction.TITLE, chatTitle);
		PacketPlayOutTitle pst = new PacketPlayOutTitle(EnumTitleAction.SUBTITLE, chatSub);
		
		((CraftPlayer) player).getHandle().playerConnection.sendPacket(pt);
		((CraftPlayer) player).getHandle().playerConnection.sendPacket(pst);
		
	}
	
}
