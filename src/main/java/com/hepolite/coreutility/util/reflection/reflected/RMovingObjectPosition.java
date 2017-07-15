package com.hepolite.coreutility.util.reflection.reflected;

import com.hepolite.coreutility.util.reflection.ReflectedClass;
import com.hepolite.coreutility.util.reflection.ReflectedField;

public class RMovingObjectPosition
{
	public static ReflectedClass nmsClass = null;
	public static ReflectedField nmsPos = null;

	// ////////////////////////////////////////////////////////////

	private final Object handle;
	private final RVec3D pos;

	public RMovingObjectPosition(Object handle)
	{
		this.handle = handle;
		this.pos = new RVec3D(nmsPos.get(handle));
	}

	/** Returns the handle */
	public final Object getHandle()
	{
		return handle;
	}

	/** Returns the position */
	public final RVec3D getPos()
	{
		return pos;
	}

	@Override
	public final String toString()
	{
		return handle == null ? "null" : handle.toString();
	}
}
