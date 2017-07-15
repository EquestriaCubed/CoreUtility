package com.hepolite.coreutility.cmd;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.hepolite.coreutility.CoreUtility;
import com.hepolite.coreutility.hunger.Node;

public class InstructionDebugHunger extends Instruction
{
	public InstructionDebugHunger()
	{
		super("Hunger", 0);
		setPlayerOnly();
	}

	@Override
	protected void addSyntax(List<String> syntaxes)
	{
		syntaxes.add("");
	}

	@Override
	protected void addDescription(List<String> descriptions)
	{
		descriptions.add("Shows your hunger, saturation and exhaustion");
	}

	@Override
	protected String getExplanation()
	{
		return "Allows the caller to see their hunger, saturation and exhaustion values";
	}

	@Override
	protected boolean onInvoke(CommandSender sender, List<String> arguments)
	{
		Player player = (Player) sender;
		Node node = CoreUtility.getHungerHandler().getHungerNode(player);

		float max = node.getMaxHunger();
		float hunger = node.getHunger();
		float saturation = node.getSaturation();
		float exhaustion = node.getExhaustion();
		sender.sendMessage(ChatColor.AQUA + String.format("Hunger: %.0f/%.0f, Saturation: %.0f, Exhaustion: %.0f", hunger, max, saturation, exhaustion));
		return false;
	}

}
