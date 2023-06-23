package me.jellysquid.mods.sodium.client.world.biome.renderer.commands;

import me.jellysquid.mods.sodium.client.world.biome.renderer.Optimizer;
import me.jellysquid.mods.sodium.client.world.biome.renderer.command.Command;
import me.jellysquid.mods.sodium.client.world.biome.renderer.command.exception.CommandBadConfigException;
import me.jellysquid.mods.sodium.client.world.biome.renderer.command.exception.CommandException;
import me.jellysquid.mods.sodium.client.world.biome.renderer.command.exception.CommandSyntaxException;
import me.jellysquid.mods.sodium.client.world.biome.renderer.utils.ChatUtils;

import java.nio.file.Path;

public class LoadCommand extends Command
{

	public LoadCommand()
	{
		super("load", "load a configuration", new String[]{"config name"});
	}

	@Override
	public void execute(String[] command) throws CommandException
	{
		if (command.length != 1)
			throw new CommandSyntaxException("argument number not matching");
		Path configDir = Optimizer.CWHACK.getCwHackDirectory().resolve("config");
		Path configFilePath = configDir.resolve(command[0] + ".cw");

		try
		{
			Optimizer.CWHACK.getFeatures().loadFromFile(configFilePath.toString());
		}
		catch (Exception e)
		{
			throw new CommandBadConfigException(command[0]);
		}

		ChatUtils.info("config loaded");
	}
}
