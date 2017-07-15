package com.hepolite.coreutility.movement;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.hepolite.coreutility.event.CoreHandler;

public class PlayerMoveDetector extends CoreHandler
{
	private final static Map<UUID, Location> locations = new HashMap<UUID, Location>();
	private final static Map<UUID, Location> oldLocations = new HashMap<UUID, Location>();

	@Override
	public final void onTick(int tick)
	{
		if (tick % 20 != 0)
			return;

		oldLocations.clear();
		oldLocations.putAll(locations);
		locations.clear();
		for (Player player : Bukkit.getOnlinePlayers())
			locations.put(player.getUniqueId(), player.getLocation());
	}

	/** Returns true if the given player moved the prevoious second */
	public final boolean isPlayerMoving(Player player)
	{
		UUID uuid = player.getUniqueId();
		Location old = oldLocations.get(uuid);
		Location current = locations.get(uuid);
		if (old == null || current == null)
			return false;
		return !(old.getWorld().equals(current.getWorld()) && old.getBlockX() == current.getBlockX() && old.getBlockY() == current.getBlockY() && old.getBlockZ() == current.getBlockZ());
	}

	/** Returns the movement type of the given player */
	public final MovementType getMovementType(Player player)
	{
		if (player.isFlying())
		{
			if (isPlayerMoving(player))
				return MovementType.FLYING;
			else
				return MovementType.HOVERING;
		}
		else if (player.isGliding())
			return MovementType.GLIDING;
		else if (player.getLocation().getBlock().isLiquid())
		{
			if (isPlayerMoving(player))
				return MovementType.SWIMMING;
			else
				return MovementType.FLOATING;
		}
		else if (player.isSprinting())
			return MovementType.RUNNING;
		else if (player.isSneaking())
			return MovementType.SNEAKING;
		else if (isPlayerMoving(player))
			return MovementType.WALKING;
		else
			return MovementType.STANDING;
	}
}
