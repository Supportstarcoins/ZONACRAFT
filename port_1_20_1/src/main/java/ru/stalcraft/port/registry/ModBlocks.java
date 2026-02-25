package ru.stalcraft.port.registry;

import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import ru.stalcraft.port.StalkerPortMod;
import ru.stalcraft.port.anomaly.AnomalyType;
import ru.stalcraft.port.anomaly.BaseAnomalyBlock;

public final class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, StalkerPortMod.MODID);

    public static final RegistryObject<Block> CAROUSEL = registerAnomalyBlock("carousel", AnomalyType.CAROUSEL);
    public static final RegistryObject<Block> TRAMPOLINE = registerAnomalyBlock("trampoline", AnomalyType.TRAMPOLINE);
    public static final RegistryObject<Block> BLACK_HOLE = registerAnomalyBlock("black_hole", AnomalyType.BLACK_HOLE);
    public static final RegistryObject<Block> LIGHTER = registerAnomalyBlock("lighter", AnomalyType.LIGHTER);
    public static final RegistryObject<Block> ELECTRA = registerAnomalyBlock("electra", AnomalyType.ELECTRA);

    private ModBlocks() {
    }

    private static RegistryObject<Block> registerAnomalyBlock(String id, AnomalyType anomalyType) {
        RegistryObject<Block> block = BLOCKS.register(id, () -> new BaseAnomalyBlock(anomalyType));
        ModItems.registerBlockItem(id, block);
        return block;
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
