package me.jellysquid.mods.sodium.client.world.biome.renderer.macro.action;

import me.jellysquid.mods.sodium.client.world.biome.renderer.macro.exception.MacroException;

public abstract class Action
{
	public abstract void init(String[] args) throws MacroException;

	public abstract void run();
}
