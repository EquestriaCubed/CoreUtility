package com.hepolite.coreutility.event.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerHungerChangeEvent extends Event implements Cancellable
{
	private final Player player;
	private final float maxHunger;
	private final float oldHunger;
	private float newHunger;

	public PlayerHungerChangeEvent(Player player, float maxHunger, float oldHunger, float newHunger)
	{
		this.player = player;
		this.maxHunger = maxHunger;
		this.oldHunger = oldHunger;
		setNewHunger(newHunger);
	}

	/** Returns the player associated with this event */
	public final Player getPlayer()
	{
		return player;
	}

	/** Returns the maximum hunger for the player */
	public final float getMaxHunger()
	{
		return maxHunger;
	}

	/** Returns the old hunger for the player */
	public final float getOldHunger()
	{
		return oldHunger;
	}

	/** Returns the new hunger for the player */
	public final float getNewHunger()
	{
		return newHunger;
	}

	/** Sets the new hunger for the player */
	public final void setNewHunger(float newHunger)
	{
		this.newHunger = Math.max(0.0f, Math.min(maxHunger, newHunger));
	}

	/** Returns the change in hunger for the player */
	public final float getHungerChange()
	{
		return newHunger - oldHunger;
	}

	/** Sets the change in hunger for the player */
	public final void setHungerChange(float change)
	{
		setNewHunger(oldHunger + change);
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
