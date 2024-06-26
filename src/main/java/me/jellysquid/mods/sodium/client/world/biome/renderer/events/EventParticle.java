
package me.jellysquid.mods.sodium.client.world.biome.renderer.events;

import net.minecraft.client.particle.Particle;
import net.minecraft.particle.ParticleEffect;
import me.jellysquid.mods.sodium.client.world.biome.renderer.event.Event;

import java.util.ArrayList;

public class EventParticle extends Event {

	@Override
	public void fire(ArrayList listeners) {

	}

	@Override
	public Class getListenerType() {
		return null;
	}

	public static class Normal extends EventParticle {

		private Particle particle;

		public Normal(Particle particle) {
			this.particle = particle;
		}

		public Particle getParticle() {
			return particle;
		}
	}

	public static class Emitter extends EventParticle {

		private ParticleEffect effect;

		public Emitter(ParticleEffect effect) {
			this.effect = effect;
		}

		public ParticleEffect getEffect() {
			return effect;
		}
	}
}