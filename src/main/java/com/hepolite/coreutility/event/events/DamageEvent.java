package com.hepolite.coreutility.event.events;

import java.util.Optional;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.hepolite.coreutility.apis.damage.Damage;

public class DamageEvent extends Event implements Cancellable
{
	private final LivingEntity target;
	private final LivingEntity attacker;
	private Damage damage;

	public DamageEvent(LivingEntity target, LivingEntity attacker, Damage damage)
	{
		this.target = target;
		this.attacker = attacker;
		this.damage = damage;
	}

	/** Returns the target */
	public final LivingEntity getTarget()
	{
		return target;
	}

	/** Returns the target as a player, or null if the targer is not a player */
	public final Optional<Player> getTargetAsPlayer()
	{
		return (target instanceof Player) ? Optional.of((Player) target) : Optional.empty();
	}

	/** Returns the attacker */
	public final Optional<LivingEntity> getAttacker()
	{
		return Optional.ofNullable(attacker);
	}

	/** Returns the attacker as a player, or null if the attacker is not a player */
	public final Optional<Player> getAttackerAsPlayer()
	{
		return (attacker instanceof Player) ? Optional.of((Player) attacker) : Optional.empty();
	}

	/** Returns the damage */
	public final Damage getDamage()
	{
		return damage;
	}

	/** Sets the damage */
	public final void setDamage(Damage damage)
	{
		this.damage = damage;
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
