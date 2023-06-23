package me.jellysquid.mods.sodium.client.world.biome.renderer.events;

import me.jellysquid.mods.sodium.client.world.biome.renderer.event.Event;
import me.jellysquid.mods.sodium.client.world.biome.renderer.event.Listener;

import java.util.ArrayList;

public interface PreActionListener extends Listener
{
	void onPreAction();

	class PreActionEvent extends Event<PreActionListener>
	{

		@Override
		public void fire(ArrayList<PreActionListener> listeners)
		{
			for (PreActionListener listener : listeners)
			{
				listener.onPreAction();
			}
		}

		@Override
		public Class<PreActionListener> getListenerType()
		{
			return PreActionListener.class;
		}
	}
}
