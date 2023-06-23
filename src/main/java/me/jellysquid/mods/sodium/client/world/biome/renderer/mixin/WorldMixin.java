package me.jellysquid.mods.sodium.client.world.biome.renderer.mixin;

import me.jellysquid.mods.sodium.client.world.biome.renderer.mixinterface.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.chunk.BlockEntityTickInvoker;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(World.class)
public class WorldMixin implements IWorld
{
	@Shadow
	@Final
	protected List<BlockEntityTickInvoker> blockEntityTickers;

	@Override
	public List<BlockEntityTickInvoker> getBlockEntityTickers()
	{
		return blockEntityTickers;
	}
}
