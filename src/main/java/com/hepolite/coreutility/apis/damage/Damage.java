package com.hepolite.coreutility.apis.damage;

public class Damage
{
	private final DamageClass damageClass;
	private final DamageType damageType;
	private float amount;

	public Damage(DamageClass damageClass, DamageType damageType, float amount)
	{
		this.damageClass = damageClass;
		this.damageType = damageType;
		this.amount = amount;
	}

	public Damage(DamageType damageType, float amount)
	{
		this.damageClass = null;
		this.damageType = damageType;
		this.amount = amount;
	}

	/** Returns the class of the damage */
	public final DamageClass getDamageClass()
	{
		return damageClass == null ? damageType.getDamageClass() : damageClass;
	}

	/** Returns the type of the damage */
	public final DamageType getDamageType()
	{
		return damageType;
	}

	/** Returns the damage amount */
	public final float getAmount()
	{
		return amount;
	}

	/** Sets the damage amount */
	public final void setAmount(float damage)
	{
		this.amount = damage;
	}
}
