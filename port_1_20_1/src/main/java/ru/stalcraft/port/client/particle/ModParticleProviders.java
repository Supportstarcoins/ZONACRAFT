package ru.stalcraft.port.client.particle;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import ru.stalcraft.port.StalkerPortMod;
import ru.stalcraft.port.registry.ModParticles;

@Mod.EventBusSubscriber(modid = StalkerPortMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class ModParticleProviders {
    private ModParticleProviders() {
    }

    @SubscribeEvent
    public static void registerParticleProviders(RegisterParticleProvidersEvent event) {
        for (var entry : ModParticles.entries()) {
            event.registerSpriteSet(entry.getValue().get(), StalkerSpriteParticle.Provider::new);
        }
    }
}
