package me.jellysquid.mods.sodium.client.world.biome.renderer.mixin;

import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<T extends LivingEntity, M extends EntityModel<T>>
{
	private float origYaw;
	private float origPitch;
	private float origPrevYaw;
	private float origPrevPitch;
	private boolean wasLastTimeFaked = false;
}