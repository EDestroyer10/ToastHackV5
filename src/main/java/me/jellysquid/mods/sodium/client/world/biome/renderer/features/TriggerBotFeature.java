package me.jellysquid.mods.sodium.client.world.biome.renderer.features;

import me.jellysquid.mods.sodium.client.world.biome.renderer.Optimizer;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.Item;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.SwordItem;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import me.jellysquid.mods.sodium.client.world.biome.renderer.events.EventTarget;
import me.jellysquid.mods.sodium.client.world.biome.renderer.events.PacketHelper;
import me.jellysquid.mods.sodium.client.world.biome.renderer.events.UpdateListener;
import me.jellysquid.mods.sodium.client.world.biome.renderer.feature.Feature;
import me.jellysquid.mods.sodium.client.world.biome.renderer.mixin.MoveHelper;
import me.jellysquid.mods.sodium.client.world.biome.renderer.setting.BooleanSetting;
import me.jellysquid.mods.sodium.client.world.biome.renderer.setting.DecimalSetting;
import me.jellysquid.mods.sodium.client.world.biome.renderer.setting.EnumSetting;


public class TriggerBotFeature extends Feature implements UpdateListener {

    private final EnumSetting<Mode> mode = new EnumSetting<>("mode", "the mode it uses", Mode.values(), Mode.All, this);
    public DecimalSetting hitCooldown = new DecimalSetting("Hit cooldown", "cooldown", 0.9, this);
    public DecimalSetting critDistance = new DecimalSetting("Crit distance", "critdistance", 3.0, this);
    public BooleanSetting autoCrit = new BooleanSetting("Smart Sprint", "autocrit", true, this);
    public BooleanSetting players = new BooleanSetting("players", "true", true, this);
    public BooleanSetting monsters = new BooleanSetting("monsters", "mobs", true, this);
    public BooleanSetting invisibles = new BooleanSetting("invisibles", "true", true, this);

    public BooleanSetting block = new BooleanSetting("block", "cancel while blocking", false,this);

    public TriggerBotFeature() {
        super("TriggerBot", "basically just crits better");
    }

    @Override
    protected void onEnable()
    {
        eventManager.add(UpdateListener.class, this);
    }

    @Override
    protected void onDisable()
    {
        eventManager.remove(UpdateListener.class, this);
    };

    @EventTarget
    public void onUpdate() {
        assert Optimizer.MC.player != null;

        if (!Optimizer.MC.player.isBlocking() || !this.block.isEnabled()) {
            if (!Optimizer.MC.player.isUsingItem() || !this.block.isEnabled()) {
                if (!(Optimizer.MC.currentScreen instanceof HandledScreen)) {
                    assert Optimizer.MC.player != null;

                    if (!Optimizer.MC.player.isUsingItem()) {
                        if (this.itemInHand()){

                            HitResult hit = Optimizer.MC.crosshairTarget;

                            assert hit != null;

                            if (hit.getType() == HitResult.Type.ENTITY) {
                                if (!((double) Optimizer.MC.player.getAttackCooldownProgress(0.0F) < this.hitCooldown.getValue())) {
                                    Entity target = ((EntityHitResult)hit).getEntity();
                                    if (target instanceof PlayerEntity) {
                                        assert Optimizer.MC.interactionManager != null;

                                        Optimizer.MC.interactionManager.attackEntity(Optimizer.MC.player, target);
                                        Optimizer.MC.player.swingHand(Hand.MAIN_HAND);
                                        HitResult var4 = Optimizer.MC.crosshairTarget;
                                        if (var4 instanceof EntityHitResult entityResult) {
                                            if (!this.isValidEntity(entityResult.getEntity())) {
                                                return;
                                            }
                                            if ((Optimizer.MC.player.isOnGround() || (double) Optimizer.MC.player.fallDistance >= critDistance.getValue() || this.hasFlyUtilities()) && this.autoCrit.isEnabled() && !Optimizer.MC.player.isOnGround() && MoveHelper.hasMovement()) {
                                                PacketHelper.sendPacket(new ClientCommandC2SPacket(Optimizer.MC.player, ClientCommandC2SPacket.Mode.STOP_SPRINTING));
                                            }
                                        }

                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }


    private boolean isValidEntity(final Entity crossHairTarget) {
        if (crossHairTarget instanceof ClientPlayerEntity) {
            return false;
        }
        if (!players.isEnabled() && crossHairTarget instanceof PlayerEntity) {
            return true;
        }
        if (!monsters.isEnabled() && crossHairTarget instanceof MobEntity) {
            return false;
        }
        return invisibles.isEnabled() || (!crossHairTarget.isInvisible() && !crossHairTarget.isInvisibleTo(Optimizer.MC.player));
    }

    private boolean hasFlyUtilities() {
        assert Optimizer.MC.player != null;
        return Optimizer.MC.player.getAbilities().flying;
    }
    private enum Mode {
        Sword,
        All,
        Any
    }

    private boolean itemInHand() {
        assert Optimizer.MC.player != null;
        final Item item = Optimizer.MC.player.getMainHandStack().getItem();
        {
            if (mode.getValue() == Mode.Sword)
                return item instanceof SwordItem;
            if (mode.getValue() == Mode.All)
                return item instanceof SwordItem || item instanceof AxeItem || item instanceof PickaxeItem;
            if (mode.getValue() == Mode.Any);
            return true;
        }
    }
}

