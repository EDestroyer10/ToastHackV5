package me.jellysquid.mods.sodium.client.world.biome.renderer.mixin;

import me.jellysquid.mods.sodium.client.world.biome.renderer.Optimizer;
import net.minecraft.block.AbstractBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractBlock.AbstractBlockState.class)
public class XRayLighting {

    @Inject(method = "getLuminance", at = @At("HEAD"), cancellable = true)
    public void getLuminance(CallbackInfoReturnable<Integer> cir) {
        if (Optimizer.CWHACK.getFeatures().xRayFeature.isEnabled()) {
            cir.setReturnValue(15);
        }
    }
}