package com.hepolite.coreutility.util;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import com.hepolite.coreutility.event.CoreHandler;

public class BlockHandler extends CoreHandler
{
	private static BlockHandler instance;
	private static boolean isTestEvent = false;
	private static boolean isTestEventCancelled = false;

	public BlockHandler()
	{
		instance = this;
	}

	/** Places the block with the given material and meta at the given location, as the given player */
	public final boolean playerPlaceBlock(Player player, Location location, Material material)
	{
		return playerPlaceBlock(player, location, new ItemStack(material));
	}

	/** Places the block with the given material and meta at the given location, as the given player */
	public final boolean playerPlaceBlock(Player player, Location location, Material material, byte meta)
	{
		return playerPlaceBlock(player, location, new ItemStack(material, 1, meta));
	}

	/** Places the block with the given material and meta at the given location, as the given player */
	@SuppressWarnings("deprecation")
	public final boolean playerPlaceBlock(Player player, Location location, ItemStack item)
	{
		Block block = location.getBlock();
		BlockState oldState = block.getState();

		// Since chests and other inventories can drop their contents when overwritten,
		// make sure to run the block place event through a fake test placement first
		isTestEvent = true;
		instance.post(new BlockPlaceEvent(block, oldState, block, item, player, false, EquipmentSlot.HAND));
		isTestEvent = false;
		if (isTestEventCancelled)
			return false;

		// At this point, the placement *should* be legal. But in case it isn't, and to
		// make sure other plugins know of the event, fire it again (after changing data)
		block.setType(item.getType());
		block.setData((byte) item.getDurability());

		BlockPlaceEvent event = (BlockPlaceEvent) instance.post(new BlockPlaceEvent(block, oldState, block, item, player, false, EquipmentSlot.HAND));
		if (event.isCancelled())
		{
			// This will NOT restore inventories for broken chests!
			oldState.update(true);
			return false;
		}
		return true;
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
	public final void onBlockPlace(BlockPlaceEvent event)
	{
		if (isTestEvent)
		{
			isTestEventCancelled = event.isCancelled();
			event.setCancelled(true);
		}
	}
}
