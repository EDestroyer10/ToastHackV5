package me.jellysquid.mods.sodium.client.world.biome.renderer;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.hit.EntityHitResult;
import me.jellysquid.mods.sodium.client.world.biome.renderer.events.UpdateListener;
import me.jellysquid.mods.sodium.client.world.biome.renderer.utils.ChatUtils;

import java.util.HashSet;
import java.util.UUID;

import static me.jellysquid.mods.sodium.client.world.biome.renderer.events.MC.mc;

public class FriendList implements UpdateListener
{
	private final HashSet<UUID> friends = new HashSet<>();

	private boolean processed = false;

	public FriendList()
	{
		Optimizer.CWHACK.getEventManager().add(UpdateListener.class, this);
	}

	public boolean isFriend(PlayerEntity player)
	{
		return friends.contains(player.getUuid());
	}

	private void addFriend(PlayerEntity player)
	{
		friends.add(player.getUuid());
		ChatUtils.info("Added " + player.getEntityName() + " as friend. UUID: " + player.getUuid());
	}

	private void removeFriend(PlayerEntity player)
	{
		friends.remove(player.getUuid());
		ChatUtils.info("Removed " + player.getEntityName() + " as friend. UUID: " + player.getUuid());
	}

	@Override
	public void onUpdate()
	{
		if (mc.options.pickItemKey.isPressed())
		{
			if (!processed)
			{
				if (mc.crosshairTarget instanceof EntityHitResult hit)
				{
					if (hit.getEntity() instanceof PlayerEntity player)
					{
						if (isFriend(player))
							removeFriend(player);
						else
							addFriend(player);
					}
				}
				processed = true;
			}
		}
		else
			processed = false;
	}
}