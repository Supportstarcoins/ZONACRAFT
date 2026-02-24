package ru.demon.pickup.handlers;

import cpw.mods.fml.client.registry.KeyBindingRegistry.KeyHandler;
import cpw.mods.fml.common.TickType;
import java.util.EnumSet;
import ru.demon.pickup.render.OverlayRenderer;
import ru.stalcraft.Logger;
import ru.stalcraft.blocks.BlockBaseWarehouse;
import ru.stalcraft.client.ClientTicker;
import ru.stalcraft.client.network.ClientPacketSender;
import ru.stalcraft.client.player.PlayerClientInfo;
import ru.stalcraft.items.ItemPNV;
import ru.stalcraft.items.ItemWeapon;
import ru.stalcraft.player.PlayerUtils;

public class DemonKeyHandler extends KeyHandler {
   public static ats pickUpKey = new ats("demon.pickUpKey.press", 33);
   public static ats fireModeKey = new ats("Режим стрельбы", 48);
   public static ats accept = new ats("Принять", 21);
   public static ats deny = new ats("Отменить", 49);
   public static ats pnv = new ats("ПНВ", 19);
   public static ats updateAmmo = new ats("Смена типа патронов", 46);

   public DemonKeyHandler() {
      super(new ats[]{pickUpKey, fireModeKey, accept, deny, pnv, updateAmmo}, new boolean[]{false, false, false, false, false, false});
   }

   public String getLabel() {
      return "Demon KeyHandler";
   }

   public void keyDown(EnumSet types, ats kb, boolean tickEnd, boolean isRepeat) {
      atv mc = atv.w();
      uf player = mc.h;
      if (tickEnd && kb.equals(pickUpKey)) {
         ata mouseOver = mc.t;
         if (player != null && mc.A) {
            OverlayRenderer.pickupItem();
            if (mouseOver != null && mouseOver.g != null) {
               ClientPacketSender.sendOpenGuiCorpse(mc.t.g.k);
            }
         }

         if (mouseOver != null && mouseOver.g == null && this.checkBlockPos(mouseOver) && mouseOver.a != null && mouseOver.a == atb.a && mc.A) {
            aqz block = aqz.s[mc.f.a(mouseOver.b, mouseOver.c, mouseOver.d)];
            if (block instanceof BlockBaseWarehouse) {
               ((BlockBaseWarehouse)block).isClickF(mc.f, mouseOver.b, mouseOver.c, mouseOver.d);
            }
         }
      }

      if (tickEnd && kb.equals(fireModeKey) && player != null && mc.n == null) {
         ClientTicker.tickFireMode = 20;
         ClientPacketSender.sendChanchingShootGrenadeLauncher();
      }

      if (tickEnd && kb.equals(accept) && player != null && mc.n == null) {
         PlayerClientInfo playerInfo = (PlayerClientInfo)PlayerUtils.getInfo(player);
         if (playerInfo.yesNoTick > 0) {
            ClientPacketSender.sendPlayerRespawn(playerInfo.position[0], playerInfo.position[1], playerInfo.position[2]);
            playerInfo.deny();
         }

         if (playerInfo.inviteTimeValid > 0) {
         }
      }

      if (tickEnd && kb.equals(deny) && player != null && mc.n == null) {
         PlayerClientInfo playerInfox = (PlayerClientInfo)PlayerUtils.getInfo(player);
         if (playerInfox.yesNoTick > 0) {
            playerInfox.deny();
         }
      }

      if (tickEnd && kb.equals(pnv) && player != null && mc.n == null) {
         ye stackArmor = player.o(3);
         if (stackArmor != null && stackArmor.b() instanceof ItemPNV) {
            ClientTicker.isPNV = !ClientTicker.isPNV;
         }
      }

      if (tickEnd && kb.equals(updateAmmo) && player != null && mc.n == null) {
         ye stackWeapon = player.by();
         if (stackWeapon != null && stackWeapon.b() instanceof ItemWeapon) {
            ItemWeapon itemWeapon = (ItemWeapon)stackWeapon.b();
            by stackWeaponNBT = PlayerUtils.getTag(stackWeapon);
            int ammoType = stackWeaponNBT.e("ammoType");
            Logger.console(ammoType);
            if (itemWeapon.bulletsID[1] != 0) {
               if (ammoType == 0 && PlayerUtils.hasItem(player, itemWeapon.bulletsID[1])) {
                  ClientPacketSender.sendAmmoType(++ammoType);
               } else if (ammoType == 1 && PlayerUtils.hasItem(player, itemWeapon.bulletsID[0])) {
                  ClientPacketSender.sendAmmoType(--ammoType);
               }
            }
         }
      }
   }

   public void keyUp(EnumSet types, ats kb, boolean tickEnd) {
   }

   public EnumSet ticks() {
      return EnumSet.of(TickType.CLIENT);
   }

   public boolean checkBlockPos(ata mouseOver) {
      return mouseOver.b != 0 && mouseOver.c != 0 && mouseOver.d != 0;
   }
}
