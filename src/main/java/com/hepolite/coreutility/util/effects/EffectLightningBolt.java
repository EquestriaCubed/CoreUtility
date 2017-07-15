package com.hepolite.coreutility.util.effects;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.bukkit.util.Vector;

public class EffectLightningBolt
{
	public static final class Segment
	{
		private static final Random random = new Random();
		public final Vector start;
		public final Vector end;

		private Segment(Vector start, Vector end)
		{
			this.start = start;
			this.end = end;
		}

		/** Calculates a center point with the given displacement */
		public final Vector getDisplacedCenter(float displacement)
		{
			Vector delta = start.getMidpoint(end);
			Vector direction = new Vector(2.0 * random.nextDouble() - 1.0,
					2.0 * random.nextDouble() - 1.0, 2.0 * random.nextDouble() - 1.0);
			Vector offset = delta.getCrossProduct(direction).normalize();
			return start.getMidpoint(end).add(offset.multiply(displacement));
		}
	}

	private final Vector start;
	private final Vector end;
	private final int generations;
	private final float displacementRatio;
	private final float branchingChance;
	private final float branchingAngle;

	public EffectLightningBolt(Vector start, Vector end, float segmentSize, float displacementRatio,
			float branchingChance)
	{
		this.start = start;
		this.end = end;
		this.generations = Math.min(6, (int) (start.distance(end) / segmentSize));
		this.displacementRatio = displacementRatio;
		this.branchingChance = branchingChance;
		this.branchingAngle = 8.0f / 180.0f * (float) Math.PI;
	}

	/** Builds up a lightning bolt structure */
	public final List<Segment> build()
	{
		List<Segment> list = new LinkedList<Segment>();
		list.add(new Segment(start, end));

		float displacement = displacementRatio * (float) start.distance(end);
		for (int i = 0; i < generations; ++i)
		{
			List<Segment> newSegments = new LinkedList<Segment>();
			for (Segment segment : list)
			{
				Vector center = segment.getDisplacedCenter(displacement);
				newSegments.add(new Segment(segment.start, center));
				newSegments.add(new Segment(center, segment.end));

				Random rnd = Segment.random;
				if (rnd.nextFloat() < branchingChance)
				{
					float a = branchingAngle;
					Vector r = new Vector(2.0 * rnd.nextDouble() - 1.0,
							2.0 * rnd.nextDouble() - 1.0, 2.0 * rnd.nextDouble() - 1.0).normalize();

					Vector direction = segment.end.clone().subtract(center);
					Vector matRow1 = new Vector((1.0f - a) + r.getX() * r.getX() * a, r.getX()
							* r.getY() * a - r.getZ() * a, r.getX() * r.getZ() * a + r.getY() * a);
					Vector matRow2 = new Vector(r.getY() * r.getX() * a + r.getZ() * a, (1.0f - a)
							* r.getY() * r.getY() * a, r.getY() * r.getZ() * a - r.getX() * a);
					Vector matRow3 = new Vector(r.getZ() * r.getX() * a - r.getY() * a, r.getZ()
							* r.getY() * a + r.getX() * a, (1.0f - a) + r.getZ() * r.getZ() * a);
					Vector transformed = new Vector(
							matRow1.getX() * direction.getX() + matRow1.getY() * direction.getY()
									+ matRow1.getZ() * direction.getZ(), matRow2.getX()
									* direction.getX() + matRow2.getY() * direction.getY()
									+ matRow2.getZ() * direction.getZ(), matRow3.getX()
									* direction.getX() + matRow3.getY() * direction.getY()
									+ matRow3.getZ() * direction.getZ());
					newSegments.add(new Segment(center, center.clone().add(
							transformed.multiply(0.7))));
				}
			}

			list.clear();
			list.addAll(newSegments);
			displacement *= 0.5f;
		}

		return list;
	}
}
