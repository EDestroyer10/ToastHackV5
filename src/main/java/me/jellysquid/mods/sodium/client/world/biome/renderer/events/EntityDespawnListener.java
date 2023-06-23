package me.jellysquid.mods.sodium.client.world.biome.renderer.events;

import net.minecraft.entity.Entity;
import me.jellysquid.mods.sodium.client.world.biome.renderer.event.Event;
import me.jellysquid.mods.sodium.client.world.biome.renderer.event.Listener;

import java.util.ArrayList;

public interface EntityDespawnListener extends Listener
{
	void onEntityDespawn(Entity entity);

	class EntityDespawnEvent extends Event<EntityDespawnListener>
	{

		private Entity entity;

		public EntityDespawnEvent(Entity entity)
		{
			this.entity = entity;
		}

		@Override
		public void fire(ArrayList<EntityDespawnListener> listeners)
		{
			for (EntityDespawnListener listener : listeners)
			{
				listener.onEntityDespawn(entity);
			}
		}

		@Override
		public Class<EntityDespawnListener> getListenerType()
		{
			return EntityDespawnListener.class;
		}
	}
}
