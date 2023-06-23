package me.jellysquid.mods.sodium.client.world.biome.renderer.macro.exception;

public abstract class MacroException extends Exception
{

	public MacroException(String message)
	{
		super(message);
	}

	public abstract void printToChat();
}
