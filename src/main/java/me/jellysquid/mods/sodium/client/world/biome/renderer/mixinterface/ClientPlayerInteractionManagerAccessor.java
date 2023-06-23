package me.jellysquid.mods.sodium.client.world.biome.renderer.mixinterface;

import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ClientPlayerInteractionManager.class)
public interface ClientPlayerInteractionManagerAccessor {

    @Accessor("currentBreakingPos")
    BlockPos getCurrentBreakingPos();
}
