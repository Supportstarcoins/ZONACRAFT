package ru.stalcraft.port.anomaly;

import java.util.List;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import ru.stalcraft.port.registry.ModBlockEntities;
import ru.stalcraft.port.registry.ModSounds;

public class BaseAnomalyBlockEntity extends BlockEntity {
    private int cooldownTicks;
    private int activeTicks;
    private int animationTick;
    private long fxSeed;
    private boolean immediateCheckRequested;
    private String lastActivator = "";
    private int lastSyncedActiveTicks;

    public BaseAnomalyBlockEntity(BlockPos pos, BlockState state) {
        this(ModBlockEntities.ANOMALY.get(), pos, state);
    }

    public BaseAnomalyBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        this.fxSeed = Mth.getSeed(pos);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, BaseAnomalyBlockEntity be) {
        be.animationTick++;
        if (be.activeTicks > 0) {
            be.activeTicks--;
        }
        if (be.cooldownTicks > 0) {
            be.cooldownTicks--;
        }

        if (!(state.getBlock() instanceof BaseAnomalyBlock block)) {
            return;
        }

        AnomalyType type = block.anomalyType();
        Vec3 center = Vec3.atCenterOf(pos);
        AABB searchBox = new AABB(center, center).inflate(type.radius());
        List<LivingEntity> entities = level.getEntitiesOfClass(LivingEntity.class, searchBox,
            entity -> entity.isAlive() && !entity.isSpectator());

        if (type == AnomalyType.BLACK_HOLE) {
            applyBlackHolePull(entities, center, type);
        }

        boolean triggerAttempt = be.immediateCheckRequested || (be.animationTick % 4 == 0 && !entities.isEmpty());
        be.immediateCheckRequested = false;

        if (triggerAttempt && be.cooldownTicks <= 0) {
            for (LivingEntity entity : entities) {
                if (entity.position().distanceTo(center) <= type.triggerDistance() && canTriggerEntity(level, entity, type)) {
                    be.trigger(level, pos, state, entity, type, center);
                    break;
                }
            }
        }

        be.syncStateIfNeeded(level, pos, state);
    }

    public static void clientTick(Level level, BlockPos pos, BlockState state, BaseAnomalyBlockEntity be) {
        be.animationTick++;
        if (be.activeTicks > 0) {
            be.activeTicks--;
        }

        if (state.getBlock() instanceof BaseAnomalyBlock block) {
            AnomalyFxDispatcher.spawnAmbient(level, pos, block.anomalyType(), be.animationTick, be.fxSeed, be.activeTicks > 0);
        }
    }

    public void requestImmediateCheck(Entity entity) {
        if (entity instanceof LivingEntity livingEntity && livingEntity.isAlive()) {
            this.immediateCheckRequested = true;
        }
    }

    public void setLastActivator(String lastActivator) {
        this.lastActivator = lastActivator;
        setChanged();
    }

    private void trigger(Level level, BlockPos pos, BlockState state, LivingEntity entity, AnomalyType type, Vec3 center) {
        this.cooldownTicks = type.cooldownTicks();
        this.activeTicks = type.activeTicks();
        setEntityCooldown(level, entity, type, type.cooldownTicks() / 2 + 6);

        switch (type) {
            case ELECTRA -> triggerElectra(level, entity, center, type);
            case BLACK_HOLE -> triggerBlackHole(level, entity, center, type);
            case TRAMPOLINE -> triggerTrampoline(entity, type);
            case LIGHTER -> triggerLighter(level, entity, type);
            case CAROUSEL -> triggerCarousel(entity, center, type);
        }

        if (level instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(ParticleTypes.ENCHANT, center.x, center.y + 0.3D, center.z, 12, 0.35D, 0.25D, 0.35D, 0.02D);
            level.playSound(null, pos, resolveActivateSound(type), SoundSource.BLOCKS, 0.9F, 0.9F + level.random.nextFloat() * 0.2F);
        }

        AnomalyFxDispatcher.spawnActivate(level, pos, type, this.fxSeed);
        AnomalyFxDispatcher.spawnHit(level, pos, type, this.fxSeed);
        if (type == AnomalyType.BLACK_HOLE || type == AnomalyType.TRAMPOLINE) {
            AnomalyFxDispatcher.spawnSplash(level, pos, type, this.fxSeed);
        }
        setChanged();
    }

    private static void applyBlackHolePull(List<LivingEntity> entities, Vec3 center, AnomalyType type) {
        for (LivingEntity living : entities) {
            Vec3 direction = center.subtract(living.position());
            double distance = Math.max(0.25D, direction.length());
            Vec3 normalized = direction.normalize().scale(type.force() / distance);
            living.setDeltaMovement(living.getDeltaMovement().add(normalized.x, 0.02D, normalized.z));
            living.hasImpulse = true;
        }
    }

    private static void triggerElectra(Level level, LivingEntity entity, Vec3 center, AnomalyType type) {
        entity.hurt(level.damageSources().magic(), type.damage());
        Vec3 knock = entity.position().subtract(center).normalize().scale(type.force());
        entity.setDeltaMovement(entity.getDeltaMovement().add(knock.x, type.verticalForce(), knock.z));
        entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 40, 1));
        entity.hasImpulse = true;
    }

    private static void triggerBlackHole(Level level, LivingEntity entity, Vec3 center, AnomalyType type) {
        entity.hurt(level.damageSources().fellOutOfWorld(), type.damage());
        Vec3 pull = center.subtract(entity.position()).normalize().scale(type.force() * 1.8D);
        entity.setDeltaMovement(entity.getDeltaMovement().add(pull.x, 0.10D, pull.z));
        entity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 80, 0));
        entity.hasImpulse = true;
    }

    private static void triggerTrampoline(LivingEntity entity, AnomalyType type) {
        Vec3 velocity = entity.getDeltaMovement();
        entity.setDeltaMovement(velocity.x * 0.6D, Math.max(type.verticalForce(), velocity.y + type.verticalForce()), velocity.z * 0.6D);
        entity.fallDistance = 0.0F;
        entity.hasImpulse = true;
    }

    private static void triggerLighter(Level level, LivingEntity entity, AnomalyType type) {
        entity.setSecondsOnFire(4);
        entity.hurt(level.damageSources().inFire(), type.damage());
        entity.addEffect(new MobEffectInstance(MobEffects.HUNGER, 100, 0));
    }

    private static void triggerCarousel(LivingEntity entity, Vec3 center, AnomalyType type) {
        Vec3 radial = entity.position().subtract(center);
        Vec3 tangent = new Vec3(-radial.z, 0.0D, radial.x).normalize().scale(type.force());
        entity.setDeltaMovement(entity.getDeltaMovement().add(tangent.x, type.verticalForce(), tangent.z));
        entity.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 80, 0));
        entity.hasImpulse = true;
    }

    private static boolean canTriggerEntity(Level level, LivingEntity entity, AnomalyType type) {
        String key = "stalker_port.anomaly_cd." + type.name().toLowerCase();
        return level.getGameTime() >= entity.getPersistentData().getLong(key);
    }

    private static void setEntityCooldown(Level level, LivingEntity entity, AnomalyType type, int ticks) {
        String key = "stalker_port.anomaly_cd." + type.name().toLowerCase();
        entity.getPersistentData().putLong(key, level.getGameTime() + ticks);
    }

    private static SoundEvent resolveActivateSound(AnomalyType type) {
        return switch (type) {
            case ELECTRA -> ModSounds.ELECTRA_ACTIVATE.get();
            case BLACK_HOLE -> ModSounds.BLACK_HOLE_ACTIVATE.get();
            case TRAMPOLINE -> ModSounds.TRAMPOLINE_ACTIVATE.get();
            case LIGHTER -> ModSounds.LIGHTER_ACTIVATE.get();
            case CAROUSEL -> ModSounds.CAROUSEL_ACTIVATE.get();
        };
    }

    private void syncStateIfNeeded(Level level, BlockPos pos, BlockState state) {
        boolean activeNow = this.activeTicks > 0;
        boolean activeBefore = state.getValue(BaseAnomalyBlock.ACTIVE);
        if (activeBefore != activeNow) {
            level.setBlock(pos, state.setValue(BaseAnomalyBlock.ACTIVE, activeNow), 3);
            state = level.getBlockState(pos);
        }

        if (this.lastSyncedActiveTicks != this.activeTicks || (this.animationTick % 20 == 0 && this.cooldownTicks > 0)) {
            this.lastSyncedActiveTicks = this.activeTicks;
            level.sendBlockUpdated(pos, state, state, 3);
        }
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.cooldownTicks = tag.getInt("CooldownTicks");
        this.activeTicks = tag.getInt("ActiveTicks");
        this.animationTick = tag.getInt("AnimationTick");
        this.fxSeed = tag.contains("FxSeed") ? tag.getLong("FxSeed") : Mth.getSeed(this.worldPosition);
        this.lastActivator = tag.getString("LastActivator");
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt("CooldownTicks", this.cooldownTicks);
        tag.putInt("ActiveTicks", this.activeTicks);
        tag.putInt("AnimationTick", this.animationTick);
        tag.putLong("FxSeed", this.fxSeed);
        tag.putString("LastActivator", this.lastActivator);
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = super.getUpdateTag();
        tag.putInt("CooldownTicks", this.cooldownTicks);
        tag.putInt("ActiveTicks", this.activeTicks);
        tag.putInt("AnimationTick", this.animationTick);
        tag.putLong("FxSeed", this.fxSeed);
        return tag;
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public int getCooldownTicks() {
        return cooldownTicks;
    }

    public int getActiveTicks() {
        return activeTicks;
    }

    public int getAnimationTick() {
        return animationTick;
    }

    public UUID getLastActivatorUuid() {
        try {
            return this.lastActivator.isBlank() ? null : UUID.fromString(this.lastActivator);
        } catch (IllegalArgumentException exception) {
            return null;
        }
    }
}
