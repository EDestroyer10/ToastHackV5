package me.jellysquid.mods.sodium.client.world.biome.renderer.mixinterface;

import net.minecraft.world.chunk.BlockEntityTickInvoker;

import java.util.List;

public interface IWorld
{
	public List<BlockEntityTickInvoker> getBlockEntityTickers();
}
