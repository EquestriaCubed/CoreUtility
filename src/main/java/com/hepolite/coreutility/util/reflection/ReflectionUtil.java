package com.hepolite.coreutility.util.reflection;

import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

import com.hepolite.coreutility.log.Log;
import com.hepolite.coreutility.plugin.CorePlugin;
import com.hepolite.coreutility.settings.Settings;
import com.hepolite.coreutility.util.reflection.reflected.RAxisAlignedBB;
import com.hepolite.coreutility.util.reflection.reflected.REntity;
import com.hepolite.coreutility.util.reflection.reflected.RItemStack;
import com.hepolite.coreutility.util.reflection.reflected.RMovingObjectPosition;
import com.hepolite.coreutility.util.reflection.reflected.RNBTTag;
import com.hepolite.coreutility.util.reflection.reflected.RVec3D;
import com.hepolite.coreutility.util.reflection.reflected.RWorld;

public class ReflectionUtil
{
	private static String version;
	private static Settings mappings;

	public ReflectionUtil(final CorePlugin plugin)
	{
		version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
		mappings = new ReflectionUtilSettings(plugin);

		initialize();
	}

	/** Binds all objects as they should be bound */
	private final void initialize()
	{
		Log.info("Loading up reflection utilities for version " + version + "...");

		RVec3D.nmsClass = getNMSClass("Vec3D");
		RVec3D.nmsConstructor = getConstructor(RVec3D.nmsClass, double.class, double.class, double.class);
		RVec3D.nmsX = getNMSField(RVec3D.nmsClass, "x");
		RVec3D.nmsY = getNMSField(RVec3D.nmsClass, "y");
		RVec3D.nmsZ = getNMSField(RVec3D.nmsClass, "z");

		RAxisAlignedBB.nmsClass = getNMSClass("AxisAlignedBB");
		RAxisAlignedBB.nmsMinX = getNMSField(RAxisAlignedBB.nmsClass, "minX");
		RAxisAlignedBB.nmsMinY = getNMSField(RAxisAlignedBB.nmsClass, "minY");
		RAxisAlignedBB.nmsMinZ = getNMSField(RAxisAlignedBB.nmsClass, "minZ");
		RAxisAlignedBB.nmsMaxX = getNMSField(RAxisAlignedBB.nmsClass, "maxX");
		RAxisAlignedBB.nmsMaxY = getNMSField(RAxisAlignedBB.nmsClass, "maxY");
		RAxisAlignedBB.nmsMaxZ = getNMSField(RAxisAlignedBB.nmsClass, "maxZ");

		RMovingObjectPosition.nmsClass = getNMSClass("MovingObjectPosition");
		RMovingObjectPosition.nmsPos = getNMSField(RMovingObjectPosition.nmsClass, "pos");

		RWorld.nmsClass = getNMSClass("World");
		RWorld.nmsRayTracePrimary = getNMSMethod(RWorld.nmsClass, "rayTrace", RVec3D.nmsClass.getHandle(), RVec3D.nmsClass.getHandle());
		RWorld.nmsRayTraceSecondary = getNMSMethod(RWorld.nmsClass, "rayTrace", RVec3D.nmsClass.getHandle(), RVec3D.nmsClass.getHandle(), boolean.class);
		RWorld.nmsRayTraceTertiary = getNMSMethod(RWorld.nmsClass, "rayTrace", RVec3D.nmsClass.getHandle(), RVec3D.nmsClass.getHandle(), boolean.class, boolean.class, boolean.class);
		RWorld.cbClass = getCBClass("World", "");
		RWorld.cbGetHandle = getCBMethod(RWorld.cbClass, "getHandle");

		RNBTTag.Base.nmsClass = getNMSClass("NBTBase");
		RNBTTag.Byte.nmsClass = getNMSClass("NBTTagByte");
		RNBTTag.Byte.nmsAsByte = getNMSMethod(RNBTTag.Byte.nmsClass, "asByte");
		RNBTTag.Compound.nmsClass = getNMSClass("NBTTagCompound");
		RNBTTag.Compound.nmsGetByte = getNMSMethod(RNBTTag.Compound.nmsClass, "getByte", String.class);
		RNBTTag.Compound.nmsGetCompound = getNMSMethod(RNBTTag.Compound.nmsClass, "getCompound", String.class);
		RNBTTag.Compound.nmsGetDouble = getNMSMethod(RNBTTag.Compound.nmsClass, "getDouble", String.class);
		RNBTTag.Compound.nmsGetFloat = getNMSMethod(RNBTTag.Compound.nmsClass, "getFloat", String.class);
		RNBTTag.Compound.nmsGetInt = getNMSMethod(RNBTTag.Compound.nmsClass, "getInt", String.class);
		RNBTTag.Compound.nmsGetKeys = getNMSMethod(RNBTTag.Compound.nmsClass, "getKeys");
		RNBTTag.Compound.nmsGetLong = getNMSMethod(RNBTTag.Compound.nmsClass, "getLong", String.class);
		RNBTTag.Compound.nmsGetShort = getNMSMethod(RNBTTag.Compound.nmsClass, "getShort", String.class);
		RNBTTag.Compound.nmsGetString = getNMSMethod(RNBTTag.Compound.nmsClass, "getString", String.class);
		RNBTTag.Compound.nmsGetTag = getNMSMethod(RNBTTag.Compound.nmsClass, "getTag", String.class);
		RNBTTag.Compound.nmsHasKey = getNMSMethod(RNBTTag.Compound.nmsClass, "hasKey", String.class);
		RNBTTag.Compound.nmsRemove = getNMSMethod(RNBTTag.Compound.nmsClass, "remove", String.class);
		RNBTTag.Compound.nmsSetByte = getNMSMethod(RNBTTag.Compound.nmsClass, "setByte", String.class, byte.class);
		RNBTTag.Compound.nmsSetDouble = getNMSMethod(RNBTTag.Compound.nmsClass, "setDouble", String.class, double.class);
		RNBTTag.Compound.nmsSetFloat = getNMSMethod(RNBTTag.Compound.nmsClass, "setFloat", String.class, float.class);
		RNBTTag.Compound.nmsSetInt = getNMSMethod(RNBTTag.Compound.nmsClass, "setInt", String.class, int.class);
		RNBTTag.Compound.nmsSetLong = getNMSMethod(RNBTTag.Compound.nmsClass, "setLong", String.class, long.class);
		RNBTTag.Compound.nmsSetShort = getNMSMethod(RNBTTag.Compound.nmsClass, "setShort", String.class, short.class);
		RNBTTag.Compound.nmsSetString = getNMSMethod(RNBTTag.Compound.nmsClass, "setString", String.class, String.class);
		RNBTTag.Compound.nmsSetTag = getNMSMethod(RNBTTag.Compound.nmsClass, "setTag", String.class, RNBTTag.Base.nmsClass.getHandle());
		RNBTTag.Double.nmsClass = getNMSClass("NBTTagDouble");
		RNBTTag.Double.nmsAsDouble = getNMSMethod(RNBTTag.Double.nmsClass, "asDouble");
		RNBTTag.Float.nmsClass = getNMSClass("NBTTagFloat");
		RNBTTag.Float.nmsAsFloat = getNMSMethod(RNBTTag.Float.nmsClass, "asFloat");
		RNBTTag.Int.nmsClass = getNMSClass("NBTTagInt");
		RNBTTag.Int.nmsAsInt = getNMSMethod(RNBTTag.Int.nmsClass, "asInt");
		RNBTTag.List.nmsClass = getNMSClass("NBTTagList");
		RNBTTag.List.nmsAdd = getNMSMethod(RNBTTag.List.nmsClass, "add", RNBTTag.Base.nmsClass.getHandle());
		RNBTTag.List.nmsGet = getNMSMethod(RNBTTag.List.nmsClass, "get", int.class);
		RNBTTag.List.nmsRemove = getNMSMethod(RNBTTag.List.nmsClass, "remove", int.class);
		RNBTTag.List.nmsSize = getNMSMethod(RNBTTag.List.nmsClass, "size");
		RNBTTag.Long.nmsClass = getNMSClass("NBTTagLong");
		RNBTTag.Long.nmsAsLong = getNMSMethod(RNBTTag.Long.nmsClass, "asLong");
		RNBTTag.Short.nmsClass = getNMSClass("NBTTagShort");
		RNBTTag.Short.nmsAsShort = getNMSMethod(RNBTTag.Short.nmsClass, "asShort");
		RNBTTag.String.nmsClass = getNMSClass("NBTTagString");
		RNBTTag.String.nmsAsString = getNMSMethod(RNBTTag.String.nmsClass, "asString");

		REntity.nmsClass = getNMSClass("Entity");
		REntity.nmsGetBoundingBox = getNMSMethod(REntity.nmsClass, "getBoundingBox");
		REntity.nmsSave = getNMSMethod(REntity.nmsClass, "save", RNBTTag.Compound.nmsClass.getHandle());
		REntity.nmsLoad = getNMSMethod(REntity.nmsClass, "load", RNBTTag.Compound.nmsClass.getHandle());
		REntity.cbClass = getCBClass("Entity", "entity");
		REntity.cbGetHandle = getCBMethod(REntity.cbClass, "getHandle");

		RItemStack.nmsClass = getNMSClass("ItemStack");
		RItemStack.nmsSetTag = getNMSMethod(RItemStack.nmsClass, "setTag", RNBTTag.Compound.nmsClass.getHandle());
		RItemStack.nmsGetTag = getNMSMethod(RItemStack.nmsClass, "getTag");
		RItemStack.nmsHasTag = getNMSMethod(RItemStack.nmsClass, "hasTag");
		RItemStack.cbClass = getCBClass("ItemStack", "inventory");
		RItemStack.cbAsCraftCopy = getCBMethod(RItemStack.cbClass, "asCraftCopy", ItemStack.class);
		RItemStack.cbHandle = getCBField(RItemStack.cbClass, "handle");

		Log.info("Finalized all reflection mappings!");
	}

	// //////////////////////////////////////////////////////////////

	/** Returns the NMS class under the given key name */
	public final static ReflectedClass getNMSClass(String name)
	{
		String mapping = mappings.getString(version + "." + name + ".class");
		try
		{
			return new ReflectedClass(name, Class.forName("net.minecraft.server." + version + "." + mapping));
		}
		catch (Exception e)
		{
			Log.warning("NMS class " + name + " not found. Is the mapping " + mapping + " correct?");
		}
		return new ReflectedClass(name, null);
	}

	/** Returns the NMS method under the given class and key name */
	public final static ReflectedMethod getNMSMethod(ReflectedClass refClass, String name, Class<?>... args)
	{
		String mapping = mappings.getString(version + "." + refClass.getName() + ".method." + name);
		try
		{
			return new ReflectedMethod(name, refClass.getHandle().getDeclaredMethod(mapping, args));
		}
		catch (Exception e)
		{
			Log.warning("NMS method " + name + " with args " + buildString(args) + " not found in " + refClass.getExtendedName() + ". Is the mapping " + mapping + " correct?");
		}
		return new ReflectedMethod(name, null);
	}

	/** Returns the NMS field under the given class and key name */
	public final static ReflectedField getNMSField(ReflectedClass refClass, String name)
	{
		String mapping = mappings.getString(version + "." + refClass.getName() + ".field." + name);
		try
		{
			return new ReflectedField(name, refClass.getHandle().getDeclaredField(mapping));
		}
		catch (Exception e)
		{
			Log.warning("NMS field " + name + " not found in " + refClass.getExtendedName() + ". Is the mapping " + mapping + " correct?");
		}
		return new ReflectedField(name, null);
	}

	/** Returns the CraftBukkit class under the given key name */
	public final static ReflectedClass getCBClass(String name, String path)
	{
		path += (path.isEmpty() ? "" : ".") + "Craft";
		try
		{
			return new ReflectedClass(name, Class.forName("org.bukkit.craftbukkit." + version + "." + path + name));
		}
		catch (Exception e)
		{
			Log.warning("CraftBukkit class " + name + " not found. Is the path " + path + name + " correct?");
		}
		return new ReflectedClass(name, null);
	}

	/** Returns the CraftBukkit method under the given class and key name */
	public final static ReflectedMethod getCBMethod(ReflectedClass refClass, String name, Class<?>... args)
	{
		try
		{
			return new ReflectedMethod(name, refClass.getHandle().getDeclaredMethod(name, args));
		}
		catch (Exception e)
		{
			Log.warning("CraftBukkit method " + name + " with args " + buildString(args) + " not found in " + refClass.getExtendedName());
		}
		return new ReflectedMethod(name, null);
	}

	/** Returns the CraftBukkit field under the given class and key name */
	public final static ReflectedField getCBField(ReflectedClass refClass, String name)
	{
		try
		{
			return new ReflectedField(name, refClass.getHandle().getDeclaredField(name));
		}
		catch (Exception e)
		{
			Log.warning("CraftBukkit field " + name + " not found in " + refClass.getExtendedName());
		}
		return new ReflectedField(name, null);
	}

	/** Returns the constructor with the given parameters */
	public final static ReflectedConstructor getConstructor(ReflectedClass refClass, Class<?>... args)
	{
		try
		{
			return new ReflectedConstructor(refClass.getHandle().getConstructor(args));
		}
		catch (Exception e)
		{
			Log.warning("Constructor with args " + buildString(args) + " not found in " + refClass.getExtendedName());
		}
		return new ReflectedConstructor(null);
	}

	/** Returns a string description of the classes */
	private final static String buildString(Class<?>... args)
	{
		String string = "";
		for (Class<?> cls : args)
			string += (string.isEmpty() ? "" : ", ") + cls.getName();
		return string.isEmpty() ? "none" : string;
	}
}
