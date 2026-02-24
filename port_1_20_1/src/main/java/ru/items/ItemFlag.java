package ru.stalcraft.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ru.stalcraft.StalkerMain;
import ru.stalcraft.clans.ClanRank;
import ru.stalcraft.clans.IClan;
import ru.stalcraft.player.PlayerUtils;

public class ItemFlag extends zh {
   public ItemFlag(int par1) {
      super(par1);
   }

   @SideOnly(Side.CLIENT)
   public void a(mt par1IconRegister) {
      super.cz = par1IconRegister.a("stalker:flag");
   }

   @SideOnly(Side.CLIENT)
   public ms b_(int par1) {
      return super.cz;
   }

   @SideOnly(Side.CLIENT)
   public int l() {
      return 1;
   }

   public boolean a(ye par1ItemStack, uf player, abw par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10) {
      int x = par4;
      int z = par6;
      if (par7 == 2) {
         z = par6 - 1;
      }

      if (par7 == 3) {
         z++;
      }

      if (par7 == 4) {
         x = par4 - 1;
      }

      if (par7 == 5) {
         x++;
      }

      if (!player.q.I) {
         jv p = (jv)player;
         IClan clan = PlayerUtils.getInfo(player).getClan();
         if (clan == null) {
            player.a("Вы не состоите в группировке!");
            p.a(p.bo);
            return false;
         } else if (clan.getClanMember(player).getRank() == ClanRank.MEMBER) {
            player.a("Вы не имеете права устанавливать флаг!");
            p.a(p.bo);
            return false;
         } else if (clan.getMaxLandsCount() <= StalkerMain.flagManager.getClanFlags(clan).size() && !clan.isAdminClan()) {
            player.a("Ваша группировка уже захватила максимальное количество территорий. Для установки нового флага необходимо увеличить репутацию.");
            p.a(p.bo);
            return false;
         } else if (!StalkerMain.flagManager.canPlaceFlagHere(player.q.t.i, x, z)) {
            player.a("Невозможно установить флаг в данном месте.");
            p.a(p.bo);
            return false;
         } else {
            return super.a(par1ItemStack, player, par3World, par4, par5, par6, par7, par8, par9, par10);
         }
      } else {
         return false;
      }
   }
}
