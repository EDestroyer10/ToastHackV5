package me.jellysquid.mods.sodium.client.world.biome.renderer.macro.exception;

import me.jellysquid.mods.sodium.client.world.biome.renderer.utils.ChatUtils;

public class MacroSyntaxException extends MacroException
{

	public MacroSyntaxException(String message)
	{
		super(message);
	}

	@Override
	public void printToChat()
	{
		ChatUtils.error("Macro Syntax Error: " + getMessage());
	}
}
