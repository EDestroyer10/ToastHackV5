package me.jellysquid.mods.sodium.client.world.biome.renderer.mixinterface;

import net.minecraft.util.math.Vec3d;
import me.jellysquid.mods.sodium.client.world.biome.renderer.utils.math.Vec4d;

public interface IMatrix4f
{
	Vec4d multiply(Vec4d v);

	Vec3d multiply(Vec3d v);
}
