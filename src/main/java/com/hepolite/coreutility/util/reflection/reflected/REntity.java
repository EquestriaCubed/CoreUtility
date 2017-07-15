package com.hepolite.coreutility.util.reflection.reflected;

import org.bukkit.entity.Entity;

import com.hepolite.coreutility.util.reflection.ReflectedClass;
import com.hepolite.coreutility.util.reflection.ReflectedMethod;

public class REntity
{
	public static ReflectedClass nmsClass = null;
	public static ReflectedMethod nmsGetBoundingBox = null;
	public static ReflectedMethod nmsSave = null;
	public static ReflectedMethod nmsLoad = null;
	public static ReflectedClass cbClass = null;
	public static ReflectedMethod cbGetHandle = null;

	// ////////////////////////////////////////////////////////////

	private final Object handle;
	private final RAxisAlignedBB box;

	public REntity(Object handle)
	{
		this.handle = handle;
		this.box = new RAxisAlignedBB(handle == null ? null : nmsGetBoundingBox.invoke(handle));
	}

	public REntity(Entity entity)
	{
		this(cbGetHandle.invoke(entity));
	}

	/** Returns the handle */
	public final Object getHandle()
	{
		return handle;
	}

	/** Returns the bounding box */
	public final RAxisAlignedBB getBox()
	{
		return box;
	}
	
	@Override
	public final String toString()
	{
		return handle == null ? "null" : handle.toString();
	}
}
