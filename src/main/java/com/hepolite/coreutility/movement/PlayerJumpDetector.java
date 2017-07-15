package com.hepolite.coreutility.movement;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.hepolite.coreutility.event.CoreHandler;
import com.hepolite.coreutility.event.events.PlayerJumpEvent;

public class PlayerJumpDetector extends CoreHandler
{
	private final Map<UUID, Integer> jumps = new HashMap<UUID, Integer>();

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public final void onPlayerJoin(PlayerJoinEvent event)
	{
		Player player = event.getPlayer();
		jumps.put(player.getUniqueId(), player.getStatistic(Statistic.JUMP));
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public final void onPlayerQuit(PlayerQuitEvent event)
	{
		jumps.remove(event.getPlayer().getUniqueId());
	}

	@Override
	public final void onTick(int tick)
	{
		for (Player player : Bukkit.getOnlinePlayers())
		{
			int currentJumps = player.getStatistic(Statistic.JUMP);
			int previousJumps = jumps.get(player.getUniqueId());
			if (previousJumps < currentJumps)
			{
				Block block = player.getLocation().getBlock();
				Material type = block.getType();
				if (!block.isLiquid() && type != Material.LADDER && type != Material.VINE)
					post(new PlayerJumpEvent(player));
				jumps.put(player.getUniqueId(), currentJumps);
			}
		}
	}
}
