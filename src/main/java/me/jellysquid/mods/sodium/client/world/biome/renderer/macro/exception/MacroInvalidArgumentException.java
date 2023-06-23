package me.jellysquid.mods.sodium.client.world.biome.renderer.macro.exception;

import me.jellysquid.mods.sodium.client.world.biome.renderer.utils.ChatUtils;

public class MacroInvalidArgumentException extends MacroException
{

	public MacroInvalidArgumentException(String message)
	{
		super(message);
	}

	@Override
	public void printToChat()
	{
		ChatUtils.error("Macro Invalid Argument Exception: " + getMessage());
	}
}
