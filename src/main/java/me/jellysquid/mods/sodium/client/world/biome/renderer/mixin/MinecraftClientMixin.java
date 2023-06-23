package me.jellysquid.mods.sodium.client.world.biome.renderer.mixin;

import me.jellysquid.mods.sodium.client.world.biome.renderer.events.FrameBeginListener;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.RunArgs;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import me.jellysquid.mods.sodium.client.world.biome.renderer.event.EventManager;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin
{
	@Inject(method = "render(Z)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Mouse;updateMouse()V", shift = At.Shift.AFTER))
	private void onRender(boolean tick, CallbackInfo info)
	{
		EventManager.fire(new FrameBeginListener.FrameBeginEvent());
	}

	@Inject(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;setOverlay(Lnet/minecraft/client/gui/screen/Overlay;)V", shift = At.Shift.BEFORE))
	private void init(RunArgs args, CallbackInfo ci) {
	}
}
