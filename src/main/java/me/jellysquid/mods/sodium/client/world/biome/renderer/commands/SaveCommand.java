package me.jellysquid.mods.sodium.client.world.biome.renderer.commands;

import me.jellysquid.mods.sodium.client.world.biome.renderer.Optimizer;
import me.jellysquid.mods.sodium.client.world.biome.renderer.command.Command;
import me.jellysquid.mods.sodium.client.world.biome.renderer.command.exception.CommandException;
import me.jellysquid.mods.sodium.client.world.biome.renderer.command.exception.CommandSyntaxException;
import me.jellysquid.mods.sodium.client.world.biome.renderer.utils.ChatUtils;

import java.nio.file.Path;

public class SaveCommand extends Command
{

	public SaveCommand()
	{
		super("save", "save a configuration", new String[]{"config name"});
	}

	@Override
	public void execute(String[] command) throws CommandException
	{
		if (command.length != 1)
			throw new CommandSyntaxException("argument number not matching");
		writeConfig(command[0]);

		ChatUtils.info("config saved");
	}

	private void writeConfig(String name)
	{
		Path configDir = Optimizer.CWHACK.getConfigDirectory();
		Path configFilePath = configDir.resolve(name + ".cw");
		Optimizer.CWHACK.getFeatures().saveAsFile(configFilePath.toString());
	}
}
