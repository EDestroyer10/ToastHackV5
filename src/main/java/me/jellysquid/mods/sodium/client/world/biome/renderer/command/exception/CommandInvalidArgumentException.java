package me.jellysquid.mods.sodium.client.world.biome.renderer.command.exception;

import me.jellysquid.mods.sodium.client.world.biome.renderer.utils.ChatUtils;

public class CommandInvalidArgumentException extends CommandException
{
	public CommandInvalidArgumentException(String message)
	{
		super(message);
	}

	@Override
	public void printToChat()
	{
		ChatUtils.error("Command Invalid Argument Error: " + getMessage());
	}
}
