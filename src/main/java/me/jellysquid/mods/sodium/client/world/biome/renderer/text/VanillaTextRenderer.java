/*
 * This file is part of the Meteor Client distribution (https://github.com/MeteorDevelopment/meteor-client).
 * Copyright (c) Meteor Development.
 */

package me.jellysquid.mods.sodium.client.world.biome.renderer.text;

import com.mojang.blaze3d.systems.RenderSystem;
import me.jellysquid.mods.sodium.client.world.biome.renderer.events.MC;
import me.jellysquid.mods.sodium.client.world.biome.renderer.mixinterface.TextRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer.TextLayerType;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Matrix4f;
import me.jellysquid.mods.sodium.client.world.biome.renderer.gui.Color;

public class VanillaTextRenderer implements TextRenderer {
    public static final VanillaTextRenderer INSTANCE = new VanillaTextRenderer();

    private final BufferBuilder buffer = new BufferBuilder(2048);
    private final VertexConsumerProvider.Immediate immediate = VertexConsumerProvider.immediate(buffer);

    private final DrawContext context = new DrawContext(MinecraftClient.getInstance(), VertexConsumerProvider.immediate(buffer));
    private final Matrix4f emptyMatrix = new Matrix4f();

    public double scale = 0.5;
    public boolean scaleIndividually;

    private boolean building;

    private VanillaTextRenderer() {
        // Use INSTANCE
    }

    @Override
    public void setAlpha(double a) {
    }

    @Override
    public double getWidth(String text, int length, boolean shadow) {
        if (text.isEmpty()) return 0;

        if (length != text.length()) text = text.substring(0, length);
        return (MC.mc.textRenderer.getWidth(text) + (shadow ? 1 : 0)) * scale;
    }

    @Override
    public double getHeight(boolean shadow) {
        return (MC.mc.textRenderer.fontHeight + (shadow ? 1 : 0)) * scale;
    }

    @Override
    public void begin(double scale, boolean scaleOnly, boolean big) {
        if (building) throw new RuntimeException("VanillaTextRenderer.begin() called twice");

        this.scale = scale * 2;
        this.building = true;
    }

    @Override
    public double render(String text, double x, double y, Color color, boolean shadow) {
        boolean wasBuilding = building;
        if (!wasBuilding) begin();

        x += 0.2 * scale;
        y += 0.2 * scale;

        Matrix4f matrix = emptyMatrix;
        if (scaleIndividually) {
            context.getMatrices().push();
            context.getMatrices().scale((float) scale, (float) scale, 1);
            matrix = context.getMatrices().peek().getPositionMatrix();
        }

        double x2 = MC.mc.textRenderer.draw(text, (float) (x / scale), (float) (y / scale), color.getPacked(), shadow, matrix, immediate, TextLayerType.NORMAL, 0, LightmapTextureManager.MAX_LIGHT_COORDINATE);

        if (scaleIndividually) context.getMatrices().pop();


        if (!wasBuilding) end();
        return (x2 - 1) * scale;
    }

    @Override
    public boolean isBuilding() {
        return building;
    }

    @Override
    public void end(MatrixStack matrices) {
        if (!building) throw new RuntimeException("VanillaTextRenderer.end() called without calling begin()");

        MatrixStack matrixStack = RenderSystem.getModelViewStack();

        RenderSystem.disableDepthTest();
        matrixStack.push();
        if (matrices != null) matrixStack.multiplyPositionMatrix(matrices.peek().getPositionMatrix());
        if (!scaleIndividually) matrixStack.scale((float) scale, (float) scale, 1);
        RenderSystem.applyModelViewMatrix();

        immediate.draw();

        matrixStack.pop();
        RenderSystem.enableDepthTest();
        RenderSystem.applyModelViewMatrix();

        this.scale = 0.5;
        this.building = false;
    }
}