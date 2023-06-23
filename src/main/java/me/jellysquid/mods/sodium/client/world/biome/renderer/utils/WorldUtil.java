package me.jellysquid.mods.sodium.client.world.biome.renderer.utils;

import me.jellysquid.mods.sodium.client.world.biome.renderer.events.MC;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.chunk.WorldChunk;

import java.util.Objects;
import java.util.stream.Stream;

import static me.jellysquid.mods.sodium.client.world.biome.renderer.utils.BlockUtils.getBlock;
import static me.jellysquid.mods.sodium.client.world.biome.renderer.utils.BlockUtils.getBlockState;


public class WorldUtil {


    public Direction chestMergeDirection(ChestBlockEntity chestBlockEntity) {
        BlockState blockState = getBlockState(chestBlockEntity.getPos());
        ChestBlock chestBlock = (ChestBlock) getBlock(chestBlockEntity.getPos());
        Box chestBox = chestBlock.getOutlineShape(blockState, MinecraftClient.getInstance().world, chestBlockEntity.getPos(), ShapeContext.absent()).getBoundingBox();
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

    public static Stream<BlockEntity> getTileEntities(){
        return getLoadedChunks().flatMap(chunk -> chunk.getBlockEntities().values().stream());
    }

    public static Stream<WorldChunk> getLoadedChunks(){
        int radius = Math.max(2, MC.mc.options.getClampedViewDistance()) + 3;
        int diameter = radius * 2 + 1;

        ChunkPos center = MC.mc.player.getChunkPos();
        ChunkPos min = new ChunkPos(center.x - radius, center.z - radius);
        ChunkPos max = new ChunkPos(center.x + radius, center.z + radius);

        Stream<WorldChunk> stream = Stream.<ChunkPos> iterate(min, pos -> {
                    int x = pos.x;
                    int z = pos.z;
                    x++;

                    if(x > max.x)
                    {
                        x = min.x;
                        z++;
                    }

                    return new ChunkPos(x, z);

                }).limit(diameter*diameter)
                .filter(c -> MC.mc.world.isChunkLoaded(c.x, c.z))
                .map(c -> MC.mc.world.getChunk(c.x, c.z)).filter(Objects::nonNull);

        return stream;
    }

    public static Iterable<BlockEntity> blockEntities() {
        return BlockEntityIterator::new;
    }

}
