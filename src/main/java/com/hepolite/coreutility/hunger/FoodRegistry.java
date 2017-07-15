package com.hepolite.coreutility.hunger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import com.hepolite.coreutility.plugin.CorePlugin;
import com.hepolite.coreutility.settings.Settings;
import com.hepolite.coreutility.settings.Settings.PotionEffectSetting;
import com.hepolite.coreutility.settings.SettingsSimple;

public class FoodRegistry
{
	private final HungerHandler hungerHandler;
	private final Settings settings;

	public FoodRegistry(CorePlugin plugin, HungerHandler hungerHandler)
	{
		this.hungerHandler = hungerHandler;
		this.settings = new SettingsSimple(plugin, "Hunger", "FoodRegistry");
	}

	/** Wipes the registry completely */
	public final void wipe()
	{
		settings.wipe();
	}

	/** Loads up all food settings from the given config file, reading from the given property */
	public final void parseGroupSettings(String group, Settings settings, String property)
	{
		for (String key : settings.getKeys(property))
		{
			String path = property + "." + key;
			String field = group + "." + key;
			if (settings.has(path + ".alwaysEdible"))
				this.settings.set(field + ".alwaysEdible", settings.getBool(path + ".alwaysEdible"));
			if (settings.has(path + ".contains"))
				this.settings.set(field + ".contains", settings.getString(path + ".contains"));
			if (settings.has(path + ".categories"))
				this.settings.set(field + ".categories", settings.getString(path + ".categories"));
			if (settings.has(path + ".food"))
				this.settings.set(field + ".food", settings.getFloat(path + ".food"));
			if (settings.has(path + ".ratio"))
				this.settings.set(field + ".ratio", settings.getFloat(path + ".ratio"));
			if (settings.has(path + ".chance"))
				this.settings.set(field + ".chance", settings.getFloat(path + ".chance"));
			if (settings.has(path + ".effects"))
				this.settings.set(field + ".effects", settings.getPotionEffects(path + ".effects").toArray(new PotionEffectSetting[0]));
			if (settings.has(path + ".items"))
				this.settings.set(field + ".items", settings.getItems(path + ".items").toArray(new ItemStack[0]));
		}
		this.settings.save();
	}

	// //////////////////////////////////////////////////////////////////////

	/** Returns true if the given group is allowed to consume the given item; if the given collection is not null, the reasons will be added to it */
	public final boolean isAllowedToConsume(Food food, Group group, Collection<String> reasons)
	{
		if (food == null || group == null)
			return false;

		Group defaultGroup = hungerHandler.getGroup("Default");
		Collection<String> forbidden = new HashSet<String>();
		forbidden.addAll(defaultGroup.getForbiddenFoods());
		forbidden.addAll(group.getForbiddenFoods());

		boolean result = true;
		for (String category : food.getCategories())
		{
			if (forbidden.contains(category))
			{
				result = false;
				if (reasons == null)
					break;
				else
					reasons.add(category);
			}
		}
		return result;
	}

	/** Returns the food settings for the given group and item; returns null if the food does not exist */
	public final Food getFood(ItemStack item, Group group)
	{
		if (item == null || group == null)
			return null;

		ItemStack copy = item.clone();
		copy.setAmount(1);
		String food = settings.writeSimpleItem(copy);
		String groupName = group.getName();

		if (!settings.has("Default." + food) && !settings.has(groupName + "." + food))
			return null;

		boolean alwaysEdibleDef = settings.getBool("Default." + food + ".alwaysEdible");
		boolean alwaysEdible = settings.getBool(groupName + "." + food + ".alwaysEdible", alwaysEdibleDef);
		float hungerDef = settings.getFloat("Default." + food + ".food");
		float hunger = settings.getFloat(groupName + "." + food + ".food", hungerDef);
		float ratioDef = settings.getFloat("Default." + food + ".ratio");
		float ratio = settings.getFloat(groupName + "." + food + ".ratio", ratioDef);
		float chanceDef = settings.getFloat("Default." + food + ".chance", 1.0f);
		float chance = settings.getFloat(groupName + "." + food + ".chance", chanceDef);

		Collection<PotionEffectSetting> effectsSettings = null;
		if (settings.has(groupName + "." + food + ".effects"))
			effectsSettings = settings.getPotionEffects(groupName + "." + food + ".effects");
		else
			effectsSettings = settings.getPotionEffects("Default." + food + ".effects");
		Collection<PotionEffect> effects = new ArrayList<PotionEffect>();
		for (PotionEffectSetting setting : effectsSettings)
			effects.add(setting.create());

		Collection<ItemStack> items = null;
		if (settings.has(groupName + "." + food + ".items"))
			items = settings.getItems(groupName + "." + food + ".items");
		else
			items = settings.getItems("Default." + food + ".items");

		Food processedFood = new Food(copy);
		processedFood.setAlwaysEdible(alwaysEdible);
		processedFood.setHunger(hunger);
		processedFood.setSaturation(hunger * ratio);
		processedFood.setChance(chance);
		processedFood.addCategories(getFoodCategories(groupName, food));
		processedFood.addEffects(effects);
		processedFood.addItems(items);
		return processedFood;
	}

	/** Returns the categories of the given food, for the given group */
	private final Collection<String> getFoodCategories(String group, String food)
	{
		Collection<String> categories = new HashSet<String>();
		categories.addAll(getFieldCategories(group, food));
		for (String string : getFieldContents(group, food))
			categories.addAll(getFieldCategories(group, string));
		return categories;
	}

	/** Returns the contents field for the food at the given group */
	private final Collection<String> getFieldContents(String group, String item)
	{
		Collection<String> contents = new HashSet<String>();
		contents.add(item);

		String def = settings.getString("Default." + item + ".contains");
		String data = settings.getString(group + "." + item + ".contains", def);
		if (data != null)
		{
			for (String string : data.split(" "))
				contents.addAll(getFieldContents(group, string));
		}
		return contents;
	}

	/** Returns the categories field for the food at the given group */
	private final Collection<String> getFieldCategories(String group, String item)
	{
		String def = settings.getString("Default." + item + ".categories");
		String data = settings.getString(group + "." + item + ".categories", def);
		return data == null ? new HashSet<String>() : Arrays.asList(data.split(" "));
	}
}
