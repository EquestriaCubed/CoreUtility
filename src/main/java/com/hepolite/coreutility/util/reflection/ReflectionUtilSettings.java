package com.hepolite.coreutility.util.reflection;

import com.hepolite.coreutility.plugin.CorePlugin;
import com.hepolite.coreutility.settings.Settings;

public class ReflectionUtilSettings extends Settings
{
	public ReflectionUtilSettings(CorePlugin plugin)
	{
		super(plugin, "Utility", "nmsMappings");
	}

	@Override
	protected void addDefaultValues()
	{
		add_v1_12_R1_mappings();
		add_v1_11_R1_mappings();
	}

	@Override
	protected void onSaveConfigFile()
	{}

	@Override
	protected void onLoadConfigFile()
	{}

	// //////////////////////////////////////////////////////////////

	/** Adds in the default values for all mappings, under the specified version id */
	private final void addDefaultMappings(String version)
	{
		set(version + ".AxisAlignedBB.class", "AxisAlignedBB");
		set(version + ".AxisAlignedBB.field.minX", "minX");
		set(version + ".AxisAlignedBB.field.minY", "minY");
		set(version + ".AxisAlignedBB.field.minZ", "minZ");
		set(version + ".AxisAlignedBB.field.maxX", "maxX");
		set(version + ".AxisAlignedBB.field.maxY", "maxY");
		set(version + ".AxisAlignedBB.field.maxZ", "maxZ");

		set(version + ".Entity.class", "Entity");
		set(version + ".Entity.method.getBoundingBox", "getBoundingBox");
		set(version + ".Entity.method.save", "save");
		set(version + ".Entity.method.load", "load");

		set(version + ".ItemStack.class", "ItemStack");
		set(version + ".ItemStack.method.setTag", "setTag");
		set(version + ".ItemStack.method.getTag", "getTag");
		set(version + ".ItemStack.method.hasTag", "hasTag");

		set(version + ".MovingObjectPosition.class", "MovingObjectPosition");
		set(version + ".MovingObjectPosition.field.pos", "pos");

		set(version + ".NBTBase.class", "NBTBase");
		set(version + ".NBTTagByte.class", "NBTTagByte");
		set(version + ".NBTTagByte.method.asByte", "asByte");
		set(version + ".NBTTagByteArray.class", "NBTTagByteArray");
		set(version + ".NBTTagByteArray.method.asByteArray", "asByteArray");
		set(version + ".NBTTagCompound.class", "NBTTagCompound");
		set(version + ".NBTTagCompound.method.getByte", "getByte");
		set(version + ".NBTTagCompound.method.getByteArray", "getByteArray");
		set(version + ".NBTTagCompound.method.getCompound", "getCompound");
		set(version + ".NBTTagCompound.method.getDouble", "getDouble");
		set(version + ".NBTTagCompound.method.getFloat", "getFloat");
		set(version + ".NBTTagCompound.method.getInt", "getInt");
		set(version + ".NBTTagCompound.method.getIntArray", "getIntArray");
		set(version + ".NBTTagCompound.method.getKeys", "getKeys");
		set(version + ".NBTTagCompound.method.getLong", "getLong");
		set(version + ".NBTTagCompound.method.getShort", "getShort");
		set(version + ".NBTTagCompound.method.getString", "getString");
		set(version + ".NBTTagCompound.method.getTag", "getTag");
		set(version + ".NBTTagCompound.method.hasKey", "hasKey");
		set(version + ".NBTTagCompound.method.remove", "remove");
		set(version + ".NBTTagCompound.method.setByte", "setByte");
		set(version + ".NBTTagCompound.method.setByteArray", "setByteArray");
		set(version + ".NBTTagCompound.method.setDouble", "setDouble");
		set(version + ".NBTTagCompound.method.setFloat", "setFloat");
		set(version + ".NBTTagCompound.method.setInt", "setInt");
		set(version + ".NBTTagCompound.method.setIntArray", "setIntArray");
		set(version + ".NBTTagCompound.method.setLong", "setLong");
		set(version + ".NBTTagCompound.method.setShort", "setShort");
		set(version + ".NBTTagCompound.method.setString", "setString");
		set(version + ".NBTTagCompound.method.setTag", "setTag");
		set(version + ".NBTTagDouble.class", "NBTTagDouble");
		set(version + ".NBTTagDouble.method.asDouble", "asDouble");
		set(version + ".NBTTagFloat.class", "NBTTagFloat");
		set(version + ".NBTTagFloat.method.asFloat", "asFloat");
		set(version + ".NBTTagInt.class", "NBTTagInt");
		set(version + ".NBTTagInt.method.asInt", "asInt");
		set(version + ".NBTTagIntArray.class", "NBTTagIntArray");
		set(version + ".NBTTagIntArray.method.asInt", "asIntArray");
		set(version + ".NBTTagList.class", "NBTTagList");
		set(version + ".NBTTagList.method.add", "add");
		set(version + ".NBTTagList.method.get", "get");
		set(version + ".NBTTagList.method.remove", "remove");
		set(version + ".NBTTagList.method.size", "size");
		set(version + ".NBTTagLong.class", "NBTTagLong");
		set(version + ".NBTTagLong.method.asLong", "asLong");
		set(version + ".NBTTagShort.class", "NBTTagShort");
		set(version + ".NBTTagShort.method.asShort", "asShort");
		set(version + ".NBTTagString.class", "NBTTagString");
		set(version + ".NBTTagString.method.asString", "asString");

		set(version + ".Vec3D.class", "Vec3D");
		set(version + ".Vec3D.field.x", "x");
		set(version + ".Vec3D.field.y", "y");
		set(version + ".Vec3D.field.z", "z");

		set(version + ".World.class", "World");
		set(version + ".World.method.rayTrace", "rayTrace");
	}

	/** Adds in all mappings for the specified version */
	private final void add_v1_11_R1_mappings()
	{
		final String version = "v1_11_R1";
		addDefaultMappings(version);

		set(version + ".AxisAlignedBB.field.minX", "a");
		set(version + ".AxisAlignedBB.field.minY", "b");
		set(version + ".AxisAlignedBB.field.minZ", "c");
		set(version + ".AxisAlignedBB.field.maxX", "d");
		set(version + ".AxisAlignedBB.field.maxY", "e");
		set(version + ".AxisAlignedBB.field.maxZ", "f");

		set(version + ".Entity.method.save", "e");
		set(version + ".Entity.method.load", "f");

		set(version + ".NBTTagByte.method.asByte", "g");
		set(version + ".NBTTagByteArray.method.asByteArray", "c");
		set(version + ".NBTTagCompound.method.getTag", "get");
		set(version + ".NBTTagCompound.method.getKeys", "c");
		set(version + ".NBTTagCompound.method.setTag", "set");
		set(version + ".NBTTagFloat.method.asFloat", "i");
		set(version + ".NBTTagInt.method.asInt", "e");
		set(version + ".NBTTagIntArray.method.asIntArray", "d");
		set(version + ".NBTTagList.method.get", "h");
		set(version + ".NBTTagLong.method.asLong", "d");
		set(version + ".NBTTagShort.method.asShort", "f");
		set(version + ".NBTTagString.method.asString", "c_");
	}

	/** Adds in all mappings for the specified version */
	private final void add_v1_12_R1_mappings()
	{
		final String version = "v1_12_R1";
		addDefaultMappings(version);

		set(version + ".AxisAlignedBB.field.minX", "a");
		set(version + ".AxisAlignedBB.field.minY", "b");
		set(version + ".AxisAlignedBB.field.minZ", "c");
		set(version + ".AxisAlignedBB.field.maxX", "d");
		set(version + ".AxisAlignedBB.field.maxY", "e");
		set(version + ".AxisAlignedBB.field.maxZ", "f");

		set(version + ".Entity.method.load", "f");

		set(version + ".NBTTagByte.method.asByte", "g");
		set(version + ".NBTTagByteArray.method.asByteArray", "c");
		set(version + ".NBTTagCompound.method.getTag", "get");
		set(version + ".NBTTagCompound.method.getKeys", "c");
		set(version + ".NBTTagCompound.method.setTag", "set");
		set(version + ".NBTTagFloat.method.asFloat", "i");
		set(version + ".NBTTagInt.method.asInt", "e");
		set(version + ".NBTTagIntArray.method.asIntArray", "d");
		set(version + ".NBTTagList.method.get", "i");
		set(version + ".NBTTagLong.method.asLong", "d");
		set(version + ".NBTTagShort.method.asShort", "f");
		set(version + ".NBTTagString.method.asString", "c_");
	}
}
