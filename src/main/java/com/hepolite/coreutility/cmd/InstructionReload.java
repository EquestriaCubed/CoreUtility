package com.hepolite.coreutility.cmd;

import java.util.List;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.command.CommandSender;

import com.hepolite.coreutility.plugin.CorePlugin;

public class InstructionReload extends Instruction
{
	private final CorePlugin plugin;

	public InstructionReload(CorePlugin plugin, String permission)
	{
		super("Reload", permission, 0);
		this.plugin = plugin;
	}

	@Override
	protected void addSyntax(List<String> syntaxes)
	{
		syntaxes.add("");
	}

	@Override
	protected void addDescription(List<String> descriptions)
	{
		descriptions.add("Reloads the plugin");
	}

	@Override
	protected String getExplanation()
	{
		return "The plugin will reload all configuration files associated with it, from disc";
	}

	@Override
	protected boolean onInvoke(CommandSender sender, List<String> arguments)
	{
		sender.sendMessage(ChatColor.RED + "Restarting plugin " + plugin.getName() + "...");
		plugin.restart();
		sender.sendMessage(ChatColor.RED + "Done restarting plugin " + plugin.getName() + "!");
		return false;
	}
}
