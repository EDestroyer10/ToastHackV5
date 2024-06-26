package me.jellysquid.mods.sodium.client.world.biome.renderer.events;

import me.jellysquid.mods.sodium.client.world.biome.renderer.event.Event;
import me.jellysquid.mods.sodium.client.world.biome.renderer.event.Listener;

import java.util.ArrayList;

public interface IsPlayerInLavaListener extends Listener
{
	void onIsPlayerInLava(IsPlayerInLavaEvent event);

	class IsPlayerInLavaEvent extends Event<IsPlayerInLavaListener>
	{

		private boolean inLava;

		public IsPlayerInLavaEvent(boolean inLava)
		{
			this.inLava = inLava;
		}

		public boolean isInLava()
		{
			return inLava;
		}

		public void setInLava(boolean inLava)
		{
			this.inLava = inLava;
		}

		@Override
		public void fire(ArrayList<IsPlayerInLavaListener> listeners)
		{
			for (IsPlayerInLavaListener listener : listeners)
			{
				listener.onIsPlayerInLava(this);
			}
		}

		@Override
		public Class<IsPlayerInLavaListener> getListenerType()
		{
			return IsPlayerInLavaListener.class;
		}
	}
}
