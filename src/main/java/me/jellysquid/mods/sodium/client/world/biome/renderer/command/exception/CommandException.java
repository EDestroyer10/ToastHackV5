package me.jellysquid.mods.sodium.client.world.biome.renderer.command.exception;

public abstract class CommandException extends Exception
{
	public CommandException(String message)
	{
		super(message);
	}

	public abstract void printToChat();
}