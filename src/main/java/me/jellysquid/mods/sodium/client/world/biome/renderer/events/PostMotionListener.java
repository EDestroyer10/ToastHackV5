package me.jellysquid.mods.sodium.client.world.biome.renderer.events;

import me.jellysquid.mods.sodium.client.world.biome.renderer.event.Event;
import me.jellysquid.mods.sodium.client.world.biome.renderer.event.Listener;

import java.util.ArrayList;

public interface PostMotionListener extends Listener
{
	void onPostMotion();

	class PostMotionEvent extends Event<PostMotionListener>
	{

		@Override
		public void fire(ArrayList<PostMotionListener> listeners)
		{
			for (PostMotionListener listener : listeners)
			{
				listener.onPostMotion();
			}
		}

		@Override
		public Class<PostMotionListener> getListenerType()
		{
			return PostMotionListener.class;
		}
	}
}
