package ru.zonecraft.npcglbcompat;

import net.minecraftforge.common.MinecraftForge;

public class CommonProxy {
    public void registerHandlers() {
        MinecraftForge.EVENT_BUS.register(new NpcStalkerWeaponControllerEvents());
        MinecraftForge.EVENT_BUS.register(new NpcVortexCompatEvents());
    }
}
