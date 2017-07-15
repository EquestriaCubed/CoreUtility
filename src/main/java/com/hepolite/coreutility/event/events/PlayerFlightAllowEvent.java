package com.hepolite.coreutility.event.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerFlightAllowEvent extends Event
{
	private final Player player;
	private boolean allowed;

	public PlayerFlightAllowEvent(Player player, boolean allowed)
	{
		this.player = player;
		this.allowed = allowed;
	}

	/** Returns the player assocated with this event */
	public final Player getPlayer()
	{
		return player;
	}

	/** Returns whether flight should be allowed or not */
	public final boolean isFlightAllowed()
	{
		return allowed;
	}

	/** Sets whether flight should be allowed or not */
	public final void setFligthAllowed(boolean allowed)
	{
		this.allowed = allowed;
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
