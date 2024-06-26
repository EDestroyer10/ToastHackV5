package me.jellysquid.mods.sodium.client.world.biome.renderer.setting;

import me.jellysquid.mods.sodium.client.world.biome.renderer.feature.Feature;

public class IntegerSetting extends Setting<Integer>
{

	private int value;

	public IntegerSetting(String name, String description, int value, Feature feature)
	{
		super(name, description, feature);
		this.value = value;
	}

	@Override
	public void loadFromStringInternal(String string)
	{
		value = Integer.parseInt(string);
	}

	@Override
	public String storeAsString()
	{
		return Integer.toString(value);
	}

	@Override
	public Integer getValue()
	{
		return value;
	}
}
