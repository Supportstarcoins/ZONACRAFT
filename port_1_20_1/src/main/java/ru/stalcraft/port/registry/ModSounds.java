package ru.stalcraft.port.registry;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import ru.stalcraft.port.StalkerPortMod;

public final class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS,
        StalkerPortMod.MODID);

    public static final RegistryObject<SoundEvent> ELECTRA_AMBIENT = register("anomaly.electra.ambient");
    public static final RegistryObject<SoundEvent> ELECTRA_ACTIVATE = register("anomaly.electra.activate");
    public static final RegistryObject<SoundEvent> ELECTRA_HIT = register("anomaly.electra.hit");

    public static final RegistryObject<SoundEvent> BLACK_HOLE_AMBIENT = register("anomaly.black_hole.ambient");
    public static final RegistryObject<SoundEvent> BLACK_HOLE_ACTIVATE = register("anomaly.black_hole.activate");
    public static final RegistryObject<SoundEvent> BLACK_HOLE_HIT = register("anomaly.black_hole.hit");

    public static final RegistryObject<SoundEvent> FUNNEL_AMBIENT = register("anomaly.funnel.ambient");
    public static final RegistryObject<SoundEvent> FUNNEL_ACTIVATE = register("anomaly.funnel.activate");
    public static final RegistryObject<SoundEvent> FUNNEL_HIT = register("anomaly.funnel.hit");
    public static final RegistryObject<SoundEvent> FUNEL_AMBIENT = register("anomaly.funel.ambient");
    public static final RegistryObject<SoundEvent> FUNEL_ACTIVATE = register("anomaly.funel.activate");
    public static final RegistryObject<SoundEvent> FUNEL_HIT = register("anomaly.funel.hit");

    public static final RegistryObject<SoundEvent> TRAMPOLINE_AMBIENT = register("anomaly.trampoline.ambient");
    public static final RegistryObject<SoundEvent> TRAMPOLINE_ACTIVATE = register("anomaly.trampoline.activate");
    public static final RegistryObject<SoundEvent> TRAMPOLINE_HIT = register("anomaly.trampoline.hit");
    public static final RegistryObject<SoundEvent> TRANPOLINE_AMBIENT = register("anomaly.tranpoline.ambient");
    public static final RegistryObject<SoundEvent> TRANPOLINE_ACTIVATE = register("anomaly.tranpoline.activate");
    public static final RegistryObject<SoundEvent> TRANPOLINE_HIT = register("anomaly.tranpoline.hit");

    public static final RegistryObject<SoundEvent> LIGHTER_AMBIENT = register("anomaly.lighter.ambient");
    public static final RegistryObject<SoundEvent> LIGHTER_ACTIVATE = register("anomaly.lighter.activate");
    public static final RegistryObject<SoundEvent> LIGHTER_HIT = register("anomaly.lighter.hit");

    public static final RegistryObject<SoundEvent> CAROUSEL_AMBIENT = register("anomaly.carousel.ambient");
    public static final RegistryObject<SoundEvent> CAROUSEL_ACTIVATE = register("anomaly.carousel.activate");
    public static final RegistryObject<SoundEvent> CAROUSEL_HIT = register("anomaly.carousel.hit");

    private ModSounds() {
    }

    private static RegistryObject<SoundEvent> register(String id) {
        ResourceLocation location = new ResourceLocation(StalkerPortMod.MODID, id);
        return SOUND_EVENTS.register(id, () -> SoundEvent.createVariableRangeEvent(location));
    }

    public static void register(IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
    }
}
