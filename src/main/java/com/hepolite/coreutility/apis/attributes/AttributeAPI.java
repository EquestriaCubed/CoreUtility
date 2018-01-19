package com.hepolite.coreutility.apis.attributes;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.LivingEntity;

public class AttributeAPI
{
	private static final Map<String, Map<UUID, Attribute>> maps = new HashMap<String, Map<UUID, Attribute>>();

	/** Ticks the API */
	public static final void onTick(int tick)
	{
		if (tick % 20 != 5)
			return;

		for (Map<UUID, Attribute> map : maps.values())
			for (Attribute attribute : map.values())
				for (Modifier modifier : attribute.getModifiers())
				{
					if (modifier.getLifetime() == -1)
						continue;
					modifier.setLifetime(modifier.getLifetime() - 1);
					if (modifier.getLifetime() <= 0)
						attribute.removeModifier(modifier);
				}
	}

	/** Returns the node map for the given player; creates the map if it does not already exist */
	private static final Map<UUID, Attribute> getMap(String id)
	{
		if (id == null)
			throw new IllegalArgumentException("Identifier cannot be null");
		if (maps.containsKey(id))
			return maps.get(id);
		Map<UUID, Attribute> map = new HashMap<UUID, Attribute>();
		maps.put(id, map);
		return map;
	}

	// /////////////////////////////////////////////////////////////////

	/** Returns an attribute node with the given id, under the given entity; creates the attribute if it does not exist */
	public static final Attribute get(LivingEntity entity, String id)
	{
		if (entity == null)
			throw new IllegalArgumentException("Entity cannot be null");
		return get(entity.getUniqueId(), id);
	}

	/** Returns an attribute node with the given id, under the given uuid; creates the attribute if it does not exist */
	public static final Attribute get(UUID uuid, String id)
	{
		if (uuid == null || id == null)
			throw new IllegalArgumentException("UUID or identifier cannot be null");
		Map<UUID, Attribute> map = getMap(id);
		if (map.containsKey(uuid))
			return map.get(uuid);
		Attribute node = new Attribute(id, uuid);
		map.put(uuid, node);
		return node;
	}

	/** Returns all attribute nodes of the given id under all entities */
	public static final Collection<Attribute> getNodes(String id)
	{
		if (id == null)
			throw new IllegalArgumentException("Identifier cannot be null");
		return Collections.unmodifiableCollection(getMap(id).values());
	}

	/** Returns true if the given entity has an attribute of the given id */
	public static final boolean has(LivingEntity entity, String id)
	{
		if (entity == null)
			throw new IllegalArgumentException("Entity cannot be null");
		return has(entity.getUniqueId(), id);
	}

	/** Returns true if the given uuid has an attribute of the given id */
	public static final boolean has(UUID uuid, String id)
	{
		if (uuid == null || id == null)
			throw new IllegalArgumentException("UUID or identifier cannot be null");
		return getMap(id).containsKey(uuid);
	}

	/** Removes the given attribute from the system */
	public static final void remove(Attribute attribute)
	{
		if (attribute == null)
			throw new IllegalArgumentException("Attribute cannot be null");
		remove(attribute.getUUID(), attribute.getID());
	}

	/** Removes the given attribute for the given entity from the system */
	public static final void remove(LivingEntity entity, String id)
	{
		if (entity == null)
			throw new IllegalArgumentException("Entity or attribute cannot be null");
		remove(entity.getUniqueId(), id);
	}

	/** Removes the given attribute for the given uuid from the system */
	public static final void remove(UUID uuid, String id)
	{
		if (uuid == null || id == null)
			throw new IllegalArgumentException("UUID or attribute cannot be null");
		getMap(id).remove(uuid);
	}

	/** Returns the cumulative effect of the given attribute nodes on the given value */
	public static final float getValue(float base, Attribute... attributes)
	{
		float scale = 1.0f;
		float multiplier = 0.0f;
		float flat = 0.0f;
		for (Attribute attribute : attributes)
		{
			scale *= attribute.getScale();
			multiplier += attribute.getMultiplier();
			flat += attribute.getFlat();
		}
		return scale * (flat + base * (1.0f + multiplier));
	}
}
