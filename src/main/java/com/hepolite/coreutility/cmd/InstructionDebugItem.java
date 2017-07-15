package com.hepolite.coreutility.cmd;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.hepolite.coreutility.plugin.CorePlugin;
import com.hepolite.coreutility.settings.Settings;
import com.hepolite.coreutility.settings.SettingsSimple;

public class InstructionDebugItem extends Instruction
{
	private final CorePlugin plugin;

	public InstructionDebugItem(CorePlugin plugin)
	{
		super("Item", new int[] {
				0,
				1
		});
		this.plugin = plugin;

		setPlayerOnly();
	}

	@Override
	protected void addSyntax(List<String> syntaxes)
	{
		syntaxes.add("");
		syntaxes.add("<anything>");
	}

	@Override
	protected void addDescription(List<String> descriptions)
	{
		descriptions.add("Writes the item held to the debug config");
		descriptions.add("Loads the item stored in the debug config");
	}

	@Override
	protected String getExplanation()
	{
		return "Allows the user to view the data stored in the held item, or overwrite the held item with what is stored in the debug config";
	}

	@Override
	protected boolean onInvoke(CommandSender sender, List<String> arguments)
	{
		Player player = (Player) sender;
		Settings settings = new SettingsSimple(plugin, "Utility", "debug");

		boolean writeToConfig = arguments.size() == 0;
		if (writeToConfig)
		{
			player.sendMessage(ChatColor.RED + "Stored the held item in the debug config file");
			settings.set("item", player.getInventory().getItemInMainHand());
			settings.save();
		}
		else
		{
			player.sendMessage(ChatColor.RED + "Loaded the stored item from the debug config file");
			player.getInventory().setItemInMainHand(settings.getItem("item"));
		}
		return false;
	}

}
