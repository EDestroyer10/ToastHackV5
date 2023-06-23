package me.jellysquid.mods.sodium.client.world.biome.renderer.command.exception;

import me.jellysquid.mods.sodium.client.world.biome.renderer.utils.ChatUtils;

public class CommandMacroException extends CommandException
{

	public CommandMacroException(String message)
	{
		super(message);
	}

	@Override
	public void printToChat()
	{
		ChatUtils.error("Command Macro Error: " + getMessage());
	}
}