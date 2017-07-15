package com.hepolite.coreutility.util.reflection.reflected;

import com.hepolite.coreutility.util.math.Box;
import com.hepolite.coreutility.util.math.Point;
import com.hepolite.coreutility.util.reflection.ReflectedClass;
import com.hepolite.coreutility.util.reflection.ReflectedField;

public class RAxisAlignedBB
{
	public static ReflectedClass nmsClass = null;
	public static ReflectedField nmsMinX = null;
	public static ReflectedField nmsMinY = null;
	public static ReflectedField nmsMinZ = null;
	public static ReflectedField nmsMaxX = null;
	public static ReflectedField nmsMaxY = null;
	public static ReflectedField nmsMaxZ = null;

	// ////////////////////////////////////////////////////////////

	private final Object handle;
	private Box box;

	public RAxisAlignedBB(Object handle)
	{
		this.handle = handle;
		this.box = handle == null ? null : Box.fromStartEnd(new Point((double) nmsMinX.get(handle),
				(double) nmsMinY.get(handle), (double) nmsMinZ.get(handle)),
				new Point((double) nmsMaxX.get(handle), (double) nmsMaxY.get(handle),
						(double) nmsMaxZ.get(handle)));
	}

	/** Returns the handle */
	public final Object getHandle()
	{
		return handle;
	}

	/** Returns the bounding box */
	public final Box getBox()
	{
		return box;
	}

	/** Updates the box */
	public final void setBox(Box box)
	{
		this.box = Box.fromStartEnd(box.getStart(), box.getEnd());
		nmsMinX.set(handle, box.getStart().x);
		nmsMinY.set(handle, box.getStart().y);
		nmsMinZ.set(handle, box.getStart().z);
		nmsMaxX.set(handle, box.getEnd().x);
		nmsMaxY.set(handle, box.getEnd().y);
		nmsMaxZ.set(handle, box.getEnd().z);
	}
	
	@Override
	public final String toString()
	{
		return handle == null ? "null" : handle.toString();
	}
}
