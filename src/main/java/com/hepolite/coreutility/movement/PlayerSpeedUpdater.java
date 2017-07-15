package com.hepolite.coreutility.movement;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.hepolite.coreutility.apis.attributes.Attribute;
import com.hepolite.coreutility.apis.attributes.AttributeAPI;
import com.hepolite.coreutility.apis.attributes.AttributeType;
import com.hepolite.coreutility.event.CoreHandler;

public class PlayerSpeedUpdater extends CoreHandler
{
	@Override
	public final void onTick(int tick)
	{
		for (Attribute node : AttributeAPI.getAttributes(AttributeType.SPEED_FLY))
		{
			Player player = node.getPlayer();
			if (player != null)
				player.setFlySpeed(node.getValue());
		}
		for (Attribute node : AttributeAPI.getAttributes(AttributeType.SPEED_WALK))
		{
			Player player = node.getPlayer();
			if (player != null)
				player.setWalkSpeed(node.getValue());
		}
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public final void onPlayerJoin(PlayerJoinEvent event)
	{
		Player player = event.getPlayer();
		AttributeAPI.get(player, AttributeType.SPEED_FLY).setBaseValue(MovementSettings.MOVEMENT_SPEED_FLY).setMinValue(-1.0f).setMaxValue(1.0f);
		AttributeAPI.get(player, AttributeType.SPEED_WALK).setBaseValue(MovementSettings.MOVEMENT_SPEED_WALK).setMinValue(-1.0f).setMaxValue(1.0f);
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public final void onPlayerQuit(PlayerQuitEvent event)
	{
		Player player = event.getPlayer();
		AttributeAPI.remove(player, AttributeType.SPEED_FLY);
		AttributeAPI.remove(player, AttributeType.SPEED_WALK);
	}
}
