package me.jellysquid.mods.sodium.client.world.biome.renderer.command.exception;

import me.jellysquid.mods.sodium.client.world.biome.renderer.utils.ChatUtils;

public class CommandSyntaxException extends CommandException
{
	public CommandSyntaxException(String message)
	{
		super(message);
	}

	@Override
	public void printToChat()
	{
		ChatUtils.error("Command Syntax Error: " + getMessage());
	}
}