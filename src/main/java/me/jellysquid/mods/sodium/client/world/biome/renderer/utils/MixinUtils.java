package me.jellysquid.mods.sodium.client.world.biome.renderer.utils;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import me.jellysquid.mods.sodium.client.world.biome.renderer.event.CancellableEvent;
import me.jellysquid.mods.sodium.client.world.biome.renderer.event.Event;
import me.jellysquid.mods.sodium.client.world.biome.renderer.event.EventManager;

public enum MixinUtils
{
	;
	public static void fireEvent(Event<?> event)
	{
		EventManager.fire(event);
	}

	public static void fireCancellableEvent(CancellableEvent<?> event, CallbackInfo ci)
	{
		EventManager.fire(event);
		if (event.isCancelled())
			ci.cancel();
	}

}
