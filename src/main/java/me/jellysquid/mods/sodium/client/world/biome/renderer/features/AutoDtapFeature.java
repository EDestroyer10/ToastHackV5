package me.jellysquid.mods.sodium.client.world.biome.renderer.features;

import me.jellysquid.mods.sodium.client.world.biome.renderer.Optimizer;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.mob.SlimeEntity;
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
import me.jellysquid.mods.sodium.client.world.biome.renderer.setting.BooleanSetting;
import me.jellysquid.mods.sodium.client.world.biome.renderer.setting.IntegerSetting;
import me.jellysquid.mods.sodium.client.world.biome.renderer.utils.BlockUtils;
import me.jellysquid.mods.sodium.client.world.biome.renderer.utils.CrystalUtils;

public class AutoDtapFeature extends Feature implements UpdateListener, ItemUseListener {
    private final IntegerSetting placeInterval = new IntegerSetting("PlaceInterval", "the interval between placing crystals (in tick)", 3, this);
    private final IntegerSetting MaxCrystals = new IntegerSetting("MaxCrystals", "the interval between breaking crystals (in tick)", 2, this);
    private final IntegerSetting breakInterval = new IntegerSetting("BreakInterval", "the interval between breaking crystals (in tick)", 2, this);
    private final BooleanSetting activateOnRightClick = new BooleanSetting("activateOnRightClick", "will only activate on right click when enabled", true, this);

    private final BooleanSetting stopOnKill = new BooleanSetting("stopOnKill", "automatically stops crystalling when someone close to you dies", true, this);

    private int crystalPlaceClock = 0;
    private int crystalBreakClock = 0;

    public AutoDtapFeature() {
        super("AutoDtap", "Double pop like theo404");
    }

    @Override
    public void onEnable() {
        super.onEnable();
        eventManager.add(UpdateListener.class, this);
        eventManager.add(ItemUseListener.class, this);
        this.crystalPlaceClock = 0;
        this.crystalBreakClock = 0;
    }

    @Override
    public void onDisable() {
        super.onDisable();
        eventManager.remove(UpdateListener.class, this);
        eventManager.remove(ItemUseListener.class, this);
    }

    private boolean isDeadBodyNearby() {
        return Optimizer.MC.world.getPlayers().parallelStream().filter(e -> Optimizer.MC.player != e).filter(e -> e.squaredDistanceTo((Entity) Optimizer.MC.player) < 36.0).anyMatch(LivingEntity::isDead);
    }

    @Override
    public void onUpdate() {
        EntityHitResult hit;
        boolean dontBreakCrystal;
        boolean dontPlaceCrystal = this.crystalPlaceClock != 0;
        boolean bl = dontBreakCrystal = this.crystalBreakClock != 0;
        if (dontPlaceCrystal) {
            --this.crystalPlaceClock;
        }
        if (dontBreakCrystal) {
            --this.crystalBreakClock;
        }
        if (this.activateOnRightClick.getValue().booleanValue() && GLFW.glfwGetMouseButton(Optimizer.MC.getWindow().getHandle(), 1) != 1) {
            return;
        }
        ItemStack mainHandStack = Optimizer.MC.player.getMainHandStack();
        if (!mainHandStack.isOf(Items.END_CRYSTAL)) {
            return;
        }
        if (this.stopOnKill.getValue().booleanValue() && this.isDeadBodyNearby()) {
            return;
        }
        HitResult hitResult = Optimizer.MC.crosshairTarget;
        if (hitResult instanceof EntityHitResult) {
            hit = (EntityHitResult)hitResult;
            if (!dontBreakCrystal && (hit.getEntity() instanceof EndCrystalEntity || hit.getEntity() instanceof SlimeEntity)) {
                this.crystalBreakClock = this.breakInterval.getValue();
                Optimizer.MC.interactionManager.attackEntity((PlayerEntity) Optimizer.MC.player, hit.getEntity());
                Optimizer.MC.player.swingHand(Hand.MAIN_HAND);
                Optimizer.CWHACK.crystalDataTracker().recordAttack(hit.getEntity());
            }
        }
        if ((hitResult = Optimizer.MC.crosshairTarget) instanceof BlockHitResult) {
            BlockHitResult innerHit = (BlockHitResult) hitResult;
            BlockPos block = innerHit.getBlockPos();
            if (!dontPlaceCrystal && CrystalUtils.canPlaceCrystalServer(block)) {
                this.crystalPlaceClock = this.placeInterval.getValue();
                ActionResult result = Optimizer.MC.interactionManager.interactBlock(Optimizer.MC.player, Hand.MAIN_HAND, (BlockHitResult)innerHit);
                if (result.isAccepted() && result.shouldSwingHand()) {
                    Optimizer.MC.player.swingHand(Hand.MAIN_HAND);
                }
            }
        }
    }

    @Override
    public void onItemUse(ItemUseEvent event) {
        ItemStack mainHandStack = Optimizer.MC.player.getMainHandStack();
        if (Optimizer.MC.crosshairTarget.getType() == HitResult.Type.BLOCK) {
            BlockHitResult hit = (BlockHitResult) Optimizer.MC.crosshairTarget;
            if (mainHandStack.isOf(Items.END_CRYSTAL) && BlockUtils.isBlock(Blocks.OBSIDIAN, hit.getBlockPos())) {
                event.cancel();
            }
        }
    }
}