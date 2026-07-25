package ru.zonecraft.npcglbcompat;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;

@Mod(
    modid = ZonecraftNpcGlbCompatMod.MODID,
    name = "Zonecraft NPC Stalker Compatibility Bridge",
    version = ZonecraftNpcGlbCompatMod.VERSION,
    dependencies = "after:stalcraftglb;after:customnpcs;after:StalkerMod",
    acceptedMinecraftVersions = "[1.6.4]"
)
public final class ZonecraftNpcGlbCompatMod {
    public static final String MODID = "zonecraftnpcglbcompat";
    public static final String VERSION = "1.2.0";

    @SidedProxy(
        clientSide = "ru.zonecraft.npcglbcompat.ClientProxy",
        serverSide = "ru.zonecraft.npcglbcompat.CommonProxy"
    )
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.registerHandlers();
        System.out.println("[Zonecraft NPC GLB Compat] 1.2.0 initialized: "
                + "real ItemWeapon cadence/reload + no ammo-box projectiles + "
                + "all-faction vortex physics + held weapon render.");
    }
}
