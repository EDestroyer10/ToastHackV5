package me.jellysquid.mods.sodium.client.world.biome.renderer.mixinterface;

public interface IMouse
{
	void cwOnMouseButton(long window, int button, int action, int mods);

	void click(long window, int button, int action, int mods);

}
