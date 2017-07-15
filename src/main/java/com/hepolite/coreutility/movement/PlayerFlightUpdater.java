package com.hepolite.coreutility.movement;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;

import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

import com.hepolite.coreutility.event.CoreHandler;
import com.hepolite.coreutility.event.events.PlayerFlightAllowEvent;

public class PlayerFlightUpdater extends CoreHandler
{
	@Override
	public final void onTick(int tick)
	{
		for (Player player : Bukkit.getOnlinePlayers())
			handleFlightPermissions(player);
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public final void onPlayerJoin(PlayerJoinEvent event)
	{
		handleFlightPermissions(event.getPlayer());
	}

	/** Fixes flight permissions for the given player */
	private final void handleFlightPermissions(Player player)
	{
		PermissionUser user = PermissionsEx.getUser(player);
		boolean permBasic = user.has(MovementSettings.PERMISSION_FLIGHT_BASIC, player.getWorld().getName());
		boolean permAdmin = user.has(MovementSettings.PERMISSION_FLIGHT_ADMIN, player.getWorld().getName());
		permAdmin |= player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR;
		player.setAllowFlight(permAdmin || permBasic);

		if (!permAdmin)
		{
			PlayerFlightAllowEvent event = new PlayerFlightAllowEvent(player, true);
			post(event);
			if (!event.isFlightAllowed())
			{
				player.setAllowFlight(false);
				player.setFlying(false);
			}
			else
				player.setAllowFlight(true);
		}
	}
}
