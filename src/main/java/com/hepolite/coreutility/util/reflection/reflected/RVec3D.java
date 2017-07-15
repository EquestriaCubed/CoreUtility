package com.hepolite.coreutility.util.reflection.reflected;

import org.bukkit.Location;
import org.bukkit.util.Vector;

import com.hepolite.coreutility.util.reflection.ReflectedClass;
import com.hepolite.coreutility.util.reflection.ReflectedConstructor;
import com.hepolite.coreutility.util.reflection.ReflectedField;

public class RVec3D
{
	public static ReflectedClass nmsClass = null;
	public static ReflectedConstructor nmsConstructor = null;
	public static ReflectedField nmsX = null;
	public static ReflectedField nmsY = null;
	public static ReflectedField nmsZ = null;

	/** Constructs a new Vec3D instance */
	public final static Object create(double x, double y, double z)
	{
		return nmsConstructor.instantiate(x, y, z);
	}

	/** Constructs a new Vec3D instance */
	public final static Object create(Vector v)
	{
		return create(v.getX(), v.getY(), v.getZ());
	}

	/** Constructs a new Vec3D instance */
	public final static Object create(Location loc)
	{
		return create(loc.getX(), loc.getY(), loc.getZ());
	}

	// ////////////////////////////////////////////////////////////

	private final Object handle;
	private double x, y, z;

	public RVec3D(Object handle)
	{
		this.handle = handle;
		this.x = (double) nmsX.get(handle);
		this.y = (double) nmsY.get(handle);
		this.z = (double) nmsZ.get(handle);
	}

	public RVec3D(double x, double y, double z)
	{
		handle = create(x, y, z);
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/** Returns the handle */
	public final Object getHandle()
	{
		return handle;
	}

	/** Returns the x-component of the Vec3D */
	public final double getX()
	{
		return x;
	}

	/** Returns the y-component of the Vec3D */
	public final double getY()
	{
		return y;
	}

	/** Returns the z-component of the Vec3D */
	public final double getZ()
	{
		return z;
	}

	/** Sets the position of the Vec3D */
	public final RVec3D set(double x, double y, double z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		nmsX.set(handle, x);
		nmsY.set(handle, y);
		nmsZ.set(handle, z);
		return this;
	}

	@Override
	public final String toString()
	{
		return handle == null ? "null" : handle.toString();
	}
}
