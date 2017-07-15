package com.hepolite.coreutility.apis.attributes;

public class AttributeModifier
{
	private final String name;

	private float multiplier = 0.0f;	// All multipliers are added together, plus one
	private float flat = 0.0f;			// All flat values are added together, then added to the product of the base value and the multipliers
	private float scale = 0.0f;			// All scales are multiplied together, then multiplied by the sum

	private int lifetime = -1;			// The lifetime of the modifier, measured in seconds; use -1 for infinite lifetime

	public AttributeModifier(String name)
	{
		this.name = name;
	}

	/** Returns the name of the modifier */
	public final String getName()
	{
		return name;
	}

	/** Sets the scale of the modifier */
	public final AttributeModifier setScale(float scale)
	{
		this.scale = scale;
		return this;
	}

	/** Returns the scale of the modifier */
	public final float getScale()
	{
		return scale;
	}

	/** Sets the scale of the modifier */
	public final AttributeModifier setMultiplier(float multiplier)
	{
		this.multiplier = multiplier;
		return this;
	}

	/** Returns the multiplier of the modifier */
	public final float getMultiplier()
	{
		return multiplier;
	}

	/** Sets the scale of the modifier */
	public final AttributeModifier setFlat(float flat)
	{
		this.flat = flat;
		return this;
	}

	/** Returns the flat value of the modifier */
	public final float getFlat()
	{
		return flat;
	}

	/** Sets the lifetime of the modifier, measured in seconds; -1 for infinite lifetime, otherwise the modifier will live for the specified number of seconds */
	public final AttributeModifier setLifetime(int lifetime)
	{
		this.lifetime = lifetime;
		return this;
	}

	/** Returns the remaining lifetime of the modifier, measured in seconds */
	public final int getLifetime()
	{
		return lifetime;
	}
}
