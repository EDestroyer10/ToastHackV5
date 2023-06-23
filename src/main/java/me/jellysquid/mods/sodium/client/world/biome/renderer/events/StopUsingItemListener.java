package me.jellysquid.mods.sodium.client.world.biome.renderer.events;

import me.jellysquid.mods.sodium.client.world.biome.renderer.event.CancellableEvent;
import me.jellysquid.mods.sodium.client.world.biome.renderer.event.Listener;

import java.util.ArrayList;

public interface StopUsingItemListener extends Listener
{
	void onStopUsingItem(StopUsingItemEvent event);

	class StopUsingItemEvent extends CancellableEvent<StopUsingItemListener>
	{

		@Override
		public void fire(ArrayList<StopUsingItemListener> listeners)
		{
			for (StopUsingItemListener listener : listeners)
				listener.onStopUsingItem(this);
		}

		@Override
		public Class<StopUsingItemListener> getListenerType()
		{
			return StopUsingItemListener.class;
		}
	}
}
