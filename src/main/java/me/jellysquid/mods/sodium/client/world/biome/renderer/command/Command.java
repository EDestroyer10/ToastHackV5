package me.jellysquid.mods.sodium.client.world.biome.renderer.command;

import me.jellysquid.mods.sodium.client.world.biome.renderer.command.exception.CommandException;

public abstract class Command
{
	private final String name;
	private final String description;
	private final String[] syntax;

	public Command(String name, String description, String[] syntax)
	{
		this.name = name;
		this.description = description;
		this.syntax = syntax;
	}

	public String getName()
	{
		return name;
	}

	public String getDescription()
	{
		return description;
	}

	public String[] getSyntax()
	{
		return syntax;
	}

	public abstract void execute(String[] command) throws CommandException;
}