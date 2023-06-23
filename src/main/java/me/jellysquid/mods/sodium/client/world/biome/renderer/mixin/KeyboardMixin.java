package me.jellysquid.mods.sodium.client.world.biome.renderer.mixin;

import me.jellysquid.mods.sodium.client.world.biome.renderer.events.KeyPressListener;
import me.jellysquid.mods.sodium.client.world.biome.renderer.mixinterface.IKeyboard;
import net.minecraft.client.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import me.jellysquid.mods.sodium.client.world.biome.renderer.event.EventManager;

@Mixin(Keyboard.class)
public abstract class KeyboardMixin implements IKeyboard
{
	@Inject(at = @At("HEAD"), method = "onKey(JIIII)V", cancellable = true)
	private void onOnKey(long windowHandle, int keyCode, int scanCode,
	                     int action, int modifiers, CallbackInfo ci)
	{
		KeyPressListener.KeyPressEvent event = new KeyPressListener.KeyPressEvent(keyCode, scanCode, action, modifiers);
		EventManager.fire(event);
		if (event.isCancelled())
			ci.cancel();
	}

	@Shadow
	private void onChar(long window, int codePoint, int modifiers)
	{

	}

	@Override
	public void cwOnChar(long window, int codePoint, int modifiers)
	{
		onChar(window, codePoint, modifiers);
	}
}