package com.hepolite.coreutility.projectile;

import java.util.AbstractMap.SimpleEntry;
import java.util.Collection;
import java.util.Map.Entry;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

import com.hepolite.coreutility.apis.entites.EntityLocater.LineLocater;
import com.hepolite.coreutility.apis.raytrace.RaytraceAPI;

public abstract class Projectile
{
	private World world;
	private final Vector pos;
	private final Vector vel;
	private final Vector acc;

	private boolean isValid = true;

	public Projectile(Location location)
	{
		this.world = location.getWorld();
		this.pos = new Vector(location.getX(), location.getY(), location.getZ());
		this.vel = new Vector();
		this.acc = new Vector();
	}

	/** Invoked every tick for as long as the projectile is active */
	public abstract void onTick(int tick);

	/** Returns true if the projectile is still valid */
	public final boolean isValid()
	{
		return isValid;
	}

	/** Invalidates the projectile, marking it for removal */
	public final void invalidate()
	{
		isValid = false;
	}

	// ////////////////////////////////////////////////////////////////////////

	/** Returns the location of the projectile */
	public final Location getLocation()
	{
		return new Location(world, pos.getX(), pos.getY(), pos.getZ());
	}

	/** Sets the location of the projectile */
	public final void setLocation(Location location)
	{
		this.world = location.getWorld();
		this.pos.setX(location.getX()).setY(location.getY()).setZ(location.getZ());
	}

	/** Returns the velocity of the projectile */
	public final Vector getVelocity()
	{
		return vel.clone();
	}

	/** Sets the velocity of the projectile */
	public final void setVelocity(Vector velocity)
	{
		vel.setX(velocity.getX()).setY(velocity.getY()).setZ(velocity.getZ());
	}

	/** Returns the acceleration of the projectile */
	public final Vector getAcceleration()
	{
		return acc.clone();
	}

	/** Sets the acceleration of the projectile */
	public final void setAcceleration(Vector acceleration)
	{
		acc.setX(acceleration.getX()).setY(acceleration.getY()).setZ(acceleration.getZ());
	}

	/** Updates the position and velocity of the projectile */
	protected void updatePositionAndVelocity()
	{
		pos.add(vel).add(acc.clone().multiply(0.5));
		vel.add(acc);
	}

	// ////////////////////////////////////////////////////////////////////////

	/** Returns the location of the collision with the world, or null if there is no impact */
	protected Location getCollisionWorld(boolean hitFluids)
	{
		return RaytraceAPI.rayTrace(getLocation(), vel, hitFluids, true, false);
	}

	/** Returns a collection of all entities hit by the projectile */
	protected Collection<LivingEntity> getCollisionEntity(boolean hitWorld, boolean hitFluids)
	{
		Location loc = getLocation();
		LineLocater line = new LineLocater(loc, vel);
		if (hitWorld)
			return line.getUnobstructed(loc, !hitFluids, LivingEntity.class);
		else
			return line.get(LivingEntity.class);
	}

	/** Returns the entity hit by the projectile */
	protected LivingEntity getCollisionEntity(LivingEntity ignored, boolean hitWorld,
			boolean hitFluids)
	{
		Location loc = getLocation();
		LineLocater line = new LineLocater(loc, vel);
		if (hitWorld)
			return line.getFirstUnobstructed(loc, ignored, !hitFluids, LivingEntity.class);
		else
			return line.getFirst(loc, ignored, LivingEntity.class);
	}

	/** Returns a the location of the world or entity, whichever is closest; returns null if no entity or block was hit. If the world was hit but an entity wasn't, the entity component is null */
	protected Entry<Location, LivingEntity> getCollision(LivingEntity ignored, boolean hitFluids)
	{
		double distance = Double.MAX_VALUE;
		Entry<Location, LivingEntity> nearest = null;
		Location loc = getLocation();

		Location worldLocation = getCollisionWorld(hitFluids);
		if (worldLocation != null)
		{
			nearest = new SimpleEntry<Location, LivingEntity>(worldLocation, null);
			distance = worldLocation.distanceSquared(loc);
		}
		for (LivingEntity entity : getCollisionEntity(false, hitFluids))
		{
			if (entity == ignored)
				continue;

			double d = entity.getLocation().distanceSquared(loc);
			if (d <= distance)
			{
				nearest = new SimpleEntry<Location, LivingEntity>(entity.getLocation().add(0.0,
						0.5 * entity.getEyeHeight(), 0.0), entity);
				distance = d;
			}
		}
		return nearest;
	}
}
