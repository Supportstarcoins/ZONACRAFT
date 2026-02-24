package ru.stalcraft.items;

import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ru.stalcraft.StalkerMain;
import ru.stalcraft.player.PlayerInfo;
import ru.stalcraft.player.PlayerUtils;

public class ItemRope extends yc {
   public ItemRope(int par1) {
      super(par1);
      this.a(StalkerMain.tab);
      this.b("stalker_rope");
      LanguageRegistry.addName(this, "Веревка");
      this.d(1);
   }

   @SideOnly(Side.CLIENT)
   public void a(mt par1IconRegister) {
      super.cz = par1IconRegister.a("stalker:rope");
   }

   public boolean a(ye stack, uf player, of entityLivingBase) {
      boolean isTargetPlayer = entityLivingBase instanceof uf;
      if (!player.q.I && isTargetPlayer) {
         uf target = (uf)entityLivingBase;
         PlayerInfo info = PlayerUtils.getInfo(target);
         info.itemInteractionForEntity(target);
      }

      return isTargetPlayer;
   }
}
