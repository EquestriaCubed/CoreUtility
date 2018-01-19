package com.hepolite.coreutility.movement;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.hepolite.coreutility.apis.attributes.Attribute;
import com.hepolite.coreutility.apis.attributes.AttributeAPI;
import com.hepolite.coreutility.apis.attributes.Attributes;
import com.hepolite.coreutility.event.CoreHandler;

public class PlayerSpeedUpdater extends CoreHandler
{
	@Override
	public final void onTick(int tick)
	{
		for (Attribute node : AttributeAPI.getNodes(Attributes.SPEED_FLY))
			node.getPlayer().ifPresent((player) -> player.setFlySpeed(node.getValue()));
		for (Attribute node : AttributeAPI.getNodes(Attributes.SPEED_WALK))
			node.getPlayer().ifPresent((player) -> player.setWalkSpeed(node.getValue()));
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public final void onPlayerJoin(PlayerJoinEvent event)
	{
		Player player = event.getPlayer();
		AttributeAPI.get(player, Attributes.SPEED_FLY).setValues(MovementSettings.MOVEMENT_SPEED_FLY, -1.0f, 1.0f);
		AttributeAPI.get(player, Attributes.SPEED_WALK).setValues(MovementSettings.MOVEMENT_SPEED_WALK, -1.0f, 1.0f);
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public final void onPlayerQuit(PlayerQuitEvent event)
	{
		Player player = event.getPlayer();
		AttributeAPI.remove(player, Attributes.SPEED_FLY);
		AttributeAPI.remove(player, Attributes.SPEED_WALK);
	}
}
