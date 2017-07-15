package com.hepolite.coreutility.movement;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import com.hepolite.coreutility.event.CoreHandler;
import com.hepolite.coreutility.plugin.CorePlugin;
import com.hepolite.coreutility.settings.Settings;

public class MovementHandler extends CoreHandler
{
	private final Settings settings;

	private final PlayerSpeedUpdater playerSpeedUpdater = new PlayerSpeedUpdater();
	private final PlayerFlightUpdater playerFlightUpdater = new PlayerFlightUpdater();
	private final PlayerJumpDetector playerJumpDetector = new PlayerJumpDetector();
	private final PlayerImpactDetector playerImpactDetector = new PlayerImpactDetector();
	private final PlayerMoveDetector playerMoveDetector = new PlayerMoveDetector();

	public MovementHandler(CorePlugin plugin)
	{
		this.settings = new MovementSettings(plugin);
		plugin.addHandler(playerSpeedUpdater);
		plugin.addHandler(playerFlightUpdater);
		plugin.addHandler(playerJumpDetector);
		plugin.addHandler(playerImpactDetector);
		plugin.addHandler(playerMoveDetector);
	}

	@Override
	public final void onRestart()
	{
		settings.load();
	}

	// ///////////////////////////////////////////////////////////////////////

	/** Returns the movement type of the given player */
	public final MovementType getMovementType(Player player)
	{
		return playerMoveDetector.getMovementType(player);
	}

	/** Returns true if the given player has moved */
	public final boolean isPlayerMoving(Player player)
	{
		return playerMoveDetector.isPlayerMoving(player);
	}

	/** Sets the distance the given entity has fallen */
	public final void setFallDistance(Entity entity, float distance)
	{
		playerImpactDetector.setFallDistance(entity, distance);
	}

	/** Returns the distance the given entity has fallen */
	public final float getFallDistance(Entity entity)
	{
		return playerImpactDetector.getFallDistance(entity);
	}

	/** Returns the fall damage the given player will be if they were to hit the ground right now */
	public final float getFallDamage(Player player)
	{
		return getFallDamage(player, getFallDistance(player));
	}

	/** Calculates the fall damage the player will take, falling from the given height */
	public final float getFallDamage(Player player, float fallDistance)
	{
		return playerImpactDetector.getFallDamage(player, fallDistance);
	}
}
