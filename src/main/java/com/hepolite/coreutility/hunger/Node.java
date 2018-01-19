package com.hepolite.coreutility.hunger;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import com.hepolite.coreutility.apis.attributes.Attribute;
import com.hepolite.coreutility.apis.attributes.AttributeAPI;
import com.hepolite.coreutility.apis.attributes.Attributes;
import com.hepolite.coreutility.event.events.PlayerExhaustionChangeEvent;
import com.hepolite.coreutility.event.events.PlayerHungerChangeEvent;
import com.hepolite.coreutility.event.events.PlayerSaturationChangeEvent;
import com.hepolite.coreutility.settings.Settings;

public class Node
{
	private final UUID uuid;
	private float hunger = 0.0f;
	private float saturation = 0.0f;
	private float exhaustion = 0.0f;

	public Node(Player player)
	{
		this.uuid = player.getUniqueId();
		this.hunger = getMaxHunger();
		this.saturation = hunger;
		this.exhaustion = 0.0f;

		AttributeAPI.get(uuid, Attributes.HUNGER_MAX).setBaseValue(100.0f);
	}

	/** Resets the node to the initial values */
	public final void reset()
	{
		hunger = getMaxHunger();
		saturation = hunger;
		exhaustion = 0.0f;
	}

	/** Loads the node from the given config */
	public final void load(Settings settings)
	{
		hunger = settings.getFloat(uuid + ".hunger");
		saturation = settings.getFloat(uuid + ".saturation");
		exhaustion = settings.getFloat(uuid + ".exhaustion");
	}

	/** Saves the node to the given config */
	public final void save(Settings settings)
	{
		settings.set(uuid + ".hunger", hunger);
		settings.set(uuid + ".saturation", saturation);
		settings.set(uuid + ".exhaustion", exhaustion);
	}

	// ////////////////////////////////////////////////////////////////////

	/** Returns the player associated with this node */
	public final Player getPlayer()
	{
		return Bukkit.getPlayer(uuid);
	}

	/** Returns the UUID of the player associated with this node */
	public final UUID getUUID()
	{
		return uuid;
	}

	/** Returns the max hunger the player can have */
	public final float getMaxHunger()
	{
		return AttributeAPI.get(uuid, Attributes.HUNGER_MAX).getValue();
	}

	/** Returns the total hunger points and saturation that the player has remaining */
	public final float getTotal()
	{
		return getHunger() + getSaturation();
	}

	/** Returns the hunger points the player has remaining */
	public final float getHunger()
	{
		return hunger;
	}

	/** Returns the saturation points the player has remaining */
	public final float getSaturation()
	{
		return saturation;
	}

	/** Returns the exhaustion points the player has */
	public final float getExhaustion()
	{
		return exhaustion;
	}

	// ////////////////////////////////////////////////////////////////////

	/** Changes the hunger points the player has left by the given amount */
	public final void changeHunger(float amount)
	{
		if (amount != 0.0f)
			setHunger(amount + getHunger());
	}

	/** Sets the hunger points the player has left to the given amount */
	public final void setHunger(float hunger)
	{
		PlayerHungerChangeEvent event = new PlayerHungerChangeEvent(getPlayer(), getMaxHunger(), this.hunger, hunger);
		post(event);
		if (!event.isCancelled())
		{
			if (event.getHungerChange() < 0.0f)
			{
				Attribute attribute = AttributeAPI.get(uuid, Attributes.HUNGER_RATEOFCHANGE);
				attribute.setBaseValue(event.getHungerChange());
				event.setHungerChange(attribute.getValue());
			}

			this.hunger = event.getNewHunger();
			this.saturation = Math.min(this.saturation, this.hunger);
			updatePlayerPoints();
		}
	}

	/** Changes the saturation points the player has left by the given amount */
	public final void changeSaturation(float amount)
	{
		if (amount != 0.0f)
			setSaturation(amount + getSaturation());
	}

	/** Sets the saturation points the player has left to the given amount */
	public final void setSaturation(float saturation)
	{
		PlayerSaturationChangeEvent event = new PlayerSaturationChangeEvent(getPlayer(), this.hunger, this.saturation, saturation);
		post(event);
		if (!event.isCancelled())
		{
			if (event.getSaturationChange() < 0.0f)
			{
				Attribute attribute = AttributeAPI.get(uuid, Attributes.HUNGER_RATEOFCHANGE);
				attribute.setBaseValue(event.getSaturationChange());
				event.setSaturationChange(attribute.getValue());
			}

			this.saturation = event.getNewSaturation();
			if (this.saturation < 0.0f)
			{
				this.hunger = Math.max(0.0f, this.hunger + this.saturation);
				this.saturation = 0.0f;
			}
			updatePlayerPoints();
		}
	}

	/** Changes the exhaustion points the player has by the given amount */
	public final void changeExhaustion(float amount)
	{
		if (amount != 0.0f)
			setExhaustion(amount + getExhaustion());
	}

	/** Sets the exhaustion points the player has to the given amount */
	public final void setExhaustion(float exhaustion)
	{
		PlayerExhaustionChangeEvent event = new PlayerExhaustionChangeEvent(getPlayer(), this.exhaustion, exhaustion);
		post(event);
		if (!event.isCancelled())
			this.exhaustion = event.getNewExhaustion();
	}

	// ////////////////////////////////////////////////////////////////////

	/** Updates the hunger and saturation points for the associated player */
	private final void updatePlayerPoints()
	{
		Player player = getPlayer();
		if (player == null)
			return;
		float max = getMaxHunger();
		player.setFoodLevel((int) Math.ceil(20.0f * hunger / max));
		player.setSaturation(20.0f * saturation / max);
	}

	/** Posts an event to the event bus */
	private final Event post(Event event)
	{
		Bukkit.getServer().getPluginManager().callEvent(event);
		return event;
	}
}
