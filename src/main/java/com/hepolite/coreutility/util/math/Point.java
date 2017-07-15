package com.hepolite.coreutility.util.math;

import org.apache.commons.lang.NotImplementedException;
import org.bukkit.Location;
import org.bukkit.util.Vector;

public class Point extends Shape
{
	public final float x, y, z;

	public Point(float x, float y, float z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Point(double x, double y, double z)
	{
		this((float) x, (float) y, (float) z);
	}

	public Point(Location l)
	{
		this((float) l.getX(), (float) l.getY(), (float) l.getZ());
	}

	public Point(Vector v)
	{
		this((float) v.getX(), (float) v.getY(), (float) v.getZ());
	}

	/** Returns a new point representing the specified vector */
	public final static Point fromYawPitchRadius(float yaw, float pitch, float radius)
	{
		yaw = 90.0f - yaw; // Because Minecraft is stupid
		float cosYaw = (float) Math.cos(yaw * Math.PI / 180.0f);
		float sinYaw = (float) Math.sin(yaw * Math.PI / 180.0f);
		float cosPitch = (float) Math.cos(pitch * Math.PI / 180.0f);
		float sinPitch = (float) Math.sin(pitch * Math.PI / 180.0f);
		return new Point(radius * cosYaw * cosPitch, radius * sinYaw * cosPitch, radius * sinPitch);
	}

	// //////////////////////////////////////////////////////////

	/** Returns the sum of the two points */
	public final Point add(Point p)
	{
		return new Point(x + p.x, y + p.y, z + p.z);
	}

	/** Returns the sum of the two points */
	public final Point acc(float x, float y, float z)
	{
		return new Point(this.x + x, this.y + y, this.z + z);
	}

	/** Returns the difference between the two points */
	public final Point subtract(Point p)
	{
		return new Point(x - p.x, y - p.y, z - p.z);
	}

	/** Returns the difference between the two points */
	public final Point subtract(float x, float y, float z)
	{
		return new Point(this.x - x, this.y - y, this.z - z);
	}

	/** Scales the point with the given scalar */
	public final Point multiply(float s)
	{
		return new Point(s * x, s * y, s * z);
	}

	/** Scales the point with the given point */
	public final Point multiply(Point s)
	{
		return new Point(s.x * x, s.y * y, s.z * z);
	}

	/** Returns a point where all components are the absolute values of this point */
	public final Point abs()
	{
		return new Point(Math.abs(x), Math.abs(y), Math.abs(z));
	}

	/** Returns the dot product between this point and the given point */
	public final float dot(Point p)
	{
		return x * p.x + y * p.y + z * p.z;
	}

	/** Returns the dot product between this point and the given point */
	public final float dot(float x, float y, float z)
	{
		return this.x * x + this.y * y + this.z * z;
	}

	/** Returns the length of the point */
	public final float length()
	{
		return (float) Math.sqrt(lengthSquared());
	}

	/** Returns the square of the length of the point */
	public final float lengthSquared()
	{
		return dot(this);
	}

	/** Returns a normalized point */
	public final Point normalize()
	{
		return multiply(1.0f / length());
	}

	// //////////////////////////////////////////////////////////

	@Override
	public final boolean intersects(Point point)
	{
		throw new NotImplementedException();
	}

	@Override
	public final boolean intersects(Line line)
	{
		throw new NotImplementedException();
	}

	@Override
	public final boolean intersects(Sphere sphere)
	{
		return sphere == null ? false : subtract(sphere.getCenter()).lengthSquared() <= sphere
				.getRadius() * sphere.getRadius();
	}

	@Override
	public final boolean intersects(Box box)
	{
		if (box == null)
			return false;
		Point bs = box.getStart();
		Point be = box.getEnd();
		return x >= bs.x && x <= be.x && y >= bs.y && y <= be.y && z >= bs.z && z <= be.z;
	}

	@Override
	public final boolean intersects(Cone cone)
	{
		if (cone == null)
			return false;

		Point delta = subtract(cone.getStart());
		float cosDelta = delta.normalize().dot(cone.getDirection().normalize());
		if (cosDelta < cone.getCosHalfAngle())
			return false;
		return delta.lengthSquared() <= cone.getDirection().lengthSquared();
	}

	@Override
	public final String toString()
	{
		return String.format("(%.2f, %.2f, %.2f)", x, y, z);
	}
}
