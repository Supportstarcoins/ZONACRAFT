package ru.zonacraft.port.registry;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import ru.zonacraft.port.ZonaCraftPortMod;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, ZonaCraftPortMod.MODID);

    public static final RegistryObject<Block> ANOMALY_DEBUG_BLOCK = BLOCKS.register("anomaly_debug_block",
            () -> new Block(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_PURPLE)
                    .strength(1.5F, 6.0F)
                    .sound(SoundType.METAL)));

    public static final RegistryObject<net.minecraft.world.item.BlockItem> ANOMALY_DEBUG_BLOCK_ITEM =
            ModItems.ITEMS.register("anomaly_debug_block", () -> new net.minecraft.world.item.BlockItem(
                    ANOMALY_DEBUG_BLOCK.get(),
                    new net.minecraft.world.item.Item.Properties()
            ));

    private ModBlocks() {}
}
