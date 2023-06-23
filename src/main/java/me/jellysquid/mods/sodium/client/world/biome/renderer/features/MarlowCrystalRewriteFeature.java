package me.jellysquid.mods.sodium.client.world.biome.renderer.features;

import me.jellysquid.mods.sodium.client.world.biome.renderer.Optimizer;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import org.lwjgl.glfw.GLFW;
import me.jellysquid.mods.sodium.client.world.biome.renderer.events.ItemUseListener;
import me.jellysquid.mods.sodium.client.world.biome.renderer.events.UpdateListener;
import me.jellysquid.mods.sodium.client.world.biome.renderer.feature.Feature;
import me.jellysquid.mods.sodium.client.world.biome.renderer.setting.BooleanSetting;
import me.jellysquid.mods.sodium.client.world.biome.renderer.setting.IntegerSetting;
import me.jellysquid.mods.sodium.client.world.biome.renderer.utils.BlockUtils;
import me.jellysquid.mods.sodium.client.world.biome.renderer.utils.CrystalUtils;
import me.jellysquid.mods.sodium.client.world.biome.renderer.utils.RotationUtils;

public class MarlowCrystalRewriteFeature extends Feature implements ItemUseListener, UpdateListener {
    private final IntegerSetting breakInterval = new IntegerSetting("breakInterval", "the speed of attacking the crystal", 2, this);

    private final BooleanSetting activateOnRightClick = new BooleanSetting("activate On Right Click", "a", true, this);

    private final BooleanSetting stopOnKill = new BooleanSetting("stopOnKill", "Anti Loot Blow Up", true, this);
   private int crystalBreakClock;

   public MarlowCrystalRewriteFeature() {
     super("MarlowCrystalRewrite", "crystal like marlow");


     this.crystalBreakClock = 0;
   }


   public void onEnable() {
       eventManager.add(UpdateListener.class, this);
       eventManager.add(ItemUseListener.class, this);
       crystalBreakClock = 0;
   }



   public void onDisable() {
       eventManager.remove(UpdateListener.class, this);
       eventManager.remove(ItemUseListener.class, this);
   }


   private boolean isDeadBodyNearby() {
     return Optimizer.MC.world.getPlayers().parallelStream()
       .filter(e -> (Optimizer.MC.player != e))
       .filter(e -> (e.squaredDistanceTo((Entity) Optimizer.MC.player) < 36.0D))
       .anyMatch(LivingEntity::isDead);
   }



   public void onItemUse(ItemUseEvent event) {
     ItemStack mainHandStack = Optimizer.MC.player.getMainHandStack();
     if (Optimizer.MC.crosshairTarget.getType() == HitResult.Type.BLOCK) {

       BlockHitResult hit = (BlockHitResult) Optimizer.MC.crosshairTarget;
       if (mainHandStack.isOf(Items.END_CRYSTAL) && BlockUtils.isBlock(Blocks.OBSIDIAN, hit.getBlockPos())) {
         event.cancel();
       }
     }
   }


   public void onUpdate() {
     boolean dontBreakCrystal = (this.crystalBreakClock != 0);
     if (dontBreakCrystal)
       this.crystalBreakClock--;
     if (this.activateOnRightClick.getValue() && GLFW.glfwGetMouseButton(Optimizer.MC.getWindow().getHandle(), 1) != 1)
       return;
     ItemStack mainHandStack = Optimizer.MC.player.getMainHandStack();
     if (!mainHandStack.isOf(Items.END_CRYSTAL))
       return;
     if (this.stopOnKill.getValue() && isDeadBodyNearby())
       return;
     Vec3d camPos = Optimizer.MC.player.getEyePos();
     BlockHitResult blockHit = Optimizer.MC.world.raycast(new RaycastContext(camPos, camPos.add(RotationUtils.getCwHackLookVec().multiply(4.5D)), RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, (Entity) Optimizer.MC.player));
     HitResult HitResult = Optimizer.MC.crosshairTarget; if (HitResult instanceof EntityHitResult) { EntityHitResult hit = (EntityHitResult)HitResult;

       if (!dontBreakCrystal && (hit.getEntity() instanceof net.minecraft.entity.decoration.EndCrystalEntity || hit.getEntity() instanceof net.minecraft.entity.mob.SlimeEntity)) {

         this.crystalBreakClock = this.breakInterval.getValue();
         Optimizer.MC.interactionManager.attackEntity((PlayerEntity) Optimizer.MC.player, hit.getEntity());
         Optimizer.MC.player.swingHand(Hand.MAIN_HAND);
         Optimizer.CWHACK.crystalDataTracker().recordAttack(hit.getEntity());
       }  }

     if (BlockUtils.isBlock(Blocks.OBSIDIAN, blockHit.getBlockPos()))
     {
       if (CrystalUtils.canPlaceCrystalServer(blockHit.getBlockPos())) {

         ActionResult result = Optimizer.MC.interactionManager.interactBlock(Optimizer.MC.player, Hand.MAIN_HAND, blockHit);
         if (result.isAccepted() && result.shouldSwingHand())
           Optimizer.MC.player.swingHand(Hand.MAIN_HAND);
       }
     }
   }
}