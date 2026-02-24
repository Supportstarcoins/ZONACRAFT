package ru.zonacraft.port.registry;

import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import ru.zonacraft.port.ZonaCraftPortMod;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ZonaCraftPortMod.MODID);

    public static final RegistryObject<Item> ANOMALY_CORE = ITEMS.register("anomaly_core", () -> new Item(new Item.Properties()));

    private ModItems() {}
}
