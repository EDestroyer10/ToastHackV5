package me.jellysquid.mods.sodium.client.world.biome.renderer.utils;

import me.jellysquid.mods.sodium.client.world.biome.renderer.Optimizer;

public enum NotificationUtils
{
	;
	public static void notify(String string)
	{
		if (!Optimizer.CWHACK.isGhostMode())
			Optimizer.CWHACK.getNotificationRenderer().sendNotification(string);
	}
}