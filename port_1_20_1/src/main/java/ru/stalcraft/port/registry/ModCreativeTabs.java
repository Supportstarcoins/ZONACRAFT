package ru.stalcraft.port.registry;

import net.minecraft.network.chat.Component;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import ru.stalcraft.port.StalkerPortMod;

public final class ModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, StalkerPortMod.MODID);

    public static final RegistryObject<CreativeModeTab> STALKER_PORT_TAB = CREATIVE_MODE_TABS.register("stalker_port", () -> CreativeModeTab.builder()
        .title(Component.translatable("itemGroup.stalker_port"))
        .icon(() -> ModItems.VODKA.get().getDefaultInstance())
        .displayItems((parameters, output) -> ModItems.getRegisteredItems().forEach(item -> output.accept(item.get())))
        .build());

    private ModCreativeTabs() {
    }

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
