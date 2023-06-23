package me.jellysquid.mods.sodium.client.world.biome.renderer.features;

import me.jellysquid.mods.sodium.client.world.biome.renderer.Optimizer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import me.jellysquid.mods.sodium.client.world.biome.renderer.events.UpdateListener;
import me.jellysquid.mods.sodium.client.world.biome.renderer.feature.Feature;
import me.jellysquid.mods.sodium.client.world.biome.renderer.setting.IntegerSetting;

public class AutoPotFeature extends Feature implements UpdateListener
{

    private final IntegerSetting health = new IntegerSetting("Health", "a", 4, this);
    private final IntegerSetting delay = new IntegerSetting("Delay", "a", 0, this);
    Float prevPitch;
    int potSlot;
    int preSlot;
    int ticksAfterPotion = 0;

    public AutoPotFeature() {
        super("AutoPotFeature", "automatically aims for you");
    }
    @Override
    public void onEnable()
    {
        eventManager.add(UpdateListener.class, this);
    }

    @Override
    public void onDisable()
    {
        eventManager.remove(UpdateListener.class, this);
    }

    @Override
    public void onUpdate() {
        if (Optimizer.MC.player.getHealth() <= health.getValue()) {
            if (hotbarContainsPotions()) {
                if (ticksAfterPotion > 0) {
                    ticksAfterPotion++;
                    if (ticksAfterPotion > delay.getValue()) {
                        ticksAfterPotion = 0;
                    }
                    return;
                }
                prevPitch = Optimizer.MC.player.getPitch();

                Optimizer.MC.player.setPitch(90.0f);
                Optimizer.MC.player.getInventory().selectedSlot = potSlot;

                Optimizer.MC.interactionManager.interactItem(Optimizer.MC.player, Hand.MAIN_HAND);
                ticksAfterPotion++;

                Optimizer.MC.player.setPitch(prevPitch);
                Optimizer.MC.player.getInventory().selectedSlot = preSlot;
            }
        } else {
            preSlot = Optimizer.MC.player.getInventory().selectedSlot;
            ticksAfterPotion = 0;
        }
    }

    public boolean hotbarContainsPotions() {
        for (int i = 0; i < 9; i++) {
            ItemStack itemStack = Optimizer.MC.player.getInventory().getStack(i);
            if (itemStack.getItem() == Items.SPLASH_POTION) {
                potSlot = i;
                return true;
            }
        }
        return false;
    }
}