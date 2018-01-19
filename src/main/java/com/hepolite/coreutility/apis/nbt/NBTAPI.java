package com.hepolite.coreutility.apis.nbt;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

import com.hepolite.coreutility.util.reflection.reflected.REntity;
import com.hepolite.coreutility.util.reflection.reflected.RItemStack;
import com.hepolite.coreutility.util.reflection.reflected.RNBTTag;

public class NBTAPI
{
	/** Returns a new ItemStack that is compatible with the NBTAPI system right off the bat; the returned ItemStack will be a CraftItemStack, or a regular ItemStack if something went wrong */
	public static final ItemStack getItemStack(Material material, int amount, int meta)
	{
		ItemStack itemStack = new ItemStack(material, amount, (short) meta);
		return (ItemStack) RItemStack.cbAsCraftCopy.invoke(RItemStack.cbClass.getHandle(), itemStack);
	}

	/** Assigns a NBTTag to the given ItemStack. Returns the item that was passed in */
	public static final ItemStack setTag(ItemStack itemStack, NBTTag tag)
	{
		Object nmsItemStack = RItemStack.cbHandle.get(itemStack);
		RItemStack.nmsSetTag.invoke(nmsItemStack, tag.getHandle());
		return itemStack;
	}

	/** Returns a new tag from the given ItemStack */
	public static final NBTTag getTag(ItemStack itemStack)
	{
		Object nmsItemStack = RItemStack.cbHandle.get(itemStack);
		return new NBTTag(RItemStack.nmsGetTag.invoke(nmsItemStack));
	}

	/** Returns true if the given ItemStack has a NBTTag associated with it */
	public static final boolean hasTag(ItemStack itemStack)
	{
		Object nmsItemStack = RItemStack.cbHandle.get(itemStack);
		return (boolean) RItemStack.nmsHasTag.invoke(nmsItemStack);
	}

	/** Assigns a NBTTag to the given Entity. Returns the entity that was passed in */
	public static final Entity setTag(Entity entity, NBTTag tag)
	{
		Object nmsEntity = REntity.cbGetHandle.invoke(entity);
		REntity.nmsSave.invoke(nmsEntity, tag.getHandle());
		return entity;
	}

	/** Returns a new tag from the given Entity; this will save the entire entity into the tag, if you change the tag you should immediately update the entity with the new data */
	public static final NBTTag getTag(Entity entity)
	{
		Object nmsEntity = REntity.cbGetHandle.invoke(entity);
		return new NBTTag(REntity.nmsLoad.invoke(nmsEntity));
	}

	// ////////////////////////////////////////////////////////////////////////

	/** Returns the type of the tag that is passed in */
	@SuppressWarnings({
			"unchecked",
			"rawtypes"
	})
	public static final NBTField getField(Object tag)
	{
		Class nbtClass = tag.getClass();
		if (nbtClass.isAssignableFrom(RNBTTag.Byte.nmsClass.getHandle()))
			return NBTField.BYTE;
		else if (nbtClass.isAssignableFrom(RNBTTag.ByteArray.nmsClass.getHandle()))
			return NBTField.BYTE_ARRAY;
		else if (nbtClass.isAssignableFrom(RNBTTag.Double.nmsClass.getHandle()))
			return NBTField.DOUBLE;
		else if (nbtClass.isAssignableFrom(RNBTTag.Float.nmsClass.getHandle()))
			return NBTField.FLOAT;
		else if (nbtClass.isAssignableFrom(RNBTTag.Int.nmsClass.getHandle()))
			return NBTField.INT;
		else if (nbtClass.isAssignableFrom(RNBTTag.IntArray.nmsClass.getHandle()))
			return NBTField.INT_ARRAY;
		else if (nbtClass.isAssignableFrom(RNBTTag.List.nmsClass.getHandle()))
			return NBTField.LIST;
		else if (nbtClass.isAssignableFrom(RNBTTag.Long.nmsClass.getHandle()))
			return NBTField.LONG;
		else if (nbtClass.isAssignableFrom(RNBTTag.Short.nmsClass.getHandle()))
			return NBTField.SHORT;
		else if (nbtClass.isAssignableFrom(RNBTTag.String.nmsClass.getHandle()))
			return NBTField.STRING;
		else if (nbtClass.isAssignableFrom(RNBTTag.Compound.nmsClass.getHandle()))
			return NBTField.TAG;
		else
			return NBTField.INVALID;
	}
}
