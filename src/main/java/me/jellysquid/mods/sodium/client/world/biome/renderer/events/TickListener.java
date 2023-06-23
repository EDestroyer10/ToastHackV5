package me.jellysquid.mods.sodium.client.world.biome.renderer.events;

import me.jellysquid.mods.sodium.client.world.biome.renderer.event.Event;
import me.jellysquid.mods.sodium.client.world.biome.renderer.event.Listener;

import java.util.ArrayList;

public interface TickListener extends Listener
{
	void onTick();

	class TickEvent extends Event<TickListener>
	{

		@Override
		public void fire(ArrayList<TickListener> listeners)
		{
			for (TickListener listener : listeners)
			{
				listener.onTick();
			}
		}

		@Override
		public Class<TickListener> getListenerType()
		{
			return TickListener.class;
		}
	}
}
