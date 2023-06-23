package me.jellysquid.mods.sodium.client.world.biome.renderer.mixinterface;

import net.minecraft.util.math.BlockPos;

public interface IClientPlayerInteractionManager
{
	void cwSyncSelectedSlot();
	void setBreakingBlock(boolean breakingBlock);
	void setCurrentBreakingPos(BlockPos pos);
}
