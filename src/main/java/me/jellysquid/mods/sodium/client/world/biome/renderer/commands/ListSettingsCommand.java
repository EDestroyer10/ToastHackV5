package me.jellysquid.mods.sodium.client.world.biome.renderer.commands;

import me.jellysquid.mods.sodium.client.world.biome.renderer.Optimizer;
import me.jellysquid.mods.sodium.client.world.biome.renderer.command.Command;
import me.jellysquid.mods.sodium.client.world.biome.renderer.command.exception.CommandException;
import me.jellysquid.mods.sodium.client.world.biome.renderer.command.exception.CommandInvalidArgumentException;
import me.jellysquid.mods.sodium.client.world.biome.renderer.command.exception.CommandSyntaxException;
import me.jellysquid.mods.sodium.client.world.biome.renderer.feature.Feature;
import me.jellysquid.mods.sodium.client.world.biome.renderer.setting.Setting;
import me.jellysquid.mods.sodium.client.world.biome.renderer.utils.ChatUtils;

public class ListSettingsCommand extends Command
{

	public ListSettingsCommand()
	{
		super("ListSettings", "List all the settings of a feature", new String[]{"feature name"});
	}

	@Override
	public void execute(String[] command) throws CommandException
	{
		if (command.length != 1)
			throw new CommandSyntaxException("argument number not matching");
		Feature feature = Optimizer.CWHACK.getFeatures().getFeature(command[0]);
		if (feature == null)
			throw new CommandInvalidArgumentException("no feature named " + command[0]);
		for (Setting setting : feature.getSettings())
		{
			ChatUtils.info(setting.getName() + ": " + setting.storeAsString());
		}
	}
}
