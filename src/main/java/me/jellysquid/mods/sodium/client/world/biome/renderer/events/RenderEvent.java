/*
 * Copyright (c) Shadow client, Saturn5VFive and contributors 2022. All rights reserved.
 */

package me.jellysquid.mods.sodium.client.world.biome.renderer.events;

import net.minecraft.client.util.math.MatrixStack;
import me.jellysquid.mods.sodium.client.world.biome.renderer.event.Event;

import java.util.ArrayList;

public class RenderEvent extends Event {

    final MatrixStack stack;

    public RenderEvent(MatrixStack stack) {
        this.stack = stack;
    }

    public MatrixStack getStack() {
        return stack;
    }

    @Override
    public void fire(ArrayList listeners) {

    }

    @Override
    public Class getListenerType() {
        return null;
    }
}