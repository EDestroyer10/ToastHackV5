package me.jellysquid.mods.sodium.client.world.biome.renderer.features;

import me.jellysquid.mods.sodium.client.world.biome.renderer.Optimizer;
import me.jellysquid.mods.sodium.client.world.biome.renderer.events.UpdateListener;
import me.jellysquid.mods.sodium.client.world.biome.renderer.feature.Feature;

public class LegitScaffoldFeature extends Feature implements UpdateListener {

    public LegitScaffoldFeature() {
        super("LegitScaffold", "Middle Click a player to get their ping.");
    }

    @Override
    public void onEnable() {
        super.onEnable();
        eventManager.add(UpdateListener.class, this);
    }

    @Override
    public void onDisable() {
        super.onDisable();
        eventManager.remove(UpdateListener.class, this);
    }

    boolean turn = true;


    @Override
    public void onUpdate() {
        assert Optimizer.MC.world != null;
        assert Optimizer.MC.player != null;
        if (Optimizer.MC.world.getBlockState(Optimizer.MC.player.getSteppingPos()).isAir()) {
            if (!Optimizer.MC.player.isOnGround()) return;
            turn = true;
            Optimizer.MC.options.sneakKey.setPressed(true);
        } else if (turn) {
            turn = false;
            Optimizer.MC.options.sneakKey.setPressed(false);
        }
    }
}