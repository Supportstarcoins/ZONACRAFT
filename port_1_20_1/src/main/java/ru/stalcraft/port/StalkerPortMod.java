package ru.stalcraft.port;

import com.mojang.logging.LogUtils;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import ru.stalcraft.port.registry.ModBlockEntities;
import ru.stalcraft.port.registry.ModBlocks;
import ru.stalcraft.port.registry.ModCreativeTabs;
import ru.stalcraft.port.registry.ModItems;
import ru.stalcraft.port.registry.ModParticles;
import ru.stalcraft.port.registry.ModSounds;

@Mod(StalkerPortMod.MODID)
public class StalkerPortMod {
    public static final String MODID = "stalker_port";
    public static final Logger LOGGER = LogUtils.getLogger();

    public StalkerPortMod() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModBlocks.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModItems.register(modEventBus);
        ModParticles.register(modEventBus);
        ModSounds.register(modEventBus);
        ModCreativeTabs.register(modEventBus);

        modEventBus.addListener(this::buildCreativeTabContents);
    }

    private void buildCreativeTabContents(final BuildCreativeModeTabContentsEvent event) {
        ModItems.addToVanillaTab(event);
    }
}
