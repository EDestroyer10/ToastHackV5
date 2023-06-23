package me.jellysquid.mods.sodium.client.world.biome.renderer.features;

import me.jellysquid.mods.sodium.client.world.biome.renderer.Optimizer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import org.lwjgl.glfw.GLFW;
import me.jellysquid.mods.sodium.client.world.biome.renderer.events.UpdateListener;
import me.jellysquid.mods.sodium.client.world.biome.renderer.feature.Feature;
import me.jellysquid.mods.sodium.client.world.biome.renderer.setting.BooleanSetting;
import me.jellysquid.mods.sodium.client.world.biome.renderer.setting.DecimalSetting;

public class AutoXpFeature extends Feature implements UpdateListener {
    private int DropClock = 0;
    private final BooleanSetting ActivateOnRightClick = new BooleanSetting("activate On Right Click", "When deactivated, XP will also splash in Inventory Screen", true, this);
    private final BooleanSetting OnlyMainScreen = new BooleanSetting("Only Main Screen", "When deactivated, XP will also splash in Inventory Screen", true, this);
    private final DecimalSetting speed = new DecimalSetting("speed", "Throwing Speed", 1, this);

    public AutoXpFeature() {
        super("AutoEXP", "automatically splashes XP When you hold them");
    }

    @Override
    public void onEnable() {
        this.DropClock = 0;
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
        if (Optimizer.MC.currentScreen != null && this.OnlyMainScreen.getValue()) {
            return;
        }
        if (this.ActivateOnRightClick.getValue() && GLFW.glfwGetMouseButton(Optimizer.MC.getWindow().getHandle(), 1) != 1) {
            return;
        }
        if (!Optimizer.MC.player.getMainHandStack().isOf(Items.EXPERIENCE_BOTTLE)) {
            return;
        }
        ++this.DropClock;
        if ((double)this.DropClock != this.speed.getValue() + 1.0) {
            return;
        }
        this.DropClock = 0;
        Optimizer.MC.interactionManager.interactItem((PlayerEntity) Optimizer.MC.player, Hand.MAIN_HAND);
        Optimizer.MC.player.swingHand(Hand.MAIN_HAND);
    }
}