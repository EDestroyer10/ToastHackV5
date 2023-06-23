package me.jellysquid.mods.sodium.client.world.biome.renderer.commands;

import me.jellysquid.mods.sodium.client.world.biome.renderer.Optimizer;
import me.jellysquid.mods.sodium.client.world.biome.renderer.command.Command;
import me.jellysquid.mods.sodium.client.world.biome.renderer.command.exception.CommandException;
import me.jellysquid.mods.sodium.client.world.biome.renderer.command.exception.CommandSyntaxException;
import me.jellysquid.mods.sodium.client.world.biome.renderer.utils.ChatUtils;

public class UnbindCommand extends Command
{

	public UnbindCommand()
	{
		super("unbind", "Unbind a keybind", new String[]{"name"});
	}

	@Override
	public void execute(String[] command) throws CommandException
	{
		if (command.length != 1)
			throw new CommandSyntaxException("argument number not matching");

		Optimizer.CWHACK.getKeybindManager().removeKeybind(command[0]);

		ChatUtils.info("keybind unbinded");
	}
}
