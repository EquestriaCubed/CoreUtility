package com.hepolite.coreutility.apis.attributes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Attribute
{
	private final String name;
	private final UUID uuid;
	private final Map<String, AttributeModifier> modifiers = new HashMap<String, AttributeModifier>();

	private float baseValue = 0.0f;
	private float maxValue = Float.MAX_VALUE;
	private float minValue = -Float.MAX_VALUE;

	public Attribute(String name, UUID uuid)
	{
		this.name = name;
		this.uuid = uuid;
	}

	/** Returns the name of the node */
	public final String getName()
	{
		return name;
	}

	/** Returns the UUID associated with the node */
	public final UUID getUUID()
	{
		return uuid;
	}

	/** Returns the player associated with the node */
	public final Player getPlayer()
	{
		return Bukkit.getPlayer(uuid);
	}

	// ///////////////////////////////////////////////////////////////////////

	/** Removes all modifiers from the node */
	public final void clear()
	{
		modifiers.clear();
	}

	/** Removes a modifier from the node */
	public final void removeModifier(AttributeModifier modifier)
	{
		if (modifier != null)
			removeModifier(modifier.getName());
	}

	/** Removes a modifier from the node */
	public final void removeModifier(String modifier)
	{
		modifiers.remove(modifier);
	}

	/** Returns the modifier with the given name, or null if it doesn't exist */
	public final AttributeModifier getModifier(String modifier)
	{
		AttributeModifier m = modifiers.get(modifier);
		if (m != null)
			return m;
		m = new AttributeModifier(modifier);
		modifiers.put(modifier, m);
		return m;
	}

	/** Returns all the modifiers associated with the attribute */
	public final Collection<AttributeModifier> getModifiers()
	{
		Collection<AttributeModifier> modifiers = new ArrayList<AttributeModifier>(
				this.modifiers.size());
		for (Entry<String, AttributeModifier> entry : this.modifiers.entrySet())
			modifiers.add(entry.getValue());
		return modifiers;
	}

	/** Returns true if there are no modifiers stored within the attribute */
	public final boolean isEmpty()
	{
		return modifiers.isEmpty();
	}

	// ///////////////////////////////////////////////////////////////////////

	/** Sets the base value of the node */
	public final Attribute setBaseValue(float baseValue)
	{
		this.baseValue = baseValue;
		return this;
	}

	/** Returns the base value of the node */
	public final float getBaseValue()
	{
		return baseValue;
	}

	/** Sets the minmium value of the node */
	public final Attribute setMinValue(float minValue)
	{
		this.minValue = minValue;
		return this;
	}

	/** Returns the minimum value of the node */
	public final float getMinValue()
	{
		return minValue;
	}

	/** Sets the maximum value of the node */
	public final Attribute setMaxValue(float maxValue)
	{
		this.maxValue = maxValue;
		return this;
	}

	/** Returns the maximum value of the node */
	public final float getMaxValue()
	{
		return maxValue;
	}

	/** Returns the total scale of the node */
	public final float getScale()
	{
		float totalScale = 1.0f;
		for (Entry<String, AttributeModifier> entry : modifiers.entrySet())
			totalScale *= 1.0f + entry.getValue().getScale();
		return totalScale;
	}

	/** Returns the total multiplier of the node */
	public final float getMultiplier()
	{
		float totalMultiplier = 0.0f;
		for (Entry<String, AttributeModifier> entry : modifiers.entrySet())
			totalMultiplier += entry.getValue().getMultiplier();
		return totalMultiplier;
	}

	/** Returns the total flat value of the node */
	public final float getFlat()
	{
		float totalFlat = 0.0f;
		for (Entry<String, AttributeModifier> entry : modifiers.entrySet())
			totalFlat += entry.getValue().getFlat();
		return totalFlat;
	}

	/** Returns the final value of the node */
	public final float getValue()
	{
		float totalScale = 1.0f;
		float totalMultiplier = 0.0f;
		float totalFlat = 0.0f;

		for (Entry<String, AttributeModifier> entry : modifiers.entrySet())
		{
			AttributeModifier modifier = entry.getValue();
			totalScale *= 1.0f + modifier.getScale();
			totalMultiplier += modifier.getMultiplier();
			totalFlat += modifier.getFlat();
		}

		float value = totalScale * (baseValue * Math.max(0.0f, 1.0f + totalMultiplier) + totalFlat);
		return Math.max(minValue, Math.min(maxValue, value));
	}
}
