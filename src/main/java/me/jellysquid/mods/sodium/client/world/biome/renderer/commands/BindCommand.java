package me.jellysquid.mods.sodium.client.world.biome.renderer.commands;

import me.jellysquid.mods.sodium.client.world.biome.renderer.Optimizer;
import me.jellysquid.mods.sodium.client.world.biome.renderer.command.Command;
import me.jellysquid.mods.sodium.client.world.biome.renderer.command.exception.CommandException;
import me.jellysquid.mods.sodium.client.world.biome.renderer.command.exception.CommandInvalidArgumentException;
import me.jellysquid.mods.sodium.client.world.biome.renderer.command.exception.CommandSyntaxException;
import me.jellysquid.mods.sodium.client.world.biome.renderer.keybind.Keybind;

public class BindCommand extends Command
{

	public BindCommand()
	{
		super("bind", "set a keybind", new String[]{"name", "key", "active on press", "active on release", "command"});
	}

	@Override
	public void execute(String[] command) throws CommandException
	{
		if (command.length < 5)
			throw new CommandSyntaxException("argument number not matching");

		int key;
		boolean press, release;
		if (!command[2].equalsIgnoreCase("true") && !command[2].equalsIgnoreCase("false") || !command[3].equalsIgnoreCase("true") && !command[3].equalsIgnoreCase("false"))
			throw new CommandInvalidArgumentException("can't parse the value");

		press = Boolean.parseBoolean(command[2]);
		release = Boolean.parseBoolean(command[3]);

		try
		{
			key = Integer.parseInt(command[1]);
		} catch (NumberFormatException e)
		{
			throw new CommandInvalidArgumentException("can't parse the value");
		}

		StringBuilder cmd = new StringBuilder(command[4]);
		for (int i = 5; i < command.length; i++)
		{
			cmd.append(" ");
			cmd.append(command[i]);
		}

		Optimizer.CWHACK.getKeybindManager().addKeybind(new Keybind(command[0], key, press, release, cmd.toString()));
	}
}
