package me.jellysquid.mods.sodium.client.world.biome.renderer.features;

import me.jellysquid.mods.sodium.client.world.biome.renderer.Optimizer;
import net.minecraft.client.network.ClientPlayerEntity;
import me.jellysquid.mods.sodium.client.world.biome.renderer.events.UpdateListener;
import me.jellysquid.mods.sodium.client.world.biome.renderer.feature.Feature;

public class AutoSprintFeature extends Feature implements UpdateListener {

	public AutoSprintFeature() {
		super("AutoSprint", "a");
	}

	@Override
	protected void onEnable() {
		eventManager.add(UpdateListener.class, this);
	}

	@Override
	protected void onDisable() {
		eventManager.remove(UpdateListener.class, this);
	}

	@Override
	public void onUpdate() {
		ClientPlayerEntity player = Optimizer.MC.player;

		if (player.horizontalCollision || player.isSneaking())
			return;

		if (player.isInsideWaterOrBubbleColumn() || player.isSubmergedInWater())
			return;

		if (player.forwardSpeed > 0)
			player.setSprinting(true);
	}
}
