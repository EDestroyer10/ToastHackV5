/**
 * A helper class that contains several useful functions.
 */
package me.jellysquid.mods.sodium.client.world.biome.renderer.utils;

import me.jellysquid.mods.sodium.client.world.biome.renderer.Optimizer;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.WorldChunk;

import java.util.Objects;
import java.util.stream.Stream;

public class ModuleUtils {
	public static Stream<BlockEntity> getTileEntities(){
		return getLoadedChunks().flatMap(chunk -> chunk.getBlockEntities().values().stream());
	}
	
	public static Stream<WorldChunk> getLoadedChunks(){
		int radius = Math.max(2, Optimizer.MC.options.getClampedViewDistance()) + 3;
		int diameter = radius * 2 + 1;
		
		ChunkPos center = Optimizer.MC.player.getChunkPos();
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
			.filter(c -> Optimizer.MC.world.isChunkLoaded(c.x, c.z))
			.map(c -> Optimizer.MC.world.getChunk(c.x, c.z)).filter(Objects::nonNull);
		
		return stream;
	}
}