
package me.jellysquid.mods.sodium.client.world.biome.renderer.events;


import me.jellysquid.mods.sodium.client.world.biome.renderer.event.Event;

import java.util.ArrayList;

public class EventWorldRender extends Event {

    public final float partialTicks;

    public EventWorldRender(float partialTicks) {
        this.partialTicks = partialTicks;
    }

    @Override
    public void fire(ArrayList listeners) {

    }

    @Override
    public Class getListenerType() {
        return null;
    }
}