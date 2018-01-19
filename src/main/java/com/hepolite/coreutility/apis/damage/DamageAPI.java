package com.hepolite.coreutility.apis.damage;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageModifier;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import com.hepolite.coreutility.apis.attributes.Attribute;
import com.hepolite.coreutility.apis.attributes.AttributeAPI;
import com.hepolite.coreutility.apis.attributes.Modifier;
import com.hepolite.coreutility.apis.attributes.Attributes;
import com.hepolite.coreutility.event.CoreHandler;
import com.hepolite.coreutility.event.events.DamageEvent;
import com.hepolite.coreutility.log.Log;

public class DamageAPI extends CoreHandler
{
	private static boolean didDamage = false;
	private static boolean customEvent = false;
	private static Damage currentDamage = null;
	private static final Attribute damageModifier = (new Attribute("", null)).setMinValue(0.0f);

	/** Applies some damage to the given entity, of the given damage type; returns true if the damage was dealt */
	public static final boolean damage(LivingEntity target, Damage damage)
	{
		return damage(target, null, damage);
	}

	/** Applies some damage to the given entity, of the given damage type; returns true if the damage was dealt */
	public static final boolean damage(LivingEntity target, LivingEntity attacker, Damage damage)
	{
		if (target == null)
			return false;
		didDamage = false;
		currentDamage = damage;
		if (attacker == null)
			target.damage(damage.getAmount());
		else
			target.damage(damage.getAmount(), attacker);
		return didDamage;
	}

	/** Applies some damage to the given entity, using the given item; returns true if the damage was dealt */
	public static final boolean damage(LivingEntity target, ItemStack item)
	{
		return damage(target, null, item);
	}

	/** Applies some damage to the given entity, using the given item; returns true if the damage was dealt */
	public static final boolean damage(LivingEntity target, LivingEntity attacker, ItemStack item)
	{
		float baseDamage;
		Material type = item == null ? Material.AIR : item.getType();
		switch (type)
		{
		case WOOD_PICKAXE:
		case GOLD_PICKAXE:
			baseDamage = 2.0f;
			break;
		case WOOD_SPADE:
		case GOLD_SPADE:
			baseDamage = 2.5f;
			break;
		case STONE_PICKAXE:
			baseDamage = 3.0f;
			break;
		case STONE_SPADE:
			baseDamage = 3.5f;
			break;
		case WOOD_SWORD:
		case GOLD_SWORD:
		case IRON_PICKAXE:
			baseDamage = 4.0f;
			break;
		case IRON_SPADE:
			baseDamage = 4.5f;
			break;
		case STONE_SWORD:
		case DIAMOND_PICKAXE:
			baseDamage = 5.0f;
			break;
		case DIAMOND_SPADE:
			baseDamage = 5.5f;
			break;
		case IRON_SWORD:
			baseDamage = 6.0f;
			break;
		case WOOD_AXE:
		case GOLD_AXE:
		case DIAMOND_SWORD:
			baseDamage = 7.0f;
			break;
		case STONE_AXE:
		case IRON_AXE:
		case DIAMOND_AXE:
			baseDamage = 9.0f;
			break;
		default:
			return damage(target, attacker, new Damage(DamageType.NORMAL, 1.0f));
		}

		int fireAspect = item.getEnchantmentLevel(Enchantment.FIRE_ASPECT);
		int sharpness = item.getEnchantmentLevel(Enchantment.DAMAGE_ALL);
		int bane = item.getEnchantmentLevel(Enchantment.DAMAGE_ARTHROPODS);
		int smite = item.getEnchantmentLevel(Enchantment.DAMAGE_UNDEAD);

		baseDamage += 0.5f * sharpness;
		EntityType mob = target.getType();
		if (mob == EntityType.SKELETON || mob == EntityType.ZOMBIE || mob == EntityType.WITHER
				|| mob == EntityType.WITHER_SKELETON || mob == EntityType.PIG_ZOMBIE
				|| mob == EntityType.SKELETON_HORSE || mob == EntityType.ZOMBIE_HORSE
				|| mob == EntityType.STRAY || mob == EntityType.ZOMBIE_VILLAGER
				|| mob == EntityType.HUSK)
			baseDamage += 2.5f * smite;
		if (mob == EntityType.SPIDER || mob == EntityType.CAVE_SPIDER
				|| mob == EntityType.SILVERFISH || mob == EntityType.ENDERMITE)
			baseDamage += 2.5f * bane;

		if (attacker != null)
		{
			PotionEffect effect = attacker.getPotionEffect(PotionEffectType.INCREASE_DAMAGE);
			if (effect != null)
				baseDamage += 3.0f * (float) (effect.getAmplifier() + 1);
		}

		Damage damage = new Damage(DamageType.NORMAL, baseDamage);
		if (damage(target, attacker, damage))
		{
			if (fireAspect > 0)
				target.setFireTicks(Math.max(80 * fireAspect, target.getFireTicks()));
			return true;
		}
		return false;
	}

	/** Heals the given entity for some amount, using the specified reason; returns true if the healing was performed */
	public static final boolean heal(LivingEntity target, float amount, RegainReason reason)
	{
		if (target == null || target.isDead() || !target.isValid())
			return false;
		EntityRegainHealthEvent event = new EntityRegainHealthEvent(target, Math.max(0.0f, amount), reason);
		Bukkit.getServer().getPluginManager().callEvent(event);
		if (event.isCancelled())
			return false;
		target.setHealth(Math.min(target.getHealth() + event.getAmount(),
				target.getAttribute(org.bukkit.attribute.Attribute.GENERIC_MAX_HEALTH).getValue()));
		return true;
	}
	
	/** Applies a knockback to the target entity, from the given location; values are measured in m/s */
	public static final void applyKnockback(LivingEntity target, Location origin, float strength,
			float lift)
	{
		Vector delta = target.getLocation().subtract(origin).toVector();
		delta.setY(0.0);
		if (delta.lengthSquared() > 0.001)
			delta.normalize();
		target.setVelocity(target.getVelocity().add(delta.multiply(0.05f * strength))
				.add(new Vector(0.0f, 0.05f * lift, 0.0f)));
	}

	// ////////////////////////////////////////////////////////

	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = false)
	public final void onEntityTakeDamage(EntityDamageEvent event)
	{
		if (customEvent)
			return;

		// Grab target and attacker, if applicable
		LivingEntity target = null;
		if (event.getEntity() instanceof LivingEntity)
			target = (LivingEntity) event.getEntity();
		if (target == null)
			return;

		LivingEntity attacker = null;
		if (event instanceof EntityDamageByEntityEvent)
		{
			EntityDamageByEntityEvent e = (EntityDamageByEntityEvent) event;
			Entity a = null;
			if (e.getDamager() instanceof Projectile)
			{
				if (((Projectile) e.getDamager()).getShooter() instanceof LivingEntity)
					a = (Entity) ((Projectile) e.getDamager()).getShooter();
			}
			else
				a = e.getDamager();
			if (a != null && a instanceof LivingEntity)
				attacker = (LivingEntity) a;
		}

		// If the attack is a normal damage event, that event needs to be
		// converted into a damage type and class
		DamageClass damageClass = DamageClass.PHYSICAL;
		DamageType damageType = DamageType.NORMAL;

		if (currentDamage == null)
		{
			switch (event.getCause())
			{
			case BLOCK_EXPLOSION:
			case ENTITY_EXPLOSION:
				damageType = DamageType.EXPLOSION;
				damageClass = DamageClass.PHYSICAL;
				break;
			case CONTACT:
			case CRAMMING:
			case ENTITY_ATTACK:
			case PROJECTILE:
			case THORNS:
				damageType = DamageType.NORMAL;
				damageClass = DamageClass.PHYSICAL;
				break;
			case CUSTOM:
			case SUICIDE:
			case VOID:
				damageType = DamageType.TRUE;
				damageClass = DamageClass.TRUE;
				break;
			case DRAGON_BREATH:
			case FIRE:
			case MELTING:
				damageType = DamageType.FIRE;
				damageClass = DamageClass.ELEMENTAL;
				break;
			case DROWNING:
			case SUFFOCATION:
				damageType = DamageType.SUFFOCATION;
				damageClass = DamageClass.PERSONAL;
				break;
			case FALL:
			case FLY_INTO_WALL:
				damageType = DamageType.FALL;
				damageClass = DamageClass.PHYSICAL;
				break;
			case FALLING_BLOCK:
				damageType = DamageType.BLUNT;
				damageClass = DamageClass.PHYSICAL;
				break;
			case FIRE_TICK:
				damageType = DamageType.BURNING;
				damageClass = DamageClass.PERSONAL;
				break;
			case HOT_FLOOR:
			case LAVA:
				damageType = DamageType.LAVA;
				damageClass = DamageClass.ELEMENTAL;
				break;
			case LIGHTNING:
				damageType = DamageType.ELECTRICITY;
				damageClass = DamageClass.ELEMENTAL;
				break;
			case MAGIC:
				damageType = DamageType.MAGIC;
				damageClass = DamageClass.MAGICAL;
				break;
			case POISON:
				damageType = DamageType.POISON;
				damageClass = DamageClass.PERSONAL;
				break;
			case STARVATION:
				damageType = DamageType.HUNGER;
				damageClass = DamageClass.PERSONAL;
				break;
			case WITHER:
				damageType = DamageType.WITHER;
				damageClass = DamageClass.PERSONAL;
				break;
			default:
				Log.warning("Attempted to convert damage cause " + event.getCause().toString()
						+ ", which is not registered in DamageAPI!");
				break;
			}
		}
		// Custom events rely on default Minecraft attacks since Spigot sucks;
		// factor in armor efficiencies against the specific types and classes of damage
		else
		{
			damageType = currentDamage.getDamageType();
			damageClass = currentDamage.getDamageClass();

			if (event.isApplicable(DamageModifier.ARMOR))
				event.setDamage(DamageModifier.ARMOR,
						damageType.getArmorEfficiency() * event.getDamage(DamageModifier.ARMOR));
			if (event.isApplicable(DamageModifier.MAGIC))
				event.setDamage(
						DamageModifier.MAGIC,
						damageType.getEnchantmentEfficiency()
								* event.getDamage(DamageModifier.MAGIC));
			if (event.isApplicable(DamageModifier.RESISTANCE))
				event.setDamage(DamageModifier.RESISTANCE, damageType.getResistanceEfficiency()
						* event.getDamage(DamageModifier.RESISTANCE));
			if (event.isApplicable(DamageModifier.BLOCKING))
				event.setDamage(
						DamageModifier.BLOCKING,
						damageType.getBlockingEfficiency()
								* event.getDamage(DamageModifier.BLOCKING));
		}

		// Calculate final damage by factoring in all damage reductions/increases,
		// if the event is cancelled, also cancel the custom event if applicable
		resetDamageModifier(target, attacker, damageClass, damageType);
		damageModifier.setBaseValue((float) event.getDamage());
		event.setDamage(damageModifier.getValue());

		if (currentDamage == null)
			currentDamage = new Damage(damageClass, damageType, (float) event.getDamage());
		else
			currentDamage.setAmount((float) event.getDamage());

		DamageEvent damageEvent = new DamageEvent(target, attacker, currentDamage);
		damageEvent.setCancelled(event.isCancelled());
		post(damageEvent);
		event.setDamage(damageEvent.getDamage().getAmount());
		if (damageEvent.isCancelled())
			event.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = false)
	public final void onEntityTakeDamageMonitor(EntityDamageEvent event)
	{
		if (!event.isCancelled())
			didDamage = true;
		currentDamage = null;
	}

	// /////////////////////////////////////////////////////////////////

	/** Applies the given attribute on the damage */
	private final void resetDamageModifier(LivingEntity target, LivingEntity attacker,
			DamageClass damageClass, DamageType damageType)
	{
		damageModifier.clear();

		// ////////////////////////////////////////////////////////////////

		Attribute defenceAll = AttributeAPI.get(target, Attributes.DEFENCE_ALL);
		Attribute defenceSpecificClass = AttributeAPI.get(target,
				Attributes.DEFENCE_CLASS(damageClass));
		Attribute defenceSpecificType = AttributeAPI.get(target,
				Attributes.DEFENCE_TYPE(damageType));

		Modifier defenceModifier = damageModifier.getModifier("defence");
		defenceModifier
				.setScale(1.0f / (defenceAll.getScale() * defenceSpecificClass.getScale() * defenceSpecificType
						.getScale()) - 1.0f);
		defenceModifier.setMultiplier(-defenceAll.getMultiplier()
				- defenceSpecificClass.getMultiplier() - defenceSpecificType.getMultiplier());
		defenceModifier.setFlat(-defenceAll.getFlat() - defenceSpecificClass.getFlat()
				- defenceSpecificType.getFlat());

		// ////////////////////////////////////////////////////////////////

		if (attacker == null)
			return;

		Attribute attackAll = AttributeAPI.get(attacker, Attributes.ATTACK_ALL);
		Attribute attackSpecificClass = AttributeAPI.get(attacker,
				Attributes.ATTACK_CLASS(damageClass));
		Attribute attackSpecificType = AttributeAPI.get(attacker,
				Attributes.ATTACK_TYPE(damageType));

		Modifier attackClassModifier = damageModifier.getModifier("attack");
		attackClassModifier.setScale(attackAll.getScale() * attackSpecificClass.getScale()
				* attackSpecificType.getScale() - 1.0f);
		attackClassModifier.setMultiplier(attackAll.getMultiplier()
				+ attackSpecificClass.getMultiplier() + attackSpecificType.getMultiplier());
		attackClassModifier.setFlat(attackAll.getFlat() + attackSpecificClass.getFlat()
				+ attackSpecificType.getFlat());
	}
}
