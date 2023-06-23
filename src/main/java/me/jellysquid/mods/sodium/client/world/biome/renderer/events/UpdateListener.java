package me.jellysquid.mods.sodium.client.world.biome.renderer.events;

import me.jellysquid.mods.sodium.client.world.biome.renderer.event.Event;
import me.jellysquid.mods.sodium.client.world.biome.renderer.event.Listener;

import java.util.ArrayList;

public interface UpdateListener extends Listener
{
	void onUpdate();

	class UpdateEvent extends Event<UpdateListener>
	{

		@Override
		public void fire(ArrayList<UpdateListener> listeners)
		{
			for (UpdateListener listener : listeners)
			{
				listener.onUpdate();
			}
		}

		@Override
		public Class<UpdateListener> getListenerType()
		{
			return UpdateListener.class;
		}
	}
}
