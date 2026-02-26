package ru.stalcraft.port.registry;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import ru.stalcraft.port.StalkerPortMod;
import ru.stalcraft.port.anomaly.BaseAnomalyBlockEntity;

public final class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES,
        StalkerPortMod.MODID);

    public static final RegistryObject<BlockEntityType<BaseAnomalyBlockEntity>> ANOMALY = BLOCK_ENTITIES.register("anomaly",
        () -> BlockEntityType.Builder.of(BaseAnomalyBlockEntity::new,
            ModBlocks.CAROUSEL.get(),
            ModBlocks.TRAMPOLINE.get(),
            ModBlocks.TRANPOLINE.get(),
            ModBlocks.BLACK_HOLE.get(),
            ModBlocks.FUNNEL.get(),
            ModBlocks.FUNEL.get(),
            ModBlocks.LIGHTER.get(),
            ModBlocks.ELECTRA.get())
            .build(null));

    private ModBlockEntities() {
    }

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
