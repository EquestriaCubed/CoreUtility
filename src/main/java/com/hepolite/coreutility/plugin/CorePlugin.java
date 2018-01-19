package com.hepolite.coreutility.plugin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import com.hepolite.coreutility.cmd.CoreCommandHandler;
import com.hepolite.coreutility.event.CoreHandler;
import com.hepolite.coreutility.log.Log;

public abstract class CorePlugin extends JavaPlugin
{
	private Optional<CoreCommandHandler> commandHandler = Optional.empty();
	private Collection<CoreHandler> handlers = new ArrayList<CoreHandler>();

	@Override
	public final void onEnable()
	{
		Log.bind(this);
		onInitialize();
	}

	@Override
	public final void onDisable()
	{
		Log.bind(this);
		Log.info("Disabling " + getName() + "...");
		onDeinitialize();
		for (CoreHandler handler : handlers)
			handler.onDeinitialize();
		Log.info("Done disabling " + getName() + "!");
	}

	@Override
	public final boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		Log.bind(this);
		commandHandler.ifPresent((handler) -> handler.onCommand(sender, cmd, label, args));
		return true;
	}

	/** Performs one tick on the plugin */
	public final void tick(int tick)
	{
		Log.bind(this);
		onTick(tick);
		for (CoreHandler handler : handlers)
			handler.onTick(tick);
	}

	/** Restarts the plugin */
	public final void restart()
	{
		Log.bind(this);
		Log.info("Restarting " + getName() + "...");
		onRestart();
		for (CoreHandler handler : handlers)
			handler.onRestart();
		Log.info("Done restarting " + getName() + "!");
	}

	// //////////////////////////////////////////////////////////////////////

	/** Invoked on plugin initialization */
	protected abstract void onInitialize();

	/** Invoked on plugin deinitialization */
	protected abstract void onDeinitialize();

	/** Invoked on plugin restart */
	protected abstract void onRestart();

	/** Invoked every tick while the plugin is active */
	protected abstract void onTick(int tick);

	// //////////////////////////////////////////////////////////////////////

	/** Assigns the command handler for this plugin */
	protected final CoreCommandHandler setCommandHandler(final CoreCommandHandler commandHandler)
	{
		if (commandHandler == null)
			throw new IllegalArgumentException("CommandHandler cannot be null");
		this.commandHandler = Optional.of(commandHandler);
		return commandHandler;
	}

	/** Adds the handler to this plugin */
	public final <T extends CoreHandler> T addHandler(final T handler)
	{
		if (handler == null)
			throw new IllegalArgumentException("Handler cannot be null");
		if (handlers.contains(handler))
			throw new IllegalStateException("Cannot add the same handler twice");
		getServer().getPluginManager().registerEvents(handler, this);
		handlers.add(handler);
		return handler;
	}
}
