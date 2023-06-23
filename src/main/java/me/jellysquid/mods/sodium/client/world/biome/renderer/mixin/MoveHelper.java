package me.jellysquid.mods.sodium.client.world.biome.renderer.mixin;

import me.jellysquid.mods.sodium.client.world.biome.renderer.Optimizer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;


public class MoveHelper {

    public static boolean hasMovement() {
        final Vec3d playerMovement = Optimizer.MC.player.getVelocity();
        return playerMovement.getX() != 0;
    }

    public static double motionY(final double motionY) {
        final Vec3d vec3d = Optimizer.MC.player.getVelocity();
        Optimizer.MC.player.setVelocity(vec3d.x, motionY, vec3d.z);
        return motionY;
    }

    public static double motionYPlus(final double motionY) {
        final Vec3d vec3d = Optimizer.MC.player.getVelocity();
        Optimizer.MC.player.setVelocity(vec3d.x, vec3d.y + motionY, vec3d.z);
        return motionY;
    }

    public static double getDistanceToGround(Entity entity) {
        final double playerX = Optimizer.MC.player.getX();
        final int playerHeight = (int) Math.floor(Optimizer.MC.player.getY());
        final double playerZ = Optimizer.MC.player.getZ();

        for (int height = playerHeight; height > 0; height--) {
            final BlockPos checkPosition = new BlockPos((int) playerX, height, (int) playerZ);

            // Check if the block is solid
            if (!Optimizer.MC.world.isAir(checkPosition)) {
                return playerHeight - height;
            }
        }
        return 0;
    }
}