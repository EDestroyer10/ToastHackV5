package me.jellysquid.mods.sodium.client.world.biome.renderer.events;

import me.jellysquid.mods.sodium.client.world.biome.renderer.event.Event;
import me.jellysquid.mods.sodium.client.world.biome.renderer.event.Listener;

import java.util.ArrayList;

public interface GameLeaveListener extends Listener
{
	void onGameLeave();

	class GameLeaveEvent extends Event<GameLeaveListener>
	{

		@Override
		public void fire(ArrayList<GameLeaveListener> listeners)
		{
			for (GameLeaveListener listener : listeners)
			{
				listener.onGameLeave();
			}
		}

		@Override
		public Class<GameLeaveListener> getListenerType()
		{
			return GameLeaveListener.class;
		}
	}
}
