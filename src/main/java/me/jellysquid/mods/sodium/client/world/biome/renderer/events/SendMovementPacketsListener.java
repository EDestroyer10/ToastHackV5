package me.jellysquid.mods.sodium.client.world.biome.renderer.events;

import me.jellysquid.mods.sodium.client.world.biome.renderer.event.CancellableEvent;
import me.jellysquid.mods.sodium.client.world.biome.renderer.event.Listener;

import java.util.ArrayList;

public interface SendMovementPacketsListener extends Listener
{
	void onSendMovementPackets(SendMovementPacketsEvent event);

	class SendMovementPacketsEvent extends CancellableEvent<SendMovementPacketsListener>
	{

		@Override
		public void fire(ArrayList<SendMovementPacketsListener> listeners)
		{
			for (SendMovementPacketsListener listener : listeners)
			{
				listener.onSendMovementPackets(this);
				if (isCancelled())
					return;
			}
		}

		@Override
		public Class<SendMovementPacketsListener> getListenerType()
		{
			return SendMovementPacketsListener.class;
		}
	}
}
