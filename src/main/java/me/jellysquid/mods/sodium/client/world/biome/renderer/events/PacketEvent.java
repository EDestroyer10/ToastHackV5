/*
 * Copyright (c) Shadow client, Saturn5VFive and contributors 2022. All rights reserved.
 */

package me.jellysquid.mods.sodium.client.world.biome.renderer.events;

import net.minecraft.network.packet.Packet;
import me.jellysquid.mods.sodium.client.world.biome.renderer.event.Event;

import java.util.ArrayList;

public class PacketEvent extends Event {

    private final Packet<?> packet;

    public PacketEvent(Packet<?> packet) {
        this.packet = packet;
    }

    public Packet<?> getPacket() {
        return packet;
    }

    @Override
    public void fire(ArrayList listeners) {

    }

    @Override
    public Class getListenerType() {
        return null;
    }
}