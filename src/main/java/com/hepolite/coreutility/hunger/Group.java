package com.hepolite.coreutility.hunger;

import java.util.Collection;
import java.util.HashSet;

import org.bukkit.ChatColor;
import org.bukkit.Sound;

import com.hepolite.coreutility.movement.MovementType;
import com.hepolite.coreutility.plugin.CorePlugin;
import com.hepolite.coreutility.settings.Settings;
import com.hepolite.coreutility.settings.Settings.SoundSetting;

public class Group
{
	private final Settings settings;
	private final String name;

	public Group(CorePlugin plugin, String name)
	{
		this.settings = new Settings(plugin, "Hunger/Groups", name)
		{
			@Override
			protected void addDefaultValues()
			{
				set("Message.cannotConsume", "&bYou are unable to consume that");
				set("Message.inedibles", "&cFood contains inedibles:&f");
				set("Message.starved", new String[] {
						"&f<player>&f starved to death",
						"&f<player>&f was unable to find food in time",
						"&f<player>&f went too long without food",
				});
				set("Sound.eat", new SoundSetting(true, Sound.ENTITY_GENERIC_EAT, 1.0f, 1.0f));

				set("Exhaustion.rateOfChange", 0.95f);
				set("Exhaustion.rateOfHunger", 0.25f);
				set("Exhaustion.restpoint.floating", 30.0f);
				set("Exhaustion.restpoint.flying", 250.0f);
				set("Exhaustion.restpoint.gliding", 185.0f);
				set("Exhaustion.restpoint.hovering", 210.0f);
				set("Exhaustion.restpoint.running", 180.0f);
				set("Exhaustion.restpoint.sneaking", 30.0f);
				set("Exhaustion.restpoint.standing", 15.0f);
				set("Exhaustion.restpoint.swimming", 150.0f);
				set("Exhaustion.restpoint.walking", 50.0f);

				set("Healing.enable", true);
				set("Healing.frequency", 10);
				set("Healing.start", 0.3f);
				set("Healing.heal", 0.75f);
				set("Healing.cost", 2.5f);

				set("Starvation.enable", true);
				set("Starvation.frequency", 3);
				set("Starvation.damage", 1.0f);

				set("Food.forbidden", "");

			}

			@Override
			protected void onSaveConfigFile()
			{}

			@Override
			protected void onLoadConfigFile()
			{}
		};
		this.name = name;
	}

	/** Returns the name of the group */
	public final String getName()
	{
		return name;
	}

	/** Returns the settings associated with the group */
	public final Settings getSettings()
	{
		return settings;
	}

	// //////////////////////////////////////////////////////////////////

	/** Returns the foods that are forbidden by this group */
	public final Collection<String> getForbiddenFoods()
	{
		Collection<String> collection = new HashSet<String>();
		String forbidden = settings.getString("Food.forbidden");
		for (String string : forbidden.split(" "))
			collection.add(string);
		return collection;
	}

	/** Returns the eating sound */
	public final SoundSetting getEatingSound()
	{
		return settings.getSound("Sound.eat");
	}

	/** Returns the message saying that the food cannot be consumed */
	public final String getCannotConsumeMessage()
	{
		return ChatColor.translateAlternateColorCodes('&', settings.getString("Message.cannotConsume"));
	}

	/** Returns the message saying that the food is inedible */
	public final String getInedibleMessage()
	{
		return ChatColor.translateAlternateColorCodes('&', settings.getString("Message.inedibles"));
	}

	/** Returns the rate of exhaustion change */
	public final float getRateOfChange()
	{
		return 0.01f * settings.getFloat("Exhaustion.rateOfChange");
	}

	/** Returns the rate of hunger change */
	public final float getRateOfHunger()
	{
		return 0.01f * settings.getFloat("Exhaustion.rateOfHunger");
	}

	/** Returns the exhaustion resting point for the given movement type */
	public final float getRestingPoint(MovementType type)
	{
		return settings.getFloat("Exhaustion.restpoint." + type.name().toLowerCase());
	}

	// //////////////////////////////////////////////////////////////////

	/** Returns true if healing is enabled */
	public final boolean isHealingEnabled()
	{
		return settings.getBool("Healing.enable");
	}

	/** Returns the healing frequency */
	public final int getHealingFrequency()
	{
		return settings.getInt("Healing.frequency");
	}

	/** Returns the ratio of health when healing can start */
	public final float getHealingStart()
	{
		return settings.getFloat("Healing.start");
	}

	/** Returns the healing amount */
	public final float getHealingAmount()
	{
		return settings.getFloat("Healing.heal");
	}

	/** Returns the saturation cost of healing */
	public final float getHealingCost()
	{
		return settings.getFloat("Healing.cost");
	}

	/** Returns true if starvation is enabled */
	public final boolean isStarvationEnabled()
	{
		return settings.getBool("Starvation.enable");
	}

	/** Returns the starvation frequency */
	public final int getStarvationFrequency()
	{
		return settings.getInt("Starvation.frequency");
	}

	/** Returns the starvation damage */
	public final float getStarvationDamage()
	{
		return settings.getFloat("Starvation.damage");
	}
}
