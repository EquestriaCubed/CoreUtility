package com.hepolite.coreutility.cmd;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

import com.hepolite.coreutility.log.Log;

public abstract class Instruction
{
	private String name = "Unnamed_Instruction";
	private String permission = null;
	private int validArgumentNumbers[] = null;

	private Map<String, Instruction> subInstructions = new TreeMap<String, Instruction>();
	private Instruction parentInstruction = null;
	
	private boolean isPlayerOnly = false;

	/** Name is the name of the instruction, argumentCount can be -1 for any number of arguments, otherwise that many arguments are required for the instruction to be valid */
	protected Instruction(String name, int argumentCount)
	{
		this(name, null, new int[] { argumentCount });
	}

	/** Name is the name of the instruction, permission is the permission the user needs, argumentCount can be -1 for any number of arguments, otherwise that many arguments are required for the instruction to be valid */
	protected Instruction(String name, String permission, int argumentCount)
	{
		this(name, permission, new int[] { argumentCount });
	}

	/** Name is the name of the instruction, argumentCount can be -1 for any number of arguments, otherwise that many arguments are required for the instruction to be valid */
	protected Instruction(String name, int validArgumentNumbers[])
	{
		this(name, null, validArgumentNumbers);
	}

	/** Name is the name of the instruction, permission is the permission the user needs, argumentCount can be -1 for any number of arguments, otherwise that many arguments are required for the instruction to be valid */
	protected Instruction(String name, String permission, int validArgumentNumbers[])
	{
		this.name = name;
		this.permission = permission;
		this.validArgumentNumbers = validArgumentNumbers;
	}

	/** Returns the name of the effect */
	public final String getName()
	{
		return name;
	}

	/** Returns the permission needed for this instruction, or null if no permission is needed */
	public final String getPermission()
	{
		return permission;
	}

	/** Returns the full name of the instruction, rooted from the parents */
	public final String getFullName()
	{
		if (getParent() != null)
			return getParent().getFullName() + " " + getName();
		return getName();
	}

	/** Returns the parent instruction */
	public final Instruction getParent()
	{
		return parentInstruction;
	}

	/** Marks the instruction as a player-only instruction */
	protected final void setPlayerOnly()
	{
		isPlayerOnly = true;
	}

	// ///////////////////////////////////////////////////////////////////////////////////////
	// CORE FUNCTIONALITY // CORE FUNCTIONALITY // CORE FUNCTIONALITY // CORE FUNCTIONALITY //
	// ///////////////////////////////////////////////////////////////////////////////////////

	/** Registers a sub-instruction for the instruction. Example: /mmob instruction subinstruction arguments */
	protected final void registerSubInstruction(Instruction instruction)
	{
		if (subInstructions.get(instruction.getName()) != null)
			Log.warning("Attempted to register sub-instruction '" + instruction.getName() + "' to instruction '" + instruction + "'. Another sub-instruction with that name has already been registered and will be overwritten!");
		subInstructions.put(instruction.getName().toLowerCase(), instruction);

		// Update some parts of the instruction settings
		instruction.parentInstruction = this;
	}

	/** Returns a sub-instruction with the given name if it exists */
	public final Instruction getSubInstruction(String name)
	{
		return subInstructions.get(name.toLowerCase());
	}

	/** Adds all registered sub-instructions to the specified list */
	public final void getSubInstructions(List<Instruction> list, CommandSender sender)
	{
		for (Instruction instruction : subInstructions.values())
		{
			if (!instruction.hasPermission(sender))
				continue;
			list.add(instruction);
			instruction.getSubInstructions(list, sender);
		}
	}

	/** Basic call; will check for sub-instructions and make sure arguments match before doing anything. Returns true if the instruction failed at something */
	public final boolean onUse(CommandSender sender, List<String> arguments)
	{
		// Deny the usage if the user does not have permission
		if (!hasPermission(sender))
		{
			sender.sendMessage(ChatColor.RED + "You do not have permission to do this.");
			return true;
		}

		// Figure out if there is a valid sub-instruction; if there is, try to invoke it
		Instruction instruction = (arguments.size() >= 1 ? getSubInstruction(arguments.get(0)) : null);
		if (instruction != null)
		{
			arguments.remove(0);
			return instruction.onUse(sender, arguments);
		}
		// Otherwise run the instruction
		else
		{
			if (isPlayerOnly && !(sender instanceof Player))
			{
				sender.sendMessage(ChatColor.RED + "This can only be used by a player");
				return true;
			}

			boolean hasValidAmountOfArguments = false;
			for (int argumentCount : validArgumentNumbers)
			{
				if (arguments.size() == argumentCount || argumentCount == -1)
				{
					hasValidAmountOfArguments = true;
					if (!onInvoke(sender, arguments))
						return false;
					break;
				}
			}
			if (!hasValidAmountOfArguments)
			{
				String argumentsExpected = "";
				for (int i = 0; i < validArgumentNumbers.length; i++)
					argumentsExpected += (i == 0 ? "" : ", ") + (validArgumentNumbers[i] == -1 ? "any" : validArgumentNumbers[i]);
				sender.sendMessage(ChatColor.RED + "Invalid number of parameters. Expected '" + argumentsExpected + "' parameters, but received '" + arguments.size() + "'");
			}
		}
		return true;
	}

	/** Adds a syntax for the instruction; all combinations of arguments should have their own syntax added */
	protected abstract void addSyntax(List<String> syntaxes);

	/** Adds a description for the instruction; all combinations of arguments should have their own description added. There must be one description for each syntax */
	protected abstract void addDescription(List<String> descriptions);

	/** Returns a bit of text that explains what the instruction does */
	protected abstract String getExplanation();

	/** The most basic place to execute an instruction; arguments will only be those that are specified. Returns true if the instruction failed at something */
	protected abstract boolean onInvoke(CommandSender sender, List<String> arguments);

	// /////////////////////////////////////////////////////////////////////////////////////////
	// HELPER METHODS // HELPER METHODS // HELPER METHODS // HELPER METHODS // HELPER METHODS //
	// /////////////////////////////////////////////////////////////////////////////////////////

	/** Returns true if the given command user has permission to use this instruction */
	public final boolean hasPermission(CommandSender sender)
	{
		if (permission == null || !(sender instanceof Player))
			return true;

		Player player = (Player) sender;
		PermissionUser user = PermissionsEx.getUser(player);
		return user.has(permission, player.getWorld().getName());
	}

	/** Helper function to get a unique identifier for the command sender */
	protected final String getSenderIdentifier(CommandSender sender)
	{
		if (sender instanceof Player)
			return ((Player) sender).getUniqueId().toString();
		else if (sender instanceof BlockCommandSender)
			return ((BlockCommandSender) sender).getBlock().getLocation().toString();
		else
			return "CONSOLE";
	}

	/** Helper function to find the location of the command sender. Returns null if the sender is the console */
	protected final Location getSenderLocation(CommandSender sender)
	{
		if (sender instanceof Player)
			return ((Player) sender).getLocation();
		else if (sender instanceof BlockCommandSender)
			return ((BlockCommandSender) sender).getBlock().getLocation();
		else
			return null;
	}

	/** Compresses all the string in the list to one string, seperated by the given character */
	protected final String compressList(List<String> list, String seperation)
	{
		if (list.isEmpty())
			return null;

		String string = "";
		for (String entry : list)
			string += seperation + entry;
		return string.substring(seperation.length());
	}
}
