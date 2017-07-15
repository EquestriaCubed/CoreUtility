package com.hepolite.coreutility.hunger;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.hepolite.coreutility.CoreUtility;
import com.hepolite.coreutility.apis.damage.Damage;
import com.hepolite.coreutility.apis.damage.DamageAPI;
import com.hepolite.coreutility.apis.damage.DamageType;
import com.hepolite.coreutility.event.CoreHandler;
import com.hepolite.coreutility.plugin.CorePlugin;
import com.hepolite.coreutility.settings.SettingsSimple;
import com.hepolite.coreutility.util.InventoryHelper;

public class HungerHandler extends CoreHandler
{
	private final CorePlugin plugin;
	private final FoodRegistry foodRegistry;
	private final NodeRegistry nodeRegistry;

	private final Map<String, Group> groups = new HashMap<String, Group>();
	private final Map<UUID, Group> players = new HashMap<UUID, Group>();
	private boolean ignoreNextSatiatedEvent = false;

	public HungerHandler(CorePlugin plugin)
	{
		this.plugin = plugin;
		this.foodRegistry = new FoodRegistry(plugin, this);
		this.nodeRegistry = new NodeRegistry(plugin);

		addGroup("Default");
	}

	/** Registers a new group with the given name */
	public final void addGroup(String name)
	{
		groups.put(name, new Group(plugin, name));
		onRestart();
	}

	/** Returns the group with the given name */
	public final Group getGroup(String name)
	{
		Group group = name == null ? null : groups.get(name);
		return group == null ? groups.get("Default") : group;
	}

	/** Assigns the group the player is in */
	public final void setPlayerGroup(Player player, String group)
	{
		players.put(player.getUniqueId(), getGroup(group));
	}

	/** Returns the group the player is in */
	public final Group getPlayerGroup(Player player)
	{
		return players.get(player.getUniqueId());
	}

	// //////////////////////////////////////////////////////////////////////

	@Override
	public final void onRestart()
	{
		foodRegistry.wipe();

		File directory = new File(plugin.getDataFolder() + "/Hunger/Consumables");
		for (File file : directory.listFiles())
			foodRegistry.parseGroupSettings("Default", new SettingsSimple(plugin, "Hunger/Consumables", file), "");

		for (Group group : groups.values())
		{
			group.getSettings().load();
			foodRegistry.parseGroupSettings(group.getName(), group.getSettings(), "Food.Consumables");
		}
	}

	@Override
	public final void onDeinitialize()
	{
		nodeRegistry.save();
	}

	@Override
	public final void onTick(int tick)
	{
		for (Player player : Bukkit.getOnlinePlayers())
		{
			if (player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR)
				continue;

			if (tick % 20 == 0)
			{
				updateExhaustion(player);
				handleStarving(player, tick / 20);
				handleHealing(player, tick / 20);
			}
			handleStatusEffects(player);
		}
	}

	/** Handle exhaustion drain and hunger loss */
	private final void updateExhaustion(Player player)
	{
		Group group = getPlayerGroup(player);
		float rateOfChange = group.getRateOfChange();
		float rateOfHunger = group.getRateOfHunger();
		float restingPoint = group.getRestingPoint(CoreUtility.getMovementHandler().getMovementType(player));

		Node node = getHungerNode(player);
		node.changeSaturation(rateOfHunger * -node.getExhaustion());
		node.changeExhaustion(rateOfChange * (restingPoint - node.getExhaustion()));
	}

	/** Handle starvation of players */
	private final void handleStarving(Player player, int tick)
	{
		Group group = getPlayerGroup(player);
		if (!group.isStarvationEnabled() || tick % group.getStarvationFrequency() != 0)
			return;
		Node node = getHungerNode(player);
		if (node.getHunger() != 0.0f)
			return;

		Damage damage = new Damage(DamageType.HUNGER, group.getStarvationDamage());
		DamageAPI.damage(player, damage);
	}

	/** Handle healing of players from being satiated */
	private final void handleHealing(Player player, int tick)
	{
		Group group = getPlayerGroup(player);
		if (!group.isHealingEnabled() || tick % group.getHealingFrequency() != 0)
			return;
		Node node = getHungerNode(player);
		if (node.getHunger() / node.getMaxHunger() < group.getHealingStart())
			return;
		if (player.getHealth() >= player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue())
			return;

		ignoreNextSatiatedEvent = true;
		if (DamageAPI.heal(player, group.getHealingAmount(), RegainReason.SATIATED))
			node.changeSaturation(-group.getHealingCost());
		ignoreNextSatiatedEvent = false;
	}

	/** Handles the status effects on the player */
	private final void handleStatusEffects(Player player)
	{
		Node node = getHungerNode(player);
		for (PotionEffect effect : player.getActivePotionEffects())
		{
			if (effect.getType().equals(PotionEffectType.HUNGER))
				node.changeSaturation(-0.05f * (float) (1 + effect.getAmplifier()));
		}
	}

	// //////////////////////////////////////////////////////////////////////

	/** Returns the node associated with the given player */
	public final Node getHungerNode(Player player)
	{
		return nodeRegistry.getNode(player);
	}

	/** Returns true if the player is allowed to eat the given item */
	public final boolean canPlayerEat(Player player, ItemStack item)
	{
		Group group = getPlayerGroup(player);
		Food food = foodRegistry.getFood(item, group);
		return foodRegistry.isAllowedToConsume(food, group, null);
	}

	/** Handles the consumption of the given food; returns true if the item was eaten */
	private final boolean playerConsume(Food food, Player player)
	{
		Group group = getPlayerGroup(player);
		Node node = getHungerNode(player);
		if (node.getHunger() >= node.getMaxHunger() && !food.isAlwaysEdible())
			return false;

		node.changeHunger(food.getHunger());
		node.changeSaturation(food.getSaturation());
		if (random.nextFloat() < food.getChance())
		{
			for (PotionEffect effect : food.getEffects())
				player.addPotionEffect(effect);
		}

		// Any food that is consumable will be consumed as normal
		// This here will ensure that all other foods will be consumed as well
		if (!InventoryHelper.isConsumable(food.getConsumed()))
		{
			group.getEatingSound().play(player.getEyeLocation());
			InventoryHelper.remove(player, food.getConsumed());
		}
		return true;
	}

	// //////////////////////////////////////////////////////////////////////

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public final void onPlayerJoin(PlayerJoinEvent event)
	{
		nodeRegistry.addPlayer(event.getPlayer());
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public final void onPlayerQuit(PlayerQuitEvent event)
	{
		nodeRegistry.removePlayer(event.getPlayer());
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public final void onPlayerRespawn(PlayerRespawnEvent event)
	{
		nodeRegistry.getNode(event.getPlayer()).reset();
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = false)
	public void onPlayerInteract(PlayerInteractEvent event)
	{
		Player player = event.getPlayer();
		ItemStack item = event.getItem();
		Action action = event.getAction();
		if (action != Action.RIGHT_CLICK_AIR || item == null || item.getType() == Material.AIR)
			return;
		if (player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR)
			return;

		if (!InventoryHelper.isConsumable(item))
			post(new PlayerItemConsumeEvent(player, item));
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onPlayerConsumeItem(PlayerItemConsumeEvent event)
	{
		ItemStack item = event.getItem();
		Player player = event.getPlayer();
		Group group = getPlayerGroup(player);

		Food food = foodRegistry.getFood(item, group);
		Collection<String> reasons = new ArrayList<String>();
		if (food == null || !foodRegistry.isAllowedToConsume(food, group, reasons))
		{
			if (InventoryHelper.isConsumable(item))
			{
				player.sendMessage(group.getCannotConsumeMessage());
				if (reasons.size() != 0)
				{
					String reason = group.getInedibleMessage();
					for (String string : reasons)
						reason += " " + string;
					player.sendMessage(ChatColor.RED + reason);
				}
			}
			event.setCancelled(true);
		}
		else
		{
			if (!playerConsume(food, player))
				event.setCancelled(true);
		}
	}

	/** Overwrites Minecraft healing from saturation system by a custom one */
	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onPlayerHeal(EntityRegainHealthEvent event)
	{
		if (event.getRegainReason() == RegainReason.SATIATED && !ignoreNextSatiatedEvent)
			event.setCancelled(true);
	}

	/** Overwrites Minecraft damage from starvation by a custom system */
	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onPlayerStarve(EntityDamageEvent event)
	{
		if (event.getCause() == DamageCause.STARVATION)
			event.setCancelled(true);
	}
}
