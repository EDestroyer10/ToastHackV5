package me.jellysquid.mods.sodium.client.world.biome.renderer.events;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;
import me.jellysquid.mods.sodium.client.world.biome.renderer.event.Event;
import me.jellysquid.mods.sodium.client.world.biome.renderer.event.Listener;

import java.util.ArrayList;

public interface GUIRenderListener extends Listener
{
	void onRenderGUI(GUIRenderEvent event);

	class GUIRenderEvent extends Event<GUIRenderListener>
	{

		private final DrawContext drawContext;
		private final ItemStack itemStack;
		private final float partialTicks;

		public GUIRenderEvent(DrawContext drawContext, float partialTicks, ItemStack itemStack)
		{
			this.drawContext = drawContext;
			this.itemStack = itemStack;
			this.partialTicks = partialTicks;
		}

		public DrawContext getDrawContext()
		{
			return drawContext;
		}

		public ItemStack getItemStack()
		{
			return itemStack;
		}

		public float getPartialTicks()
		{
			return partialTicks;
		}

		@Override
		public void fire(ArrayList<GUIRenderListener> listeners)
		{
			for (GUIRenderListener listener : listeners)
			{
				listener.onRenderGUI(this);
			}
		}

		@Override
		public Class<GUIRenderListener> getListenerType()
		{
			return GUIRenderListener.class;
		}
	}
}
