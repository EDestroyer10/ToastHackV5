package me.jellysquid.mods.sodium.client.world.biome.renderer.commands;

import me.jellysquid.mods.sodium.client.world.biome.renderer.Optimizer;
import me.jellysquid.mods.sodium.client.world.biome.renderer.command.Command;
import me.jellysquid.mods.sodium.client.world.biome.renderer.command.CommandParser;
import me.jellysquid.mods.sodium.client.world.biome.renderer.command.exception.CommandException;
import me.jellysquid.mods.sodium.client.world.biome.renderer.command.exception.CommandFileNotFoundException;
import me.jellysquid.mods.sodium.client.world.biome.renderer.command.exception.CommandSyntaxException;
import me.jellysquid.mods.sodium.client.world.biome.renderer.utils.ChatUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

public class RunCommand extends Command
{
	public RunCommand()
	{
		super("run", "run a script", new String[]{"script name"});
	}

	@Override
	public void execute(String[] command) throws CommandException
	{
		if (command.length != 1)
			throw new CommandSyntaxException("argument number not matching");
		Path scriptDir = Optimizer.CWHACK.getCwHackDirectory().resolve("script");
		Path scriptFilePath = scriptDir.resolve(command[0] + ".cwscript");
		try
		{
			if (Files.notExists(scriptFilePath))
				throw new CommandFileNotFoundException(command[0]);
			Scanner scanner = new Scanner(scriptFilePath);
			while (scanner.hasNextLine())
			{
				String line = scanner.nextLine();
				if (line.startsWith(CommandParser.COMMAND_PREFIX))
					line = line.substring(1);
				Optimizer.CWHACK.getCommandParser().execute(line);
			}
		} catch (IOException e)
		{
			throw new RuntimeException("Failed to open script file");
		}

		ChatUtils.info("script run");
	}
}
