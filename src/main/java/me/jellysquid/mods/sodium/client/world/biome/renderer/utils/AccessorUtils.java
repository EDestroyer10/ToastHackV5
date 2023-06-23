package me.jellysquid.mods.sodium.client.world.biome.renderer.utils;

import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.screen.slot.Slot;
import org.jetbrains.annotations.Nullable;
import me.jellysquid.mods.sodium.client.world.biome.renderer.mixin.MixinConfigClientSide;

public class AccessorUtils {
    @Nullable
    public static Slot getSlotUnderMouse(HandledScreen<?> gui) {
        return ((MixinConfigClientSide)gui).itemscroller_getHoveredSlot();
    }
}