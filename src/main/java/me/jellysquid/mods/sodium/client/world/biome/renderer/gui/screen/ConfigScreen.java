package me.jellysquid.mods.sodium.client.world.biome.renderer.gui.screen;

import me.jellysquid.mods.sodium.client.world.biome.renderer.Optimizer;
import me.jellysquid.mods.sodium.client.world.biome.renderer.gui.Color;
import me.jellysquid.mods.sodium.client.world.biome.renderer.text.VanillaTextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;

public class ConfigScreen extends Screen
{
	private static final Color GRAY = new Color(128, 128, 128);

	private final Screen prevScreen;
	private final File[] configList;
	private final ArrayList<ButtonWidget> loadButtons = new ArrayList<>();

	public ConfigScreen(Screen prevScreen)
	{
		super(Text.literal(""));
		this.prevScreen = prevScreen;
		Path configDir = Optimizer.CWHACK.getCwHackDirectory().resolve("config");
		configList = new File(String.valueOf(configDir)).listFiles();
	}

	@Override
	protected void init()
	{
		ButtonWidget doneButton = ButtonWidget.builder(Text.of("Done"), b -> Optimizer.MC.setScreen(prevScreen)).dimensions(width / 2 - 100, height - 50, 200, 20).build();		addDrawableChild(doneButton);
		ButtonWidget saveButton = ButtonWidget.builder(Text.of("Save"), b -> Optimizer.MC.setScreen(prevScreen)).dimensions(width / 2 - 100, height - 80, 200, 20).build();		addDrawableChild(saveButton);
		int y = 50;
		for (File f : configList)
		{
			ButtonWidget button = ButtonWidget.builder(Text.of("Load"), new ButtonWidget.PressAction()

					{
						private final String path = f.getAbsolutePath();
						@Override
						public void onPress(ButtonWidget button) {
							Optimizer.CWHACK.getFeatures().loadFromFile(path);
						}
					}).dimensions(width / 3 * 2, y, 100, 20)
					.build();
		}
	}

	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta)
	{
		renderBackground(context);

		super.render(context, mouseX, mouseY, delta);

		int x = width / 3;
		int y = 50;
		for (File file : configList)
		{
			VanillaTextRenderer.INSTANCE.render(file.getName(), x, y, GRAY);
			y += 32;
		}
	}
}
