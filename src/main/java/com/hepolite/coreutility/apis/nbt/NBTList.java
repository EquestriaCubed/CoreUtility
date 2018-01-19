package com.hepolite.coreutility.apis.nbt;

import java.util.ArrayList;
import java.util.List;

import com.hepolite.coreutility.log.Log;
import com.hepolite.coreutility.util.reflection.ReflectedMethod;
import com.hepolite.coreutility.util.reflection.reflected.RNBTTag;

public class NBTList
{
	private final Object handle;
	private final List<NBTField> fields = new ArrayList<NBTField>();

	/** Do not use this constructor unless you know what you're doing! */
	public NBTList(Object handle)
	{
		this.handle = handle;

		for (int i = 0; i < size(); ++i)
			fields.add(NBTAPI.getField(get(i)));
	}

	/** Constructs a new empty NBT Tag */
	public NBTList()
	{
		this(RNBTTag.List.nmsClass.create());
	}

	/** Returns the handle for the tag */
	public final Object getHandle()
	{
		return handle;
	}

	// //////////////////////////////////////////////////////////////

	/** Retrieves the given value using the given method */
	private final Object getValue(int index, ReflectedMethod method)
	{
		return method.invoke(RNBTTag.List.nmsGet.invoke(handle, index));
	}

	/** Stores the given value in the tag */
	public final void add(Object value)
	{
		if (value instanceof Boolean)
			addBoolean((boolean) value);
		else if (value instanceof Byte)
			addByte((byte) value);
		else if (value instanceof byte[])
			addByteArray((byte[]) value);
		else if (value instanceof Double)
			addDouble((double) value);
		else if (value instanceof Float)
			addFloat((float) value);
		else if (value instanceof Integer)
			addInt((int) value);
		else if (value instanceof int[])
			addIntArray((int[]) value);
		else if (value instanceof NBTList)
			addList((NBTList) value);
		else if (value instanceof Long)
			addLong((long) value);
		else if (value instanceof Short)
			addShort((short) value);
		else if (value instanceof String)
			addString((String) value);
		else if (value instanceof NBTTag)
			addTag((NBTTag) value);
		else
			Log.warning("Unknown NBTList value " + value);
	}

	/** Retrieves the raw NBT tag stored under the given index */
	private final Object get(int index)
	{
		return RNBTTag.List.nmsGet.invoke(handle, index);
	}

	/** Stores the given value in the tag */
	public final void addBoolean(boolean value)
	{
		addByte((byte) (value ? 1 : 0));
	}

	/** Retrieves the given value in the tag under the given index */
	public final boolean getBoolean(int index)
	{
		return getBoolean(index, false);
	}

	/** Retrieves the given value in the tag under the given index; returns the default value if the index was not found, or an invalid type was found */
	public final boolean getBoolean(int index, boolean def)
	{
		return getByte(index, (byte) (def ? 1 : 0)) == 0 ? false : true;
	}

	/** Stores the given value in the tag */
	public final void addByte(byte value)
	{
		fields.add(NBTField.BYTE);
		RNBTTag.List.nmsAdd.invoke(handle, RNBTTag.Byte.nmsClass.create(value));
	}

	/** Retrieves the given value in the tag under the given index */
	public final byte getByte(int index)
	{
		return getByte(index, (byte) 0);
	}

	/** Retrieves the given value in the tag under the given index; returns the default value if the index was not found, or an invalid type was found */
	public final byte getByte(int index, byte def)
	{
		if (getField(index) == NBTField.BYTE)
			return (byte) getValue(index, RNBTTag.Byte.nmsAsByte);
		return def;
	}

	/** Stores the given value in the tag */
	public final void addByteArray(byte[] value)
	{
		fields.add(NBTField.BYTE_ARRAY);
		RNBTTag.List.nmsAdd.invoke(handle, RNBTTag.ByteArray.nmsClass.create(value));
	}

	/** Retrieves the given value in the tag under the given index */
	public final byte[] getByteArray(int index)
	{
		return getByteArray(index, new byte[] {});
	}

	/** Retrieves the given value in the tag under the given index; returns the default value if the index was not found, or an invalid type was found */
	public final byte[] getByteArray(int index, byte[] def)
	{
		if (getField(index) == NBTField.BYTE_ARRAY)
			return (byte[]) getValue(index, RNBTTag.ByteArray.nmsAsByteArray);
		return def;
	}

	/** Stores the given value in the tag */
	public final void addDouble(double value)
	{
		fields.add(NBTField.DOUBLE);
		RNBTTag.List.nmsAdd.invoke(handle, RNBTTag.Double.nmsClass.create(value));
	}

	/** Retrieves the given value in the tag under the given index */
	public final double getDouble(int index)
	{
		return getDouble(index, 0.0);
	}

	/** Retrieves the given value in the tag under the given index; returns the default value if the index was not found, or an invalid type was found */
	public final double getDouble(int index, double def)
	{
		if (getField(index) == NBTField.DOUBLE)
			return (double) getValue(index, RNBTTag.Double.nmsAsDouble);
		return def;
	}

	/** Stores the given value in the tag */
	public final void addFloat(float value)
	{
		fields.add(NBTField.FLOAT);
		RNBTTag.List.nmsAdd.invoke(handle, RNBTTag.Float.nmsClass.create(value));
	}

	/** Retrieves the given value in the tag under the given index */
	public final float getFloat(int index)
	{
		return getFloat(index, 0.0f);
	}

	/** Retrieves the given value in the tag under the given index; returns the default value if the index was not found, or an invalid type was found */
	public final float getFloat(int index, float def)
	{
		if (getField(index) == NBTField.FLOAT)
			return (float) getValue(index, RNBTTag.Float.nmsAsFloat);
		return def;
	}

	/** Stores the given value in the tag */
	public final void addInt(int value)
	{
		fields.add(NBTField.INT);
		RNBTTag.List.nmsAdd.invoke(handle, RNBTTag.Int.nmsClass.create(value));
	}

	/** Retrieves the given value in the tag under the given index */
	public final int getInt(int index)
	{
		return getInt(index, 0);
	}

	/** Retrieves the given value in the tag under the given index; returns the default value if the index was not found, or an invalid type was found */
	public final int getInt(int index, int def)
	{
		if (getField(index) == NBTField.INT)
			return (int) getValue(index, RNBTTag.Int.nmsAsInt);
		return def;
	}

	/** Stores the given value in the tag */
	public final void addIntArray(int[] value)
	{
		fields.add(NBTField.INT_ARRAY);
		RNBTTag.List.nmsAdd.invoke(handle, RNBTTag.IntArray.nmsClass.create(value));
	}

	/** Retrieves the given value in the tag under the given index */
	public final int[] getIntArray(int index)
	{
		return getIntArray(index, new int[] {});
	}

	/** Retrieves the given value in the tag under the given index; returns the default value if the index was not found, or an invalid type was found */
	public final int[] getIntArray(int index, int[] def)
	{
		if (getField(index) == NBTField.INT_ARRAY)
			return (int[]) getValue(index, RNBTTag.IntArray.nmsAsIntArray);
		return def;
	}

	/** Stores the given value in the tag */
	public final void addList(NBTList value)
	{
		fields.add(NBTField.LIST);
		RNBTTag.List.nmsAdd.invoke(handle, value.handle);
	}

	/** Retrieves the given value in the tag under the given index */
	public final NBTList getList(int index)
	{
		return getList(index, new NBTList());
	}

	/** Retrieves the given value in the tag under the given index; returns the default value if the index was not found, or an invalid type was found */
	public final NBTList getList(int index, NBTList def)
	{
		if (getField(index) == NBTField.LIST)
			return new NBTList(RNBTTag.List.nmsGet.invoke(handle, index));
		return def;
	}

	/** Stores the given value in the tag */
	public final void addLong(long value)
	{
		fields.add(NBTField.LONG);
		RNBTTag.List.nmsAdd.invoke(handle, RNBTTag.Long.nmsClass.create(value));
	}

	/** Retrieves the given value in the tag under the given index */
	public final long getLong(int index)
	{
		return getLong(index, 0);
	}

	/** Retrieves the given value in the tag under the given index; returns the default value if the index was not found, or an invalid type was found */
	public final long getLong(int index, long def)
	{
		if (getField(index) == NBTField.LONG)
			return (long) getValue(index, RNBTTag.Long.nmsAsLong);
		return def;
	}

	/** Stores the given value in the tag */
	public final void addShort(short value)
	{
		fields.add(NBTField.SHORT);
		RNBTTag.List.nmsAdd.invoke(handle, RNBTTag.Short.nmsClass.create(value));
	}

	/** Retrieves the given value in the tag under the given index */
	public final short getShort(int index)
	{
		return getShort(index, (short) 0);
	}

	/** Retrieves the given value in the tag under the given index; returns the default value if the index was not found, or an invalid type was found */
	public final short getShort(int index, short def)
	{
		if (getField(index) == NBTField.SHORT)
			return (short) getValue(index, RNBTTag.Short.nmsAsShort);
		return def;
	}

	/** Stores the given value in the tag */
	public final void addString(String value)
	{
		fields.add(NBTField.STRING);
		RNBTTag.List.nmsAdd.invoke(handle, RNBTTag.String.nmsClass.create(value));
	}

	/** Retrieves the given value in the tag under the given index */
	public final String getString(int index)
	{
		return getString(index, "");
	}

	/** Retrieves the given value in the tag under the given index; returns the default value if the index was not found, or an invalid type was found */
	public final String getString(int index, String def)
	{
		if (getField(index) == NBTField.STRING)
			return (String) getValue(index, RNBTTag.String.nmsAsString);
		return def;
	}

	/** Stores the given value in the tag */
	public final void addTag(NBTTag value)
	{
		fields.add(NBTField.TAG);
		RNBTTag.List.nmsAdd.invoke(handle, value.getHandle());
	}

	/** Retrieves the given value in the tag under the given index */
	public final NBTTag getTag(int index)
	{
		return getTag(index, new NBTTag());
	}

	/** Retrieves the given value in the tag under the given index; returns the default value if the index was not found, or an invalid type was found */
	public final NBTTag getTag(int index, NBTTag def)
	{
		if (getField(index) == NBTField.TAG)
			return new NBTTag(RNBTTag.List.nmsGet.invoke(handle, index));
		return def;
	}

	// //////////////////////////////////////////////////////////////

	/** Removes the value with the given index */
	public final void remove(int index)
	{
		if (index < 0 || index >= size())
			return;
		RNBTTag.List.nmsRemove.invoke(handle, index);
		fields.remove(index);
	}

	/** Returns the size of the list */
	public final int size()
	{
		return (int) RNBTTag.List.nmsSize.invoke(handle);
	}

	/** Returns the type of the value stored under the given index; returns null if there was no value stored under the index */
	public final NBTField getField(int index)
	{
		return index < 0 || index > fields.size() ? null : fields.get(index);
	}

	@Override
	public final String toString()
	{
		return "<" + (handle == null ? "null" : handle.toString()) + ";" + fields.toString() + ">";
	}
}
