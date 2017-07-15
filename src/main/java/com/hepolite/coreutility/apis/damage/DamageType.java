package com.hepolite.coreutility.apis.damage;

import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public enum DamageType
{
	/** Damage dealt by heavy physical attacks */
	BLUNT(DamageClass.PHYSICAL, DamageCause.ENTITY_ATTACK, 0.5f, 0.75f, 0.5f, 1.0f),

	/** Damage dealt by being on fire */
	BURNING(DamageClass.PERSONAL, DamageCause.FIRE, 0.0f, 0.0f, 0.0f, 0.0f),

	/** Damage deal by electrical attacks */
	ELECTRICITY(DamageClass.ELEMENTAL, DamageCause.LIGHTNING, 0.0f, 0.0f, 0.0f, 0.0f),

	/** Damage dealt by explosive attacks */
	EXPLOSION(DamageClass.PHYSICAL, DamageCause.ENTITY_EXPLOSION, 0.25f, 0.5f, 0.25f, 1.0f),

	/** Damage dealt to those disrespecting the sudden stop at the end of a fall */
	FALL(DamageClass.PHYSICAL, DamageCause.FALL, 0.0f, 0.0f, 0.25f, 0.5f),

	/** Damage dealt to those who got too close to fire */
	FIRE(DamageClass.ELEMENTAL, DamageCause.FIRE, 0.0f, 0.0f, 0.0f, 0.0f),

	/** Damage dealt to those who got too distant from fire */
	FROST(DamageClass.ELEMENTAL, DamageCause.CUSTOM, 0.5f, 0.0f, 0.0f, 0.0f),

	/** Damage dealt due to starvation */
	HUNGER(DamageClass.PERSONAL, DamageCause.STARVATION, 0.0f, 0.0f, 0.0f, 0.0f),

	/** Damage dealt to those who got too close to lava */
	LAVA(DamageClass.ELEMENTAL, DamageCause.LAVA, 0.0f, 0.0f, 0.0f, 0.0f),

	/** Damage dealt by magical attacks */
	MAGIC(DamageClass.MAGICAL, DamageCause.MAGIC, 0.0f, 0.0f, 0.0f, 0.0f),

	/** Damage dealt by normal attacks, such as swords, bows, punches, etc */
	NORMAL(DamageClass.PHYSICAL, DamageCause.ENTITY_ATTACK, 1.0f, 1.0f, 1.0f, 1.0f),

	/** Damage dealt by sharp objects */
	PIERCING(DamageClass.PHYSICAL, DamageCause.ENTITY_ATTACK, 0.25f, 1.0f, 0.25f, 1.0f),

	/** Damage deal by poison-based attacks */
	POISON(DamageClass.PERSONAL, DamageCause.POISON, 0.0f, 0.0f, 0.0f, 0.0f),

	/** Damage deal by potion-based attacks */
	POTION(DamageClass.PERSONAL, DamageCause.MAGIC, 0.0f, 0.0f, 0.0f, 0.0f),

	/** Damage deal due to suffocation */
	SUFFOCATION(DamageClass.PERSONAL, DamageCause.SUFFOCATION, 0.0f, 0.0f, 0.0f, 0.0f),

	/** Damage that is not blocked by anything */
	TRUE(DamageClass.TRUE, DamageCause.CUSTOM, 0.0f, 0.0f, 0.0f, 0.0f),

	/** Damage by water-based attacks */
	WATER(DamageClass.ELEMENTAL, DamageCause.DROWNING, 0.0f, 0.0f, 0.0f, 1.0f),

	/** Damage deal by wither-based attacks */
	WITHER(DamageClass.PERSONAL, DamageCause.WITHER, 0.0f, 0.0f, 0.0f, 0.0f);

	// ///////////////////////////////////////////////////////////////////////////

	private final DamageClass damageClass;
	private final DamageCause underlyingCause;
	private final float armorEfficiency;
	private final float blockingEfficiency;
	private final float enchantmentEfficiency;
	private final float resistanceEfficiency;

	private DamageType(DamageClass damageClass, DamageCause underlyingCause, float armorEfficiency, float blockingEfficiency, float enchantmentEfficiency, float resistanceEfficiency)
	{
		this.damageClass = damageClass;
		this.underlyingCause = underlyingCause;
		this.armorEfficiency = armorEfficiency;
		this.blockingEfficiency = blockingEfficiency;
		this.enchantmentEfficiency = enchantmentEfficiency;
		this.resistanceEfficiency = resistanceEfficiency;
	}

	/** Returns the class of the damage type */
	public final DamageClass getDamageClass()
	{
		return damageClass;
	}

	/** Returns the cause associated with this damage type */
	public final DamageCause getUnderlyingCause()
	{
		return underlyingCause;
	}

	/** Returns the efficiency of the armor against the damage type */
	public final float getArmorEfficiency()
	{
		return armorEfficiency;
	}

	/** Returns the efficiency of the blocking against the damage type */
	public final float getBlockingEfficiency()
	{
		return blockingEfficiency;
	}

	/** Returns the efficiency of enchantments against the damage type */
	public final float getEnchantmentEfficiency()
	{
		return enchantmentEfficiency;
	}

	/** Returns the efficiency of the resistance effect against the damage type */
	public final float getResistanceEfficiency()
	{
		return resistanceEfficiency;
	}
}
