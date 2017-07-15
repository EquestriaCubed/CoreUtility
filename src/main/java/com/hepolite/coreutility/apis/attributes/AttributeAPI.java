package com.hepolite.coreutility.apis.attributes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.entity.LivingEntity;

public class AttributeAPI
{
	private final static Map<String, Map<UUID, Attribute>> maps = new HashMap<String, Map<UUID, Attribute>>();

	/** Ticks the API */
	public final static void onTick(int tick)
	{
		if (tick % 20 != 5)
			return;

		// Collection<Attribute> emptyAttributes = new ArrayList<Attribute>();
		for (Entry<String, Map<UUID, Attribute>> e : maps.entrySet())
		{
			for (Entry<UUID, Attribute> entry : e.getValue().entrySet())
			{
				Attribute attribute = entry.getValue();
				for (AttributeModifier modifier : attribute.getModifiers())
				{
					if (modifier.getLifetime() == -1)
						continue;
					modifier.setLifetime(modifier.getLifetime() - 1);
					if (modifier.getLifetime() <= 0)
						attribute.removeModifier(modifier);
				}
				// if (attribute.isEmpty())
				// emptyAttributes.add(attribute);
			}
		}
		// for (Attribute attribute : emptyAttributes)
		// remove(attribute);
	}

	/** Returns the node map for the given player; will return a generic map if null is specified */
	private final static Map<UUID, Attribute> getMap(String name)
	{
		Map<UUID, Attribute> map = maps.get(name);
		if (map != null)
			return map;
		map = new HashMap<UUID, Attribute>();
		maps.put(name, map);
		return map;
	}

	// /////////////////////////////////////////////////////////////////

	/** Returns an attribute with the given name */
	public final static Attribute get(String name)
	{
		return get((UUID) null, name);
	}

	/** Returns an attribute with the given name, under the given entity; returns a generic node if the entity is null */
	public final static Attribute get(LivingEntity entity, String name)
	{
		return get(entity == null ? null : entity.getUniqueId(), name);
	}

	/** Returns an attribute with the given name, under the given uuid */
	public final static Attribute get(UUID uuid, String name)
	{
		Map<UUID, Attribute> map = getMap(name);
		Attribute node = map.get(uuid);
		if (node != null)
			return node;
		node = new Attribute(name, uuid);
		map.put(uuid, node);
		return node;
	}

	/** Returns all attributes of the given attribute name */
	public final static Collection<Attribute> getAttributes(String name)
	{
		Map<UUID, Attribute> map = getMap(name);
		Collection<Attribute> nodes = new ArrayList<Attribute>(map.size());
		for (Entry<UUID, Attribute> entry : map.entrySet())
			nodes.add(entry.getValue());
		return nodes;
	}

	/** Removes the given attribute from the system */
	public final static void remove(Attribute node)
	{
		if (node == null)
			return;
		remove(node.getUUID(), node.getName());
	}

	/** Removes the given attribute for the given entity from the system */
	public final static void remove(LivingEntity entity, String name)
	{
		remove(entity == null ? null : entity.getUniqueId(), name);
	}

	/** Removes the given attribute for the given uuid from the system */
	public final static void remove(UUID uuid, String name)
	{
		Map<UUID, Attribute> map = maps.get(name);
		if (map != null)
			map.remove(uuid);
	}
}
