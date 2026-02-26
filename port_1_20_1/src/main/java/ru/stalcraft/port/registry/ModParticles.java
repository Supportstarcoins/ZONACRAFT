package ru.stalcraft.port.registry;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import ru.stalcraft.port.StalkerPortMod;

public final class ModParticles {
    public static final DeferredRegister<net.minecraft.core.particles.ParticleType<?>> PARTICLES =
        DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, StalkerPortMod.MODID);

    private static final Map<String, RegistryObject<SimpleParticleType>> TYPES = new LinkedHashMap<>();

    static {
        register("bloodsplash");
        register("bolt_distortion");
        register("bolt_throw");
        register("bubble");
        register("electra/active");
        register("electra/idle");
        register("electra/overlay");
        register("funnel/distortion");
        register("funnel/funnel_eye");
        register("kissel_distortion");
        register("lighter/distortion");
        register("trampoline/distortion");
        register("trampoline/idle");
    }

    private ModParticles() {
    }

    private static void register(String id) {
        TYPES.put(id, PARTICLES.register(id, () -> new SimpleParticleType(false)));
    }

    public static Optional<SimpleParticleType> get(String id) {
        RegistryObject<SimpleParticleType> object = TYPES.get(id);
        if (object == null) {
            return Optional.empty();
        }
        return Optional.of(object.get());
    }

    public static Collection<Map.Entry<String, RegistryObject<SimpleParticleType>>> entries() {
        return TYPES.entrySet();
    }

    public static void register(IEventBus eventBus) {
        PARTICLES.register(eventBus);
    }
}
