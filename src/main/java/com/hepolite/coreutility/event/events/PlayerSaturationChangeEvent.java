package com.hepolite.coreutility.event.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerSaturationChangeEvent extends Event implements Cancellable
{
	private final Player player;
	private final float maxSaturation;
	private final float oldSaturation;
	private float newSaturation;

	public PlayerSaturationChangeEvent(Player player, float maxSaturation, float oldSaturation, float newSaturation)
	{
		this.player = player;
		this.maxSaturation = maxSaturation;
		this.oldSaturation = oldSaturation;
		setNewSaturation(newSaturation);
	}

	/** Returns the player associated with this event */
	public final Player getPlayer()
	{
		return player;
	}

	/** Returns the maximum saturation for the player */
	public final float getMaxSaturation()
	{
		return maxSaturation;
	}

	/** Returns the old saturation for the player */
	public final float getOldSaturation()
	{
		return oldSaturation;
	}

	/** Returns the new saturation for the player */
	public final float getNewSaturation()
	{
		return newSaturation;
	}

	/** Sets the new saturation for the player */
	public final void setNewSaturation(float newSaturation)
	{
		this.newSaturation = Math.min(maxSaturation, newSaturation);
	}

	/** Returns the change in saturation for the player */
	public final float getSaturationChange()
	{
		return newSaturation - oldSaturation;
	}

	/** Sets the change in saturation for the player */
	public final void setSaturationChange(float change)
	{
		setNewSaturation(oldSaturation + change);
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
