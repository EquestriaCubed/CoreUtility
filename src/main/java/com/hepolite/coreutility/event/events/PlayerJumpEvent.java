package com.hepolite.coreutility.event.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerJumpEvent extends Event
{
	private final Player player;

	public PlayerJumpEvent(Player player)
	{
		this.player = player;
	}

	/** Returns the player */
	public final Player getPlayer()
	{
		return player;
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
