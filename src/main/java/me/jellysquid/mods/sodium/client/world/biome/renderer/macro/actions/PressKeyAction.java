package me.jellysquid.mods.sodium.client.world.biome.renderer.macro.actions;

import me.jellysquid.mods.sodium.client.world.biome.renderer.Optimizer;
import me.jellysquid.mods.sodium.client.world.biome.renderer.macro.exception.MacroInvalidArgumentException;
import me.jellysquid.mods.sodium.client.world.biome.renderer.mixinterface.IKeyboard;
import org.lwjgl.glfw.GLFW;
import me.jellysquid.mods.sodium.client.world.biome.renderer.macro.action.Action;
import me.jellysquid.mods.sodium.client.world.biome.renderer.macro.exception.MacroException;
import me.jellysquid.mods.sodium.client.world.biome.renderer.macro.exception.MacroSyntaxException;

public class PressKeyAction extends Action
{

	private int key;

	@Override
	public void init(String[] args) throws MacroException
	{
		if (args.length != 1)
			throw new MacroSyntaxException("argument number not matching");
		try
		{
			key = Integer.parseInt(args[0]);
		} catch (Exception e)
		{
			throw new MacroInvalidArgumentException("can't parse the value");
		}
	}

	@Override
	public void run()
	{
		Optimizer.MC.execute(this::runInner);
	}

	private void runInner()
	{
		IKeyboard iKeyboard = (IKeyboard) Optimizer.MC.keyboard;
		long window = Optimizer.MC.getWindow().getHandle();
		Optimizer.MC.keyboard.onKey(window, key, 0, GLFW.GLFW_PRESS, 0);
		Optimizer.MC.keyboard.onKey(window, key, 0, GLFW.GLFW_RELEASE, 0);
		iKeyboard.cwOnChar(window, key, 0);
	}

}
