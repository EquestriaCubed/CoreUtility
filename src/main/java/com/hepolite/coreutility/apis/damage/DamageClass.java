package com.hepolite.coreutility.apis.damage;

public enum DamageClass
{
	/** Damage caused by climate, such as temperature, storms, etc */
	CLIMATE,

	/** Damage caused by fire, frost, water, electricity, and similar damage sources */
	ELEMENTAL,

	/** Damage caused by magical effects, such as spells */
	MAGICAL,

	/** Damage caused by internal causes, such as hunger, thirst, poison, etc */
	PERSONAL,

	/** Damage caused by physical attacks, such as punches, swords, arrows, guns, explosions, impacts, etc */
	PHYSICAL,

	/** Damage which bypasses all forms of natural defenses */
	TRUE,
}
