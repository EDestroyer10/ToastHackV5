package me.jellysquid.mods.sodium.client.world.biome.renderer.features;

import me.jellysquid.mods.sodium.client.world.biome.renderer.Optimizer;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import org.lwjgl.glfw.GLFW;
import me.jellysquid.mods.sodium.client.world.biome.renderer.events.UpdateListener;
import me.jellysquid.mods.sodium.client.world.biome.renderer.feature.Feature;
import me.jellysquid.mods.sodium.client.world.biome.renderer.setting.BooleanSetting;
import me.jellysquid.mods.sodium.client.world.biome.renderer.utils.ChatUtils;

public class MiddleClickPingFeature extends Feature implements UpdateListener {
    private final BooleanSetting includePrefix = new BooleanSetting("Include Prefix", "whether or not to include the prefix in the ping message", false, this);
    private boolean isMiddleClicking = false;

    public MiddleClickPingFeature() {
        super("MidClickPing", "Middle Click a player to get their ping.");
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

    @Override
    public void onUpdate() {
        HitResult hit = Optimizer.MC.crosshairTarget;
        if (hit.getType() != HitResult.Type.ENTITY) {
            return;
        }
        Entity target = ((EntityHitResult)hit).getEntity();
        if (!(target instanceof PlayerEntity)) {
            return;
        }
        if (GLFW.glfwGetMouseButton(Optimizer.MC.getWindow().getHandle(), 2) == 1 && !this.isMiddleClicking) {
            this.isMiddleClicking = true;
            if (this.includePrefix.getValue()) {
                ChatUtils.plainMessageWithPrefix(target.getEntityName() + "'s §l§bping is " + MiddleClickPingFeature.getPing(target));
            } else {
                ChatUtils.sendPlainMessage(target.getEntityName() + "'s §l§bping is " + MiddleClickPingFeature.getPing(target));
            }
        }
        if (GLFW.glfwGetMouseButton(Optimizer.MC.getWindow().getHandle(), 2) == 0 && this.isMiddleClicking) {
            this.isMiddleClicking = false;
        }
    }

    public static int getPing(Entity player) {
        if (Optimizer.MC.getNetworkHandler() == null) {
            return 0;
        }
        PlayerListEntry playerListEntry = Optimizer.MC.getNetworkHandler().getPlayerListEntry(player.getUuid());
        if (playerListEntry == null) {
            return 0;
        }
        return playerListEntry.getLatency();
    }
}