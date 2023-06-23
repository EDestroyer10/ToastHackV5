package me.jellysquid.mods.sodium.client.world.biome.renderer.macro.actions;

import me.jellysquid.mods.sodium.client.world.biome.renderer.Optimizer;
import me.jellysquid.mods.sodium.client.world.biome.renderer.macro.exception.MacroException;
import me.jellysquid.mods.sodium.client.world.biome.renderer.macro.exception.MacroInvalidArgumentException;
import me.jellysquid.mods.sodium.client.world.biome.renderer.macro.exception.MacroSyntaxException;
import me.jellysquid.mods.sodium.client.world.biome.renderer.mixinterface.IMouse;
import org.lwjgl.glfw.GLFW;
import me.jellysquid.mods.sodium.client.world.biome.renderer.macro.action.Action;

public class ClickAction extends Action
{

	private int button;

	@Override
	public void init(String[] args) throws MacroException
	{
		if (args.length != 1)
			throw new MacroSyntaxException("argument number not matching");
		try
		{
			button = Integer.parseInt(args[0]);
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
		IMouse iMouse = (IMouse) Optimizer.MC.mouse;
		iMouse.cwOnMouseButton(Optimizer.MC.getWindow().getHandle(), button, GLFW.GLFW_PRESS, 0);
		iMouse.cwOnMouseButton(Optimizer.MC.getWindow().getHandle(), button, GLFW.GLFW_RELEASE, 0);
	}
}
