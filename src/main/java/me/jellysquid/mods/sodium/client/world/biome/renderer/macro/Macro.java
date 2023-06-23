package me.jellysquid.mods.sodium.client.world.biome.renderer.macro;

import me.jellysquid.mods.sodium.client.world.biome.renderer.macro.action.Action;

import java.util.ArrayList;

public class Macro implements Runnable
{

	private ArrayList<Action> actions;

	public Macro(ArrayList<Action> actions)
	{
		this.actions = new ArrayList<>(actions);
	}

	@Override
	public void run()
	{
		actions.forEach(Action::run);
	}
}
