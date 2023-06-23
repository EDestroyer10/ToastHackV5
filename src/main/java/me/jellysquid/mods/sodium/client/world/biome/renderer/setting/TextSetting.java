package me.jellysquid.mods.sodium.client.world.biome.renderer.setting;

import me.jellysquid.mods.sodium.client.world.biome.renderer.feature.Feature;

public class TextSetting extends Setting<String>
{

	private String value;

	public TextSetting(String name, String description, String value, Feature feature)
	{
		super(name, description, feature);
		this.value = value;
	}

	public void setValue(String value)
	{
		this.value = value;
	}

	@Override
	public String getValue()
	{
		return value;
	}

	@Override
	public void loadFromStringInternal(String string)
	{
		value = string;
	}

	@Override
	public String storeAsString()
	{
		return value;
	}
}
