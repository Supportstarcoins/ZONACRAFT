package ru.zonecraft.armorbootfix;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@Mod(
    modid = ArmorBootGroundFixMod.MODID,
    name = "Zonecraft Universal Armor Boot Ground Fix",
    version = ArmorBootGroundFixMod.VERSION,
    dependencies = "after:stalcraftglb;after:CustomNpcs",
    acceptedMinecraftVersions = "[1.6.4]"
)
public final class ArmorBootGroundFixMod {
    public static final String MODID = "zonecraftarmorbootfix";
    public static final String VERSION = "1.0.0";

    @SidedProxy(
        clientSide = "ru.zonecraft.armorbootfix.ClientProxy",
        serverSide = "ru.zonecraft.armorbootfix.CommonProxy"
    )
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        BootFixSettings.load(event.getModConfigurationDirectory());
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.registerHandlers();
        System.out.println("[Zonecraft Armor Boot Fix] 1.0.0 initialized; lift="
                + BootFixSettings.baseLiftBlocks + " blocks.");
    }
}
