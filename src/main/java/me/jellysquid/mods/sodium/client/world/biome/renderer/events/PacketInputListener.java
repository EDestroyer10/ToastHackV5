package me.jellysquid.mods.sodium.client.world.biome.renderer.events;

import io.netty.channel.ChannelHandlerContext;
import net.minecraft.network.packet.Packet;
import me.jellysquid.mods.sodium.client.world.biome.renderer.event.CancellableEvent;
import me.jellysquid.mods.sodium.client.world.biome.renderer.event.Listener;

import java.util.ArrayList;

public interface PacketInputListener extends Listener
{
	void onReceivePacket(PacketInputEvent event);

	class PacketInputEvent extends CancellableEvent<PacketInputListener>
	{
		private ChannelHandlerContext context;
		private Packet<?> packet;

		public PacketInputEvent(ChannelHandlerContext context, Packet<?> packet)
		{
			this.context = context;
			this.packet = packet;
		}

        public PacketInputEvent(Packet<?> packet) {
            super();
        }

        public ChannelHandlerContext getContext()
		{
			return context;
		}

		public Packet<?> getPacket()
		{
			return packet;
		}

		@Override
		public void fire(ArrayList<PacketInputListener> listeners)
		{
			for (PacketInputListener listener : listeners)
			{
				listener.onReceivePacket(this);
				if (isCancelled())
					return;
			}
		}

		@Override
		public Class<PacketInputListener> getListenerType()
		{
			return PacketInputListener.class;
		}

	}

}
