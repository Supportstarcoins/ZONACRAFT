package ru.stalcraft.port.anomaly;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import net.minecraft.core.BlockPos;
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
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;
import ru.stalcraft.port.StalkerPortMod;
import ru.stalcraft.port.registry.ModBlockEntities;
import ru.stalcraft.port.registry.ModParticles;
import ru.stalcraft.port.registry.ModSounds;

public class BaseAnomalyBlockEntity extends BlockEntity {
    private static final int DEBUG_LOG_INTERVAL = 40;

    private int cooldownTicks;
    private int activeTicks;
    private int animationTick;
    private long fxSeed;
    private boolean immediateCheckRequested;
    private String lastActivator = "";
    private int lastSyncedActiveTicks;
    private final Map<UUID, Integer> entityCooldownTicks = new HashMap<>();

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
        be.tickEntityCooldowns();

        if (!(state.getBlock() instanceof BaseAnomalyBlock block)) {
            return;
        }

        if (be.animationTick % DEBUG_LOG_INTERVAL == 0) {
            StalkerPortMod.LOGGER.info("[ANOMALY] {} tick @{} cooldown={} active={}", block.anomalyType(), pos, be.cooldownTicks, be.activeTicks);
        }

        AnomalyType type = be.resolveAnomalyType(state, block);
        Vec3 center = Vec3.atCenterOf(pos);
        AABB searchBox = new AABB(pos).inflate(type.radius());
        List<LivingEntity> entities = level.getEntitiesOfClass(LivingEntity.class, searchBox,
            entity -> entity.isAlive() && !entity.isSpectator());

        if (!entities.isEmpty()) {
            for (LivingEntity entity : entities) {
                StalkerPortMod.LOGGER.info("[ANOMALY] detect {} entity={} uuid={} @{}", type, entity.getType().toShortString(), entity.getUUID(), pos);
            }
        }

        if (type == AnomalyType.BLACK_HOLE) {
            applyBlackHolePull(entities, center, type);
        }

        boolean triggerAttempt = be.immediateCheckRequested || !entities.isEmpty();
        be.immediateCheckRequested = false;

        if (triggerAttempt) {
            for (LivingEntity entity : entities) {
                if (entity.position().distanceTo(center) <= type.triggerDistance() && be.tryTrigger(level, pos, state, entity, type, center)) {
                    break;
                }
            }
        }

        if (be.activeTicks <= 0 && state.getValue(BaseAnomalyBlock.ACTIVE)) {
            be.setActiveState(level, pos, state, false);
            state = level.getBlockState(pos);
        }

        be.syncStateIfNeeded(level, pos, state);
    }

    public static void clientTick(Level level, BlockPos pos, BlockState state, BaseAnomalyBlockEntity be) {
        be.animationTick++;
        if (be.activeTicks > 0) {
            be.activeTicks--;
        }

        if (state.getBlock() instanceof BaseAnomalyBlock block) {
            AnomalyType type = be.resolveAnomalyType(state, block);
            AnomalyFxDispatcher.spawnAmbient(level, pos, type, be.animationTick, be.fxSeed, be.activeTicks > 0);
        }
    }

    private AnomalyType resolveAnomalyType(BlockState state, BaseAnomalyBlock fallbackBlock) {
        var key = ForgeRegistries.BLOCKS.getKey(state.getBlock());
        if (key == null) {
            return fallbackBlock.anomalyType();
        }

        return switch (key.getPath()) {
            case "trampoline", "tranpoline" -> AnomalyType.TRAMPOLINE;
            case "carousel" -> AnomalyType.CAROUSEL;
            default -> fallbackBlock.anomalyType();
        };
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

    private boolean tryTrigger(Level level, BlockPos pos, BlockState state, LivingEntity entity, AnomalyType type, Vec3 center) {
        if (this.cooldownTicks > 0 || !this.canTriggerEntity(entity)) {
            return false;
        }

        this.setCooldownTicks(type.cooldownTicks());
        this.setActiveTicks(type.activeTicks());
        this.lastActivator = entity.getUUID().toString();
        this.entityCooldownTicks.put(entity.getUUID(), type.cooldownTicks() / 2 + 6);

        switch (type) {
            case ELECTRA -> triggerElectra(level, entity, center, type);
            case BLACK_HOLE -> triggerBlackHole(level, entity, center, type);
            case TRAMPOLINE -> triggerTrampoline(entity, type);
            case LIGHTER -> triggerLighter(level, entity, type);
            case CAROUSEL -> triggerCarousel(entity, center, type);
        }

        StalkerPortMod.LOGGER.info("ACTIVATE anomaly={} tick={}", type.name().toLowerCase(), level.getGameTime());
        this.setActiveState(level, pos, state, true);
        state = level.getBlockState(pos);

        if (level instanceof ServerLevel serverLevel) {
            String activateParticleId = type == AnomalyType.CAROUSEL ? "carousel/distortion" : "bolt_distortion";
            ModParticles.get(activateParticleId).ifPresent(particle ->
                serverLevel.sendParticles(particle, center.x, center.y + 0.3D, center.z, 12, 0.35D, 0.25D, 0.35D, 0.02D));
            SoundEvent activate = resolveActivateSound(type);
            level.playSound(null, pos, activate, SoundSource.BLOCKS, 1.0F, 1.0F);
            StalkerPortMod.LOGGER.info("PLAY sound={} tick={}", activate.getLocation(), level.getGameTime());
        }

        AnomalyFxDispatcher.spawnActivate(level, pos, type, this.fxSeed);
        AnomalyFxDispatcher.spawnHit(level, pos, type, this.fxSeed);
        if (type == AnomalyType.BLACK_HOLE || type == AnomalyType.TRAMPOLINE) {
            AnomalyFxDispatcher.spawnSplash(level, pos, type, this.fxSeed);
        }
        level.sendBlockUpdated(pos, state, state, 3);
        setChanged();
        return true;
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

    private boolean canTriggerEntity(LivingEntity entity) {
        return this.entityCooldownTicks.getOrDefault(entity.getUUID(), 0) <= 0;
    }

    public void setCooldownTicks(int cooldownTicks) {
        this.cooldownTicks = Math.max(cooldownTicks, 0);
    }

    public void setActiveTicks(int activeTicks) {
        this.activeTicks = Math.max(activeTicks, 0);
    }

    private void tickEntityCooldowns() {
        Iterator<Map.Entry<UUID, Integer>> iterator = this.entityCooldownTicks.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<UUID, Integer> entry = iterator.next();
            int next = entry.getValue() - 1;
            if (next <= 0) {
                iterator.remove();
            } else {
                entry.setValue(next);
            }
        }
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
            this.setActiveState(level, pos, state, activeNow);
            state = level.getBlockState(pos);
        }

        if (this.lastSyncedActiveTicks != this.activeTicks || (this.animationTick % 20 == 0 && this.cooldownTicks > 0)) {
            this.lastSyncedActiveTicks = this.activeTicks;
            level.sendBlockUpdated(pos, state, state, 3);
            setChanged();
        }
    }

    private void setActiveState(Level level, BlockPos pos, BlockState state, boolean active) {
        if (state.hasProperty(BaseAnomalyBlock.ACTIVE) && state.getValue(BaseAnomalyBlock.ACTIVE) != active) {
            BlockState updated = state.setValue(BaseAnomalyBlock.ACTIVE, active);
            level.setBlock(pos, updated, 3);
            level.sendBlockUpdated(pos, state, updated, 3);
            setChanged();
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
