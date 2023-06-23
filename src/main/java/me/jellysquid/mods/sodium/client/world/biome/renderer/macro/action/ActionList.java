package me.jellysquid.mods.sodium.client.world.biome.renderer.macro.action;

import me.jellysquid.mods.sodium.client.world.biome.renderer.macro.actions.ClickAction;
import me.jellysquid.mods.sodium.client.world.biome.renderer.macro.actions.JumpAction;
import me.jellysquid.mods.sodium.client.world.biome.renderer.macro.actions.PressKeyAction;
import me.jellysquid.mods.sodium.client.world.biome.renderer.macro.actions.SleepAction;

import java.util.HashMap;

public class ActionList
{

	private final HashMap<String, Class<? extends Action>> actions = new HashMap<>();

	public ActionList()
	{
		actions.put("click", ClickAction.class);
		actions.put("jump", JumpAction.class);
		actions.put("press", PressKeyAction.class);
		actions.put("sleep", SleepAction.class);
	}

	public Class<? extends Action> getAction(String name)
	{
		return actions.get(name);
	}
}
