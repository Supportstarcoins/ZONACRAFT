package ru.stalcraft.server;

import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import net.minecraft.server.MinecraftServer;
import ru.stalcraft.StalkerMain;
import ru.stalcraft.player.PlayerUtils;
import ru.stalcraft.server.clans.ClanManager;
import ru.stalcraft.tile.TileEntityAnomaly;
import ru.stalcraft.tile.TileEntityExtendedAnomaly;

public class ServerTicker implements ITickHandler {
   public static ArrayList anomaliesToCheck = new ArrayList();
   public static HashMap itemsToAdd = new HashMap();
   public static long tickId = 0L;
   private static int lastTickCounter = -1;

   public void tickStart(EnumSet type, Object... tickData) {
      js worldServer = (js)MinecraftServer.F().f_();
      Iterator iterator = null;
      int i = 0;
      if (CommonProxy.serverShopData != null) {
         CommonProxy.serverShopData.tick();
      }

      if (lastTickCounter != MinecraftServer.F().aj()) {
         iterator = itemsToAdd.entrySet().iterator();
         Entry entry1 = null;
         ye stack = null;

         while (iterator.hasNext()) {
            entry1 = (Entry)iterator.next();

            for (ye var13 : (ArrayList)entry1.getValue()) {
               PlayerUtils.addItem((uf)entry1.getKey(), var13);
            }

            ((jv)entry1.getKey()).a(((uf)entry1.getKey()).bo);
         }

         itemsToAdd.clear();
         if (StalkerMain.getProxy().getEjectionManager() != null) {
            StalkerMain.getProxy().getEjectionManager().tick();
         }

         if (ClanManager.instance() != null) {
            ClanManager.instance().onUpdate(worldServer);
         }

         tickId++;
         lastTickCounter = MinecraftServer.F().aj();
         CommonProxy.antiRelog.tick();
      }

      asp tile = null;

      for (int var9 = 0; var9 < worldServer.g.size(); var9++) {
         tile = (asp)worldServer.g.get(var9);
         if (tile instanceof TileEntityAnomaly && ((TileEntityAnomaly)tile).reloadTime > 0) {
            ((TileEntityAnomaly)tile).reloadTime--;
         }
      }
   }

   public void tickEnd(EnumSet type, Object... tickData) {
      ArrayList copyOfList = (ArrayList)anomaliesToCheck.clone();
      anomaliesToCheck.clear();
      Iterator iterator = copyOfList.iterator();
      TileEntityExtendedAnomaly tileExtendedAnomaly = null;

      while (iterator.hasNext()) {
         tileExtendedAnomaly = (TileEntityExtendedAnomaly)iterator.next();
         if (!tileExtendedAnomaly.r()) {
            tileExtendedAnomaly.addNeighborBlocks();
         }
      }
   }

   public EnumSet ticks() {
      return EnumSet.of(TickType.SERVER);
   }

   public String getLabel() {
      return "StalkerServer";
   }
}
