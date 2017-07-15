package com.hepolite.coreutility.hunger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

public class Food
{
	private final ItemStack consumed;
	private boolean alwaysEdible = false;

	private float hunger = 0.0f;
	private float saturation = 0.0f;
	private float chance = 1.0f;
	private final Collection<String> categories = new HashSet<String>();
	private final Collection<PotionEffect> effects = new ArrayList<PotionEffect>();
	private final Collection<ItemStack> items = new ArrayList<ItemStack>();

	public Food(ItemStack consumed)
	{
		this.consumed = consumed;
	}

	/** Marks the food as always being edible */
	public final void setAlwaysEdible(boolean alwaysEdible)
	{
		this.alwaysEdible = alwaysEdible;
	}

	/** Returns true if the food should always be edible */
	public final boolean isAlwaysEdible()
	{
		return alwaysEdible;
	}

	/** Returns the item that is to be consumed */
	public final ItemStack getConsumed()
	{
		return consumed;
	}

	/** Sets the hunger the food will restore */
	public final void setHunger(float hunger)
	{
		this.hunger = hunger;
	}

	/** Returns the hunger this food will restore */
	public final float getHunger()
	{
		return hunger;
	}

	/** Sets the saturation the food will restore */
	public final void setSaturation(float saturation)
	{
		this.saturation = saturation;
	}

	/** Returns the saturation this food will restore */
	public final float getSaturation()
	{
		return saturation;
	}

	/** Adds a collection of categories that the food contains */
	public final void addCategories(Collection<String> categories)
	{
		this.categories.addAll(categories);
	}

	/** Returns the categories that this food contains */
	public final Collection<String> getCategories()
	{
		return categories;
	}

	/** Sets the chance for the effects of the food to take effect */
	public final void setChance(float chance)
	{
		this.chance = chance;
	}

	/** Returns the chance for the effects of the food to take effect */
	public final float getChance()
	{
		return chance;
	}

	/** Adds a collection of effects that the food will have */
	public final void addEffects(Collection<PotionEffect> effects)
	{
		this.effects.addAll(effects);
	}

	/** Adds a single effect that the food will have */
	public final void addEffect(PotionEffect effect)
	{
		effects.add(effect);
	}

	/** Returns the effects that this food will have */
	public final Collection<PotionEffect> getEffects()
	{
		return effects;
	}

	/** Adds a collection of items that the consumer will be given */
	public final void addItems(Collection<ItemStack> items)
	{
		this.items.addAll(items);
	}

	/** Adds a single item that the consumer will be given */
	public final void addItem(ItemStack item)
	{
		items.add(item);
	}

	/** Returns the items the consumer will be given */
	public final Collection<ItemStack> getItems()
	{
		return items;
	}
}
