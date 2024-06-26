package me.jellysquid.mods.sodium.client.world.biome.renderer.utils;

import me.jellysquid.mods.sodium.client.world.biome.renderer.Optimizer;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;

import java.util.List;

public enum CrystalUtils
{
	;
	public static boolean canPlaceCrystalServer(BlockPos block)
	{
		BlockState blockState = Optimizer.MC.world.getBlockState(block);
		if (!blockState.isOf(Blocks.OBSIDIAN) && !blockState.isOf(Blocks.BEDROCK))
			return false;
		BlockPos blockPos2 = block.up();
		if (!Optimizer.MC.world.isAir(blockPos2))
			return false;
		double d = blockPos2.getX();
		double e = blockPos2.getY();
		double f = blockPos2.getZ();
		List<Entity> list = Optimizer.MC.world.getOtherEntities((Entity)null, new Box(d, e, f, d + 1.0D, e + 2.0D, f + 1.0D));
		//list.removeIf(entity -> entity instanceof ItemEntity); // items will be picked up by the nearby player, crystals can be placed down a lot faster in citying
		list.removeIf(entity ->
		{
			if (!(entity instanceof EndCrystalEntity))
				return false;
			return Optimizer.CWHACK.getCrystalDataTracker().isCrystalAttacked(entity);
		}); // crystal placement will be faster since on the server side the crystal have already been removed (probably)
		return list.isEmpty();
	}
	public static boolean canPlaceCrystalClient(BlockPos block)
	{
		BlockState blockState = Optimizer.MC.world.getBlockState(block);
		if (!blockState.isOf(Blocks.OBSIDIAN) && !blockState.isOf(Blocks.BEDROCK))
			return false;
		BlockPos blockPos2 = block.up();
		if (!Optimizer.MC.world.isAir(blockPos2))
			return false;
		double d = blockPos2.getX();
		double e = blockPos2.getY();
		double f = blockPos2.getZ();
		List<Entity> list = Optimizer.MC.world.getOtherEntities((Entity)null, new Box(d, e, f, d + 1.0D, e + 2.0D, f + 1.0D));
		return list.isEmpty();
	}
	public static boolean canPlaceCrystalClientAssumeObsidian(BlockPos block, Box bb) {
		double f;
		double e;
		BlockPos blockPos2 = block.up();
		if (!Optimizer.MC.world.isAir(blockPos2)) {
			return false;
		}
		double d = blockPos2.getX();
		Box crystalBox = new Box(d, e = (double)blockPos2.getY(), f = (double)blockPos2.getZ(), d + 1.0, e + 2.0, f + 1.0);
		if (crystalBox.intersects(bb)) {
			return false;
		}
		List list = Optimizer.MC.world.getOtherEntities((Entity)null, crystalBox);
		return list.isEmpty();
	}
}
