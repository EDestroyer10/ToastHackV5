package me.jellysquid.mods.sodium.client.world.biome.renderer.macro.actions;

import me.jellysquid.mods.sodium.client.world.biome.renderer.Optimizer;
import me.jellysquid.mods.sodium.client.world.biome.renderer.macro.exception.MacroException;
import me.jellysquid.mods.sodium.client.world.biome.renderer.macro.exception.MacroSyntaxException;
import me.jellysquid.mods.sodium.client.world.biome.renderer.macro.action.Action;

public class JumpAction extends Action
{

	@Override
	public void init(String[] args) throws MacroException
	{
		if (args.length != 0)
			throw new MacroSyntaxException("argument number not matching");
	}

	@Override
	public void run()
	{
		Optimizer.MC.execute(() ->
		{
			if (Optimizer.MC.player != null && Optimizer.MC.player.isOnGround())
				Optimizer.MC.player.jump();
		});
	}
}
