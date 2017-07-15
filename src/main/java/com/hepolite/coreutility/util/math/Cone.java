package com.hepolite.coreutility.util.math;

import org.apache.commons.lang.NotImplementedException;
import org.bukkit.util.Vector;

public class Cone extends Shape
{
	private final Point start;
	private final Point direction;
	private final float angle, cosHalfAngle;

	private Cone(Point start, Point direction, float angle)
	{
		this.start = start;
		this.direction = direction;
		this.angle = angle;
		this.cosHalfAngle = (float) Math.cos(0.5f * angle * Math.PI / 180.0f);
	}

	/** Creates a new line from the two vectors */
	public final static Cone fromStartDirection(Vector start, Vector direction, float width)
	{
		return new Cone(new Point(start), new Point(direction), width);
	}

	/** Creates a new line from the two vectors */
	public final static Cone fromStartDirection(Vector start, float yaw, float pitch, float radius,
			float angle)
	{
		return new Cone(new Point(start), Point.fromYawPitchRadius(yaw, pitch, radius), angle);
	}

	/** Creates a new line from the two vectors */
	public final static Cone fromStartEnd(Vector start, Vector end, float width)
	{
		return new Cone(new Point(start), new Point(end.clone().subtract(start)), width);
	}

	/** Returns the start of the cone */
	public final Point getStart()
	{
		return start;
	}

	/** Returns the direction of the cone */
	public final Point getDirection()
	{
		return direction;
	}

	/** Returns the angle of the cone */
	public final float getAngle()
	{
		return angle;
	}

	/** Returns the cosine of half of the angle of the cone */
	public final float getCosHalfAngle()
	{
		return cosHalfAngle;
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
		return sphere == null ? false : sphere.intersects(this);
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
		return String.format("[%s -> %s; %.2f]", start.toString(), direction.toString(), angle);
	}
}
