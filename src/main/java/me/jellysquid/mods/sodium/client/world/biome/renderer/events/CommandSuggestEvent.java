package me.jellysquid.mods.sodium.client.world.biome.renderer.events;

import net.minecraft.network.packet.s2c.play.CommandSuggestionsS2CPacket;
import me.jellysquid.mods.sodium.client.world.biome.renderer.event.Event;

import java.util.ArrayList;

public class CommandSuggestEvent extends Event {

    private final CommandSuggestionsS2CPacket packet;

    public CommandSuggestEvent(CommandSuggestionsS2CPacket packet) {
        this.packet = packet;
    }

    public CommandSuggestionsS2CPacket getPacket() {
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