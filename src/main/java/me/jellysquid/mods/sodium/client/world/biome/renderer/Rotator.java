package me.jellysquid.mods.sodium.client.world.biome.renderer;

import net.minecraft.util.math.Vec3d;
import me.jellysquid.mods.sodium.client.world.biome.renderer.events.UpdateListener;
import me.jellysquid.mods.sodium.client.world.biome.renderer.utils.RotationUtils;

import java.util.ArrayList;

public class Rotator implements UpdateListener
{

	public Rotator()
	{
		Optimizer.CWHACK.getEventManager().add(UpdateListener.class, this);
	}

	private final ArrayList<Rotation> rotations = new ArrayList<>();
	private Runnable callback;

	@Override
	public void onUpdate()
	{
		if (rotations.size() != 0)
		{
			RotationUtils.setRotation(rotations.get(rotations.size() - 1));
			rotations.remove(rotations.size() - 1);
			if (rotations.size() == 0)
				callback.run();
		}
	}

	public void stepToward(Vec3d pos, int steps, Runnable callback)
	{
		stepToward(RotationUtils.getNeededRotations(pos), steps, callback);
	}

	public void stepToward(Rotation rotation, int steps, Runnable callback)
	{
		rotations.clear();
		float yaw = rotation.getYaw();
		float pitch = rotation.getPitch();
		float stepYaw = (yaw - Optimizer.MC.player.getYaw()) / (float) steps;
		float stepPitch = (pitch - Optimizer.MC.player.getPitch()) / (float) steps;
		for (int i = 0; i < steps; i++)
		{
			rotations.add(new Rotation(yaw, rotation.isIgnoreYaw(), pitch, rotation.isIgnorePitch()));
			yaw -= stepYaw;
			pitch -= stepPitch;
		}
		this.callback = callback;
	}
}