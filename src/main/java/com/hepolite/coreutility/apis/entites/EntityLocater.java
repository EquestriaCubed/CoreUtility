package com.hepolite.coreutility.apis.entites;

import java.util.ArrayList;
import java.util.Collection;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

import com.hepolite.coreutility.apis.raytrace.RaytraceAPI;
import com.hepolite.coreutility.util.math.Box;
import com.hepolite.coreutility.util.math.Cone;
import com.hepolite.coreutility.util.math.Line;
import com.hepolite.coreutility.util.math.Point;
import com.hepolite.coreutility.util.math.Shape;
import com.hepolite.coreutility.util.math.Sphere;

public class EntityLocater
{
	public abstract static class Locater
	{
		private final World world;

		protected Locater(World world)
		{
			this.world = world;
		}

		/** Returns all entities of the given type within the shape */
		public final <T extends Entity> Collection<T> get(Class<T> entityClass)
		{
			Shape shape = getShape();
			Collection<T> entities = world.getEntitiesByClass(entityClass);
			Collection<T> collection = new ArrayList<T>();
			for (T entity : entities)
			{
				Box bbox = EntityAPI.getBoundingBox(entity);
				if (shape.intersects(bbox))
					collection.add(entity);
			}
			return collection;
		}

		/** Returns the nearest entity of the given type within the shape */
		public final <T extends Entity> T getFirst(Location location, Entity ignored,
				Class<T> entityClass)
		{
			Point origin = new Point(location);
			Shape shape = getShape();
			T nearest = null;
			float distance = Float.MAX_VALUE;

			Collection<T> entities = world.getEntitiesByClass(entityClass);
			for (T entity : entities)
			{
				if (entity == ignored)
					continue;

				Box bbox = EntityAPI.getBoundingBox(entity);
				float d = origin.subtract(bbox.getCenter()).lengthSquared();
				if (d < distance && shape.intersects(bbox))
				{
					distance = d;
					nearest = entity;
				}
			}
			return nearest;
		}

		/** Returns all entities of the given type within the shape, that have a clear path to the given location */
		public final <T extends Entity> Collection<T> getUnobstructed(Location location,
				boolean hitFluids, Class<T> entityClass)
		{
			Shape shape = getShape();
			Collection<T> entities = world.getEntitiesByClass(entityClass);
			Collection<T> collection = new ArrayList<T>();
			for (T entity : entities)
			{
				Box bbox = EntityAPI.getBoundingBox(entity);
				if (shape.intersects(bbox)
						&& RaytraceAPI.rayTrace(location, bbox.getCenter(), hitFluids, true, false) == null)
					collection.add(entity);
			}
			return collection;
		}

		/** Returns the nearest entity of the given type within the shape, that have a clear path to the given location */
		public final <T extends Entity> T getFirstUnobstructed(Location location, Entity ignored,
				boolean hitFluids, Class<T> entityClass)
		{
			Point origin = new Point(location);
			Shape shape = getShape();
			T nearest = null;
			float distance = Float.MAX_VALUE;
			for (T entity : world.getEntitiesByClass(entityClass))
			{
				if (entity == ignored)
					continue;

				Box bbox = EntityAPI.getBoundingBox(entity);
				float d = origin.subtract(bbox.getCenter()).lengthSquared();
				if (d < distance
						&& shape.intersects(bbox)
						&& RaytraceAPI.rayTrace(location, bbox.getCenter(), hitFluids, true, false) == null)
				{
					distance = d;
					nearest = entity;
				}
			}
			return nearest;
		}

		/** Returns the base shape for the locater */
		protected abstract Shape getShape();
	}

	public final static class PointLocater extends Locater
	{
		private final Location location;

		public PointLocater(Location location)
		{
			super(location.getWorld());
			this.location = location;
		}

		@Override
		protected Shape getShape()
		{
			return new Point(location);
		}
	}

	public final static class LineLocater extends Locater
	{
		private final Location start, end;

		public LineLocater(Location start, Location end)
		{
			super(start.getWorld());
			this.start = start;
			this.end = end;
		}

		public LineLocater(Location start, Vector direction)
		{
			super(start.getWorld());
			this.start = start;
			this.end = start.clone().add(direction);
		}

		@Override
		protected Shape getShape()
		{
			return Line.fromStartEnd(start.toVector(), end.toVector());
		}
	}

	public final static class SphereLocater extends Locater
	{
		private final Location center;
		private final float radius;

		public SphereLocater(Location start, float radius)
		{
			super(start.getWorld());
			this.center = start;
			this.radius = radius;
		}

		@Override
		protected Shape getShape()
		{
			return Sphere.fromCenterRadius(center.toVector(), radius);
		}
	}

	public final static class ConeLocater extends Locater
	{
		private final Location location;
		private final Vector direction;
		private final float angle;

		public ConeLocater(Location location, Vector direction, float angle)
		{
			super(location.getWorld());
			this.location = location;
			this.direction = direction;
			this.angle = angle;
		}

		@Override
		protected Shape getShape()
		{
			return Cone.fromStartDirection(location.toVector(), direction, angle);
		}
	}
}
