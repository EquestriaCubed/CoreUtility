package com.hepolite.coreutility.util.math;

public abstract class Shape
{
	/** Returns true if the shape intersects with the given point */
	public abstract boolean intersects(Point point);

	/** Returns true if the shape intersects with the given line */
	public abstract boolean intersects(Line line);

	/** Returns true if the shape intersects with the given sphere */
	public abstract boolean intersects(Sphere sphere);

	/** Returns true if the shape intersects with the given box */
	public abstract boolean intersects(Box box);

	/** Returns true if the shape intersects with the given cone */
	public abstract boolean intersects(Cone cone);
}
