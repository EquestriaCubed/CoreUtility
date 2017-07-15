package com.hepolite.coreutility.movement;

import com.hepolite.coreutility.plugin.CorePlugin;
import com.hepolite.coreutility.settings.Settings;

public class MovementSettings extends Settings
{
	public static float DAMAGE_FALL_START = 4.5f;
	public static float DAMAGE_FALL_END = 23.0f;
	public static float DAMAGE_FALL_SCALE = 20.0f;
	public static float WATER_DRAG_FRACTION = 0.25f;
	public static float WATER_DRAG_FIXED = 0.15f;
	public static float MOVEMENT_SPEED_FLY = 0.05f;
	public static float MOVEMENT_SPEED_WALK = 0.20f;
	public static String PERMISSION_FLIGHT_BASIC = "coreutility.flight.basic";
	public static String PERMISSION_FLIGHT_ADMIN = "coreutility.flight.admin";

	public MovementSettings(CorePlugin plugin)
	{
		super(plugin, "Movement");
	}

	@Override
	protected void addDefaultValues()
	{
		set("fall.damage.start", DAMAGE_FALL_START);
		set("fall.damage.end", DAMAGE_FALL_END);
		set("fall.damage.scale", DAMAGE_FALL_SCALE);
		set("fall.water.dragFraction", WATER_DRAG_FRACTION);
		set("fall.water.dragFixed", WATER_DRAG_FIXED);
		set("movement.speed.fly", MOVEMENT_SPEED_FLY);
		set("movement.speed.walk", MOVEMENT_SPEED_WALK);
		set("permission.flight.basic", PERMISSION_FLIGHT_BASIC);
		set("permission.flight.basic", PERMISSION_FLIGHT_ADMIN);
	}

	@Override
	protected void onSaveConfigFile()
	{}

	@Override
	protected void onLoadConfigFile()
	{
		DAMAGE_FALL_START = getFloat("fall.damage.start");
		DAMAGE_FALL_END = getFloat("fall.damage.end");
		DAMAGE_FALL_SCALE = getFloat("fall.damage.scale");
		WATER_DRAG_FRACTION = getFloat("fall.water.dragFraction");
		WATER_DRAG_FIXED = getFloat("fall.water.dragFixed");
		MOVEMENT_SPEED_FLY = getFloat("movement.speed.fly");
		MOVEMENT_SPEED_WALK = getFloat("movement.speed.walk");
		PERMISSION_FLIGHT_BASIC = getString("permission.flight.basic");
		PERMISSION_FLIGHT_ADMIN = getString("permission.flight.admin");
	}
}
