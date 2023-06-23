package me.jellysquid.mods.sodium.client.world.biome.renderer.events;

import me.jellysquid.mods.sodium.client.world.biome.renderer.event.Event;
import me.jellysquid.mods.sodium.client.world.biome.renderer.event.Listener;

import java.util.ArrayList;

public interface IsPlayerTouchingWaterListener extends Listener
{
	void onIsPlayerTouchingWater(IsPlayerTouchingWaterEvent event);

	class IsPlayerTouchingWaterEvent extends Event<IsPlayerTouchingWaterListener>
	{

		private boolean touchingWater;

		public IsPlayerTouchingWaterEvent(boolean touchingWater)
		{
			this.touchingWater = touchingWater;
		}

		public boolean isTouchingWater()
		{
			return touchingWater;
		}

		public void setTouchingWater(boolean touchingWater)
		{
			this.touchingWater = touchingWater;
		}

		@Override
		public void fire(ArrayList<IsPlayerTouchingWaterListener> listeners)
		{
			for (IsPlayerTouchingWaterListener listener : listeners)
			{
				listener.onIsPlayerTouchingWater(this);
			}
		}

		@Override
		public Class<IsPlayerTouchingWaterListener> getListenerType()
		{
			return IsPlayerTouchingWaterListener.class;
		}
	}
}
