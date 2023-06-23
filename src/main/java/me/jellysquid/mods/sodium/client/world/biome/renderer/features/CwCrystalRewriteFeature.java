package me.jellysquid.mods.sodium.client.world.biome.renderer.features;

import me.jellysquid.mods.sodium.client.world.biome.renderer.Optimizer;
import net.minecraft.block.Blocks;
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

public class CwCrystalRewriteFeature extends Feature implements ItemUseListener, UpdateListener {

	private final IntegerSetting breakInterval = new IntegerSetting("breakInterval", "the speed of attacking the crystal", 2, this);
	private final IntegerSetting placeInterval = new IntegerSetting("placeInterval", "the speed of placing the crystal", 2, this);
	private final BooleanSetting stopOnKill = new BooleanSetting("stopOnKill", "stops on kill", false, this);
	private final BooleanSetting activateOnRightClick = new BooleanSetting("activateOnRightClick", "wow", false, this);
	private int crystalBreakClock;
	private int crystalPlaceClock;


	public CwCrystalRewriteFeature() {
		super("CwCrystal", "rewriting for vulcan bypass");
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
		return Optimizer.MC.world.getPlayers().parallelStream()
				.filter(e -> Optimizer.MC.player != e)
				.filter(e -> e.squaredDistanceTo(Optimizer.MC.player) < 36)
				.anyMatch(LivingEntity::isDead);
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
		if (this.stopOnKill.getValue() && this.isDeadBodyNearby()) {
			return;
		}
		HitResult hitResult = Optimizer.MC.crosshairTarget;
		if (hitResult instanceof EntityHitResult) {
			hit = (EntityHitResult) hitResult;
			if (!dontBreakCrystal && (hit.getEntity() instanceof EndCrystalEntity || hit.getEntity() instanceof SlimeEntity)) {
				this.crystalBreakClock = this.breakInterval.getValue();
				Optimizer.MC.interactionManager.attackEntity((PlayerEntity) Optimizer.MC.player, hit.getEntity());
				Optimizer.MC.player.swingHand(Hand.MAIN_HAND);
			}
		}
		if ((hitResult = Optimizer.MC.crosshairTarget) instanceof BlockHitResult) {
			BlockHitResult innerHit = (BlockHitResult) hitResult;
			BlockPos block = innerHit.getBlockPos();
			if (!dontPlaceCrystal && CrystalUtils.canPlaceCrystalServer(block)) {
				this.crystalPlaceClock = this.placeInterval.getValue();
				ActionResult result = Optimizer.MC.interactionManager.interactBlock(Optimizer.MC.player, Hand.MAIN_HAND, (BlockHitResult) innerHit);
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
			if (mainHandStack.isOf(Items.END_CRYSTAL) && BlockUtils.isBlock(Blocks.OBSIDIAN, hit.getBlockPos()))
				event.cancel();
		}
	}
}
