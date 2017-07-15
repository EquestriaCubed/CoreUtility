package com.hepolite.coreutility.util.math;

import org.bukkit.util.Vector;

public class Box extends Shape
{
	private final Point start;
	private final Point end;

	private Box(Point start, Point end)
	{
		this.start = start;
		this.end = end;
	}

	/** Creates a new box from the two vectors */
	public final static Box fromStartEnd(Vector start, Vector end)
	{
		return fromStartEnd(new Point(start), new Point(end));
	}

	/** Creates a new box from the two points */
	public final static Box fromStartEnd(Point start, Point end)
	{
		Point min = new Point(Math.min(start.x, end.x), Math.min(start.y, end.y), Math.min(start.z,
				end.z));
		Point max = new Point(Math.max(start.x, end.x), Math.max(start.y, end.y), Math.max(start.z,
				end.z));
		return new Box(min, max);
	}

	/** Returns the start of the box */
	public final Point getStart()
	{
		return start;
	}

	/** Returns the end of the box */
	public final Point getEnd()
	{
		return end;
	}

	/** Returns the center coordinate of the box */
	public final Point getCenter()
	{
		return (start.add(end)).multiply(0.5f);
	}

	// ///////////////////////////////////////////////////////////////////////

	/** Returns a new box with an offset from this box */
	public final Box offset(Point p)
	{
		return Box.fromStartEnd(start.add(p), end.add(p));
	}

	/** Returns a new box with an offset from this box */
	public final Box offset(Vector v)
	{
		return offset(new Point(v));
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
		// TODO: This is not 100% accurate, might require an upgrade
		Point[] points = new Point[9];
		points[0] = start;
		points[1] = new Point(start.x, start.y, end.z);
		points[2] = new Point(start.x, end.y, start.z);
		points[3] = new Point(start.x, end.y, end.z);
		points[4] = new Point(end.x, start.y, start.z);
		points[5] = new Point(end.x, start.y, end.z);
		points[6] = new Point(end.x, end.y, start.z);
		points[7] = end;
		points[8] = getCenter();
		for (Point point : points)
		{
			if (point.intersects(sphere))
				return true;
		}
		return false;
	}

	@Override
	public final boolean intersects(Box box)
	{
		if (box == null)
			return false;
		Point bs = box.getStart();
		Point be = box.getEnd();
		return !(end.x < bs.x || start.x > be.x || end.y < bs.y || start.y > be.y || end.z < bs.z || start.z > be.z);
	}

	@Override
	public final boolean intersects(Cone cone)
	{
		// TODO: This is not 100% accurate, might require an upgrade
		Point[] points = new Point[9];
		points[0] = start;
		points[1] = new Point(start.x, start.y, end.z);
		points[2] = new Point(start.x, end.y, start.z);
		points[3] = new Point(start.x, end.y, end.z);
		points[4] = new Point(end.x, start.y, start.z);
		points[5] = new Point(end.x, start.y, end.z);
		points[6] = new Point(end.x, end.y, start.z);
		points[7] = end;
		points[8] = (start.add(end)).multiply(0.5f);
		for (Point point : points)
		{
			if (point.intersects(cone))
				return true;
		}
		return false;
	}

	@Override
	public final String toString()
	{
		return String.format("[%s -> %s]", start.toString(), end.toString());
	}
}
