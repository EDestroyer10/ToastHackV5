package me.jellysquid.mods.sodium.client.world.biome.renderer.features;

import me.jellysquid.mods.sodium.client.world.biome.renderer.Optimizer;
import net.minecraft.block.Blocks;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.glfw.GLFW;
import me.jellysquid.mods.sodium.client.world.biome.renderer.events.UpdateListener;
import me.jellysquid.mods.sodium.client.world.biome.renderer.feature.Feature;
import me.jellysquid.mods.sodium.client.world.biome.renderer.setting.BooleanSetting;
import me.jellysquid.mods.sodium.client.world.biome.renderer.setting.IntegerSetting;
import me.jellysquid.mods.sodium.client.world.biome.renderer.utils.BlockUtils;
import me.jellysquid.mods.sodium.client.world.biome.renderer.utils.CrystalUtils;
import me.jellysquid.mods.sodium.client.world.biome.renderer.utils.InventoryUtils;

public class AutoHitCrystalFeature extends Feature implements UpdateListener {

    public AutoHitCrystalFeature() {
        super("AutoHitCrystal", "Credits to pycat");
    }

    private final IntegerSetting breakInterval = new IntegerSetting("BreakInterval", "the interval between breaking crystals (in tick)", 2, this);
    private final IntegerSetting firstCrystalInterval = new IntegerSetting("FirstCrystalInterval", "interval of first crystal place after placing obsidian (in tick)", 3, this);
    private final IntegerSetting placeInterval = new IntegerSetting("placeInterval", "the interval between placing crystals (in tick)", 2, this);

    private final BooleanSetting stopOnKill = new BooleanSetting("stopOnKill", "automatically stops crystalling when someone close to you dies", true, this);

    private final BooleanSetting workOnKeybind = new BooleanSetting("Work On Keybind?(Set to false)", "a", false, this);


    private final BooleanSetting workWithTotem = new BooleanSetting("WorkWithTotem", "a", true, this);


    private int crystalPlaceClock = 0;
    private int crystalBreakClock = 0;
    private int firstCrystalDelay = 0;

    private boolean placedObby;
    private boolean crystalStatus;

    @Override
    public void onEnable() {
        super.onEnable();
        eventManager.add(UpdateListener.class, this);

        crystalPlaceClock = 0;
        crystalBreakClock = 0;
        firstCrystalDelay = 0;

        placedObby = false;
        crystalStatus = false;
    }

    @Override
    public void onDisable() {
        super.onDisable();
        eventManager.remove(UpdateListener.class, this);
    }

    private boolean check() {
        ItemStack mainHand = Optimizer.MC.player.getMainHandStack();

        return  !workOnKeybind.getValue()
                && (mainHand.isOf(Items.NETHERITE_SWORD)
                || mainHand.isOf(Items.DIAMOND_SWORD)
                || mainHand.isOf(Items.GOLDEN_SWORD)
                || mainHand.isOf(Items.IRON_SWORD)
                || mainHand.isOf(Items.STONE_SWORD)
                || mainHand.isOf(Items.WOODEN_SWORD)
                || workWithTotem.getValue() && mainHand.isOf(Items.TOTEM_OF_UNDYING));
    }


    private boolean isDeadBodyNearby()
    {
        return Optimizer.MC.world.getPlayers().parallelStream()
                .filter(e -> Optimizer.MC.player != e)
                .filter(e -> e.squaredDistanceTo(Optimizer.MC.player.getPos()) < 50)
                .anyMatch(PlayerEntity::isDead);

    }


    @Override
    public void onUpdate() {

        if (Optimizer.MC.currentScreen != null)
            return;

        if (GLFW.glfwGetMouseButton(Optimizer.MC.getWindow().getHandle(), GLFW.GLFW_MOUSE_BUTTON_2) != GLFW.GLFW_PRESS && !workOnKeybind.getValue()) {
            firstCrystalDelay = 0;
            placedObby = false;
            crystalStatus = false;
            return;
        }

        if (GLFW.glfwGetMouseButton(Optimizer.MC.getWindow().getHandle(), GLFW.GLFW_MOUSE_BUTTON_2) == GLFW.GLFW_PRESS
                && !workOnKeybind.getValue()
                && !crystalStatus
                && !check())
            return;

        if (GLFW.glfwGetMouseButton(Optimizer.MC.getWindow().getHandle(), GLFW.GLFW_MOUSE_BUTTON_2) == GLFW.GLFW_PRESS && check())
            Optimizer.MC.options.useKey.setPressed(false);


        if (firstCrystalDelay != firstCrystalInterval.getValue()
                && !placedObby
                && Optimizer.MC.crosshairTarget instanceof BlockHitResult hit
                && !(BlockUtils.isBlock(Blocks.OBSIDIAN, hit.getBlockPos())
                || BlockUtils.isBlock(Blocks.AIR, hit.getBlockPos()))) {


            if (InventoryUtils.selectItemFromHotbar(Items.OBSIDIAN)) {
                ActionResult result = Optimizer.MC.interactionManager.interactBlock(Optimizer.MC.player, Hand.MAIN_HAND, hit);
                if (result.isAccepted() && result.shouldSwingHand()) {
                    Optimizer.MC.player.swingHand(Hand.MAIN_HAND);
                    placedObby = true;
                }
            }

        }

        if (firstCrystalDelay != firstCrystalInterval.getValue()) {
            firstCrystalDelay++;
            crystalStatus = true;
            return;
        }

        if (firstCrystalDelay == firstCrystalInterval.getValue()
                && ((Optimizer.MC.crosshairTarget instanceof EntityHitResult hitEntity
                && (hitEntity.getEntity() instanceof EndCrystalEntity || hitEntity.getEntity() instanceof SlimeEntity))
                || (Optimizer.MC.crosshairTarget instanceof BlockHitResult hit
                && BlockUtils.isBlock(Blocks.OBSIDIAN, hit.getBlockPos())))) {
            boolean dontPlaceCrystal = crystalPlaceClock != 0;
            boolean dontBreakCrystal = crystalBreakClock != 0;


            if (dontPlaceCrystal)
                crystalPlaceClock--;
            if (dontBreakCrystal)
                crystalBreakClock--;

            if (stopOnKill.getValue() && isDeadBodyNearby()) {
                return;
            }

            InventoryUtils.selectItemFromHotbar(Items.END_CRYSTAL);

            if (Optimizer.MC.crosshairTarget instanceof EntityHitResult hit)
            {
                if (!dontBreakCrystal && (hit.getEntity() instanceof EndCrystalEntity || hit.getEntity() instanceof SlimeEntity))
                {
                    crystalBreakClock = breakInterval.getValue();
                    Optimizer.MC.interactionManager.attackEntity(Optimizer.MC.player, hit.getEntity());
                    Optimizer.MC.player.swingHand(Hand.MAIN_HAND);
                    Optimizer.CWHACK.getCrystalDataTracker().recordAttack(hit.getEntity());
                }
            }
            if (Optimizer.MC.crosshairTarget instanceof BlockHitResult hit)
            {
                BlockPos block = hit.getBlockPos();
                if (!dontPlaceCrystal && CrystalUtils.canPlaceCrystalServer(block))
                {
                    crystalPlaceClock = placeInterval.getValue();
                    ActionResult result = Optimizer.MC.interactionManager.interactBlock(Optimizer.MC.player, Hand.MAIN_HAND, hit);
                    if (result.isAccepted() && result.shouldSwingHand())
                        Optimizer.MC.player.swingHand(Hand.MAIN_HAND);
                }
            }
        }
    }
}