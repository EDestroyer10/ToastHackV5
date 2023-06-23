/*    */
package me.jellysquid.mods.sodium.client.world.biome.renderer.features;
/*    */

import me.jellysquid.mods.sodium.client.world.biome.renderer.Optimizer;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import org.lwjgl.glfw.GLFW;
import me.jellysquid.mods.sodium.client.world.biome.renderer.events.ItemUseListener;
import me.jellysquid.mods.sodium.client.world.biome.renderer.events.UpdateListener;
import me.jellysquid.mods.sodium.client.world.biome.renderer.feature.Feature;
import me.jellysquid.mods.sodium.client.world.biome.renderer.setting.IntegerSetting;
import me.jellysquid.mods.sodium.client.world.biome.renderer.utils.BlockUtils;
import me.jellysquid.mods.sodium.client.world.biome.renderer.utils.CrystalUtils;
import me.jellysquid.mods.sodium.client.world.biome.renderer.utils.RotationUtils;

public class AntiAntiCrystalFeature extends Feature implements ItemUseListener, UpdateListener {
   private final IntegerSetting placeInterval;

   public AntiAntiCrystalFeature() {
     super("FastCrystals", "make crystal placing fast");


     this.placeInterval = new IntegerSetting("placeInterval", "the delay between placing crystals (in tick)", 2, this);

     this.delay = 0;
   }

   private int delay;

   public void onEnable() {
     super.onEnable();
     eventManager.add(UpdateListener.class, this);
     eventManager.add(ItemUseListener.class, this);

     this.delay = 0;
   }


   public void onDisable() {
     super.onDisable();
     eventManager.remove(UpdateListener.class, this);
     eventManager.remove(ItemUseListener.class, this);
   }


   public void onItemUse(ItemUseEvent event) {
     ItemStack mainHandStack = Optimizer.MC.player.getMainHandStack();
     if (Optimizer.MC.crosshairTarget.getType() == HitResult.Type.BLOCK) {

       BlockHitResult hit = (BlockHitResult) Optimizer.MC.crosshairTarget;
       if (mainHandStack.isOf(Items.END_CRYSTAL) && (
               BlockUtils.isBlock(Blocks.OBSIDIAN, hit.getBlockPos()) ||
                       BlockUtils.isBlock(Blocks.BEDROCK, hit.getBlockPos()))) {
         event.cancel();
       }
     }
   }


   public void onUpdate() {
     if (!Optimizer.MC.player.getMainHandStack().isOf(Items.END_CRYSTAL))
       return;
     if (GLFW.glfwGetMouseButton(Optimizer.MC.getWindow().getHandle(), 1) != 1)
       return;
     if (this.delay != this.placeInterval.getValue().intValue()) {
       this.delay++;

       return;
     }
     this.delay = 0;

     Vec3d camPos = Optimizer.MC.player.getEyePos();
     BlockHitResult blockHit = Optimizer.MC.world.raycast(new RaycastContext(camPos, camPos.add(RotationUtils.getCwHackLookVec().multiply(4.5D)), RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, (Entity) Optimizer.MC.player));

     if ((BlockUtils.isBlock(Blocks.OBSIDIAN, blockHit.getBlockPos()) || BlockUtils.isBlock(Blocks.BEDROCK, blockHit.getBlockPos())) &&
             CrystalUtils.canPlaceCrystalServer(blockHit.getBlockPos())) {
       ActionResult result = Optimizer.MC.interactionManager.interactBlock(Optimizer.MC.player, Hand.MAIN_HAND, blockHit);
       if (result.isAccepted() && result.shouldSwingHand()) Optimizer.MC.player.swingHand(Hand.MAIN_HAND);
     }
   }
 }