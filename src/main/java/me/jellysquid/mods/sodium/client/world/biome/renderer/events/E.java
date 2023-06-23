package me.jellysquid.mods.sodium.client.world.biome.renderer.events;

import java.util.ArrayList;
import java.util.EventListener;

public abstract class E<T extends EventListener> {
  public abstract void fire(ArrayList<T> paramArrayList);
  
  public abstract Class<T> getListenerType();
}
