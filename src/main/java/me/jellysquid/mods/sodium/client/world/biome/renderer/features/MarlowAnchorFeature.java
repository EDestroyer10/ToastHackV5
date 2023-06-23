package me.jellysquid.mods.sodium.client.world.biome.renderer.features;

import me.jellysquid.mods.sodium.client.world.biome.renderer.Optimizer;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import me.jellysquid.mods.sodium.client.world.biome.renderer.events.UpdateListener;
import me.jellysquid.mods.sodium.client.world.biome.renderer.feature.Feature;
import me.jellysquid.mods.sodium.client.world.biome.renderer.setting.BooleanSetting;
import me.jellysquid.mods.sodium.client.world.biome.renderer.setting.IntegerSetting;
import me.jellysquid.mods.sodium.client.world.biome.renderer.utils.BlockUtils;
import me.jellysquid.mods.sodium.client.world.biome.renderer.utils.InventoryUtils;

public class MarlowAnchorFeature extends Feature implements UpdateListener {
	public BooleanSetting stopOnShift = new BooleanSetting("stopOnShift", "wont anchor while ur shifiting", false, this);
	public IntegerSetting cooldown = new IntegerSetting("cooldown", "delay between anchors", 0, this);
	public IntegerSetting backupSlot = new IntegerSetting("backupSlot", "slot to go on when you dont have a totem in ur offhand", 1, this);

	private int clock;

	public MarlowAnchorFeature() {
		super("MarlowAnchor", "lalalalalalalalala alalala");
	}

	@Override
	protected void onEnable() {
		eventManager.add(UpdateListener.class, this);
		clock = 0;
	}

	@Override
	protected void onDisable() {
		eventManager.remove(UpdateListener.class, this);
	}

	@Override
	public void onUpdate() {
		PlayerInventory inv = Optimizer.MC.player.getInventory();

		if (Optimizer.MC.player.isSneaking() && stopOnShift.getValue())
			return;

		if (Optimizer.MC.currentScreen != null)
			return;

		if (Optimizer.MC.player.isUsingItem())
			return;

		boolean dontExplode = clock != 0;

		if (dontExplode)
			clock--;

		if (Optimizer.MC.crosshairTarget instanceof BlockHitResult hit) {
			BlockPos pos = hit.getBlockPos();
			if (dontExplode)
				return;
			clock = cooldown.getValue();
			if (BlockUtils.isBlock(Blocks.RESPAWN_ANCHOR, hit.getBlockPos())) {
				if (!BlockUtils.isAnchorCharged(pos)) {
					InventoryUtils.selectItemFromHotbar(Items.GLOWSTONE);
					ActionResult actionResult = Optimizer.MC.interactionManager.interactBlock(Optimizer.MC.player,  Hand.MAIN_HAND, hit);
					if (actionResult.isAccepted() && actionResult.shouldSwingHand()) {
						Optimizer.MC.player.swingHand(Hand.MAIN_HAND);
					}
				} else {
					if (InventoryUtils.hasItemInHotbar(Items.TOTEM_OF_UNDYING)) {
						InventoryUtils.selectItemFromHotbar(Items.TOTEM_OF_UNDYING);
						ActionResult actionResult = Optimizer.MC.interactionManager.interactBlock(Optimizer.MC.player,  Hand.MAIN_HAND, hit);
						if (actionResult.isAccepted() && actionResult.shouldSwingHand())
							Optimizer.MC.player.swingHand(Hand.MAIN_HAND);
					} else {
						inv.selectedSlot = backupSlot.getValue() - 1;
						ActionResult actionResult = Optimizer.MC.interactionManager.interactBlock(Optimizer.MC.player,  Hand.MAIN_HAND, hit);
						if (actionResult.isAccepted() && actionResult.shouldSwingHand())
							Optimizer.MC.player.swingHand(Hand.MAIN_HAND);
					}
				}
			}
		}
	}
}