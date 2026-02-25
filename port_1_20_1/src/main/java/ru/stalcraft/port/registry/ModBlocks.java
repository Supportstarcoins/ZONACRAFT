package ru.stalcraft.port.registry;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import ru.stalcraft.port.StalkerPortMod;

public final class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, StalkerPortMod.MODID);

    public static final RegistryObject<Block> CAROUSEL = registerBasicAnomalyBlock("carousel");
    public static final RegistryObject<Block> TRAMPOLINE = registerBasicAnomalyBlock("trampoline");
    public static final RegistryObject<Block> BLACK_HOLE = registerBasicAnomalyBlock("black_hole");
    public static final RegistryObject<Block> LIGHTER = registerBasicAnomalyBlock("lighter");
    public static final RegistryObject<Block> ELECTRA = registerBasicAnomalyBlock("electra");

    private ModBlocks() {
    }

    private static RegistryObject<Block> registerBasicAnomalyBlock(String id) {
        // TODO(porting): replace base Block with dedicated anomaly block behavior from legacy implementation.
        RegistryObject<Block> block = BLOCKS.register(id, () -> new Block(BlockBehaviour.Properties.of()
            .mapColor(MapColor.COLOR_PURPLE)
            .strength(1.5F, 6.0F)
            .sound(SoundType.STONE)
            .requiresCorrectToolForDrops()));

        ModItems.registerBlockItem(id, block);
        return block;
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
