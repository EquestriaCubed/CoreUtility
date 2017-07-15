package com.hepolite.coreutility.cmd;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.hepolite.coreutility.apis.attributes.Attribute;
import com.hepolite.coreutility.apis.attributes.AttributeAPI;

public class InstructionDebugAttribute extends Instruction
{
	public InstructionDebugAttribute()
	{
		super("Attribute", new int[] {
				1,
				2
		});
	}

	@Override
	protected void addSyntax(List<String> syntaxes)
	{
		syntaxes.add("<attribute>");
		syntaxes.add("<attribute> <basevalue>");
	}

	@Override
	protected void addDescription(List<String> descriptions)
	{
		descriptions.add("Displays the value of the attribute");
		descriptions.add("Displays the value of the attribute, with a given base value");
	}

	@Override
	protected String getExplanation()
	{
		return "Allows the caller to check what the value of various attributes of the caller is";
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

		String attributeName = arguments.get(0);
		Attribute attribute = AttributeAPI.get(player, attributeName);

		float actualBaseValue = attribute.getBaseValue();
		float baseValue = arguments.size() >= 2 ? Float.valueOf(arguments.get(1)) : actualBaseValue;
		attribute.setBaseValue(baseValue);

		player.sendMessage(String.format(ChatColor.AQUA + "Attribute %s has value %.5f",
				attributeName, attribute.getValue()));

		attribute.setBaseValue(actualBaseValue);
		return false;
	}
}
