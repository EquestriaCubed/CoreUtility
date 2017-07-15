package com.hepolite.coreutility.hunger;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Player;

import com.hepolite.coreutility.plugin.CorePlugin;
import com.hepolite.coreutility.settings.Settings;
import com.hepolite.coreutility.settings.SettingsSimple;

public class NodeRegistry
{
	private final Settings settings;
	private final Map<UUID, Node> nodes = new HashMap<UUID, Node>();

	public NodeRegistry(CorePlugin plugin)
	{
		settings = new SettingsSimple(plugin, "Hunger", "NodeRegistry");
	}

	/** Saves the registry to disc */
	public final void save()
	{
		for (Node node : nodes.values())
			node.save(settings);
		settings.save();
	}

	/** Adds a player to the registry */
	public final void addPlayer(Player player)
	{
		settings.load();
		getNode(player).load(settings);
	}

	/** Adds a player from the registry */
	public final void removePlayer(Player player)
	{
		getNode(player).save(settings);
		settings.save();
		nodes.remove(player.getUniqueId());
	}

	/** Returns the node associated with the given player */
	public final Node getNode(Player player)
	{
		UUID uuid = player.getUniqueId();
		if (!nodes.containsKey(uuid))
			nodes.put(uuid, new Node(player));
		return nodes.get(uuid);
	}
}
