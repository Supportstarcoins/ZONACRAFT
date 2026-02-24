package ru.stalcraft.items;

import ru.stalcraft.StalkerMain;
import ru.stalcraft.clans.IClan;
import ru.stalcraft.clans.IFlag;
import ru.stalcraft.entity.EntityTurrel;
import ru.stalcraft.player.PlayerUtils;

public abstract class ItemTurrel extends yc {
   public ItemTurrel(int par1) {
      super(par1);
      super.cw = 1;
      this.a(StalkerMain.tab);
   }

   public boolean a(ye stack, uf player, abw world, int x, int y, int z, int side, float par8, float par9, float par10) {
      if (side == 1 && !world.I) {
         for (int clan = 0; clan < 9; clan++) {
            if (world.a(x - 1 + clan / 3, y + 1, z - 1 + clan % 3) != 0) {
               return false;
            }
         }

         IClan var14 = PlayerUtils.getInfo(player).getClan();
         if (var14 == null) {
            player.a("Вы не состоите в группировке!");
            return false;
         } else {
            IFlag flag = StalkerMain.flagManager.getFlagNearby(world.t.i, x, z);
            if (flag != null && flag.getClan() == var14) {
               EntityTurrel entity = this.getTurrel(
                  world, var14.getName(), asx.a(flag.getPosZ() - 16, 0.0, flag.getPosY() - 16, flag.getPosX() + 17, 256.0, flag.getPosZ() + 17)
               );
               entity.b(x + 0.5, y + 1, z + 0.5);
               world.d(entity);
               if (!player.bG.d) {
                  stack.b--;
               }

               return true;
            } else {
               player.a("Это не территория вашей группировки!");
               return false;
            }
         }
      } else {
         return false;
      }
   }

   protected abstract EntityTurrel getTurrel(abw var1, String var2, asx var3);
}
