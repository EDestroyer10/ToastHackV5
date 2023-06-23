package me.jellysquid.mods.sodium.client.world.biome.renderer.features;

import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.glfw.GLFW;
import me.jellysquid.mods.sodium.client.world.biome.renderer.events.ItemUseListener;
import me.jellysquid.mods.sodium.client.world.biome.renderer.events.UpdateListener;
import me.jellysquid.mods.sodium.client.world.biome.renderer.feature.Feature;
import me.jellysquid.mods.sodium.client.world.biome.renderer.mixinterface.IMouse;
import me.jellysquid.mods.sodium.client.world.biome.renderer.setting.BooleanSetting;
import me.jellysquid.mods.sodium.client.world.biome.renderer.setting.IntegerSetting;
import me.jellysquid.mods.sodium.client.world.biome.renderer.utils.BlockUtils;
import me.jellysquid.mods.sodium.client.world.biome.renderer.utils.CrystalUtils;

import static me.jellysquid.mods.sodium.client.world.biome.renderer.events.MC.mc;


public class MarlowCrystalFakeCPSFeature extends Feature implements ItemUseListener, UpdateListener {
    private final IntegerSetting crystalBreakInterval = new IntegerSetting("breakInterval", "the speed of attacking the crystal", 3, this);
    private final IntegerSetting crystalPlaceInterval = new IntegerSetting("placeInterval", "the speed of placing the crystal", 3, this);
    private final BooleanSetting stopOnKill = new BooleanSetting("stopOnKill", "stops on kill", false, this);
    private final BooleanSetting fakeCPS = new BooleanSetting("fakeCPS", "fakes your cps", false, this);

    private int crystalBreakClock;
    private int crystalPlaceClock;

    public MarlowCrystalFakeCPSFeature() {
        super("MarlowCrystalFakeCPS", "rewriting for vulcan bypass");
    }

    @Override
    public void onEnable() {
        crystalBreakClock = 0;
        crystalPlaceClock = 0;
        eventManager.add(ItemUseListener.class, this);
        eventManager.add(UpdateListener.class, this);
    }

    @Override
    public void onDisable() {
        eventManager.remove(UpdateListener.class, this);
        eventManager.remove(ItemUseListener.class, this);
    }

    private boolean isDeadBodyNearby() {
        return mc.world.getPlayers().parallelStream()
                .filter(e -> mc.player != e)
                .filter(e -> e.squaredDistanceTo(mc.player) < 36)
                .anyMatch(LivingEntity::isDead);
    }

    public void onUpdate() {
        IMouse mouse = (IMouse)mc.mouse;
        boolean dontPlaceCrystal = (this.crystalPlaceClock != 0);
        boolean dontBreakCrystal = (this.crystalBreakClock != 0);
        if (dontPlaceCrystal)
            this.crystalPlaceClock--;
        if (dontBreakCrystal)
            this.crystalBreakClock--;
        if (GLFW.glfwGetMouseButton(mc.getWindow().getHandle(), 1) != 1)
            return;
        if (mc.player.isUsingItem())
            return;
        if (mc.currentScreen != null)
            return;
        if (this.stopOnKill.getValue().booleanValue() && isDeadBodyNearby())
            return;
        ItemStack mainHandStack = mc.player.getMainHandStack();
        if (!mainHandStack.isOf(Items.END_CRYSTAL))
            return;
        HitResult hitResult = mc.crosshairTarget;
        if (hitResult instanceof EntityHitResult) {
            EntityHitResult hit = (EntityHitResult) hitResult;
            if (!dontBreakCrystal) {
                Entity entity = hit.getEntity();
                if (entity instanceof EndCrystalEntity crystal) {
                    this.crystalBreakClock = this.crystalBreakInterval.getValue();
                    assert mc.interactionManager != null;
                    mc.interactionManager.attackEntity((PlayerEntity) mc.player, crystal);
                    mc.player.swingHand(Hand.MAIN_HAND);
                    if (this.fakeCPS.getValue()) {
                        mouse.cwOnMouseButton(mc.getWindow().getHandle(), 0, 1, 0);
                        mouse.cwOnMouseButton(mc.getWindow().getHandle(), 0, 0, 0);
                    }
                }
            }
        }
        hitResult = mc.crosshairTarget;
        if (hitResult instanceof EntityHitResult) {
            EntityHitResult hit = (EntityHitResult) hitResult;
            BlockPos block = BlockPos.ofFloored(hit.getPos());
            if (!dontPlaceCrystal && CrystalUtils.canPlaceCrystalServer(block)) {
                this.crystalPlaceClock = this.crystalPlaceInterval.getValue().intValue();
                ActionResult result = mc.interactionManager.interactBlock(mc.player, Hand.MAIN_HAND, (BlockHitResult) hitResult);
                if (result.isAccepted() && result.shouldSwingHand()) {
                    mc.player.swingHand(Hand.MAIN_HAND);
                    if (this.fakeCPS.getValue().booleanValue()) {
                        mouse.cwOnMouseButton(mc.getWindow().getHandle(), 1, 1, 0);
                        mouse.cwOnMouseButton(mc.getWindow().getHandle(), 1, 0, 0);
                    }
                }
            }
        }
    }

    @Override
    public void onItemUse(ItemUseEvent event) {
        ItemStack mainHandStack = mc.player.getMainHandStack();
        if (mc.crosshairTarget.getType() == HitResult.Type.BLOCK) {
            EntityHitResult hit = (EntityHitResult) mc.crosshairTarget;
            if (mainHandStack.isOf(Items.END_CRYSTAL) && BlockUtils.isBlock(Blocks.OBSIDIAN, BlockPos.ofFloored(hit.getPos())))
                event.cancel();
        }
    }
}
