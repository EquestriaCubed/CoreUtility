package com.hepolite.coreutility.apis.nbt;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.hepolite.coreutility.log.Log;
import com.hepolite.coreutility.util.reflection.reflected.RNBTTag;

public class NBTTag
{
	private final Object handle;
	private final Map<String, NBTField> fields = new HashMap<String, NBTField>();

	/** Do not use this constructor unless you know what you're doing! */
	public NBTTag(Object handle)
	{
		this.handle = handle;

		for (String key : getKeys())
			fields.put(key, NBTAPI.getField(get(key)));
	}

	/** Constructs a new empty NBT Tag */
	public NBTTag()
	{
		this(RNBTTag.Compound.nmsClass.create());
	}

	/** Returns the handle for the tag */
	public final Object getHandle()
	{
		return handle;
	}

	// //////////////////////////////////////////////////////////////

	/** Stores the given value in the tag under the given key */
	public final void set(String key, Object value)
	{
		if (value instanceof Boolean)
			setBoolean(key, (boolean) value);
		else if (value instanceof Byte)
			setByte(key, (byte) value);
		else if (value instanceof Double)
			setDouble(key, (double) value);
		else if (value instanceof Float)
			setFloat(key, (float) value);
		else if (value instanceof Integer)
			setInt(key, (int) value);
		else if (value instanceof NBTList)
			setList(key, (NBTList) value);
		else if (value instanceof Long)
			setLong(key, (long) value);
		else if (value instanceof Short)
			setShort(key, (short) value);
		else if (value instanceof String)
			setString(key, (String) value);
		else if (value instanceof NBTTag)
			setTag(key, (NBTTag) value);
		else
			Log.warning("Unknown NBTTag value " + value);
	}

	/** Retrieves the raw NBT tag stored under the given key */
	private final Object get(String key)
	{
		return RNBTTag.Compound.nmsGetTag.invoke(handle, key);
	}

	/** Stores the given value in the tag under the given key */
	public final void setBoolean(String key, boolean value)
	{
		setByte(key, (byte) (value ? 1 : 0));
	}

	/** Retrieves the given value in the tag under the given key */
	public final boolean getBoolean(String key)
	{
		return getBoolean(key, false);
	}

	/** Retrieves the given value in the tag under the given key; returns the default value if the key was not found, or an invalid type was found */
	public final boolean getBoolean(String key, boolean def)
	{
		return getByte(key, (byte) (def ? 1 : 0)) == 0 ? false : true;
	}

	/** Stores the given value in the tag under the given key */
	public final void setByte(String key, byte value)
	{
		fields.put(key, NBTField.BYTE);
		RNBTTag.Compound.nmsSetByte.invoke(handle, key, value);
	}

	/** Retrieves the given value in the tag under the given key */
	public final byte getByte(String key)
	{
		return getByte(key, (byte) 0);
	}

	/** Retrieves the given value in the tag under the given key; returns the default value if the key was not found, or an invalid type was found */
	public final byte getByte(String key, byte def)
	{
		if (getField(key) == NBTField.BYTE)
			return (byte) RNBTTag.Compound.nmsGetByte.invoke(handle, key);
		return def;
	}

	/** Stores the given value in the tag under the given key */
	public final void setDouble(String key, double value)
	{
		fields.put(key, NBTField.DOUBLE);
		RNBTTag.Compound.nmsSetDouble.invoke(handle, key, value);
	}

	/** Retrieves the given value in the tag under the given key */
	public final double getDouble(String key)
	{
		return getDouble(key, 0.0);
	}

	/** Retrieves the given value in the tag under the given key; returns the default value if the key was not found, or an invalid type was found */
	public final double getDouble(String key, double def)
	{
		if (getField(key) == NBTField.DOUBLE)
			return (double) RNBTTag.Compound.nmsGetDouble.invoke(handle, key);
		return def;
	}

	/** Stores the given value in the tag under the given key */
	public final void setFloat(String key, float value)
	{
		fields.put(key, NBTField.FLOAT);
		RNBTTag.Compound.nmsSetFloat.invoke(handle, key, value);
	}

	/** Retrieves the given value in the tag under the given key */
	public final float getFloat(String key)
	{
		return getFloat(key, 0.0f);
	}

	/** Retrieves the given value in the tag under the given key; returns the default value if the key was not found, or an invalid type was found */
	public final float getFloat(String key, float def)
	{
		if (getField(key) == NBTField.FLOAT)
			return (float) RNBTTag.Compound.nmsGetFloat.invoke(handle, key);
		return def;
	}

	/** Stores the given value in the tag under the given key */
	public final void setInt(String key, int value)
	{
		fields.put(key, NBTField.INT);
		RNBTTag.Compound.nmsSetInt.invoke(handle, key, value);
	}

	/** Retrieves the given value in the tag under the given key */
	public final int getInt(String key)
	{
		return getInt(key, 0);
	}

	/** Retrieves the given value in the tag under the given key; returns the default value if the key was not found, or an invalid type was found */
	public final int getInt(String key, int def)
	{
		if (getField(key) == NBTField.INT)
			return (int) RNBTTag.Compound.nmsGetInt.invoke(handle, key);
		return def;
	}

	/** Stores the given value in the tag under the given key */
	public final void setList(String key, NBTList value)
	{
		fields.put(key, NBTField.LIST);
		RNBTTag.Compound.nmsSetTag.invoke(handle, key, value.getHandle());
	}

	/** Retrieves the given value in the tag under the given key */
	public final NBTList getList(String key)
	{
		return getList(key, new NBTList());
	}

	/** Retrieves the given value in the tag under the given key; returns the default value if the key was not found, or an invalid type was found */
	public final NBTList getList(String key, NBTList def)
	{
		if (getField(key) == NBTField.LIST)
			return new NBTList(RNBTTag.Compound.nmsGetTag.invoke(handle, key));
		return def;
	}

	/** Stores the given value in the tag under the given key */
	public final void setLong(String key, long value)
	{
		fields.put(key, NBTField.LONG);
		RNBTTag.Compound.nmsSetLong.invoke(handle, key, value);
	}

	/** Retrieves the given value in the tag under the given key */
	public final long getLong(String key)
	{
		return getLong(key, 0);
	}

	/** Retrieves the given value in the tag under the given key; returns the default value if the key was not found, or an invalid type was found */
	public final long getLong(String key, long def)
	{
		if (getField(key) == NBTField.LONG)
			return (long) RNBTTag.Compound.nmsGetLong.invoke(handle, key);
		return def;
	}

	/** Stores the given value in the tag under the given key */
	public final void setShort(String key, short value)
	{
		fields.put(key, NBTField.SHORT);
		RNBTTag.Compound.nmsSetShort.invoke(handle, key, value);
	}

	/** Retrieves the given value in the tag under the given key */
	public final short getShort(String key)
	{
		return getShort(key, (short) 0);
	}

	/** Retrieves the given value in the tag under the given key; returns the default value if the key was not found, or an invalid type was found */
	public final short getShort(String key, short def)
	{
		if (getField(key) == NBTField.SHORT)
			return (short) RNBTTag.Compound.nmsGetShort.invoke(handle, key);
		return def;
	}

	/** Stores the given value in the tag under the given key */
	public final void setString(String key, String value)
	{
		fields.put(key, NBTField.STRING);
		RNBTTag.Compound.nmsSetString.invoke(handle, key, value);
	}

	/** Retrieves the given value in the tag under the given key */
	public final String getString(String key)
	{
		return getString(key, "");
	}

	/** Retrieves the given value in the tag under the given key; returns the default value if the key was not found, or an invalid type was found */
	public final String getString(String key, String def)
	{
		if (getField(key) == NBTField.STRING)
			return (String) RNBTTag.Compound.nmsGetString.invoke(handle, key);
		return def;
	}

	/** Stores the given value in the tag under the given key */
	public final void setTag(String key, NBTTag value)
	{
		fields.put(key, NBTField.TAG);
		RNBTTag.Compound.nmsSetTag.invoke(handle, key, value.handle);
	}

	/** Retrieves the given value in the tag under the given key */
	public final NBTTag getTag(String key)
	{
		return getTag(key, new NBTTag());
	}

	/** Retrieves the given value in the tag under the given key; returns the default value if the key was not found, or an invalid type was found */
	public final NBTTag getTag(String key, NBTTag def)
	{
		if (getField(key) == NBTField.TAG)
			return new NBTTag(RNBTTag.Compound.nmsGetTag.invoke(handle, key));
		return def;
	}

	// //////////////////////////////////////////////////////////////

	/** Removes the value with the given key */
	public final void remove(String key)
	{
		if (!has(key))
			return;
		RNBTTag.Compound.nmsRemove.invoke(handle, key);
		fields.remove(key);
	}
	
	/** Returns true if the given key exists in the tag */
	public final boolean has(String key)
	{
		return (boolean) RNBTTag.Compound.nmsHasKey.invoke(handle, key);
	}

	/** Returns a set of all keys stored in the tag; this will never return null */
	public final Set<String> getKeys()
	{
		Set<String> set = new HashSet<String>();
		@SuppressWarnings("unchecked")
		Set<String> keys = handle == null ? null : (Set<String>) RNBTTag.Compound.nmsGetKeys.invoke(handle);
		if (keys != null)
			set.addAll(keys);
		return set;
	}

	/** Returns the type of the value stored under the given key; returns null if there was no value stored under the key */
	public final NBTField getField(String key)
	{
		return fields.get(key);
	}

	// //////////////////////////////////////////////////////////////

	@Override
	public final String toString()
	{
		return "<" + (handle == null ? "null" : handle.toString()) + ";" + fields.toString() + ">";
	}
}
