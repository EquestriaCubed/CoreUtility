package com.hepolite.coreutility.event.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerExhaustionChangeEvent extends Event implements Cancellable
{
	private final Player player;
	private final float oldExhaustion;
	private float newExhaustion;

	public PlayerExhaustionChangeEvent(Player player, float oldExhaustion, float newExhaustion)
	{
		this.player = player;
		this.oldExhaustion = oldExhaustion;
		setNewExhaustion(newExhaustion);
	}

	/** Returns the player associated with this event */
	public final Player getPlayer()
	{
		return player;
	}

	/** Returns the old exhaustion for the player */
	public final float getOldExhaustion()
	{
		return oldExhaustion;
	}

	/** Returns the new exhaustion for the player */
	public final float getNewExhaustion()
	{
		return newExhaustion;
	}

	/** Sets the new exhaustion for the player */
	public final void setNewExhaustion(float newExhaustion)
	{
		this.newExhaustion = Math.max(0.0f, newExhaustion);
	}

	/** Returns the change in exhaustion for the player */
	public final float getExhaustionChange()
	{
		return newExhaustion - oldExhaustion;
	}

	/** Sets the change in exhaustion for the player */
	public final void setExhaustionChange(float change)
	{
		setNewExhaustion(oldExhaustion + change);
	}

	// //////////////////////////////////////////////////////////

	private static final HandlerList handlers = new HandlerList();
	private boolean isCancelled = false;

	@Override
	public boolean isCancelled()
	{
		return isCancelled;
	}

	@Override
	public void setCancelled(boolean cancel)
	{
		isCancelled = cancel;
	}

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
