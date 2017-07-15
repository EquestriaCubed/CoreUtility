package com.hepolite.coreutility.log;

import java.util.logging.Logger;

import org.bukkit.plugin.Plugin;

public class Log
{
	private static Logger logger;
	
	/** Binds the logger to the specified plugin */
	public final static void bind(Plugin plugin)
	{
		logger = (plugin == null ? null : plugin.getLogger());
	}
	
	/** Logs the specified message as debug, if debugging is enabled for the currently bound plugin */
	public final static void debug(String message)
	{
		// TODO: Finish this method so that debugging can be disabled
		if (logger != null)
			logger.info(message);
	}
	
	/** Logs the specified message as information */
	public final static void info(String message)
	{
		if (logger != null)
			logger.info(message);
	}
	
	/** Logs the specified message as a warning */
	public final static void warning(String message)
	{
		if (logger != null)
			logger.warning(message);
	}
	
	/** Logs the specified message as an error */
	public final static void severe(String message)
	{
		if (logger != null)
			logger.severe(message);
	}
}
