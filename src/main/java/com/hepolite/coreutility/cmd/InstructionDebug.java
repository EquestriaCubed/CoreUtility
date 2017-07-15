package com.hepolite.coreutility.cmd;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.hepolite.coreutility.plugin.CorePlugin;

public class InstructionDebug extends Instruction
{
	public InstructionDebug(CorePlugin plugin)
	{
		super("Debug", "coreutility.cu", -1);
		registerSubInstruction(new InstructionDebugAttribute());
		registerSubInstruction(new InstructionDebugHunger());
		registerSubInstruction(new InstructionDebugItem(plugin));
		registerSubInstruction(new InstructionDebugSound());
	}

	@Override
	protected void addSyntax(List<String> syntaxes)
	{}

	@Override
	protected void addDescription(List<String> descriptions)
	{}

	@Override
	protected String getExplanation()
	{
		return "This is the base for all debug instructions and will do nothing if invoked.";
	}

	@Override
	protected boolean onInvoke(CommandSender sender, List<String> arguments)
	{
		sender.sendMessage(ChatColor.RED + "Invoked the 'Debug' instruction. Did you mistype anything?");
		return false;
	}
}
