package com.hepolite.coreutility.settings;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.hepolite.coreutility.apis.nbt.NBTAPI;
import com.hepolite.coreutility.apis.nbt.NBTField;
import com.hepolite.coreutility.apis.nbt.NBTList;
import com.hepolite.coreutility.apis.nbt.NBTTag;
import com.hepolite.coreutility.log.Log;
import com.hepolite.coreutility.plugin.CorePlugin;
import com.hepolite.coreutility.util.InventoryHelper;

public abstract class Settings
{
	private final String name;
	private final File configFile;
	private final FileConfiguration config;

	/** Sets up a settings file under the given plugin folder */
	public Settings(CorePlugin plugin, String name)
	{
		this(plugin, null, name);
	}

	/** Sets up a settings file under the given plugin folder */
	public Settings(CorePlugin plugin, String path, String name)
	{
		this.name = name;
		this.configFile = new File(plugin.getDataFolder(), (path == null ? "" : path + "/") + name + ".yml");
		this.config = YamlConfiguration.loadConfiguration(configFile);

		initialize();
	}

	/** Adds in all the default fields that should exist in the config when it is created for the first time */
	protected abstract void addDefaultValues();

	// ///////////////////////////////////////////////////////////////////////////////////////
	// CORE FUNCTIONALITY // CORE FUNCTIONALITY // CORE FUNCTIONALITY // CORE FUNCTIONALITY //
	// ///////////////////////////////////////////////////////////////////////////////////////

	/** Sets up the config file, creating it if it does not exist */
	private final void initialize()
	{
		boolean exists = configFile.exists();
		if (!exists)
		{
			Log.debug("Config file " + configFile.getPath() + " does not exist, creating it...");
			addDefaultValues();
			save();
		}
		else
			load();
	}

	/** Removes all settings */
	public final void wipe()
	{
		for (String key : getKeys(""))
			remove(key);
	}

	/** Saves the config file to disc */
	public final void save()
	{
		try
		{
			onSaveConfigFile();
			config.save(configFile);
		}
		catch (IOException e)
		{
			Log.warning("Failed to save configuration file '" + name + "'!");
			Log.warning(e.getLocalizedMessage());
		}
	}

	/** Loads the config file from disc */
	public final void load()
	{
		try
		{
			config.load(configFile);
			onLoadConfigFile();
		}
		catch (IOException | InvalidConfigurationException e)
		{
			Log.warning("Failed to load configuration file '" + name + "'!");
			Log.warning(e.getLocalizedMessage());
		}
	}

	/** Invoked whenever the config file is saved */
	protected abstract void onSaveConfigFile();

	/** Invoked whenever the config file is loaded */
	protected abstract void onLoadConfigFile();

	// ///////////////////////////////////////////////////////////////////////////////////////////////
	// GET/SET DATA // GET/SET DATA // GET/SET DATA // GET/SET DATA // GET/SET DATA // GET/SET DATA //
	// ///////////////////////////////////////////////////////////////////////////////////////////////

	/** Assigns one default value to the calling configuration class */
	public final void set(String propertyName, Object value)
	{
		if (value instanceof SoundSetting)
			set(propertyName, (SoundSetting) value);
		else if (value instanceof PotionEffectSetting)
			set(propertyName, (PotionEffectSetting) value);
		else if (value instanceof PotionEffectSetting[])
			set(propertyName, (PotionEffectSetting[]) value);
		else if (value instanceof ItemStack)
			set(propertyName, (ItemStack) value);
		else if (value instanceof ItemStack[])
			set(propertyName, (ItemStack[]) value);
		else if (value instanceof PotionEffectType)
			set(propertyName, (PotionEffectType) value);
		else if (value instanceof PotionEffectType[])
			set(propertyName, (PotionEffectType[]) value);
		else if (value instanceof Enchantment)
			set(propertyName, (Enchantment) value);
		else if (value instanceof Enchantment[])
			set(propertyName, (Enchantment[]) value);
		else if (value instanceof EntityType)
			set(propertyName, (EntityType) value);
		else if (value instanceof EntityType[])
			set(propertyName, (EntityType[]) value);
		else if (value instanceof NBTTag)
			set(propertyName, (NBTTag) value);
		else if (value instanceof NBTList)
			set(propertyName, (NBTList) value);
		else
			config.set(propertyName, value);
	}

	private final void set(String propertyName, SoundSetting setting)
	{
		config.set(propertyName + ".enable", setting.enable);
		config.set(propertyName + ".sound", setting.sound.toString().toLowerCase());
		config.set(propertyName + ".volume", setting.volume);
		config.set(propertyName + ".pitch", setting.pitch);
	}

	private final void set(String propertyName, PotionEffectSetting setting)
	{
		config.set(propertyName + ".type", setting.type.getName().toLowerCase());
		config.set(propertyName + ".duration", setting.duration);
		config.set(propertyName + ".amplifier", setting.amplifier);
	}

	private final void set(String propertyName, PotionEffectSetting[] setting)
	{
		for (int i = 0; i < setting.length; i++)
			set(propertyName + ".Effect " + i, setting[i]);
	}

	private final void set(String propertyName, ItemStack item)
	{
		config.set(propertyName + ".type", item.getType().toString().toLowerCase());
		config.set(propertyName + ".amount", item.getAmount());
		config.set(propertyName + ".meta", item.getDurability());

		ItemMeta meta = item.getItemMeta();
		config.set(propertyName + ".name", meta == null ? null : meta.getDisplayName());
		config.set(propertyName + ".lore", meta == null ? null : meta.getLore());

		set(propertyName + ".nbt", NBTAPI.hasTag(item) ? NBTAPI.getTag(item) : null);
	}

	private final void set(String propertyName, ItemStack[] items)
	{
		for (int i = 0; i < items.length; i++)
			set(propertyName + "Item " + i, items[i]);
	}

	private final void set(String propertyName, PotionEffectType setting)
	{
		config.set(propertyName, setting.getName().toLowerCase());
	}

	private final void set(String propertyName, PotionEffectType[] setting)
	{
		List<String> types = new LinkedList<String>();
		for (PotionEffectType type : setting)
			types.add(type.getName().toLowerCase());
		config.set(propertyName, types);
	}

	private final void set(String propertyName, Enchantment setting)
	{
		config.set(propertyName, setting.getName().toLowerCase());
	}

	private final void set(String propertyName, Enchantment[] setting)
	{
		List<String> types = new LinkedList<String>();
		for (Enchantment enchantment : setting)
			types.add(enchantment.getName().toLowerCase());
		config.set(propertyName, types);
	}

	private final void set(String propertyName, EntityType setting)
	{
		config.set(propertyName, setting.toString().toLowerCase());
	}

	private final void set(String propertyName, EntityType[] setting)
	{
		List<String> types = new LinkedList<String>();
		for (EntityType entityType : setting)
			types.add(entityType.toString().toLowerCase());
		config.set(propertyName, types);
	}

	public final void set(String property, NBTTag tag)
	{
		remove(property);
		if (tag == null)
			return;
		for (String key : tag.getKeys())
		{
			String path = property + "." + key;
			NBTField field = tag.getField(key);
			if (field == null)
				continue;

			switch (field)
			{
			case BYTE:
				set(path + "=BYTE", tag.getByte(key));
				break;
			case DOUBLE:
				set(path + "=DOUBLE", tag.getDouble(key));
				break;
			case FLOAT:
				set(path + "=FLOAT", tag.getFloat(key));
				break;
			case INT:
				set(path + "=INT", tag.getInt(key));
				break;
			case LIST:
				Log.debug("Storing list " + tag.getList(key) + " at path " + path);
				set(path + "=LIST", tag.getList(key));
				break;
			case LONG:
				set(path + "=LONG", tag.getLong(key));
				break;
			case SHORT:
				set(path + "=SHORT", tag.getShort(key));
				break;
			case STRING:
				Log.debug("Storing list " + tag.getString(key) + " at path " + path);
				set(path + "=STRING", tag.getString(key));
				break;
			case TAG:
				Log.debug("Storing list " + tag.getTag(key) + " at path " + path);
				set(path + "=TAG", tag.getTag(key));
				break;
			default:
				break;
			}
		}
	}

	public final void set(String property, NBTList tag)
	{
		remove(property);
		if (tag == null)
			return;
		for (int i = 0; i < tag.size(); i++)
		{
			String path = property + "." + i;
			NBTField field = tag.getField(i);
			if (field == null)
				continue;

			switch (field)
			{
			case BYTE:
				set(path + "=BYTE", tag.getByte(i));
				break;
			case DOUBLE:
				set(path + "=DOUBLE", tag.getDouble(i));
				break;
			case FLOAT:
				set(path + "=FLOAT", tag.getFloat(i));
				break;
			case INT:
				set(path + "=INT", tag.getInt(i));
				break;
			case LIST:
				set(path + "=LIST", tag.getList(i));
				break;
			case LONG:
				set(path + "=LONG", tag.getLong(i));
				break;
			case SHORT:
				set(path + "=SHORT", tag.getShort(i));
				break;
			case STRING:
				Log.debug("Storing string " + tag.getString(i) + " at path " + path);
				set(path + "=STRING", tag.getString(i));
				break;
			case TAG:
				Log.debug("Storing tag " + tag.getTag(i) + " at path " + path);
				set(path + "=TAG", tag.getTag(i));
				break;
			default:
				break;
			}
		}
	}

	/** Returns true if the given property exists in the config file */
	public final boolean has(String propertyName)
	{
		return config.contains(propertyName);
	}

	/** Removes the given field from the config */
	public final void remove(String propertyName)
	{
		config.set(propertyName, null);
	}

	/** Returns a boolean value from the config file */
	public final boolean getBool(String propertyName)
	{
		return config.getBoolean(propertyName);
	}

	/** Returns a boolean value from the config file */
	public final boolean getBool(String propertyName, boolean def)
	{
		return has(propertyName) ? getBool(propertyName) : def;
	}

	/** Returns a long value from the config file */
	public final long getLong(String propertyName)
	{
		return config.getLong(propertyName);
	}

	/** Returns a long value from the config file */
	public final long getLong(String propertyName, long def)
	{
		return has(propertyName) ? getLong(propertyName) : def;
	}

	/** Returns an integer value from the config file */
	public final int getInt(String propertyName)
	{
		return config.getInt(propertyName);
	}

	/** Returns an integer value from the config file */
	public final int getInt(String propertyName, int def)
	{
		return has(propertyName) ? getInt(propertyName) : def;
	}

	/** Returns a short value from the config file */
	public final short getShort(String propertyName)
	{
		return (short) config.getInt(propertyName);
	}

	/** Returns a short value from the config file */
	public final short getShort(String propertyName, short def)
	{
		return has(propertyName) ? getShort(propertyName) : def;
	}

	/** Returns a byte value from the config file */
	public final byte getByte(String propertyName)
	{
		return (byte) config.getInt(propertyName);
	}

	/** Returns a byte value from the config file */
	public final byte getByte(String propertyName, byte def)
	{
		return has(propertyName) ? getByte(propertyName) : def;
	}

	/** Returns a floating point value from the config file */
	public final float getFloat(String propertyName)
	{
		return (float) config.getDouble(propertyName);
	}

	/** Returns a floating point value from the config file */
	public final float getFloat(String propertyName, float def)
	{
		return has(propertyName) ? getFloat(propertyName) : def;
	}

	/** Returns a double point value from the config file */
	public final double getDouble(String propertyName)
	{
		return config.getDouble(propertyName);
	}

	/** Returns a double point value from the config file */
	public final double getDouble(String propertyName, double def)
	{
		return has(propertyName) ? getDouble(propertyName) : def;
	}

	/** Returns a string from the config file */
	public final String getString(String propertyName)
	{
		return config.getString(propertyName);
	}

	/** Returns a string from the config file */
	public final String getString(String propertyName, String def)
	{
		return has(propertyName) ? getString(propertyName) : def;
	}

	/** Returns a list of string from the config file */
	public final List<String> getStringList(String propertyName)
	{
		return config.getStringList(propertyName);
	}

	/** Returns a list of string from the config file */
	public final List<String> getStringList(String propertyName, List<String> def)
	{
		return has(propertyName) ? getStringList(propertyName) : def;
	}

	/** Returns a set of property keys from the config file */
	public final Set<String> getKeys(String propertyName)
	{
		ConfigurationSection section = config.getConfigurationSection(propertyName);
		if (section == null)
			return new HashSet<String>();
		return section.getKeys(false);
	}

	/** Returns a sound setting from the config file */
	public final SoundSetting getSound(String propertyName)
	{
		boolean enable = getBool(propertyName + ".enable");
		Sound sound = Sound.valueOf(getString(propertyName + ".sound").toUpperCase());
		float volume = getFloat(propertyName + ".volume");
		float pitch = getFloat(propertyName + ".pitch");
		return new SoundSetting(enable, sound, volume, pitch);
	}

	/** Returns a potion effect setting from the config file */
	public final PotionEffectSetting getPotionEffect(String propertyName)
	{
		PotionEffectType type = PotionEffectType.getByName(getString(propertyName + ".type").toUpperCase());
		int duration = getInt(propertyName + ".duration");
		int amplifier = getInt(propertyName + ".amplifier");
		return new PotionEffectSetting(type, duration, amplifier);
	}

	/** Returns multiple a potion effect setting from the config file */
	public final List<PotionEffectSetting> getPotionEffects(String propertyName)
	{
		List<PotionEffectSetting> list = new LinkedList<PotionEffectSetting>();
		Set<String> effects = getKeys(propertyName);
		for (String effect : effects)
			list.add(getPotionEffect(propertyName + "." + effect));
		return list;
	}

	/** Returns a potion effect type from the config file */
	public final PotionEffectType getPotionEffectType(String propertyName)
	{
		return PotionEffectType.getByName(getString(propertyName).toUpperCase());
	}

	/** Returns multiple potion effect types from the config file */
	public final List<PotionEffectType> getPotionEffectTypes(String propertyName)
	{
		List<PotionEffectType> types = new LinkedList<PotionEffectType>();
		for (String string : getStringList(propertyName))
		{
			PotionEffectType type = PotionEffectType.getByName(string.toUpperCase());
			if (type != null)
				types.add(type);
		}
		return types;
	}

	/** Returns an enchantment type from the config file */
	public final Enchantment getEnchantmentType(String propertyName)
	{
		return Enchantment.getByName(getString(propertyName).toUpperCase());
	}

	/** Returns multiple enchantment types from the config file */
	public final List<Enchantment> getEnchantmentTypes(String propertyName)
	{
		List<Enchantment> types = new LinkedList<Enchantment>();
		for (String string : getStringList(propertyName))
		{
			Enchantment type = Enchantment.getByName(string.toUpperCase());
			if (type != null)
				types.add(type);
		}
		return types;
	}

	/** Returns an entity type from the config file */
	public final EntityType getEntityType(String propertyName)
	{
		EntityType type = null;
		try
		{
			type = EntityType.valueOf(getString(propertyName).toUpperCase());
		}
		catch (Exception e)
		{}
		return type;
	}

	/** Returns multiple entity types from the config file */
	public final List<EntityType> getEntityTypes(String propertyName)
	{
		List<EntityType> types = new LinkedList<EntityType>();
		for (String string : getStringList(propertyName))
		{
			EntityType type = null;
			try
			{
				type = EntityType.valueOf(string.toUpperCase());
			}
			catch (Exception e)
			{}
			if (type != null)
				types.add(type);
		}
		return types;
	}

	/** Returns a full item with lore, type and display name */
	public final ItemStack getItem(String propertyName)
	{
		// Load up all the data
		Material material = null;
		try
		{
			material = Material.getMaterial(config.getString(propertyName + ".type").toUpperCase());
			if (material == null)
				throw new NullPointerException("Invalid material!");
		}
		catch (Exception exception)
		{
			Log.warning("Failed to parse item '" + propertyName + "'!");
			Log.warning(exception.getMessage());
			return null;
		}
		int amount = config.contains(propertyName + ".amount") ? config.getInt(propertyName + ".amount") : 1;
		short meta = (short) config.getInt(propertyName + ".meta");
		if (meta == -1)
			meta = InventoryHelper.ANY_META;
		String name = config.getString(propertyName + ".name");
		List<String> lore = config.getStringList(propertyName + ".lore");

		// Actually create the item
		ItemStack item = NBTAPI.getItemStack(material, amount, meta);

		// Load additional information
		if (has(propertyName + ".nbt"))
			NBTAPI.setTag(item, getNBTTag(propertyName + ".nbt"));

		ItemMeta itemMeta = item.getItemMeta();
		if (name != null)
			itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
		if (lore != null)
		{
			List<String> list = new LinkedList<String>();
			for (String line : lore)
				list.add(ChatColor.translateAlternateColorCodes('&', line));
			itemMeta.setLore(list);
		}
		item.setItemMeta(itemMeta);
		return item;
	}

	/** Returns a list of full items with lore, type and display name */
	public final List<ItemStack> getItems(String propertyName)
	{
		List<ItemStack> items = new LinkedList<ItemStack>();
		Set<String> keys = getKeys(propertyName);
		for (String key : keys)
			items.add(getItem(propertyName + "." + key));
		return items;
	}

	/** Loads up a NBT Tag from the configuration */
	public final NBTTag getNBTTag(String property)
	{
		NBTTag tag = new NBTTag();

		Set<String> keys = getKeys(property);
		for (String composition : keys)
		{
			String parts[] = composition.split("=");
			if (parts.length != 2)
				continue;

			NBTField field = NBTField.valueOf(parts[1].toUpperCase());
			if (field == null)
				continue;

			String path = property + "." + composition;
			String key = parts[0];

			switch (field)
			{
			case BYTE:
				tag.setByte(key, getByte(path));
				break;
			case DOUBLE:
				tag.setDouble(key, getDouble(path));
				break;
			case FLOAT:
				tag.setFloat(key, getFloat(path));
				break;
			case INT:
				tag.setInt(key, getInt(path));
				break;
			case LIST:
				tag.setList(key, getNBTList(path));
				break;
			case LONG:
				tag.setLong(key, getLong(path));
				break;
			case SHORT:
				tag.setShort(key, getShort(path));
				break;
			case STRING:
				tag.setString(key, getString(path));
				break;
			case TAG:
				tag.setTag(key, getNBTTag(path));
				break;
			default:
				break;
			}
		}
		return tag;
	}

	/** Loads up a NBT List from the configuration */
	public final NBTList getNBTList(String property)
	{
		NBTList tag = new NBTList();

		for (String composition : getKeys(property))
		{
			String parts[] = composition.split("=");
			if (parts.length != 2)
				continue;

			NBTField field = NBTField.valueOf(parts[1].toUpperCase());
			if (field == null)
				continue;

			String path = property + "." + composition;

			switch (field)
			{
			case BYTE:
				tag.addByte(getByte(path));
				break;
			case DOUBLE:
				tag.addDouble(getDouble(path));
				break;
			case FLOAT:
				tag.addFloat(getFloat(path));
				break;
			case INT:
				tag.addInt(getInt(path));
				break;
			case LIST:
				tag.addList(getNBTList(path));
				break;
			case LONG:
				tag.addLong(getLong(path));
				break;
			case SHORT:
				tag.addShort(getShort(path));
				break;
			case STRING:
				tag.addString(getString(path));
				break;
			case TAG:
				tag.addTag(getNBTTag(path));
				break;
			default:
				break;
			}
		}
		return tag;
	}

	// ///////////////////////////////////////////////////////////////////////////////////////////

	/** Converts an itemstack to a simple itemstack string, used for storage. Returns null if item is null */
	public final String writeSimpleItem(ItemStack item)
	{
		return writeSimpleItem(item, true);
	}

	/** Converts an itemstack to a simple itemstack string, used for storage. Returns null if item is null */
	public final String writeSimpleItem(ItemStack item, boolean writeName)
	{
		if (item == null)
			return null;
		ItemMeta itemMeta = item.getItemMeta();

		String itemString = item.getType().toString().toLowerCase();
		if (item.getDurability() != 0)
			itemString += "-" + item.getDurability();
		if (itemMeta != null && itemMeta.hasDisplayName() && writeName)
			itemString += ";" + itemMeta.getDisplayName().replaceAll(String.valueOf(ChatColor.COLOR_CHAR), "&");
		if (item.getAmount() != 1)
			itemString += "=" + item.getAmount();
		return itemString;
	}

	/** Returns a simple itemstack, which is stored on the form MATERIAL-META;NAME=AMOUNT. Meta, name and amount are optional parameters */
	public final ItemStack parseSimpleItem(String itemString)
	{
		Material material = Material.AIR;
		short meta = 0;
		int amount = 1;
		String name = "";

		// Parse the string
		try
		{
			String[] parts = itemString.split("=");
			if (parts.length == 2)
				amount = Integer.parseInt(parts[1]);
			parts = itemString.split(";");
			if (parts.length == 2)
				name = parts[1];
			parts = parts[0].split("-");
			if (parts.length == 2)
				meta = Short.parseShort(parts[1]);
			material = Material.getMaterial(parts[0].toUpperCase());
			if (material == null)
				throw new NullPointerException("Invalid material!");
		}
		catch (Exception exception)
		{
			Log.warning("Failed to parse simple item '" + itemString + "'!");
			Log.warning(exception.getLocalizedMessage());
			return null;
		}
		ItemStack item = new ItemStack(material, amount, meta);
		ItemMeta itemMeta = item.getItemMeta();
		if (!name.equals(""))
			itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
		item.setItemMeta(itemMeta);
		return item;
	}

	/** Converts an location to a simple location string, used for storage. Returns null if the location is null */
	public final String writeSimpleLocation(Location location)
	{
		if (location == null || location.getWorld() == null)
			return null;
		return String.format("%s=%.0f=%.0f=%.0f", location.getWorld().getName(), location.getX(), location.getY(), location.getZ());
	}

	/** Returns a simple location, which is stored on the form WORLD_NAME=X=Y=Z */
	public final Location parseSimpleLocation(String locationString)
	{
		String parts[] = locationString.split("=");
		World world = null;
		int x = 0, y = 0, z = 0;
		try
		{
			world = Bukkit.getWorld(parts[0]);
			x = Integer.parseInt(parts[1]);
			y = Integer.parseInt(parts[2]);
			z = Integer.parseInt(parts[3]);
		}
		catch (Exception e)
		{
			Log.warning("Failed to parse simple location '" + locationString + "'!");
			Log.warning(e.getLocalizedMessage());
			return null;
		}
		return new Location(world, x, y, z);
	}

	// ///////////////////////////////////////////////////////////////////////////////////////////

	/** Sound data node, used to store a sound object to the config file */
	public final static class SoundSetting
	{
		public boolean enable;
		public Sound sound;
		public float volume;
		public float pitch;

		public SoundSetting(boolean enable, Sound sound, float volume, float pitch)
		{
			this.enable = enable;
			this.sound = sound;
			this.volume = volume;
			this.pitch = pitch;
		}

		/** Plays the sound at the given location, if the sound is enabled */
		public final void play(Location location)
		{
			if (enable && sound != null && location != null)
				location.getWorld().playSound(location, sound, volume, pitch);
		}
	}

	/** Potion effect data node, used to store a potion effect object to the config file */
	public final static class PotionEffectSetting
	{
		public PotionEffectType type;
		public int duration;
		public int amplifier;
		public float chance;

		public PotionEffectSetting(PotionEffectType type, int duration, int amplifier)
		{
			this.type = type;
			this.duration = duration;
			this.amplifier = amplifier;
		}

		/** Creates a new potion effect object from the potion effect data */
		public final PotionEffect create()
		{
			if (type != null)
				return new PotionEffect(type, duration, amplifier - 1);
			return null;
		}
	}
}
