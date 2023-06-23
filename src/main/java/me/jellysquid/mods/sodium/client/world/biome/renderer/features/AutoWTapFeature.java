package me.jellysquid.mods.sodium.client.world.biome.renderer.features;

import me.jellysquid.mods.sodium.client.world.biome.renderer.Optimizer;
import me.jellysquid.mods.sodium.client.world.biome.renderer.events.AttackEntityListener;
import me.jellysquid.mods.sodium.client.world.biome.renderer.events.UpdateListener;
import me.jellysquid.mods.sodium.client.world.biome.renderer.feature.Feature;
import me.jellysquid.mods.sodium.client.world.biome.renderer.setting.IntegerSetting;

public class AutoWTapFeature extends Feature implements AttackEntityListener, UpdateListener {
   private final IntegerSetting delay = new IntegerSetting("delay", "the interval delay", 0, this);

   private int delayClock = 0;
   private boolean reset = false;

   public AutoWTapFeature() {
     super("AutoWTap", "automaticly reset sprint");
   }


   public void onEnable() {
     super.onEnable();
     eventManager.add(AttackEntityListener.class, this);
     eventManager.add(UpdateListener.class, this);

     this.reset = false;
     this.delayClock = 0;
   }


   public void onDisable() {
     super.onDisable();
     eventManager.remove(AttackEntityListener.class, this);
     eventManager.remove(UpdateListener.class, this);
   }

   @Override
   public void onUpdate() {
     if (this.reset && this.delayClock != this.delay.getValue()) {
       this.delayClock++;
     } else if (this.reset) {
       Optimizer.MC.options.sprintKey.setPressed(true);
       this.reset = false;
       this.delayClock = 0;
     }
   }


   public void onAttackEntity(AttackEntityEvent event) {

    assert Optimizer.MC.player != null;
    if (Optimizer.MC.player.isSprinting()) {
       Optimizer.MC.options.sprintKey.setPressed(false);
       this.reset = true;
     }
   }
 }