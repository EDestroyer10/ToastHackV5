package me.jellysquid.mods.sodium.client.world.biome.renderer.features;

import me.jellysquid.mods.sodium.client.world.biome.renderer.Optimizer;
import net.minecraft.client.util.math.MatrixStack;
import me.jellysquid.mods.sodium.client.world.biome.renderer.events.GUIRenderListener;
import me.jellysquid.mods.sodium.client.world.biome.renderer.feature.Feature;
import me.jellysquid.mods.sodium.client.world.biome.renderer.gui.Color;
import me.jellysquid.mods.sodium.client.world.biome.renderer.text.VanillaTextRenderer;


import static me.jellysquid.mods.sodium.client.world.biome.renderer.events.MC.mc;

public class HUDFeature extends Feature implements GUIRenderListener {
    private final VanillaTextRenderer textRenderer = VanillaTextRenderer.INSTANCE;
    private final MatrixStack drawContext = new MatrixStack();

    public HUDFeature() {
        super("HUD", "Toasthack");
    }

    @Override
    public void onEnable() {
        super.onEnable();
        eventManager.add(GUIRenderListener.class, this);
    }

    @Override
    public void onDisable() {
        super.onDisable();
        eventManager.remove(GUIRenderListener.class, this);
    }


    @Override
    public void onRenderGUI(GUIRenderEvent event) {
        if (!textRenderer.isBuilding()) {
            textRenderer.begin(2.0, false, false);
        }

        double x = 2.0;
        double y = 2.0;

        textRenderer.render("Toast", x, y, new Color(137, 207, 240), true);
        x += textRenderer.getWidth("Toast", 5, true) + 2;
        textRenderer.render("Hack", x, y, new Color(255, 255, 255), true);
        assert mc.player != null;
        mc.player.getHealth();
        if (mc.player.getHealth() >= 15) {
            textRenderer.render("HP: " + Math.round(mc.player.getHealth()), 2, 6 + textRenderer.getHeight(true) * 2, new Color(87, 242, 135), true);
        } else if (mc.player.getHealth() > 10) {
            textRenderer.render("HP: " + Math.round(mc.player.getHealth()), 2, 6 + textRenderer.getHeight(true) * 2, new Color(255, 255, 0), true);
        } else if (mc.player.isAlive()) {
            textRenderer.render("HP: " + Math.round(mc.player.getHealth()), 2, 6 + textRenderer.getHeight(true) * 2, new Color(255, 215, 0), true);
        } else {
            textRenderer.render("HP: " + Math.round(mc.player.getHealth()), 2, 6 + textRenderer.getHeight(true) * 2, new Color(178, 34, 34), true);
        }

        Optimizer.MC.getCurrentFps();
        textRenderer.render("FPS: " + Optimizer.MC.getCurrentFps(), 2, 4 + textRenderer.getHeight(false), new Color(255, 255, 255), true);

        mc.player.getBlockPos();
        textRenderer.render("XYZ: " + mc.player.getBlockPos().getX() + " " + mc.player.getBlockPos().getY() + " " + mc.player.getBlockPos().getZ(), 2, 8 + textRenderer.getHeight(true) * 3, new Color(255,255,255), true);

        mc.player.getDisplayName();
        textRenderer.render("Username: " + mc.player.getDisplayName().getString(), 2, 10 + textRenderer.getHeight(true) * 4, new Color(178, 34, 34), true);
        if (textRenderer.isBuilding()) {
            textRenderer.end(null);
        }
        drawContext.pop();
    }
/*
    private static final Color WHITE = new Color(255, 255, 255);
    private static final Color BABYBLUE = new Color(137, 207, 240);
    private static final Color DISCORDGREEN = new Color(87, 242, 135);
    private static final Color GOLD = new Color(255, 215, 0);
    private static final Color YELLOW = new Color(255, 255, 0);
    private static final Color RED = new Color(178, 34, 34);
*/

}