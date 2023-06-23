package me.jellysquid.mods.sodium.client.world.biome.renderer.events;

import me.jellysquid.mods.sodium.client.world.biome.renderer.event.Event;
import me.jellysquid.mods.sodium.client.world.biome.renderer.event.Listener;

import java.util.ArrayList;

public interface PostUpdateListener extends Listener
{
	void onPostUpdate();

	class PostUpdateEvent extends Event<PostUpdateListener>
	{

		@Override
		public void fire(ArrayList<PostUpdateListener> listeners)
		{
			for (PostUpdateListener listener : listeners)
			{
				listener.onPostUpdate();
			}
		}

		@Override
		public Class<PostUpdateListener> getListenerType()
		{
			return PostUpdateListener.class;
		}
	}
}
