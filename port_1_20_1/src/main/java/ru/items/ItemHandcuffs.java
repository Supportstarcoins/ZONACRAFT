package ru.stalcraft.items;

import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ru.stalcraft.StalkerMain;
import ru.stalcraft.server.network.ServerPacketSender;

public class ItemHandcuffs extends yc {
   public ItemHandcuffs(int par1) {
      super(par1);
      this.a(StalkerMain.tab);
      this.b("handcuffs");
      LanguageRegistry.addName(this, "Наручники");
      this.d(1);
   }

   @SideOnly(Side.CLIENT)
   public void a(mt par1IconRegister) {
      super.cz = par1IconRegister.a("stalker:handcuffs");
   }

   public boolean a(ye stack, uf player, of entityLivingBase) {
      boolean isTargetPlayer = entityLivingBase instanceof uf;
      if (!player.q.I && isTargetPlayer) {
         ServerPacketSender.sendHandcuffsRequest(player, (uf)entityLivingBase);
      }

      return isTargetPlayer;
   }
}
