package me.jellysquid.mods.sodium.client.world.biome.renderer.commands;

import me.jellysquid.mods.sodium.client.world.biome.renderer.Optimizer;
import me.jellysquid.mods.sodium.client.world.biome.renderer.command.Command;
import me.jellysquid.mods.sodium.client.world.biome.renderer.command.exception.CommandException;
import me.jellysquid.mods.sodium.client.world.biome.renderer.command.exception.CommandSyntaxException;

public class PanicCommand extends Command
{
	public PanicCommand()
	{
		super("panic", "Turn off all features and unbind all keybinds", new String[]{});
	}

	@Override
	public void execute(String[] command) throws CommandException
	{
		if (command.length != 0)
			throw new CommandSyntaxException("argument number not matching");
		Optimizer.CWHACK.getKeybindManager().removeAll();
		Optimizer.CWHACK.getFeatures().turnOffAll();
	}
}
