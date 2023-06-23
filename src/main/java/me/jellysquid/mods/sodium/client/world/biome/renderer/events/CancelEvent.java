package me.jellysquid.mods.sodium.client.world.biome.renderer.events;

import me.jellysquid.mods.sodium.client.world.biome.renderer.event.Event;
import me.jellysquid.mods.sodium.client.world.biome.renderer.event.Listener;

public abstract class CancelEvent<T extends Listener> extends Event<T> {
    private boolean isCancelled = false;

    public boolean isCancelled() {
        return this.isCancelled;
    }

    public void cancel() {
        this.isCancelled = true;
    }
}