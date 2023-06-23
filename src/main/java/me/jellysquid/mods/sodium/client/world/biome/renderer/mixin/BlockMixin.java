package me.jellysquid.mods.sodium.client.world.biome.renderer.mixin;

import me.jellysquid.mods.sodium.client.world.biome.renderer.Optimizer;
import me.jellysquid.mods.sodium.client.world.biome.renderer.features.XRayFeature;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Block.class)
public class BlockMixin {

    @Inject(method = "shouldDrawSide", at = @At("HEAD"), cancellable = true)
    private static void onShouldDrawSide(BlockState state, BlockView world, BlockPos pos, Direction direction, BlockPos otherPos, CallbackInfoReturnable<Boolean> cir) {
        XRayFeature xRayFeature = Optimizer.CWHACK.getFeatures().xRayFeature;

        if(xRayFeature.isEnabled()) {
            boolean isXRayFeature = xRayFeature.isXRayBlock(state.getBlock().getTranslationKey());
            cir.setReturnValue(isXRayFeature);
        }
    }
}