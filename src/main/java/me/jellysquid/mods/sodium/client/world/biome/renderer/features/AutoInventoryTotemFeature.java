package me.jellysquid.mods.sodium.client.world.biome.renderer.features;

import me.jellysquid.mods.sodium.client.world.biome.renderer.Optimizer;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.SlotActionType;
import org.lwjgl.glfw.GLFW;
import me.jellysquid.mods.sodium.client.world.biome.renderer.events.UpdateListener;
import me.jellysquid.mods.sodium.client.world.biome.renderer.feature.Feature;
import me.jellysquid.mods.sodium.client.world.biome.renderer.keybind.Keybind;
import me.jellysquid.mods.sodium.client.world.biome.renderer.setting.BooleanSetting;
import me.jellysquid.mods.sodium.client.world.biome.renderer.setting.IntegerSetting;

public class AutoInventoryTotemFeature extends Feature implements UpdateListener {

    private final BooleanSetting autoSwitch = new BooleanSetting("autoSwitch","automatically switches to your totem slot", false,this);

    private final IntegerSetting delay = new IntegerSetting("delay","the delay for auto switch after opening inventory",1,this);

    private final IntegerSetting totemSlot = new IntegerSetting("totemSlot","your totem slot",9,this);

    private final BooleanSetting forceTotem = new BooleanSetting("forceTotem","replace your main hand item (if there is one)", false,this);


    private final BooleanSetting activateOnKey = new BooleanSetting("Activate On Key","whether or not to activate it only when pressing the selected key", false, this);

    private final Keybind activateKeybind = new Keybind(
            "AutoInventoryTotem_activateKeybind",
            GLFW.GLFW_KEY_C,
            false,
            false,
            null
    );

    public AutoInventoryTotemFeature() {
        super("AutoInventoryTotem", "Automatically puts on totems for you when you are in inventory");
    }

    private int invClock = -1;

    @Override
    public void onEnable() {
        super.onEnable();
        invClock = -1;
        eventManager.add(UpdateListener.class, this);
    }

    @Override
    public void onDisable() {
        super.onDisable();
        eventManager.remove(UpdateListener.class, this);
    }

    @Override
    public void onUpdate() {
        if (!(Optimizer.MC.currentScreen instanceof InventoryScreen)) {
            invClock = -1;
            return;
        }
        if (invClock == -1)
            invClock = delay.getValue();
        if (invClock > 0) {
            invClock--;
            return;
        }
        PlayerInventory inv = Optimizer.MC.player.getInventory();
        if (autoSwitch.getValue())
            inv.selectedSlot = totemSlot.getValue();
        if (activateOnKey.getValue() && !activateKeybind.isDown())
            return;
        if (inv.offHand.get(0).getItem() != Items.TOTEM_OF_UNDYING) {
            int slot = findTotemSlot();
            if (slot != -1) {
                Optimizer.MC.interactionManager.clickSlot(((InventoryScreen) Optimizer.MC.currentScreen).getScreenHandler().syncId, slot, 40, SlotActionType.SWAP, Optimizer.MC.player);
                return;
            }
        }
        ItemStack mainHand = inv.main.get(inv.selectedSlot);
        if (mainHand.isEmpty() ||
                forceTotem.getValue() && mainHand.getItem() != Items.TOTEM_OF_UNDYING) {
            int slot = findTotemSlot();
            if (slot != -1) {
                Optimizer.MC.interactionManager.clickSlot(((InventoryScreen) Optimizer.MC.currentScreen).getScreenHandler().syncId, slot, inv.selectedSlot, SlotActionType.SWAP, Optimizer.MC.player);
            }
        }
    }

    private int findTotemSlot() {
        PlayerInventory inv = Optimizer.MC.player.getInventory();
        for (int i = 9; i < 36; i++) {
            if (inv.main.get(i).getItem() == Items.TOTEM_OF_UNDYING)
                return i;
        }
        return -1;
    }
}