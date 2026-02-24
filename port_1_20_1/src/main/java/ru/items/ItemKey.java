package ru.stalcraft.items;

import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ru.stalcraft.StalkerMain;
import ru.stalcraft.player.PlayerInfo;
import ru.stalcraft.player.PlayerUtils;

public class ItemKey extends yc {
   public ItemKey(int par1) {
      super(par1);
      this.a(StalkerMain.tab);
      this.b("key");
      LanguageRegistry.addName(this, "Ключ");
      this.d(1);
   }

   @SideOnly(Side.CLIENT)
   public void a(mt par1IconRegister) {
      super.cz = par1IconRegister.a("stalker:key");
   }

   public boolean a(ye stack, uf player, of entityLivingBase) {
      boolean isTargetPlayer = entityLivingBase instanceof uf;
      if (!player.q.I && isTargetPlayer) {
         uf target = (uf)entityLivingBase;
         PlayerInfo info = PlayerUtils.getInfo(target);
         if (info.getHandcuffs()) {
            target.a("С вас снял наручники игрок " + player.bu);
            player.a("Вы сняли наручники с игрока " + target.bu);
            info.setHandcuffs(false);
            player.c(0, (ye)null);
         }
      }

      return isTargetPlayer;
   }
}
