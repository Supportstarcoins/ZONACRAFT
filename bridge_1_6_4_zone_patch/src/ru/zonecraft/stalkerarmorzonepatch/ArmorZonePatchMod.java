package ru.zonecraft.stalkerarmorzonepatch;

import java.io.File;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.common.MinecraftForge;

@Mod(
    modid = ArmorZonePatchMod.MODID,
    name = "Zonecraft Armor Zone & Tooltip Patch",
    version = ArmorZonePatchMod.VERSION,
    dependencies = "required-after:zonecraftstalkerarmorbridge;after:stalcraftglb;after:StalkerMod",
    acceptedMinecraftVersions = "[1.6.4]"
)
public final class ArmorZonePatchMod {
    public static final String MODID = "zonecraftarmorzonepatch";
    public static final String VERSION = "1.0.3";

    private static File configDirectory;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        configDirectory = event.getModConfigurationDirectory();
        PatchSettings.initialize(new File(configDirectory, "zonecraftstalkerarmorzones.cfg"));
        BridgeProfiles.initialize(configDirectory);
        System.out.println("[Zonecraft Armor Zone Patch] PREINIT 1.0.3 OK");
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new ZonePatchEvents());
        System.out.println("[Zonecraft Armor Zone Patch] Public Forge event handler registered.");
    }

    public static File getConfigDirectory() {
        return configDirectory;
    }
}
