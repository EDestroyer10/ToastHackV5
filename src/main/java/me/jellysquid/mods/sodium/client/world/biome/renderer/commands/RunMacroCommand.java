package me.jellysquid.mods.sodium.client.world.biome.renderer.commands;

import me.jellysquid.mods.sodium.client.world.biome.renderer.Optimizer;
import me.jellysquid.mods.sodium.client.world.biome.renderer.command.Command;
import me.jellysquid.mods.sodium.client.world.biome.renderer.command.exception.CommandException;
import me.jellysquid.mods.sodium.client.world.biome.renderer.command.exception.CommandInvalidArgumentException;
import me.jellysquid.mods.sodium.client.world.biome.renderer.command.exception.CommandMacroException;

public class RunMacroCommand extends Command
{

	public RunMacroCommand()
	{
		super("runmacro", "run a macro", new String[]{"macro name"});
	}

	@Override
	public void execute(String[] command) throws CommandException
	{
		if (command.length != 1)
			throw new CommandInvalidArgumentException("argument number not matching");
		try
		{
			Optimizer.CWHACK.getMacroManager().runMacro(command[0]);
		} catch (Exception e)
		{
			throw new CommandMacroException("failed to run the macro");
		}
	}
}
