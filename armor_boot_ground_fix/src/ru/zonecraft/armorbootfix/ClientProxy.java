package ru.zonecraft.armorbootfix;

import net.minecraftforge.common.MinecraftForge;

public final class ClientProxy extends CommonProxy {
    @Override
    public void registerHandlers() {
        MinecraftForge.EVENT_BUS.register(new BootGroundRenderEvents());
    }
}
