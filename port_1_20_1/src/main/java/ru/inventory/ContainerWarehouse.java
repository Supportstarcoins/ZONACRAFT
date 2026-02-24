package ru.stalcraft.inventory;

import java.util.ArrayList;
import java.util.List;
import noppes.npcs.NoppesUtilPlayer;
import ru.stalcraft.clans.ClanMember;
import ru.stalcraft.client.ClientProxy;
import ru.stalcraft.client.gui.clans.GuiBaseWarehouse;
import ru.stalcraft.items.ItemArmorArtefakt;
import ru.stalcraft.items.ItemBullet;
import ru.stalcraft.items.ItemMedicine;
import ru.stalcraft.items.ItemWeapon;
import ru.stalcraft.player.PlayerUtils;
import ru.stalcraft.server.clans.BaseContent;
import ru.stalcraft.server.clans.ClanManager;
import ru.stalcraft.server.clans.Flag;
import ru.stalcraft.server.clans.FlagManager;
import ru.stalcraft.server.network.ServerPacketSender;

public class ContainerWarehouse extends uy {
   public uf player;
   public InventoryWarehouse inventory;
   public int baseID;

   public ContainerWarehouse(uf player, InventoryWarehouse inventory) {
      this.player = player;
      int i = 0;
      int j = 0;
      int k = 0;

      for (int var7 = 0; var7 < 3; var7++) {
         for (int var10 = 0; var10 < 9; var10++) {
            this.a(new we(player.bn, var10 + var7 * 9 + 9, 8 + var10 * 18, 88 + var7 * 18 + i));
         }
      }

      for (int var8 = 0; var8 < 9; var8++) {
         this.a(new we(player.bn, var8, 8 + var8 * 18, 146 + i));
      }

      for (int var9 = 0; var9 < 15; var9++) {
         for (int var11 = 0; var11 < 9; var11++) {
            int posY = 0;
            if (var9 > 2) {
               posY = 1000;
            }

            this.a(new SlotWarehouse(inventory, var11 + var9 * 9, 8 + var11 * 18, 20 + var9 * 18 + i + posY));
         }
      }

      this.inventory = inventory;
   }

   public void readList(List<Integer> list) {
      if (this.player.bG.d) {
         for (int i = 0; i < list.size(); i++) {
            ye itemStack = new ye(yc.g[list.get(i)], 1);
            by itemStackNBT = PlayerUtils.getTag(itemStack);
            yc item = itemStack.b();
            if (itemStackNBT.e("setClan") == 0) {
               if (this.player.q.I) {
                  itemStackNBT.a("clan", ClientProxy.clanData.thePlayerClan);
               } else {
                  itemStackNBT.a("clan", PlayerUtils.getInfo(this.player).getClan().getName());
               }
            }

            if (item instanceof ItemBullet) {
               itemStack.b = 64;
            } else if (item instanceof ItemMedicine) {
               itemStack.b = 8;
            } else if (item instanceof ItemWeapon || item instanceof ItemArmorArtefakt) {
               itemStackNBT.a("no_drop", 1);
               itemStackNBT.a("personal", 1);
            }

            this.inventory.contents[i] = itemStack;
         }
      }
   }

   public boolean a(uf entityplayer) {
      return true;
   }

   public void b(uf player) {
      ud inventoryplayer = player.bn;
      if (inventoryplayer.o() != null && PlayerUtils.getTag(inventoryplayer.o()).e("personal") == 0) {
         player.b(inventoryplayer.o());
         inventoryplayer.b((ye)null);
      } else if (inventoryplayer.o() != null) {
         inventoryplayer.a(inventoryplayer.o());
         inventoryplayer.g = null;
         player.a(new cv().a(a.e + "Невозможно выкинуть персональный предмет."));
      }

      if (!player.q.I && player.bG.d) {
         Flag flag = FlagManager.instance().getFlagById(this.baseID);
         List<BaseContent> baseContent = new ArrayList<>();

         for (int i = 0; i < 135; i++) {
            ye stack = this.inventory.contents[i];
            if (stack != null) {
               baseContent.add(new BaseContent(stack.d, 0, 0));
            }
         }

         if (flag != null && flag != null) {
            flag.baseContents = baseContent;
         }
      }
   }

   public void loadPage(int page) {
      int coord = page > 2 ? 27 : 0;

      for (int slotIndex = 36 + coord; slotIndex < 63 + coord * (page > 3 ? page : 1); slotIndex++) {
         SlotWarehouse slot = (SlotWarehouse)super.a(slotIndex);
         if (slot != null) {
            slot.i += 1000;
         }
      }

      for (int slotIndexx = 63 + coord; slotIndexx < 90 + coord * (page > 3 ? page : 1); slotIndexx++) {
         SlotWarehouse slot = (SlotWarehouse)super.a(slotIndexx);
         if (slot != null) {
            slot.prevPosY = slot.i;
            slot.i = slot.i - 1054 - (page > 2 ? 54 : 0);
         }
      }
   }

   public void deloadPage(int page) {
      int coord = page > 1 ? 27 : 0;

      for (int slotIndex = 36 + coord; slotIndex < 63 + coord * (page > 3 ? page : 1); slotIndex++) {
         SlotWarehouse slot = (SlotWarehouse)super.a(slotIndex);
         if (slot != null) {
            slot.i -= 1000;
         }
      }

      for (int slotIndexx = 63 + coord; slotIndexx < 90 + coord * (page > 3 ? page : 1); slotIndexx++) {
         SlotWarehouse slot = (SlotWarehouse)super.a(slotIndexx);
         if (slot != null) {
            if (page < 2) {
               slot.i += 1054;
            } else {
               slot.i = slot.prevPosY;
            }
         }
      }
   }

   public ye a(int slotIndex, int par2, int par3, uf player) {
      if (player.bG.d) {
         return super.a(slotIndex, par2, par3, player);
      } else if (slotIndex <= 35) {
         return super.a(slotIndex, par2, par3, player);
      } else {
         we slot = super.a(slotIndex);
         if (slot != null && (slot == null || slot.d() != null)) {
            if (slot == null || slot.d() == null) {
               return null;
            } else if (!player.q.I) {
               ye itemStack = slot.d();
               ye copyItemStack = itemStack.m();
               by itemStackNBT = PlayerUtils.getTag(copyItemStack);
               ClanMember clanMember = ClanManager.instance().getPlayerClan(player).getClanMember(player);
               if (itemStack != null && clanMember.loyalePoint >= itemStackNBT.e("prices")) {
                  this.givePlayer(copyItemStack, player);
                  ServerPacketSender.sendContentRemove(player, slotIndex);
                  Flag flag = FlagManager.instance().getFlagById(this.baseID);
                  clanMember.loyalePoint = clanMember.loyalePoint - itemStackNBT.e("prices");
                  this.inventory.contents[slotIndex - 36] = null;
                  flag.baseActiveContent.remove(slotIndex - 36);
                  player.bp = this;
               }

               return copyItemStack;
            } else if (player.q.I) {
               ye itemStack = slot.d();
               ye copyItemStack = itemStack.m();
               if (((GuiBaseWarehouse)atv.w().n).loyalePoint >= PlayerUtils.getTag(copyItemStack).e("price")) {
                  this.inventory.contents[slotIndex - 36] = null;
                  this.givePlayer(copyItemStack, player);
                  return copyItemStack;
               } else {
                  return null;
               }
            } else {
               return null;
            }
         } else {
            return null;
         }
      }
   }

   private void givePlayer(ye var1, uf var2) {
      ye var3 = var2.bn.o();
      if (var3 == null) {
         var2.bn.b(var1);
      } else if (NoppesUtilPlayer.compareItems(var3, var1, false)) {
         int var4 = var1.b;
         if (var4 > 0 && var4 + var3.b <= var3.e()) {
            var3.b += var4;
         }
      }
   }
}
