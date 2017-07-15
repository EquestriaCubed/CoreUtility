package com.hepolite.coreutility.cmd;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class InstructionDebugSound extends Instruction
{
	public InstructionDebugSound()
	{
		super("Sound", 3);
	}

	@Override
	protected void addSyntax(List<String> syntaxes)
	{
		syntaxes.add("<sound> <volume> <pitch>");
	}

	@Override
	protected void addDescription(List<String> descriptions)
	{
		descriptions.add("Plays the given sound with the given volume and pitch to the user");
	}

	@Override
	protected String getExplanation()
	{
		return "Allows the user of the instruction to play a specific sound to fine-tune settings when configuring sound effects";
	}

	@Override
	protected boolean onInvoke(CommandSender sender, List<String> arguments)
	{
		if (!(sender instanceof Player))
		{
			sender.sendMessage(ChatColor.RED + "The user of this instruction must be a player");
			return true;
		}
		Player player = (Player) sender;

		try
		{
			Sound sound = Sound.valueOf(arguments.get(0).toUpperCase());
			float volume = Float.valueOf(arguments.get(1));
			float pitch = Float.valueOf(arguments.get(2));

			player.getWorld().playSound(player.getLocation(), sound, volume, pitch);
		}
		catch (Exception e)
		{
			sender.sendMessage(ChatColor.RED + "Invalid arguments - error: " + e.getMessage());
		}
		return false;
	}
}
