package com.hepolite.coreutility.apis.damage;

import com.hepolite.coreutility.plugin.CorePlugin;
import com.hepolite.coreutility.settings.Settings;

public class DamageAPISettings extends Settings
{
	public DamageAPISettings(CorePlugin plugin)
	{
		super(plugin, "API", "DamageAPI_localization");
	}

	@Override
	protected void addDefaultValues()
	{
		final String deathMessagePath = "messages.death.";
		set(deathMessagePath + "default.generic", new String[] {
			"<target> died",
		});
		set(deathMessagePath + "default.attacker", new String[] {
			"<target> was killed by <attacker>",
		});
		set(deathMessagePath + "fall.generic", new String[] {
				"<target> has an unfortunate meeting with the ground",
				"<target> was in a hurry, downwards",
				"<target> wanted to check if gravity still worked",
				"<target> faceplanted a bit too hard",
				"<target> fell to their death",
				"<target> found out that the ground was hard",
				"<target> hit the ground too hard",
				"<target> could not fly as well as expected",
				"<target> was taken by surprise by the ground",
		});
		set(deathMessagePath + "fall.attacker", new String[] {
			"<target> fell to their death while fighting <attacker>",
		});
		set(deathMessagePath + "magical.generic", new String[] {
			"<target> was killed by magic",
		});
		set(deathMessagePath + "magical.attacker", new String[] {
			"<target> was killed by <attacker> using magic",
		});
		set(deathMessagePath + "physical.generic", new String[] {
				"<target> died to <attacker>",
				"<target> was defeated by <attacker>",
				"<target> was killed by <attacker>",
				"<target> was slain by <attacker>",
		});
		set(deathMessagePath + "wither.generic", new String[] {
			"<target> withered away",
		});
		set(deathMessagePath + "wither.attacker", new String[] {
			"<target> withered away while fighting <attacker>",
		});
	}

	@Override
	protected void onSaveConfigFile()
	{}

	@Override
	protected void onLoadConfigFile()
	{}
}
