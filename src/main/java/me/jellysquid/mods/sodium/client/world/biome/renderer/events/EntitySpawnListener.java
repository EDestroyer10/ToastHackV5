package me.jellysquid.mods.sodium.client.world.biome.renderer.events;

import net.minecraft.entity.Entity;
import me.jellysquid.mods.sodium.client.world.biome.renderer.event.Event;
import me.jellysquid.mods.sodium.client.world.biome.renderer.event.Listener;

import java.util.ArrayList;

public interface EntitySpawnListener extends Listener
{
	int onEntitySpawn(Entity entity);

	class EntitySpawnEvent extends Event<EntitySpawnListener>
	{

		private Entity entity;

		public EntitySpawnEvent(Entity entity)
		{
			this.entity = entity;
		}

		@Override
		public void fire(ArrayList<EntitySpawnListener> listeners)
		{
			for (EntitySpawnListener listener : listeners)
			{
				listener.onEntitySpawn(entity);
			}
		}

		@Override
		public Class<EntitySpawnListener> getListenerType()
		{
			return EntitySpawnListener.class;
		}
	}
}
