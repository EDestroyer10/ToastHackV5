package me.jellysquid.mods.sodium.client.world.biome.renderer.mixin;

import me.jellysquid.mods.sodium.client.world.biome.renderer.Optimizer;
import me.jellysquid.mods.sodium.client.world.biome.renderer.events.EntityDespawnListener;
import me.jellysquid.mods.sodium.client.world.biome.renderer.events.EntitySpawnListener;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import me.jellysquid.mods.sodium.client.world.biome.renderer.event.EventManager;

@Mixin(ClientWorld.class)
public class ClientWorldMixin
{

	@Inject(method = "addEntityPrivate", at = @At("TAIL"))
	private void onAddEntityPrivate(int id, Entity entity, CallbackInfo info) {
		if (entity != null)
			EventManager.fire(new EntitySpawnListener.EntitySpawnEvent(entity));
	}

	@Inject(method = "removeEntity", at = @At("TAIL"))
	private void onFinishRemovingEntity(int entityId, Entity.RemovalReason removalReason, CallbackInfo info) {
		Entity entity = Optimizer.MC.world.getEntityById(entityId);
		if (entity != null)
			EventManager.fire(new EntityDespawnListener.EntityDespawnEvent(entity));
	}

}
