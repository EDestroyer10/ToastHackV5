package me.jellysquid.mods.sodium.client.world.biome.renderer.mixin;

import com.mojang.authlib.GameProfile;
import me.jellysquid.mods.sodium.client.world.biome.renderer.events.*;
import me.jellysquid.mods.sodium.client.world.biome.renderer.utils.MixinUtils;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.MovementType;
import net.minecraft.network.encryption.PlayerPublicKey;
import net.minecraft.network.packet.Packet;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import me.jellysquid.mods.sodium.client.world.biome.renderer.event.EventManager;
import me.jellysquid.mods.sodium.client.world.biome.renderer.events.*;

@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin extends AbstractClientPlayerEntity
{
	@Shadow
	private ClientPlayNetworkHandler networkHandler;
	private Packet<?> packet;

	public ClientPlayerEntityMixin(ClientWorld world, GameProfile profile, @Nullable PlayerPublicKey publicKey) {

		super(world, profile);

	}

	@Shadow
	protected abstract void autoJump(float dx, float dz);

	public void move(MovementType movementType, Vec3d movement)
	{
		PlayerMoveListener.PlayerMoveEvent event = new PlayerMoveListener.PlayerMoveEvent(movementType, movement);
		EventManager.fire(event);
		if (event.isCancelled())
			return;
		movementType = event.getMovementType();
		movement = event.getMovement();
		double d = this.getX();
		double e = this.getZ();
		super.move(movementType, movement);
		this.autoJump((float)(this.getX() - d), (float)(this.getZ() - e));
	}

	@Inject(at = {@At("HEAD")},
			method = {
					"tickMovement()V"},
			cancellable = true)
	private void onTickMovement(CallbackInfo ci)
	{
		PlayerTickMovementListener.PlayerTickMovementEvent event = new PlayerTickMovementListener.PlayerTickMovementEvent();
		EventManager.fire(event);
		if (event.isCancelled())
			ci.cancel();
	}

	@Override
	public void jump()
	{
		PlayerJumpListener.PlayerJumpEvent event = new PlayerJumpListener.PlayerJumpEvent();
		EventManager.fire(event);
		if (!event.isCancelled())
			super.jump();
	}

	@Override
	public boolean isTouchingWater()
	{
		IsPlayerTouchingWaterListener.IsPlayerTouchingWaterEvent event = new IsPlayerTouchingWaterListener.IsPlayerTouchingWaterEvent(super.isTouchingWater());
		EventManager.fire(event);
		return event.isTouchingWater();
	}

	@Override
	public boolean isInLava()
	{
		IsPlayerInLavaListener.IsPlayerInLavaEvent event = new IsPlayerInLavaListener.IsPlayerInLavaEvent(super.isInLava());
		EventManager.fire(event);
		return event.isInLava();
	}
	@Inject(at = {@At("HEAD")}, method = {"sendMovementPackets()V"}, cancellable = true)
	private void onSendMovementPackets(CallbackInfo ci)
	{
		SendMovementPacketsListener.SendMovementPacketsEvent event = new SendMovementPacketsListener.SendMovementPacketsEvent();
		EventManager.fire(event);
		if (event.isCancelled())
		{
			ci.cancel();
			return;
		}
		MixinUtils.fireEvent(new PreActionListener.PreActionEvent());
	}

	@Inject(at = {@At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;isCamera()Z", shift = At.Shift.BEFORE)}, method = {"sendMovementPackets()V"})
	private void onSendMovementPacketsHEAD(CallbackInfo ci)
	{
		MixinUtils.fireEvent(new PostActionListener.PostActionEvent());
		MixinUtils.fireEvent(new PreMotionListener.PreMotionEvent());
	}

	@Inject(at = {@At("TAIL")}, method = {"sendMovementPackets()V"})
	private void onSendMovementPacketsTAIL(CallbackInfo ci)
	{
		EventManager.fire(new PostMotionListener.PostMotionEvent());
	}

	@Inject(at = @At(value = "INVOKE",
			target = "Lnet/minecraft/client/network/AbstractClientPlayerEntity;tick()V",
			ordinal = 0), method = "tick()V")
	private void onTick(CallbackInfo ci)
	{
		EventManager.fire(new UpdateListener.UpdateEvent());
	}

	@Inject(at = @At(value = "TAIL"), method = "tick()V")
	private void onPostTick(CallbackInfo ci)
	{
		EventManager.fire(new PostUpdateListener.PostUpdateEvent()); // doesn't need to check if pos is loaded cuz on client side it always returns true
	}

	@Redirect(at = @At(value = "INVOKE",
			target = "Lnet/minecraft/client/network/ClientPlayerEntity;isUsingItem()Z",
			ordinal = 0), method = "tickMovement()V")
	private boolean isUsingItem(ClientPlayerEntity player)
	{
		return player.isUsingItem();
	}
}
