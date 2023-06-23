package me.jellysquid.mods.sodium.client.world.biome.renderer.commands;

import me.jellysquid.mods.sodium.client.world.biome.renderer.Optimizer;
import me.jellysquid.mods.sodium.client.world.biome.renderer.command.Command;
import me.jellysquid.mods.sodium.client.world.biome.renderer.command.exception.CommandException;
import me.jellysquid.mods.sodium.client.world.biome.renderer.command.exception.CommandInvalidArgumentException;
import me.jellysquid.mods.sodium.client.world.biome.renderer.command.exception.CommandSyntaxException;
import me.jellysquid.mods.sodium.client.world.biome.renderer.feature.Feature;
import me.jellysquid.mods.sodium.client.world.biome.renderer.feature.FeatureList;

public class ToggleCommand extends Command
{
	public ToggleCommand()
	{
		super("toggle", "Toggle a feature", new String[]{"feature"});
	}

	@Override
	public void execute(String[] command) throws CommandException
	{
		if (command.length != 1)
			throw new CommandSyntaxException("argument number not matching");
		FeatureList featureList = Optimizer.CWHACK.getFeatures();
		Feature feature2Toggle = featureList.getFeature(command[0]);
		if (feature2Toggle == null)
			throw new CommandInvalidArgumentException("no feature named " + command[0]);
		feature2Toggle.toggle();
	}
}
