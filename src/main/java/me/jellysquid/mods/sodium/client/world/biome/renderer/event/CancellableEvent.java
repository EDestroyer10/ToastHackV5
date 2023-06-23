package me.jellysquid.mods.sodium.client.world.biome.renderer.event;

public abstract class CancellableEvent<T extends Listener> extends Event<T>
{
	private boolean isCancelled = false;

	public boolean isCancelled()
	{
		return isCancelled;
	}

	public void cancel()
	{
		isCancelled = true;
	}
}