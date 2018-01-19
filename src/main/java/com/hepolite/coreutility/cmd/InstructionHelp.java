package com.hepolite.coreutility.cmd;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class InstructionHelp extends Instruction
{
	private final int INSTRUCTIONS_PER_PAGE = 15;
	private final CoreCommandHandler handler;
	private final String name;

	public InstructionHelp(CoreCommandHandler handler, String name)
	{
		super("Help", -1);
		this.handler = handler;
		this.name = name;
	}

	@Override
	protected final void addSyntax(List<String> syntaxes)
	{
		syntaxes.add("");
		syntaxes.add("<page>");
		syntaxes.add("<instruction>");
	}

	@Override
	protected final void addDescription(List<String> descriptions)
	{
		descriptions.add("Displays the generic help menu");
		descriptions.add("Dispays a specific page in the generic help menu");
		descriptions.add("Dispays information about the given instruction");
	}

	@Override
	protected final String getExplanation()
	{
		return "Provides information of the syntax of the instructions in the plugin, and what they do.";
	}

	@Override
	protected final boolean onInvoke(CommandSender sender, List<String> arguments)
	{
		if (arguments.size() == 0)
			displayInstructionInformation(sender, 0);
		else
		{
			String argument = compressList(arguments, " ");
			if (!displayInstructionHelp(sender, argument))
			{
				try
				{
					displayInstructionInformation(sender, Integer.parseInt(argument) - 1);
				}
				catch (NumberFormatException exception)
				{
					sender.sendMessage(ChatColor.RED + "Couldn't find instruction with name '" + argument + "'!");
				}
			}
		}
		return false;
	}

	/** Displays the generic help menu for the specified page */
	private final void displayInstructionInformation(CommandSender sender, int page)
	{
		// Grab all instructions
		List<Instruction> instructions = handler.getInstructions(sender);
		page = Math.max(0, page);

		sender.sendMessage(ChatColor.WHITE + "Displaying page " + (page + 1) + " of " + (instructions.size() / INSTRUCTIONS_PER_PAGE + 1) + " pages of instructions.");
		sender.sendMessage(ChatColor.WHITE + "Use /" + name + " help <page> or /" + name + " help <instruction> for more information");

		// Display the instructions that were found
		for (int i = INSTRUCTIONS_PER_PAGE * page; i < Math.min(INSTRUCTIONS_PER_PAGE * (page + 1), instructions.size()); i++)
			sender.sendMessage(ChatColor.WHITE + " - " + ChatColor.AQUA + instructions.get(i).getFullName());
	}

	/** Attempts to display information on the given instruction name; if that instruction doesn't exist, returns false */
	private final boolean displayInstructionHelp(CommandSender sender, String instructionName)
	{
		// Grab all instructions
		List<Instruction> instructions = handler.getInstructions(sender);
		for (Instruction instruction : instructions)
		{
			if (instruction.getFullName().equalsIgnoreCase(instructionName))
			{
				// Grab all the data for the instruction
				List<String> syntaxes = new ArrayList<String>();
				List<String> descriptions = new ArrayList<String>();
				instruction.addSyntax(syntaxes);
				instruction.addDescription(descriptions);

				// Show all the data to the user
				sender.sendMessage(ChatColor.WHITE + "" + ChatColor.BOLD + "Instruction " + instruction.getFullName() + ":");
				sender.sendMessage(ChatColor.WHITE + instruction.getExplanation());
				sender.sendMessage(ChatColor.WHITE + "" + ChatColor.BOLD + "Usage:");
				for (int i = 0; i < syntaxes.size(); i++)
				{
					sender.sendMessage(ChatColor.AQUA + "/" + name + " " + instruction.getFullName() + " " + syntaxes.get(i));
					sender.sendMessage("  " + ChatColor.WHITE + descriptions.get(i));
				}
				return true;
			}
		}
		return false;
	}
}
