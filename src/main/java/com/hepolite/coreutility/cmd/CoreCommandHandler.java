package com.hepolite.coreutility.cmd;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.hepolite.coreutility.log.Log;
import com.hepolite.coreutility.plugin.CorePlugin;

public class CoreCommandHandler implements CommandExecutor
{
	private Map<String, Instruction> instructions = new TreeMap<String, Instruction>();

	public CoreCommandHandler(CorePlugin plugin, String name)
	{
		plugin.getCommand(name).setExecutor(this);
		registerInstruction(new InstructionHelp(this, name));
	}

	/** Registers a new instruction for use */
	public final void registerInstruction(final Instruction instruction)
	{
		if (instructions.get(instruction.getName()) != null)
			Log.warning("Attempted to register instruction '" + instruction.getName() + "'. Another instruction with that name has already been registered and will be overwritten!");
		instructions.put(instruction.getName().toLowerCase(), instruction);
	}

	/** Returns the instruction with the given name */
	private final Instruction getInstruction(final String name)
	{
		return instructions.get(name.toLowerCase());
	}

	/** Returns a list of all registered instructions that the given sender can use */
	public final List<Instruction> getInstructions(CommandSender sender)
	{
		List<Instruction> list = new ArrayList<Instruction>();
		for (Instruction instruction : instructions.values())
		{
			if (!instruction.hasPermission(sender))
				continue;
			list.add(instruction);
			instruction.getSubInstructions(list, sender);
		}
		return list;
	}

	/** Called whenever a command is performed */
	@Override
	public final boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args)
	{
		if (args.length == 0)
		{
			sender.sendMessage(cmd.getDescription());
			return true;
		}

		// Run the instruction, if it was a valid one
		Instruction instruction = getInstruction(args[0]);
		if (instruction == null)
			sender.sendMessage(ChatColor.RED + "There are no instructions with the name '" + args[0] + "' registered");
		else
		{
			List<String> arguments = new ArrayList<String>(args.length);
			for (String argument : args)
				arguments.add(argument);
			arguments.remove(0);
			instruction.onUse(sender, arguments);
		}
		return true;
	}
}
