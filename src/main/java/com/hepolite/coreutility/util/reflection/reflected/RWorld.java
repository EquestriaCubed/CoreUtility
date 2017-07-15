package com.hepolite.coreutility.util.reflection.reflected;

import org.bukkit.World;

import com.hepolite.coreutility.util.reflection.ReflectedClass;
import com.hepolite.coreutility.util.reflection.ReflectedMethod;

public class RWorld
{
	public static ReflectedClass nmsClass = null;
	public static ReflectedMethod nmsRayTracePrimary = null;
	public static ReflectedMethod nmsRayTraceSecondary = null;
	public static ReflectedMethod nmsRayTraceTertiary = null;
	public static ReflectedClass cbClass = null;
	public static ReflectedMethod cbGetHandle = null;

	// //////////////////////////////////////////////////////////////

	private final Object handle;

	public RWorld(Object handle)
	{
		this.handle = handle;
	}

	public RWorld(World world)
	{
		this.handle = cbGetHandle.invoke(world);
	}

	/** Returns the handle */
	public final Object getHandle()
	{
		return handle;
	}

	/** Performs a raytrace */
	public RMovingObjectPosition rayTrace(RVec3D start, RVec3D end)
	{
		Object result = nmsRayTracePrimary.invoke(handle, start.getHandle(), end.getHandle());
		return result == null ? null : new RMovingObjectPosition(result);
	}

	/** Performs a raytrace, optionally ignoring fluids */
	public RMovingObjectPosition rayTrace(RVec3D start, RVec3D end, boolean hitFluids)
	{
		Object result = nmsRayTraceSecondary.invoke(handle, start.getHandle(), end.getHandle(),
				hitFluids);
		return result == null ? null : new RMovingObjectPosition(result);
	}

	/** Performs a raytrace, optionally ignoring fluids and blocks without bounding boxes (torches, buttons, etc) */
	public RMovingObjectPosition rayTrace(RVec3D start, RVec3D end, boolean hitFluids,
			boolean ignoreNonBBoxes, boolean returnLastRayLocation)
	{
		Object result = nmsRayTraceTertiary.invoke(handle, start.getHandle(), end.getHandle(),
				hitFluids, ignoreNonBBoxes, returnLastRayLocation);
		return result == null ? null : new RMovingObjectPosition(result);
	}
	
	@Override
	public final String toString()
	{
		return handle == null ? "null" : handle.toString();
	}
}
