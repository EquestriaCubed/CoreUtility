package com.hepolite.coreutility.movement;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.hepolite.coreutility.apis.damage.Damage;
import com.hepolite.coreutility.apis.damage.DamageAPI;
import com.hepolite.coreutility.apis.damage.DamageType;
import com.hepolite.coreutility.event.CoreHandler;
import com.hepolite.coreutility.event.events.PlayerImpactGroundEvent;

public class PlayerImpactDetector extends CoreHandler
{
	private final Map<UUID, Float> fallDistances = new HashMap<UUID, Float>();

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public final void onPlayerJoin(PlayerJoinEvent event)
	{
		fallDistances.put(event.getPlayer().getUniqueId(), 0.0f);
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public final void onPlayerQuit(PlayerQuitEvent event)
	{
		fallDistances.remove(event.getPlayer().getUniqueId());
	}

	@Override
	public final void onTick(int tick)
	{
		for (Player player : Bukkit.getOnlinePlayers())
		{
			float currentFallDistance = player.getFallDistance();
			float previousFallDistance = getFallDistance(player);
			setFallDistance(player, currentFallDistance);

			if (previousFallDistance > 0.0f && currentFallDistance <= 0.0f)
			{
				Block block = player.getLocation().getBlock();
				if (block.isLiquid())
				{
					previousFallDistance = Math.max(0.0f, (1.0f - MovementSettings.WATER_DRAG_FRACTION) * previousFallDistance - MovementSettings.WATER_DRAG_FIXED);
					if (block.getRelative(BlockFace.DOWN).getType().isSolid() && previousFallDistance > 0.0f)
					{
						post(new PlayerImpactGroundEvent(player, previousFallDistance));
						setFallDistance(player, 0.0f);
					}
					else
						setFallDistance(player, previousFallDistance);
				}
				else
				{
					if (block.getType() == Material.AIR)
						block = block.getRelative(BlockFace.DOWN);

					Material type = block.getType();
					if (type != Material.LADDER && type != Material.VINE && !player.isFlying())
						post(new PlayerImpactGroundEvent(player, previousFallDistance));
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public final void onPlayerImpactGround(PlayerImpactGroundEvent event)
	{
		Player player = event.getPlayer();
		float damage = getFallDamage(player, event.getFallDistance());
		if (damage > 0.0f)
			DamageAPI.damage(player, new Damage(DamageType.FALL, damage));
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public final void onPlayerTakeDamage(EntityDamageEvent event)
	{
		if (event.getEntityType() == EntityType.PLAYER && event.getCause() == DamageCause.FALL)
			event.setCancelled(true);
	}

	/** Sets the fall distance for the given player */
	public final void setFallDistance(Entity entity, float distance)
	{
		if (entity instanceof Player)
			fallDistances.put(entity.getUniqueId(), distance);
		entity.setFallDistance(distance);
	}

	/** Returns the fall distance for the given player */
	public final float getFallDistance(Entity entity)
	{
		if (entity instanceof Player)
			return fallDistances.get(entity.getUniqueId());
		return entity.getFallDistance();
	}

	/** Calculates the fall damage the player will take, falling from the given height */
	public final float getFallDamage(Player player, float fallDistance)
	{
		Block block = player.getLocation().getBlock();
		for (int i = -2; i <= 1; ++i)
			if (block.getRelative(0, i, 0).getType() == Material.SLIME_BLOCK)
				return 0.0f;

		float v = (float) Math.sqrt(2.0 * 0.08 * Math.max(fallDistance, 0.0f));
		final float minV = (float) Math.sqrt(2.0 * 0.08 * MovementSettings.DAMAGE_FALL_START);
		final float maxV = (float) Math.sqrt(2.0 * 0.08 * MovementSettings.DAMAGE_FALL_END);
		return (v - minV) / (maxV - minV) * MovementSettings.DAMAGE_FALL_SCALE;
	}
}
