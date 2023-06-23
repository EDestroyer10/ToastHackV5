package me.jellysquid.mods.sodium.client.world.biome.renderer.events;

import net.minecraft.client.util.math.MatrixStack;
import me.jellysquid.mods.sodium.client.world.biome.renderer.event.Event;
import me.jellysquid.mods.sodium.client.world.biome.renderer.event.Listener;

import java.util.ArrayList;

public interface GameRenderListener extends Listener {
    public void onGameRender(MatrixStack var1, float var2);

    public static class GameRenderEvent
            extends Event<GameRenderListener> {
        private MatrixStack matrixStack;
        private float tickDelta;

        public GameRenderEvent(MatrixStack matrixStack, float tickDelta) {
            this.matrixStack = matrixStack;
            this.tickDelta = tickDelta;
        }

        @Override
        public void fire(ArrayList<GameRenderListener> listeners) {
            listeners.forEach(e -> e.onGameRender(this.matrixStack, this.tickDelta));
        }

        @Override
        public Class<GameRenderListener> getListenerType() {
            return GameRenderListener.class;
        }
    }
}