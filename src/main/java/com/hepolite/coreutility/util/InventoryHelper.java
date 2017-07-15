package com.hepolite.coreutility.util;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class InventoryHelper
{
	public static final short ANY_META = 32767;

	// ////////////////////////////////////////////////////////////////

	/** Returns true if the given item is a subtype of the other item (Same types, metas, and the main either is without name, or has the same name as the sub) */
	public static final boolean isItemSubType(ItemStack main, ItemStack sub)
	{
		if (!areTypesEqual(main, sub) || !areMetaEqual(main, sub))
			return false;
		String nameMain = getItemName(main);
		String nameSub = getItemName(sub);
		return nameMain == null || (nameSub != null && nameMain.equals(nameSub));
	}

	/** Returns true if the items have equal names, types and metas */
	public static final boolean areItemsSimilar(ItemStack itemA, ItemStack itemB)
	{
		return areTypesEqual(itemA, itemB) && areMetaEqual(itemA, itemB) && areNamesEqual(itemA, itemB);
	}

	/** Returns true if the types of the items are equal */
	public static final boolean areTypesEqual(ItemStack itemA, ItemStack itemB)
	{
		if (itemA == null || itemB == null)
			return false;
		return itemA.getType().equals(itemB.getType());
	}

	/** Returns true if the meta of the items are equal */
	public static final boolean areMetaEqual(ItemStack itemA, ItemStack itemB)
	{
		if (itemA == null || itemB == null)
			return false;
		short metaA = itemA.getDurability();
		short metaB = itemB.getDurability();
		return (metaA == metaB || metaA == ANY_META || metaB == ANY_META);
	}

	/** Returns true if the names of the items are equal */
	public static final boolean areNamesEqual(ItemStack itemA, ItemStack itemB)
	{
		if (itemA == null || itemB == null)
			return false;
		String nameA = getItemName(itemA);
		String nameB = getItemName(itemB);
		if (nameA == null && nameB == null)
			return true;
		if ((nameA == null) ^ (nameB == null))
			return false;
		return nameA.equals(nameB);
	}

	/** Returns the name of the given item, or null if it has no custom name */
	public static final String getItemName(ItemStack item)
	{
		if (item == null || !item.hasItemMeta())
			return null;
		ItemMeta meta = item.getItemMeta();
		return meta.hasDisplayName() ? meta.getDisplayName() : null;
	}

	// ////////////////////////////////////////////////////////////////

	/** Returns a list containing the items the player is missing to cover the items */
	public static final List<ItemStack> findMissing(Player player, Collection<ItemStack> items)
	{
		return findMissing(player.getInventory(), items);
	}

	/** Returns a list containing the items the inventory is missing to cover the items */
	public static final List<ItemStack> findMissing(Inventory inventory, Collection<ItemStack> items)
	{
		List<ItemStack> list = new LinkedList<ItemStack>();
		if (items == null || items.size() == 0)
			return list;
		for (ItemStack item : items)
		{
			int requiredAmount = countMissing(inventory, item);
			if (requiredAmount > 0)
			{
				ItemStack stack = item.clone();
				stack.setAmount(requiredAmount);
				list.add(stack);
			}
		}
		return list;
	}

	/** Returns the number of items the player is missing to cover the itemstack */
	public static final int countMissing(Player player, ItemStack itemstack)
	{
		return countMissing(player.getInventory(), itemstack);
	}

	/** Returns the number of items the inventory is missing to cover the itemstack */
	public static final int countMissing(Inventory inventory, ItemStack itemstack)
	{
		if (itemstack == null)
			return 0;
		int amount = itemstack.getAmount();
		for (ItemStack item : inventory.getContents())
		{
			if (item == null || !isItemSubType(itemstack, item))
				continue;

			amount -= item.getAmount();
			if (amount <= 0)
				break;
		}
		return Math.max(0, amount);
	}

	/** Removes the given amount of the resource from the given player */
	public static final void remove(Player player, Collection<ItemStack> items)
	{
		remove(player.getInventory(), items);
	}

	/** Removes the given amount of the resource from the given inventory */
	public static final void remove(Inventory inventory, Collection<ItemStack> items)
	{
		if (items != null)
			for (ItemStack item : items)
				remove(inventory, item);
	}

	/** Removes the given amount of the resource from the given player */
	public static final void remove(Player player, ItemStack itemstack)
	{
		remove(player.getInventory(), itemstack);
	}

	/** Removes the given amount of the resource from the given inventory */
	public static final void remove(Inventory inventory, ItemStack itemstack)
	{
		if (itemstack == null)
			return;
		int amount = itemstack.getAmount();
		for (int i = 0; i < inventory.getSize(); ++i)
		{
			ItemStack item = inventory.getItem(i);
			if (item == null || !isItemSubType(itemstack, item))
				continue;

			int count = item.getAmount();
			if (count > amount)
			{
				item.setAmount(count - amount);
				return;
			}
			amount -= count;
			inventory.setItem(i, null);
		}
	}

	/** Adds the given amount of the resource to the given player; returns a list of all items that could not be added */
	public static final List<ItemStack> add(Player player, Collection<ItemStack> items)
	{
		return add(player.getInventory(), items);
	}

	/** Adds the given amount of the resource to the given inventory; returns a list of all items that could not be added */
	public static final List<ItemStack> add(Inventory inventory, Collection<ItemStack> items)
	{
		List<ItemStack> list = new LinkedList<ItemStack>();
		if (items == null || items.size() == 0)
			return list;
		Map<Integer, ItemStack> map = inventory.addItem(items.toArray(new ItemStack[0]));
		if (map != null)
			list.addAll(map.values());
		return list;
	}

	/** Adds the given amount of the resource to the given inventory; drops all items not added at the given location */
	public static final void addWithDrop(Player player, Collection<ItemStack> items)
	{
		addWithDrop(player.getInventory(), player.getLocation(), items);
	}

	/** Adds the given amount of the resource to the given inventory; drops all items not added at the given location */
	public static final void addWithDrop(Inventory inventory, Location location, Collection<ItemStack> items)
	{
		for (ItemStack item : add(inventory, items))
			location.getWorld().dropItemNaturally(location, item);
	}

	/** Adds the given amount of the resource to the given player; returns the amount of items that could not be added */
	public static final int add(Player player, ItemStack itemstack)
	{
		return add(player.getInventory(), itemstack);
	}

	/** Adds the given amount of the resource to the given inventory; returns the amount of items that could not be added */
	public static final int add(Inventory inventory, ItemStack itemstack)
	{
		if (itemstack == null)
			return 0;
		Map<Integer, ItemStack> map = inventory.addItem(itemstack);
		if (map != null)
			for (ItemStack item : map.values())
				return item.getAmount();
		return 0;
	}

	/** Adds the given amount of the resource to the given inventory; drops all items not added next to the player */
	public static final void addWithDrop(Player player, ItemStack itemstack)
	{
		addWithDrop(player.getInventory(), player.getLocation(), itemstack);
	}

	/** Adds the given amount of the resource to the given inventory; drops all items not added at the given location */
	public static final void addWithDrop(Inventory inventory, Location location, ItemStack itemstack)
	{
		ItemStack clone = itemstack.clone();
		clone.setAmount(add(inventory, itemstack));
		if (clone.getAmount() > 0)
			location.getWorld().dropItemNaturally(location, clone);
	}

	// ////////////////////////////////////////////////////////////////

	/** Returns true if the given item is consumable */
	public static boolean isConsumable(ItemStack item)
	{
		return isEdible(item) || isDrinkable(item);
	}

	/** Returns true if the given item is edible */
	public static boolean isEdible(ItemStack item)
	{
		return item == null ? false : item.getType().isEdible();
	}

	/** Returns true if the given item is drinkable */
	public static boolean isDrinkable(ItemStack item)
	{
		if (item == null)
			return false;
		Material type = item.getType();
		return type == Material.POTION || type == Material.MILK_BUCKET;
	}

	/** Returns true if the given item is a weapon */
	public static boolean isWeapon(ItemStack item)
	{
		return isBow(item) || isSword(item);
	}

	/** Returns true if the given item is a bow */
	public static boolean isBow(ItemStack item)
	{
		return item == null ? false : item.getType() == Material.BOW;
	}

	/** Returns true if the given item is a sword */
	public static boolean isSword(ItemStack item)
	{
		if (item == null)
			return false;
		Material type = item.getType();
		return type == Material.WOOD_SWORD || type == Material.STONE_SWORD || type == Material.GOLD_SWORD || type == Material.IRON_SWORD || type == Material.DIAMOND_SWORD;
	}

	/** Returns true if the given item is no longer a valid item (either null, amount less or equal to 0, or durability is used up */
	public static final boolean isDestroyed(ItemStack item)
	{
		if (item == null || item.getAmount() <= 0)
			return false;
		return (item.getDurability() > item.getType().getMaxDurability());
	}
}
