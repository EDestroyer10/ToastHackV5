package me.jellysquid.mods.sodium.client.world.biome.renderer.events;

import net.minecraft.network.packet.Packet;
import me.jellysquid.mods.sodium.client.world.biome.renderer.event.CancellableEvent;
import me.jellysquid.mods.sodium.client.world.biome.renderer.event.Listener;

import java.util.ArrayList;

public interface PacketOutputListener extends Listener
{
	void onSendPacket(PacketOutputEvent event);

	class PacketOutputEvent extends CancellableEvent<PacketOutputListener>
	{
		private Packet<?> packet;

		public PacketOutputEvent(Packet<?> packet)
		{
			this.packet = packet;
		}

		public Packet<?> getPacket()
		{
			return packet;
		}

		@Override
		public void fire(ArrayList<PacketOutputListener> listeners)
		{
			for (PacketOutputListener listener : listeners)
			{
				listener.onSendPacket(this);
				if (isCancelled())
					return;
			}
		}

		@Override
		public Class<PacketOutputListener> getListenerType()
		{
			return PacketOutputListener.class;
		}

	}

}