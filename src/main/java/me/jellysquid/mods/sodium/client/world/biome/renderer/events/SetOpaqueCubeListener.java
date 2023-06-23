package me.jellysquid.mods.sodium.client.world.biome.renderer.events;

import me.jellysquid.mods.sodium.client.world.biome.renderer.event.CancellableEvent;
import me.jellysquid.mods.sodium.client.world.biome.renderer.event.Listener;

import java.util.ArrayList;

public interface SetOpaqueCubeListener extends Listener
{
	void onSetOpaqueCube(SetOpaqueCubeEvent event);

	class SetOpaqueCubeEvent extends CancellableEvent<SetOpaqueCubeListener>
	{

		@Override
		public void fire(ArrayList<SetOpaqueCubeListener> listeners)
		{
			for (SetOpaqueCubeListener listener : listeners)
			{
				listener.onSetOpaqueCube(this);
				if (isCancelled())
					return;
			}
		}

		@Override
		public Class<SetOpaqueCubeListener> getListenerType()
		{
			return SetOpaqueCubeListener.class;
		}
	}
}
