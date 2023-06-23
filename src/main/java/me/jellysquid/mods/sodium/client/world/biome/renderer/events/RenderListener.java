package me.jellysquid.mods.sodium.client.world.biome.renderer.events;

import net.minecraft.client.util.math.MatrixStack;
import me.jellysquid.mods.sodium.client.world.biome.renderer.event.Event;
import me.jellysquid.mods.sodium.client.world.biome.renderer.event.Listener;

import java.util.ArrayList;

public interface RenderListener extends Listener
{
	void onRender(RenderEvent event);

	class RenderEvent extends Event<RenderListener>
	{

		private final MatrixStack matrixStack;
		private final float partialTicks;

		public RenderEvent(MatrixStack matrixStack, float partialTicks)
		{
			this.matrixStack = matrixStack;
			this.partialTicks = partialTicks;
		}

		public MatrixStack getMatrixStack()
		{
			return matrixStack;
		}

		public float getPartialTicks()
		{
			return partialTicks;
		}

		@Override
		public void fire(ArrayList<RenderListener> listeners)
		{
			for (RenderListener listener : listeners)
			{
				listener.onRender(this);
			}
		}

		@Override
		public Class<RenderListener> getListenerType()
		{
			return RenderListener.class ;
		}
	}
}
