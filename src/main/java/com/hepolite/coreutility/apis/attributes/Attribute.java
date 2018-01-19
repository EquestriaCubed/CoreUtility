package com.hepolite.coreutility.apis.attributes;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class Attribute
{
	private final String id;
	private final UUID uuid;
	private final Map<String, Modifier> modifiers = new HashMap<String, Modifier>();

	private float baseValue = 0.0f;
	private float maxValue = Float.MAX_VALUE;
	private float minValue = -Float.MAX_VALUE;

	public Attribute(String id, UUID uuid)
	{
		this.id = id;
		this.uuid = uuid;
	}

	/** Returns the identifier of the attribute node */
	public final String getID()
	{
		return id;
	}

	/** Returns the UUID associated with the attribute node */
	public final UUID getUUID()
	{
		return uuid;
	}

	/** Returns the entity associated with the attribute node */
	public final Optional<LivingEntity> getEntity()
	{
		for (World world : Bukkit.getWorlds())
			for (LivingEntity entity : world.getLivingEntities())
				if (entity.getUniqueId().equals(uuid))
					return Optional.of(entity);
		return Optional.empty();
	}

	/** Returns the player associated with the attribute node */
	public final Optional<Player> getPlayer()
	{
		return Optional.ofNullable(Bukkit.getPlayer(uuid));
	}

	// ///////////////////////////////////////////////////////////////////////

	/** Removes all modifiers from the attribute node */
	public final void clear()
	{
		modifiers.clear();
	}

	/** Removes a modifier from the attribute node */
	public final void removeModifier(Modifier modifier)
	{
		if (modifier == null)
			throw new IllegalArgumentException("Modifier cannot be null");
		removeModifier(modifier.getID());
	}

	/** Removes a modifier from the attribute node */
	public final void removeModifier(String id)
	{
		if (id == null)
			throw new IllegalArgumentException("Identifier cannot be null");
		modifiers.remove(id);
	}

	/** Returns the modifier with the given id; will create the modifier if it does not exist */
	public final Modifier getModifier(String id)
	{
		if (id == null)
			throw new IllegalArgumentException("Identifier cannot be null");
		if (modifiers.containsKey(id))
			return modifiers.get(id);
		Modifier modifier = new Modifier(id);
		modifiers.put(id, modifier);
		return modifier;
	}

	/** Returns all the modifiers associated with the attribute node */
	public final Collection<Modifier> getModifiers()
	{
		return Collections.unmodifiableCollection(modifiers.values());
	}

	/** Returns true if there are no modifiers stored within the attribute node */
	public final boolean isEmpty()
	{
		return modifiers.isEmpty();
	}

	// ///////////////////////////////////////////////////////////////////////

	/** Sets the various values for this attribute */
	public final Attribute setValues(float base, float min, float max)
	{
		return setBaseValue(base).setMaxValue(min).setMaxValue(max);
	}

	/** Sets the base value of the attribute node */
	public final Attribute setBaseValue(float baseValue)
	{
		this.baseValue = baseValue;
		return this;
	}

	/** Returns the base value of the attribute node */
	public final float getBaseValue()
	{
		return baseValue;
	}

	/** Sets the minmium value of the attribute node */
	public final Attribute setMinValue(float minValue)
	{
		this.minValue = minValue;
		return this;
	}

	/** Returns the minimum value of the attribute node */
	public final float getMinValue()
	{
		return minValue;
	}

	/** Sets the maximum value of the attribute node */
	public final Attribute setMaxValue(float maxValue)
	{
		this.maxValue = maxValue;
		return this;
	}

	/** Returns the maximum value of the attribute node */
	public final float getMaxValue()
	{
		return maxValue;
	}

	/** Returns the total scale of the attribute node */
	public final float getScale()
	{
		float totalScale = 1.0f;
		for (Modifier modifier : modifiers.values())
			totalScale *= modifier.getScale();
		return totalScale;
	}

	/** Returns the total multiplier of the attribute node */
	public final float getMultiplier()
	{
		float totalMultiplier = 0.0f;
		for (Modifier modifier : modifiers.values())
			totalMultiplier += modifier.getMultiplier();
		return totalMultiplier;
	}

	/** Returns the total flat value of the attribute node */
	public final float getFlat()
	{
		float totalFlat = 0.0f;
		for (Modifier modifier : modifiers.values())
			totalFlat += modifier.getFlat();
		return totalFlat;
	}

	/** Returns the final value of the attribute node */
	public final float getValue()
	{
		float totalScale = 1.0f;
		float totalMultiplier = 0.0f;
		float totalFlat = 0.0f;

		for (Modifier modifier : modifiers.values())
		{
			totalScale *= modifier.getScale();
			totalMultiplier += modifier.getMultiplier();
			totalFlat += modifier.getFlat();
		}

		float value = totalScale * (totalFlat + baseValue * (1.0f + totalMultiplier));
		return Math.max(minValue, Math.min(maxValue, value));
	}
}
