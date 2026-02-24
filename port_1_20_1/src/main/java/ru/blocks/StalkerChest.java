package ru.stalcraft.blocks;

import ru.stalcraft.StalkerMain;
import ru.stalcraft.clans.IFlag;
import ru.stalcraft.player.PlayerUtils;

public class StalkerChest extends ank {
   public StalkerChest(int par1) {
      super(par1, 0);
   }

   public boolean a(abw world, int x, int y, int z, uf player, int side, float x1, float y1, float z1) {
      if (world.I) {
         return true;
      } else {
         IFlag flag = StalkerMain.flagManager.getFlagNearby(player.ar, x, z);
         return flag != null && flag.getClan() != PlayerUtils.getInfo(player).getClan() ? false : super.a(world, x, y, z, player, side, x1, y1, z1);
      }
   }
}
