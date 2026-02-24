package ru.stalcraft.client;

import cpw.mods.fml.client.registry.KeyBindingRegistry.KeyHandler;
import cpw.mods.fml.common.TickType;
import java.util.EnumSet;
import ru.stalcraft.client.network.ClientPacketSender;
import ru.stalcraft.client.player.PlayerClientInfo;
import ru.stalcraft.items.ItemEnergy;
import ru.stalcraft.items.ItemMedicine;
import ru.stalcraft.items.ItemWeapon;
import ru.stalcraft.player.PlayerUtils;

public class ClientController extends KeyHandler {
   private EnumSet tickTypes = EnumSet.of(TickType.CLIENT);
   public atv mc = atv.w();
   private static boolean pressedFireModeUp;
   private static boolean pressedFireModeDown = pressedFireModeUp;

   public ClientController(ats[] keyBindings, boolean[] repeatings) {
      super(keyBindings, repeatings);
   }

   public String getLabel() {
      return "StalkerKey";
   }

   public void keyDown(EnumSet types, ats kb, boolean tickEnd, boolean isRepeat) {
      if (kb.e && this.mc.A && this.mc.h != null && tickEnd) {
         bdi player = this.mc.h;
         PlayerClientInfo playerInfo = (PlayerClientInfo)PlayerUtils.getInfo(player);
         ye stack = player.bn.a[player.bn.c];
         if (playerInfo.medicineCooldown != 0
            || kb.d != super.keyBindings[super.keyBindings.length - 4].d
               && kb.d != super.keyBindings[super.keyBindings.length - 3].d
               && kb.d != super.keyBindings[super.keyBindings.length - 2].d
               && kb.d != super.keyBindings[super.keyBindings.length - 1].d) {
            if (kb.d == super.keyBindings[0].d) {
               if (playerInfo.weaponInfo.currentGun != null) {
                  ClientPacketSender.sendMachineGunReloadRequest();
               } else if (stack != null && yc.g[stack.d] instanceof ItemWeapon && !playerInfo.weaponInfo.isReloading(stack)) {
                  ClientPacketSender.sendReloadRequest(0);
               }
            } else if (kb.d == super.keyBindings[1].d) {
               ClientPacketSender.sendFlashlightRequest();
            } else if (kb.d == super.keyBindings[2].d) {
               ClientProxy.displayClanGui();
            } else if (kb.d == super.keyBindings[3].d && stack != null && yc.g[stack.d] instanceof ItemWeapon) {
               ClientPacketSender.sendWeaponFireMode();
            }
         } else {
            this.handleNumberInput(kb, playerInfo);
         }
      }
   }

   private void handleNumberInput(ats kb, PlayerClientInfo playerInfo) {
      int number = 0;
      if (kb.d == super.keyBindings[super.keyBindings.length - 4].d) {
         number = 1;
      } else if (kb.d == super.keyBindings[super.keyBindings.length - 3].d) {
         number = 2;
      } else if (kb.d == super.keyBindings[super.keyBindings.length - 2].d) {
         number = 3;
      } else if (kb.d == super.keyBindings[super.keyBindings.length - 1].d) {
         number = 4;
      }

      if (number != 0 && playerInfo.stInv.mainInventory[number + 7] != null) {
         this.mc.v.a(((ItemMedicine)yc.g[playerInfo.stInv.mainInventory[number + 7].d]).sound, 1.0F, 1.0F);
         ClientPacketSender.sendUseMedicine(number);
         ItemMedicine item = (ItemMedicine)yc.g[playerInfo.stInv.mainInventory[number + 7].d];
         playerInfo.medicineCooldown = item.coolDown;
         if (yc.g[playerInfo.stInv.mainInventory[number + 7].d] instanceof ItemEnergy) {
            playerInfo.activeEnergyEffect = 440;
         }
      }
   }

   public void keyUp(EnumSet types, ats kb, boolean tickEnd) {
   }

   public EnumSet ticks() {
      return this.tickTypes;
   }
}
