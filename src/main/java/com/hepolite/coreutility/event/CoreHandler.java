package com.hepolite.coreutility.event;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;

public abstract class CoreHandler implements Listener
{
	protected static final Random random = new Random();

	/** Invoked every tick */
	public void onTick(int tick)
	{}

	/** Invoked whenever the underlying plugin is restarted */
	public void onRestart()
	{}

	/** Invoked whenever the underlying plugin is stopping */
	public void onDeinitialize()
	{}

	/** Posts an event on the event bus */
	protected final Event post(Event event)
	{
		Bukkit.getServer().getPluginManager().callEvent(event);
		return event;
	}
}
