package com.hepolite.coreutility.util.math;

import org.apache.commons.lang.NotImplementedException;
import org.bukkit.util.Vector;

public class Sphere extends Shape
{
	private final Point center;
	private final float radius;

	private Sphere(Point center, float radius)
	{
		this.center = center;
		this.radius = radius;
	}

	/** Creates a new sphere from the vector */
	public final static Sphere fromCenterRadius(Vector center, float radius)
	{
		return new Sphere(new Point(center), radius);
	}

	/** Returns the center of the sphere */
	public final Point getCenter()
	{
		return center;
	}

	/** Returns the radius of the sphere */
	public final float getRadius()
	{
		return radius;
	}

	// ///////////////////////////////////////////////////////////////////////

	@Override
	public final boolean intersects(Point point)
	{
		return point == null ? false : point.intersects(this);
	}

	@Override
	public final boolean intersects(Line line)
	{
		return line == null ? false : line.intersects(this);
	}

	@Override
	public final boolean intersects(Sphere sphere)
	{
		if (sphere == null)
			return false;
		return center.subtract(sphere.getCenter()).lengthSquared() <= getRadius() * getRadius()
				+ sphere.getRadius() * sphere.getRadius();
	}

	@Override
	public final boolean intersects(Box box)
	{
		return box == null ? false : box.intersects(this);
	}

	@Override
	public final boolean intersects(Cone cone)
	{
		throw new NotImplementedException();
	}

	@Override
	public final String toString()
	{
		return String.format("[%s; %.2f]", center.toString(), radius);
	}
}
