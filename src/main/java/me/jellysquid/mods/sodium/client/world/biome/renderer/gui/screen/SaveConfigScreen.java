package me.jellysquid.mods.sodium.client.world.biome.renderer.gui.screen;

import me.jellysquid.mods.sodium.client.world.biome.renderer.Optimizer;
import me.jellysquid.mods.sodium.client.world.biome.renderer.gui.Color;
import me.jellysquid.mods.sodium.client.world.biome.renderer.text.VanillaTextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;

public class SaveConfigScreen extends Screen
{
	private ButtonWidget doneButton;

	private final Screen prevScreen;
	private TextFieldWidget textWidget;

	public SaveConfigScreen(Screen prevScreen)
	{
		super(Text.of(""));
		this.prevScreen = prevScreen;
	}

	@Override
	protected void init()
	{
		ButtonWidget doneButton = ButtonWidget.builder(Text.of("Done"), b -> Optimizer.MC.setScreen(prevScreen)).dimensions(width / 2 - 100, height - 50, 200, 20).build();
		addDrawableChild(doneButton);
		textWidget = new TextFieldWidget(Optimizer.MC.textRenderer, width / 2 - 200, height / 3, 400, 20, Text.of(""));
		addSelectableChild(textWidget);
		setInitialFocus(textWidget);
	}
	private void done()
	{
		Optimizer.CWHACK.getFeatures().saveAsFile(Optimizer.CWHACK.getConfigDirectory().resolve(textWidget.getText()).toString() + ".cw");
		Optimizer.MC.setScreen(prevScreen);
	}

	@Override
	public void tick()
	{
		textWidget.tick();
	}

	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta)
	{
		renderBackground(context);
		super.render(context, mouseX, mouseY, delta);
		Color color = new Color(128, 128, 128);
		VanillaTextRenderer.INSTANCE.render("Save config as ...", width / 2, height / 3 - 20, color);
	}
}
