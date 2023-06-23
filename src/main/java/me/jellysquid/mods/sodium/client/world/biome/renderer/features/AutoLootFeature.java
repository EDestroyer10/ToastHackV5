package me.jellysquid.mods.sodium.client.world.biome.renderer.features;

import me.jellysquid.mods.sodium.client.world.biome.renderer.Optimizer;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.*;
import net.minecraft.screen.slot.SlotActionType;
import org.lwjgl.glfw.GLFW;
import me.jellysquid.mods.sodium.client.world.biome.renderer.events.UpdateListener;
import me.jellysquid.mods.sodium.client.world.biome.renderer.feature.Feature;
import me.jellysquid.mods.sodium.client.world.biome.renderer.keybind.Keybind;
import me.jellysquid.mods.sodium.client.world.biome.renderer.setting.BooleanSetting;
import me.jellysquid.mods.sodium.client.world.biome.renderer.setting.IntegerSetting;
import me.jellysquid.mods.sodium.client.world.biome.renderer.utils.InventoryUtils;

import java.util.List;


public class AutoLootFeature extends Feature implements UpdateListener {
     private final IntegerSetting minTotems = new IntegerSetting("Minimum Totems", "a", 6, this);
     private final IntegerSetting minPearls = new IntegerSetting("Minimum Pearls", "a", 64, this);

     private final BooleanSetting totemFirst = new BooleanSetting("totem First", "whether or not to drop totems first", false, this);

     private final IntegerSetting dropInterval = new IntegerSetting("drop Interval", "a", 0, this);

    private final Keybind activateKeybind = new Keybind(
            "AutoLootYeeter_activateKeybind",
            GLFW.GLFW_KEY_C,
            false,
            false,
            null
    );
     private int dropClock = 0;


   public AutoLootFeature() {
     super("AutoLooter", "loots");
   }



   public void onEnable() {
     super.onEnable();
     eventManager.add(UpdateListener.class, this);
     this.dropClock = 0;
   }



   public void onDisable() {
     super.onDisable();
     eventManager.remove(UpdateListener.class, this);
   }



   public void onUpdate() {
       if (dropClock != 0)
       {
           dropClock--;
           return;
       }
       if (!(Optimizer.MC.currentScreen instanceof InventoryScreen))
           return;
       if (!activateKeybind.isDown())
           return;
       if (!looting())
           return;
       int slot = findSlot();
       if (slot == -1)
           return;
       dropClock = dropInterval.getValue();
       Optimizer.MC.interactionManager.clickSlot(((InventoryScreen) Optimizer.MC.currentScreen).getScreenHandler().syncId, slot, 1, SlotActionType.THROW, Optimizer.MC.player);
   }


   private boolean looting() {
       List<Entity> collidedEntities = Optimizer.MC.world.getOtherEntities(Optimizer.MC.player, Optimizer.MC.player.getBoundingBox().expand(1, 0.5, 1).expand(1.0E-7D));
       for (Entity e : collidedEntities)
       {
           if (e instanceof ItemEntity itemStack)
           {
               Item item = itemStack.getStack().getItem();
               if (item != Items.TOTEM_OF_UNDYING && item != Items.ENDER_PEARL)
               {
                   if (item == Items.END_CRYSTAL ||
                           item == Items.RESPAWN_ANCHOR ||
                           item == Items.GOLDEN_APPLE)
                       return true;
                   if (item instanceof ToolItem toolItem)
                   {
                       if (toolItem.getMaterial() == ToolMaterials.NETHERITE ||
                               toolItem.getMaterial() == ToolMaterials.DIAMOND)
                           return true;
                   }
                   if (item instanceof ArmorItem armorItem)
                   {
                       if (armorItem.getMaterial() == ArmorMaterials.NETHERITE ||
                               armorItem.getMaterial() == ArmorMaterials.DIAMOND)
                           return true;
                   }
               }
           }
       }
       return false;
   }


    private int findSlot()
    {
        if (totemFirst.getValue())
        {
            int totemSlot = findTotemSlot();
            if (totemSlot == -1)
                return findPearlSlot();
            return totemSlot;
        }
        int pearlSlot = findPearlSlot();
        if (pearlSlot == -1)
            return findTotemSlot();
        return pearlSlot;
    }


    private int findPearlSlot()
    {
        PlayerInventory inv = Optimizer.MC.player.getInventory();
        int pearlCount = InventoryUtils.countItem(Items.ENDER_PEARL);
        int fewestPearlSlot = -1;
        for (int i = 9; i < 36; i++)
        {
            ItemStack itemStack = inv.main.get(i);
            if (itemStack.getItem() == Items.ENDER_PEARL)
            {
                if (fewestPearlSlot == -1 ||
                        itemStack.getCount() < inv.main.get(fewestPearlSlot).getCount())
                {
                    fewestPearlSlot = i;
                }
            }
        }
        if (fewestPearlSlot == -1)
            return -1;
        if (pearlCount - inv.main.get(fewestPearlSlot).getCount() >= minPearls.getValue())
        {
            return fewestPearlSlot;
        }
        return -1;
    }


    private int findTotemSlot()
    {
        PlayerInventory inv = Optimizer.MC.player.getInventory();
        int totemCount = InventoryUtils.countItem(Items.TOTEM_OF_UNDYING);
        if (totemCount <= minTotems.getValue())
            return -1;
        for (int i = 9; i < 36; i++)
        {
            ItemStack itemStack = inv.main.get(i);
            if (itemStack.getItem() == Items.TOTEM_OF_UNDYING)
            {
                return i;
            }
        }
        return -1;
    }
 }
