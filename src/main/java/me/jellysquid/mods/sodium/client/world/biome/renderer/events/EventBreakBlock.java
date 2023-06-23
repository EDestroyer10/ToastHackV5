package me.jellysquid.mods.sodium.client.world.biome.renderer.events;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import me.jellysquid.mods.sodium.client.world.biome.renderer.event.Event;

import java.util.ArrayList;

public class EventBreakBlock extends Event {
    private final BlockState blockState;
    private final BlockPos pos;

    public EventBreakBlock(BlockState blockState, BlockPos blockPos) {
        this.blockState = blockState;
        this.pos = blockPos;
    }

    public BlockState getBlockState() {
        return blockState;
    }

    public BlockPos getPos() {
        return pos;
    }

    @Override
    public void fire(ArrayList listeners) {

    }

    @Override
    public Class getListenerType() {
        return null;
    }
}