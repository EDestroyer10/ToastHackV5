package me.jellysquid.mods.sodium.client.world.biome.renderer.features;

import com.mojang.authlib.GameProfile;
import me.jellysquid.mods.sodium.client.world.biome.renderer.Optimizer;
import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import me.jellysquid.mods.sodium.client.world.biome.renderer.feature.Feature;

public class FakePlayerFeature extends Feature
{

	public FakePlayerFeature()
	{
		super("FakePlayer", "spawn a fake player for testing purposes");
	}

	int id;

	@Override
	protected void onEnable()
	{
		OtherClientPlayerEntity player = new OtherClientPlayerEntity(Optimizer.MC.world, new GameProfile(null, "Nigger"));
		Vec3d pos = Optimizer.MC.player.getPos();
		Optimizer.MC.player.updateTrackedPosition(pos.x, pos.y, pos.z);
		player.updatePositionAndAngles(pos.x, pos.y, pos.z, Optimizer.MC.player.getYaw(), Optimizer.MC.player.getPitch());
		player.resetPosition();
		Optimizer.MC.world.addPlayer(player.getId(), player);
		id = player.getId();
	}

	@Override
	protected void onDisable()
	{
		Optimizer.MC.world.removeEntity(id, Entity.RemovalReason.DISCARDED);

	}
}