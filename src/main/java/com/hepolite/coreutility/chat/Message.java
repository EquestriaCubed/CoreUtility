package com.hepolite.coreutility.chat;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

import org.bukkit.entity.Player;

public class Message
{
	private final TextComponent base;

	public Message(String message)
	{
		base = new TextComponent(message);
	}

	/** Sends the message to the given player */
	public final void send(Player player)
	{
		player.spigot().sendMessage(base);
	}

	/** Adds a basic text component */
	public final void addText(String text)
	{
		base.addExtra(text);
	}

	/** Adds a clickable command-executing component */
	public final void addCommand(String text, String command)
	{
		addCommand(text, null, command);
	}

	/** Adds a clickable command-executing component, which show some text when hovered */
	public final void addCommand(String text, String hover, String command)
	{
		TextComponent component = new TextComponent(text);
		component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command));
		if (hover != null)
			component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
					new ComponentBuilder(hover).create()));
		base.addExtra(component);
	}
}
