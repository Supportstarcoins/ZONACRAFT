package ru.zonecraft.npcglbcompat;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;

@Mod(
    modid = ZonecraftNpcGlbCompatMod.MODID,
    name = "Zonecraft NPC Stalker Compatibility Bridge",
    version = ZonecraftNpcGlbCompatMod.VERSION,
    dependencies = "after:stalcraftglb;after:CustomNpcs;after:StalkerMod",
    acceptedMinecraftVersions = "[1.6.4]"
)
public final class ZonecraftNpcGlbCompatMod {
    public static final String MODID = "zonecraftnpcglbcompat";
    public static final String VERSION = "1.1.0";

    @SidedProxy(
        clientSide = "ru.zonecraft.npcglbcompat.ClientProxy",
        serverSide = "ru.zonecraft.npcglbcompat.CommonProxy"
    )
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.registerHandlers();
        System.out.println("[Zonecraft NPC Stalker Compat] 1.1.0 initialized.");
    }
}
