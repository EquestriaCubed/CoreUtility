package com.hepolite.coreutility.util.effects;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;

import com.hepolite.coreutility.plugin.CorePlugin;
import com.hepolite.coreutility.util.effects.EffectLightningBolt.Segment;

import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.EffectType;
import de.slikey.effectlib.effect.LineEffect;
import de.slikey.effectlib.effect.SphereEffect;
import de.slikey.effectlib.util.ParticleEffect;

public class EffectHelper
{
	private static EffectManager effectManager;

	public EffectHelper(CorePlugin plugin)
	{
		effectManager = new EffectManager(plugin);
	}

	/** Cleans up all effects */
	public static final void cleanup()
	{
		effectManager.dispose();
	}

	/** Returns the underlying effect manager */
	public static final EffectManager getInstance()
	{
		return effectManager;
	}

	// /////////////////////////////////////////////////////////

	public static final void electricalDischarge(Location location)
	{
		World world = location.getWorld();
		world.playSound(location, Sound.ENTITY_FIREWORK_BLAST, 0.8f, 0.0f);
		world.spawnParticle(Particle.SNOWBALL, location, 40, 1.25, 1.25, 1.25);
	}

	public static final void magicalDischarge(Location location)
	{
		World world = location.getWorld();
		world.playSound(location, Sound.ENTITY_FIREWORK_BLAST, 0.8f, 0.0f);
		genericSphere(location, 1.25f, ParticleEffect.SPELL_WITCH, 75);
	}

	public static final void genericBolt(Location start, Location end, ParticleEffect particle,
			int count)
	{
		LineEffect effect = new LineEffect(effectManager);
		effect.particle = particle;
		effect.particles = count;
		effect.setLocation(start);
		effect.setTargetLocation(end);
		effect.start();
	}

	public static final void electricalBolt(Location start, Location end, float segmentSize,
			float maxDisplacement, float brancingChance)
	{
		World world = start.getWorld();
		world.playSound(start, Sound.ENTITY_LIGHTNING_THUNDER, 5.0f, 1.0f);
		world.playSound(end, Sound.ENTITY_LIGHTNING_IMPACT, 2.5f, 0.0f);

		EffectLightningBolt bolt = new EffectLightningBolt(start.toVector(), end.toVector(),
				segmentSize, maxDisplacement, brancingChance);
		for (Segment segment : bolt.build())
			genericBolt(segment.start.toLocation(world), segment.end.toLocation(world),
					ParticleEffect.FLAME, 35);
	}

	public static final void magicalBolt(Location start, Location end)
	{
		genericBolt(start, end, ParticleEffect.SPELL_WITCH, 25);
	}

	public static final void genericSphere(Location location, float radius,
			ParticleEffect particle, int count)
	{
		SphereEffect effect = new SphereEffect(effectManager);
		effect.type = EffectType.INSTANT;
		effect.particle = particle;
		effect.particles = count;
		effect.radius = radius;
		effect.setLocation(location);
		effect.start();
	}

	public static final void explosionEffect(Location location)
	{
		World world = location.getWorld();
		world.playSound(location, Sound.ENTITY_GENERIC_EXPLODE, 2.5f, 1.0f);
		world.spawnParticle(Particle.EXPLOSION_HUGE, location.getX(), location.getY(),
				location.getZ(), 10, 1.25, 1.25, 1.25, 0.05, null);
	}

	public static final void teleportEffect(Location location, float radius)
	{
		World world = location.getWorld();
		world.playSound(location, Sound.ENTITY_ENDERMEN_TELEPORT, 1.0f, 0.0f);
		genericSphere(location, radius, ParticleEffect.PORTAL, 125);
	}
}
