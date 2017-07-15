package com.hepolite.coreutility.util.math;

import org.apache.commons.lang.NotImplementedException;
import org.bukkit.util.Vector;

public class Line extends Shape
{
	private final Point start;
	private final Point direction;

	private Line(Point start, Point direction)
	{
		this.start = start;
		this.direction = direction;
	}

	/** Creates a new line from the two vectors */
	public final static Line fromStartDirection(Vector start, Vector direction)
	{
		return new Line(new Point(start), new Point(direction));
	}

	/** Creates a new line from the two vectors */
	public final static Line fromStartEnd(Vector start, Vector end)
	{
		return new Line(new Point(start), new Point(end.clone().subtract(start)));
	}

	/** Returns the start of the line */
	public final Point getStart()
	{
		return start;
	}

	/** Returns the direction of the line */
	public final Point getDirection()
	{
		return direction;
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
		throw new NotImplementedException();
	}

	@Override
	public final boolean intersects(Sphere line)
	{
		throw new NotImplementedException();
	}

	@Override
	public final boolean intersects(Box box)
	{
		if (box == null)
			return false;

		Point dirfrac = new Point(1.0f / direction.x, 1.0f / direction.y, 1.0f / direction.z);
		Point t1 = (box.getStart().subtract(start)).multiply(dirfrac);
		Point t2 = (box.getEnd().subtract(start)).multiply(dirfrac);
		float tMin = Math.max(Math.max(Math.min(t1.x, t2.x), Math.min(t1.y, t2.y)),
				Math.min(t1.z, t2.z));
		float tMax = Math.min(Math.min(Math.max(t1.x, t2.x), Math.max(t1.y, t2.y)),
				Math.max(t1.z, t2.z));
		return tMax >= tMin && tMax >= 0.0f && tMin <= 1.0f; // tMin <= tMax
	}

	@Override
	public final boolean intersects(Cone cone)
	{
		throw new NotImplementedException();
	}

	@Override
	public final String toString()
	{
		return String.format("[%s + t * %s]", start.toString(), direction.toString());
	}
}
