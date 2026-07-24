package ru.zonecraft.npcglbcompat;

import net.minecraftforge.common.MinecraftForge;

public final class ClientProxy extends CommonProxy {
    @Override
    public void registerHandlers() {
        super.registerHandlers();
        MinecraftForge.EVENT_BUS.register(new NpcHeldWeaponRenderEvents());
    }
}
