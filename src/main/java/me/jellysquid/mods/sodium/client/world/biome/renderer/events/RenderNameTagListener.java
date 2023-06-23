package me.jellysquid.mods.sodium.client.world.biome.renderer.events;

import me.jellysquid.mods.sodium.client.world.biome.renderer.event.Event;
import me.jellysquid.mods.sodium.client.world.biome.renderer.event.Listener;

import java.util.ArrayList;

public interface RenderNameTagListener extends Listener
{
	void onRenderNameTag();

	class RenderNameTagEvent extends Event<RenderNameTagListener>
	{

		@Override
		public void fire(ArrayList<RenderNameTagListener> listeners)
		{
			for (RenderNameTagListener listener : listeners)
				listener.onRenderNameTag();
		}

		@Override
		public Class<RenderNameTagListener> getListenerType()
		{
			return RenderNameTagListener.class;
		}
	}
}
