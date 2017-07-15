package com.hepolite.coreutility.projectile;

import java.util.Collection;
import java.util.HashSet;

import com.hepolite.coreutility.event.CoreHandler;

public class ProjectileHandler extends CoreHandler
{
	private static final Collection<Projectile> projectiles = new HashSet<Projectile>();
	private static final Collection<Projectile> projectilesToAdd = new HashSet<Projectile>();
	private static final Collection<Projectile> projectilesToRemove = new HashSet<Projectile>();

	@Override
	public void onTick(int tick)
	{
		projectiles.removeAll(projectilesToRemove);
		projectilesToRemove.clear();
		projectiles.addAll(projectilesToAdd);
		projectilesToAdd.clear();
		for (Projectile projectile : projectiles)
		{
			if (projectile.isValid())
				projectile.onTick(tick);
			else
				projectilesToRemove.add(projectile);
		}
	}

	/** Adds a new projectile to the system */
	public final void addProjectile(Projectile projectile)
	{
		if (projectile != null && projectile.isValid())
			projectiles.add(projectile);
	}

	/** Removes the given projectile from the system */
	public final void removeProjectile(Projectile projectile)
	{
		if (projectile != null)
			projectilesToRemove.add(projectile);
	}
}
