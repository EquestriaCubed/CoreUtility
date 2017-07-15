package com.hepolite.coreutility.settings;

import java.io.File;

import com.hepolite.coreutility.plugin.CorePlugin;

/** The simple settings are used for very basic configuration files; basically anything that stores and loads trivial data can rely on this class */
public class SettingsSimple extends Settings
{
	public SettingsSimple(CorePlugin plugin, String path, String name)
	{
		super(plugin, path, name);
	}

	public SettingsSimple(CorePlugin plugin, String name)
	{
		super(plugin, name);
	}

	public SettingsSimple(CorePlugin plugin, String path, File file)
	{
		super(plugin, path, file.getName().split("\\.")[0]);
	}

	public SettingsSimple(CorePlugin plugin, File file)
	{
		super(plugin, file.getName().split("\\.")[0]);
	}

	@Override
	protected void addDefaultValues(){}
	@Override
	protected void onSaveConfigFile(){}
	@Override
	protected void onLoadConfigFile(){}
}