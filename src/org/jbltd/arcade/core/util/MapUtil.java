package org.jbltd.arcade.core.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;

public class MapUtil
{
	@SuppressWarnings("unused")
	private static World _world;
	
	public static void copyWorld(File sourceLocation, File targetLocation)
	{
		
		try
		{
			System.out.println("Map> Copying world for current game...");
			if (sourceLocation.isDirectory())
			{
				if (!targetLocation.exists())
				{
					targetLocation.mkdir();
				}
				
				String[] children = sourceLocation.list();
				for (int i = 0; i < children.length; i++)
				{
					
					copyWorld(new File(sourceLocation, children[i]), new File(targetLocation, children[i]));
				}
			}
			else
			{
				
				InputStream in = new FileInputStream(sourceLocation);
				OutputStream out = new FileOutputStream(targetLocation);
				
				byte[] buf = new byte[1024];
				int len;
				
				while ((len = in.read(buf)) > 0)
				{
					out.write(buf, 0, len);
				}
				in.close();
				out.close();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static void loadWorld(String world)
	{
		if (!world.equals(null))
		{
			Bukkit.getServer().createWorld(new WorldCreator(world));
		}
		
	}
	
	public static void unloadWorld(World world)
	{
		_world = Bukkit.getWorld("");
		if (!world.equals(null))
		{
			Bukkit.getServer().unloadWorld(world, true);
		}
	}
	
	public static boolean deleteWorld(File path)
	{
		if (path.exists())
		{
			File files[] = path.listFiles();
			for (int i = 0; i < files.length; i++)
			{
				if (files[i].isDirectory())
				{
					deleteWorld(files[i]);
				}
				else
				{
					System.out.println("Map> Deleted " + files[i].getName());
					files[i].delete();
				}
			}
		}
		return (path.delete());
	}
	
}
