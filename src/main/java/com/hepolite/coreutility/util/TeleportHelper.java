package com.hepolite.coreutility.util;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

public class TeleportHelper
{
	private static final Random random = new Random();

	/** Transforms the location to a scaled location */
	public static final Location scale(Location location, float fromScaleH, float fromScaleV,
			float toScaleH, float toScaleV)
	{
		double ratioH = fromScaleH == 0.0f ? 1.0f : (toScaleH / fromScaleH);
		double ratioV = fromScaleV == 0.0f ? 1.0f : (toScaleV / fromScaleV);
		return new Location(location.getWorld(), ratioH * location.getX(),
				ratioV * location.getY(), ratioH * location.getZ());
	}

	/** Returns a safe location in the vicinity of the given location; returns null if no safe location could be found */
	public static final Location findSafe(Location location)
	{
		World world = location.getWorld();
		int x = location.getBlockX();
		int y = location.getBlockY();
		int z = location.getBlockZ();

		int recursionsLeft = 20;
		outer: while (--recursionsLeft > 0)
		{
			for (int side = 0; side < 2; side++)
			{
				boolean up = (side == 0);
				for (int i = 0; i < 150; i++)
				{
					int Y = i * (up ? 1 : -1);
					Block lowerBlock = world.getBlockAt(x, y - 1 + Y, z);
					Block currentBlock = world.getBlockAt(x, y + Y, z);
					Block upperBlock = world.getBlockAt(x, y + 1 + Y, z);

					if (lowerBlock.getType().isSolid() && currentBlock.getType() == Material.AIR
							&& upperBlock.getType() == Material.AIR)
					{
						y += Y;
						break outer;
					}
				}
			}
			x += random.nextInt(17) / 2 - 8;
			z += random.nextInt(17) / 2 - 8;
		}
		return recursionsLeft > 0 ? new Location(world, (double) x + 0.5, (double) y + 0.5,
				(double) z + 0.5) : null;
	}
}
