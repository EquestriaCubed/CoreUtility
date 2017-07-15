package com.hepolite.coreutility.apis.entites;

import org.bukkit.entity.Entity;

import com.hepolite.coreutility.util.math.Box;
import com.hepolite.coreutility.util.reflection.reflected.REntity;

public class EntityAPI
{
	/** Returns a bounding box for the given entity */
	public static Box getBoundingBox(Entity entity)
	{
		return (new REntity(entity)).getBox().getBox();
	}
}
