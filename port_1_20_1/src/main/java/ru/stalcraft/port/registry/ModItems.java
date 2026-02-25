package ru.stalcraft.port.registry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import ru.stalcraft.port.StalkerPortMod;

public final class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, StalkerPortMod.MODID);

    private static final List<RegistryObject<Item>> VANILLA_TAB_ITEMS = new ArrayList<>();

    public static final RegistryObject<Item> RADIATION_DETECTOR = registerItem("radiation_detector");
    public static final RegistryObject<Item> CHEMICAL_DETECTOR = registerItem("chemical_detector");
    public static final RegistryObject<Item> BIOLOGICAL_DETECTOR = registerItem("biological_detector");
    public static final RegistryObject<Item> VODKA = registerItem("vodka");
    public static final RegistryObject<Item> EMPTY_BOTTLE = registerItem("empty_bottle");
    public static final RegistryObject<Item> BACKPACK_A = registerItem("backpack_a");
    public static final RegistryObject<Item> HANDCUFFS = registerItem("handcuffs");
    public static final RegistryObject<Item> ROPE = registerItem("rope");
    public static final RegistryObject<Item> KEY = registerItem("key");
    public static final RegistryObject<Item> BOLT = registerItem("bolt");

    private ModItems() {
    }

    private static RegistryObject<Item> registerItem(String id) {
        RegistryObject<Item> item = ITEMS.register(id, () -> new Item(new Item.Properties()));
        VANILLA_TAB_ITEMS.add(item);
        return item;
    }

    public static RegistryObject<Item> registerBlockItem(String id, Supplier<? extends Block> blockSupplier) {
        RegistryObject<Item> item = ITEMS.register(id, () -> new BlockItem(blockSupplier.get(), new Item.Properties()));
        VANILLA_TAB_ITEMS.add(item);
        return item;
    }

    public static void addToVanillaTab(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.INGREDIENTS) {
            VANILLA_TAB_ITEMS.forEach(event::accept);
        }
    }

    public static List<RegistryObject<Item>> getRegisteredItems() {
        return Collections.unmodifiableList(VANILLA_TAB_ITEMS);
    }

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
