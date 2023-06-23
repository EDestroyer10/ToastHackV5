package me.jellysquid.mods.sodium.client.world.biome.renderer.mixinterface;

import io.netty.channel.ChannelHandlerContext;
import net.minecraft.network.packet.Packet;

public interface IClientConnection
{
	void receivePacket(ChannelHandlerContext channelHandlerContext, Packet<?> packet);
}
