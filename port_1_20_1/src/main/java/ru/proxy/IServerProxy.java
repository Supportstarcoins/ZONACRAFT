package ru.stalcraft.proxy;

import cpw.mods.fml.common.event.FMLServerStartingEvent;
import ru.stalcraft.clans.IClanManager;
import ru.stalcraft.clans.IFlagManager;
import ru.stalcraft.player.IAntiRelog;
import ru.stalcraft.tile.TileEntityFlag;

public interface IServerProxy extends IProxy {
   void serverStart(FMLServerStartingEvent var1);

   IFlagManager getFlagManager();

   IClanManager getClanManager();

   IAntiRelog getAntiRelog();

   void getRespawn(uf var1, asp var2);

   void getPlayerProperties(uf var1, int var2, int var3, int var4);

   void onFlagCapture(TileEntityFlag var1);
}
