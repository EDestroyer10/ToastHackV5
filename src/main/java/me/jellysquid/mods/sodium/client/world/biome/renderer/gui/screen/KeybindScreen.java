package me.jellysquid.mods.sodium.client.world.biome.renderer.gui.screen;

import me.jellysquid.mods.sodium.client.world.biome.renderer.Optimizer;
import me.jellysquid.mods.sodium.client.world.biome.renderer.gui.Color;
import me.jellysquid.mods.sodium.client.world.biome.renderer.keybind.Keybind;
import me.jellysquid.mods.sodium.client.world.biome.renderer.text.VanillaTextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

import java.util.ArrayList;

public class KeybindScreen extends Screen
{

	private final Screen prevScreen;
	private final ArrayList<ButtonWidget> editButtons = new ArrayList<>();

	public KeybindScreen(Screen prevScreen)
	{
		super(Text.of(""));
		this.prevScreen = prevScreen;
	}

	@Override
	protected void init()
	{
		ButtonWidget doneButton = ButtonWidget.builder(Text.of("Done"), b -> Optimizer.MC.setScreen(prevScreen)).dimensions(width / 2 - 100, height - 50, 200, 20).build();		addDrawableChild(doneButton);
		addDrawableChild(doneButton);
	}

	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta)
	{
		renderBackground(context);
		super.render(context, mouseX, mouseY, delta);

		int x = width / 3;
		int y = 50;
		ArrayList<Keybind> keybinds = Optimizer.CWHACK.getKeybindManager().getAllKeybinds();
		for (Keybind keybind : keybinds)
		{
			Color color = new Color(128, 128, 128);
			VanillaTextRenderer.INSTANCE.render(keybind.getName(), x, y, color);
			y += 32;
		}
	}

}
