package me.jellysquid.mods.sodium.client.world.biome.renderer.features;

import me.jellysquid.mods.sodium.client.world.biome.renderer.Optimizer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import me.jellysquid.mods.sodium.client.world.biome.renderer.events.UpdateListener;
import me.jellysquid.mods.sodium.client.world.biome.renderer.feature.Feature;
import me.jellysquid.mods.sodium.client.world.biome.renderer.setting.BooleanSetting;

import java.util.ArrayList;
import java.util.List;

public class ESPFeature extends Feature implements UpdateListener {
    private List<Entity> targetEntities = new ArrayList<>();
    private final BooleanSetting invisibles = new BooleanSetting("Invisibles", "It just renders invis entities", true, this);

    public ESPFeature() {
        super("ESP", "Sense nearby users!");
        get = this;
    }

    @Override
    public void onUpdate() {
        assert Optimizer.MC.world != null;
        for (Entity e : Optimizer.MC.world.getEntities()) {
            if (e instanceof PlayerEntity && !e.isGlowing()) {
                e.setGlowing(true);
            }
        }
        if (invisibles.isEnabled()) {
            Optimizer.MC.world.getEntities().forEach(entity -> {
                if (entity.isInvisible() && !targetEntities.contains(entity)) {
                    targetEntities.add(entity);
                    entity.setInvisible(false);
                }
            });
        }
    }
    @Override
    public void onEnable() {
        assert Optimizer.MC.world != null;
        for (Entity e : Optimizer.MC.world.getEntities()) {
            if (e instanceof PlayerEntity && !e.isGlowing()) {
                e.setGlowing(true);
            }
        }
        eventManager.add(UpdateListener.class, this);
    }

    @Override
    public void onDisable() {
        assert Optimizer.MC.world != null;
        for (Entity e : Optimizer.MC.world.getEntities()) {
            if (e instanceof PlayerEntity && !e.isGlowing()) {
                e.setGlowing(false);
            }
        }
        eventManager.remove(UpdateListener.class, this);
        if (invisibles.isEnabled()) {
            targetEntities.forEach(entity -> {entity.setInvisible(true);});
            targetEntities.clear();
        }
    super.onDisable();
    }


    public boolean shouldRenderEntity(Entity entity) {
        if (!isEnabled()) return false;
        if (entity == null) return false;
        
        return true;
    }

    public static ESPFeature get;
}
