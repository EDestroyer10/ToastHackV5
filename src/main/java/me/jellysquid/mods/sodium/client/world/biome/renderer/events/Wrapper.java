package me.jellysquid.mods.sodium.client.world.biome.renderer.events;

import me.jellysquid.mods.sodium.client.world.biome.renderer.mixinterface.IClientPlayerInteractionManager;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.Window;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import me.jellysquid.mods.sodium.client.world.biome.renderer.event.Event;

import static me.jellysquid.mods.sodium.client.world.biome.renderer.utils.BlockUtils.getBlock;
import static me.jellysquid.mods.sodium.client.world.biome.renderer.utils.BlockUtils.getBlockState;

public enum Wrapper {
    CWHACK;

    public MinecraftClient getMinecraft() {
        return MinecraftClient.getInstance();
    }

    public ClientWorld getWorld() {
        return getMinecraft().world;
    }

    public ClientPlayerInteractionManager getClientPlayerInteractionManager() {
        return getMinecraft().interactionManager;
    }

    public Direction chestMergeDirection(ChestBlockEntity chestBlockEntity) {
        BlockState blockState = getBlockState(chestBlockEntity.getPos());
        ChestBlock chestBlock = (ChestBlock) getBlock(chestBlockEntity.getPos());
        Box chestBox = chestBlock.getOutlineShape(blockState, Wrapper.CWHACK.getWorld(), chestBlockEntity.getPos(), ShapeContext.absent()).getBoundingBox();
        if (chestBox.minZ == 0)
            return Direction.NORTH;
        if (chestBox.maxZ == 1)
            return Direction.SOUTH;
        if (chestBox.maxX == 1)
            return Direction.EAST;
        if (chestBox.minX == 0)
            return Direction.WEST;
        return Direction.UP;
    }

    public IClientPlayerInteractionManager getIClientPlayerInteractionManager() { return (IClientPlayerInteractionManager)getMinecraft().interactionManager; }

    public Window getWindow() {
        return getMinecraft().getWindow();
    }

    public TextRenderer getTextRenderer() {
        return getMinecraft().textRenderer;
    }

    public WorldRenderer getWorldRenderer() {
        return getMinecraft().worldRenderer;
    }

    public GameRenderer getGameRenderer() {
        return getMinecraft().gameRenderer;
    }
    public enum EventType {
        PACKET_SEND(PacketEvent.class), PACKET_RECEIVE(PacketEvent.class);
        private final Class<? extends Event> expectedType = null;

        EventType(Class<PacketEvent> entityRenderEventClass) {
        }

        public BlockState getBlockState(BlockPos pos) {
            try {
                if (CWHACK.getWorld() == null)
                    return null;
                return CWHACK.getWorld().getBlockState(pos);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        public Class<? extends Event> getExpectedType() {
            return expectedType;
        }
    }
}
