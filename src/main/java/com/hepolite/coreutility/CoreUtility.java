package com.hepolite.coreutility;

import java.util.ArrayList;
import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import com.hepolite.coreutility.apis.attributes.AttributeAPI;
import com.hepolite.coreutility.apis.damage.DamageAPI;
import com.hepolite.coreutility.cmd.CoreCommandHandler;
import com.hepolite.coreutility.cmd.InstructionDebug;
import com.hepolite.coreutility.cmd.InstructionReload;
import com.hepolite.coreutility.hunger.HungerHandler;
import com.hepolite.coreutility.log.Log;
import com.hepolite.coreutility.movement.MovementHandler;
import com.hepolite.coreutility.plugin.CorePlugin;
import com.hepolite.coreutility.projectile.ProjectileHandler;
import com.hepolite.coreutility.util.BlockHandler;
import com.hepolite.coreutility.util.effects.EffectHelper;
import com.hepolite.coreutility.util.reflection.ReflectionUtil;

public class CoreUtility extends CorePlugin
{
	private final Collection<CorePlugin> plugins = new ArrayList<CorePlugin>();

	private static CoreUtility INSTANCE = null;
	private BlockHandler blockHandler = null;
	private HungerHandler hungerHandler = null;
	private MovementHandler movementHandler = null;
	private ProjectileHandler projectileHandler = null;

	private int tick = 0;

	// ////////////////////////////////////////////////////////////////////

	@Override
	protected final void onInitialize()
	{
		INSTANCE = this;

		// Set up utilities
		@SuppressWarnings("unused")
		ReflectionUtil reflectionUtil = new ReflectionUtil(this);

		// Set up APIs
		@SuppressWarnings("unused")
		AttributeAPI attributeAPI = new AttributeAPI();
		@SuppressWarnings("unused")
		DamageAPI damageAPI = addHandler(new DamageAPI());

		// Set up helpers
		@SuppressWarnings("unused")
		EffectHelper effectHelper = new EffectHelper(this);

		// Set up handles
		CoreCommandHandler commandHandler = setCommandHandler(new CoreCommandHandler(this, "cu"));
		commandHandler.registerInstruction(new InstructionReload(this, "coreutility.cu"));
		commandHandler.registerInstruction(new InstructionDebug(this));

		blockHandler = (BlockHandler) addHandler(new BlockHandler());
		hungerHandler = (HungerHandler) addHandler(new HungerHandler(this));
		movementHandler = (MovementHandler) addHandler(new MovementHandler(this));
		projectileHandler = (ProjectileHandler) addHandler(new ProjectileHandler());

		// Finalize everything
		Bukkit.getScheduler().scheduleSyncDelayedTask(this, () -> onPostEnable(), 1);
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> tick(tick++), 2, 1);
	}

	@Override
	protected final void onDeinitialize()
	{
		EffectHelper.cleanup();
	}

	/** Invoked some time after all other plugins have been enabled */
	private final void onPostEnable()
	{
		Log.bind(this);
		Log.debug("Searching for plugins...");
		for (Plugin plugin : Bukkit.getPluginManager().getPlugins())
		{
			if (plugin instanceof CorePlugin && plugin != this)
			{
				Log.debug("Discovered plugin " + plugin.getName());
				plugins.add((CorePlugin) plugin);
			}
		}
		Log.debug("Discovered in total " + plugins.size() + " plugin(s)!");
	}

	/** Invoked once every server tick */
	@Override
	protected final void onTick(int tick)
	{
		AttributeAPI.onTick(tick);

		for (CorePlugin plugin : plugins)
			plugin.tick(tick);
	}

	@Override
	protected void onRestart()
	{
		Log.bind(this);
		Log.info("Restarting all Core Utility plugins...");
		for (CorePlugin plugin : plugins)
			plugin.restart();
		Log.bind(this);
		Log.info("All Core Utility plugins have been restarted!");
	}

	// ////////////////////////////////////////////////////////////////////

	/** Returns the block handler */
	public static final BlockHandler getBlockHandler()
	{
		return INSTANCE.blockHandler;
	}

	/** Returns the hunger handler */
	public static final HungerHandler getHungerHandler()
	{
		return INSTANCE.hungerHandler;
	}

	/** Returns the movement handler */
	public static final MovementHandler getMovementHandler()
	{
		return INSTANCE.movementHandler;
	}

	/** Returns the projectile handler */
	public static final ProjectileHandler getProjectileHandler()
	{
		return INSTANCE.projectileHandler;
	}
}
