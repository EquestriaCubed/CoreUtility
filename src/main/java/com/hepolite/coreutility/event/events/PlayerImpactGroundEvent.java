package com.hepolite.coreutility.event.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerImpactGroundEvent extends Event
{
	private final Player player;
	private final float fallDistance;

	public PlayerImpactGroundEvent(Player player, float fallDistance)
	{
		this.player = player;
		this.fallDistance = fallDistance;
	}

	/** Returns the player */
	public final Player getPlayer()
	{
		return player;
	}
	
	/** Returns the distance the player has been falling */
	public final float getFallDistance()
	{
		return fallDistance;
	}

	// //////////////////////////////////////////////////////////

	private static final HandlerList handlers = new HandlerList();

	@Override
	public HandlerList getHandlers()
	{
		return handlers;
	}

	public static HandlerList getHandlerList()
	{
		return handlers;
	}
}
