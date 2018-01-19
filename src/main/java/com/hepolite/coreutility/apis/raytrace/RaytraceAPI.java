package com.hepolite.coreutility.apis.raytrace;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.Vector;

import com.hepolite.coreutility.util.math.Point;
import com.hepolite.coreutility.util.reflection.reflected.RMovingObjectPosition;
import com.hepolite.coreutility.util.reflection.reflected.RVec3D;
import com.hepolite.coreutility.util.reflection.reflected.RWorld;

public class RaytraceAPI
{
	/** Returns the first block hit along the ray, or null if no block was found */
	public static final Location rayTrace(Location start, Vector direction)
	{
		return rayTrace(start, start.clone().add(direction));
	}

	/** Returns the first block hit along the ray, or null if no block was found */
	public static final Location rayTrace(Location start, Point end)
	{
		return rayTrace(start, new Location(start.getWorld(), end.x, end.y, end.z));
	}

	/** Returns the first block hit along the ray, or null if no block was found */
	public static final Location rayTrace(Location start, Location end)
	{
		updateVecs(start, end);
		RWorld world = getWorld(start.getWorld());
		RMovingObjectPosition mop = world.rayTrace(from, to);
		if (mop == null)
			return null;
		RVec3D pos = mop.getPos();
		return new Location(start.getWorld(), pos.getX(), pos.getY(), pos.getZ());
	}

	/** Returns the first block hit along the ray, or null if no block was found; optionally ignoring fluids */
	public static final Location rayTrace(Location start, Point end, boolean hitFluids)
	{
		return rayTrace(start, new Location(start.getWorld(), end.x, end.y, end.z), hitFluids);
	}

	/** Returns the first block hit along the ray, or null if no block was found; optionally ignoring fluids */
	public static final Location rayTrace(Location start, Vector direction, boolean hitFluids)
	{
		return rayTrace(start, start.clone().add(direction), hitFluids);
	}

	/** Returns the first block hit along the ray, or null if no block was found; optionally ignoring fluids */
	public static final Location rayTrace(Location start, Location end, boolean hitFluids)
	{
		updateVecs(start, end);
		RWorld world = getWorld(start.getWorld());
		RMovingObjectPosition mop = world.rayTrace(from, to, hitFluids);
		if (mop == null)
			return null;
		RVec3D pos = mop.getPos();
		return new Location(start.getWorld(), pos.getX(), pos.getY(), pos.getZ());
	}

	/** Returns the first block hit along the ray, or null if no block was found; optionally ignoring fluids and blocks without bounding boxes (torches, buttons, liquids, etc) */
	public static final Location rayTrace(Location start, Vector direction, boolean hitFluids, boolean ignoreNonBBoxes, boolean returnLastRayLocation)
	{
		return rayTrace(start, start.clone().add(direction), hitFluids, ignoreNonBBoxes, returnLastRayLocation);
	}

	/** Returns the first block hit along the ray, or null if no block was found; optionally ignoring fluids and blocks without bounding boxes (torches, buttons, liquids, etc) */
	public static final Location rayTrace(Location start, Point end, boolean hitFluids, boolean ignoreNonBBoxes, boolean returnLastRayLocation)
	{
		return rayTrace(start, new Location(start.getWorld(), end.x, end.y, end.z), hitFluids, ignoreNonBBoxes, returnLastRayLocation);
	}

	/** Returns the first block hit along the ray, or null if no block was found; optionally ignoring fluids and blocks without bounding boxes (torches, buttons, liquids, etc) */
	public static final Location rayTrace(Location start, Location end, boolean hitFluids, boolean ignoreNonBBoxes, boolean returnLastRayLocation)
	{
		updateVecs(start, end);
		RWorld world = getWorld(start.getWorld());
		RMovingObjectPosition mop = world.rayTrace(from, to, hitFluids, ignoreNonBBoxes, returnLastRayLocation);
		if (mop == null)
			return null;
		RVec3D pos = mop.getPos();
		return new Location(start.getWorld(), pos.getX(), pos.getY(), pos.getZ());
	}

	// ////////////////////////////////////////////////////////////

	private static final Map<String, RWorld> worlds = new HashMap<String, RWorld>();
	private static final RVec3D from = new RVec3D(0.0, 0.0, 0.0);
	private static final RVec3D to = new RVec3D(0.0, 0.0, 0.0);

	/** Retrieves the NMS world for the given world */
	private static final RWorld getWorld(World world)
	{
		if (world == null)
			return null;
		if (!worlds.containsKey(world.getName()))
			worlds.put(world.getName(), new RWorld(world));
		return worlds.get(world.getName());
	}

	/** Updates the NMS vectors */
	private static final void updateVecs(Location start, Location end)
	{
		from.set(start.getX(), start.getY(), start.getZ());
		to.set(end.getX(), end.getY(), end.getZ());
	}
}
