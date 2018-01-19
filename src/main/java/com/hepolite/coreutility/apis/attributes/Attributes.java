package com.hepolite.coreutility.apis.attributes;

import com.hepolite.coreutility.apis.damage.DamageClass;
import com.hepolite.coreutility.apis.damage.DamageType;

/** Built-in attribute types; functionality might not be specified within the core, but may be specified in other plugins */
public class Attributes
{
	// Generic entity attributes
	public final static String ATTACK_ALL = "ATTACK_ALL";
	public final static String ATTACK_CLASS(DamageClass damageClass) { return "ATTACK_CLASS_" + damageClass.name(); }
	public final static String ATTACK_TYPE(DamageType damageType) { return "ATTACK_TYPE_" + damageType.name(); }
	public final static String DEFENCE_ALL = "DEFENCE_ALL";
	public final static String DEFENCE_CLASS(DamageClass damageClass) { return "DEFENCE_CLASS_" + damageClass.name(); }
	public final static String DEFENCE_TYPE(DamageType damageType) { return "DEFENCE_TYPE_" + damageType.name(); }

	// Player-only attributes
	public final static String AIR_MAX = "AIR_MAX";
	public final static String HUNGER_MAX = "HUNGER_MAX";
	public final static String HUNGER_RATEOFCHANGE = "HUNGER_RATEOFCHANGE";
	public final static String SPEED_FLY = "SPEED_FLY";
	public final static String SPEED_WALK = "SPEED_WALK";
}
