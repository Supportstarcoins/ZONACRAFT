package ru.stalcraft.inventory;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.Iterator;
import ru.stalcraft.StalkerMain;
import ru.stalcraft.client.gui.GuiWeaponUpgrade;
import ru.stalcraft.items.ItemArmorArtefakt;
import ru.stalcraft.items.ItemWeapon;
import ru.stalcraft.player.IPlayerServerInfo;
import ru.stalcraft.player.PlayerInfo;
import ru.stalcraft.player.PlayerUtils;
import ru.stalcraft.server.network.ServerPacketSender;

public class StalkerContainer extends uy implements ICustomContainer {
   public vk craftMatrix = new vk(this, 2, 2);
   public mo craftResult = new wc();
   public boolean isLocalWorld = false;
   protected final uf player;
   public final PlayerInfo info;
   private boolean hasBackpackSlots = false;
   public ArrayList backpackSlots = new ArrayList();
   public ArrayList armorSlots = new ArrayList();
   public ArrayList leftSlots = new ArrayList();
   public ArrayList hotbarSlots = new ArrayList();
   public SlotBackpack backpackSlot;

   public StalkerContainer(ud par1InventoryPlayer, boolean par2, uf par3EntityPlayer) {
      this.isLocalWorld = par2;
      this.player = par3EntityPlayer;
      this.info = PlayerUtils.getInfo(par3EntityPlayer);
      this.hotbarSlots.add(new StalkerSlot(this, this, par1InventoryPlayer, 0, 26, 26));
      this.hotbarSlots.add(new StalkerSlot(this, this, par1InventoryPlayer, 1, 26, 44));
      this.hotbarSlots.add(new StalkerSlot(this, this, par1InventoryPlayer, 2, 62, 26));
      this.hotbarSlots.add(new StalkerSlot(this, this, par1InventoryPlayer, 3, 62, 44));

      for (we slot1 : this.hotbarSlots) {
         this.a(slot1);
      }

      for (int i = 0; i < 4; i++) {
         for (int j = 0; j < 8; j++) {
            this.a(new StalkerSlot(this, this, par1InventoryPlayer, i * 8 + j + 4, 125 + i * 18, 16 + j * 18));
         }
      }

      for (int var8 = 0; var8 < 4; var8++) {
         SlotCustomArmor var8x = new SlotCustomArmor(this.player, this, this, par1InventoryPlayer, par1InventoryPlayer.j_() - 1 - var8, 44, 8 + var8 * 18, var8);
         this.a(var8x);
         this.armorSlots.add(var8x);
         this.leftSlots.add(var8x);
      }

      for (int var9 = 0; var9 < 5; var9++) {
         ArtefaktSlot var9x = new ArtefaktSlot(this, this, this.info.stInv, var9, 8 + var9 * 18, 108);
         this.a(var9x);
         this.leftSlots.add(var9x);
      }

      for (int var10 = 0; var10 < 3; var10++) {
         SlotDetector var10x = new SlotDetector(this, this, this.info.stInv, var10 + 5, 26 + var10 * 18, 131);
         this.a(var10x);
         this.leftSlots.add(var10x);
      }

      for (int var111 = 0; var111 < 4; var111++) {
         SlotMedicine var11x = new SlotMedicine(this, this, this.info.stInv, var111 + 8, 17 + var111 * 18, 85);
         this.a(var11x);
         this.leftSlots.add(var11x);
      }

      this.backpackSlot = new SlotBackpack(this.player, this, this, this.info.stInv, 12, 44, 155);
      this.a(this.backpackSlot);
      this.leftSlots.add(this.backpackSlot);
      this.a(this.craftMatrix);
      this.hasBackpackSlots = this.hasBackpack();
   }

   public void a(vi par1ICrafting) {
      if (!this.e.contains(par1ICrafting)) {
         this.e.add(par1ICrafting);
      }

      par1ICrafting.a(this, this.a());
      this.b();
   }

   public void a(mo par1IInventory) {
      if (this.player != null) {
         this.craftResult.a(0, aaf.a().a(this.craftMatrix, this.player.q));
      }
   }

   public we a(int par1) {
      return par1 < super.c.size() ? (we)super.c.get(par1) : null;
   }

   public boolean a(uf par1EntityPlayer) {
      return true;
   }

   public ye b(uf par1EntityPlayer, int par2) {
      return null;
   }

   public ye a(int par1, int par2, int par3, uf par4EntityPlayer) {
      if (par1 >= 0 && this.a(par1) != null && !this.isSlotActive(this.a(par1))) {
         return null;
      } else if (par2 == 1 && par1 >= 0 && par4EntityPlayer != this.info.player && this.a(par1) != null && this.a(par1).e()) {
         if (par4EntityPlayer.bn.a(this.a(par1).d())) {
            this.a(par1).c((ye)null);
         } else if (par4EntityPlayer.q.I) {
            par4EntityPlayer.a("Ваш инвентарь заполнен!");
         }

         return null;
      } else if (par1 >= 0 && par2 == 1 && this.a(par1) != null && this.a(par1).e() && this.a(par1).d().b() instanceof ItemWeapon) {
         we slot = this.a(par1);
         ye stack = slot.d();
         if (!par4EntityPlayer.q.I) {
            par4EntityPlayer.openGui(StalkerMain.instance, 1, this.player.q, par1, 0, 0);
            ServerPacketSender.sendWindowId(par4EntityPlayer, par4EntityPlayer.bp.d);
         }

         return null;
      } else if (par1 >= 0 && par2 == 1 && this.a(par1) != null && this.a(par1).e() && this.a(par1).d().b() instanceof ItemArmorArtefakt) {
         we slot = this.a(par1);
         ye stack = slot.d();
         if (!par4EntityPlayer.q.I) {
            par4EntityPlayer.openGui(StalkerMain.instance, 6, this.player.q, par1, 0, 0);
            ServerPacketSender.sendWindowId(par4EntityPlayer, par4EntityPlayer.bp.d);
         }

         return null;
      } else {
         return this.slotClicked(par1, par2, par3, par4EntityPlayer);
      }
   }

   public ye slotClicked(int par1, int par2, int par3, uf par4EntityPlayer) {
      ye itemstack = null;
      ud inventoryplayer = par4EntityPlayer.bn;
      if (par3 == 5) {
         int i1 = this.g;
         this.g = c(par2);
         if ((i1 != 1 || this.g != 2) && i1 != this.g) {
            this.d();
         } else if (inventoryplayer.o() == null) {
            this.d();
         } else if (this.g == 0) {
            this.f = b(par2);
            if (d(this.f)) {
               this.g = 1;
               this.h.clear();
            } else {
               this.d();
            }
         } else if (this.g == 1) {
            we slot = (we)this.c.get(par1);
            if (slot != null && a(slot, inventoryplayer.o(), true) && slot.a(inventoryplayer.o()) && inventoryplayer.o().b > this.h.size() && this.b(slot)) {
               this.h.add(slot);
            }
         } else if (this.g == 2) {
            if (!this.h.isEmpty()) {
               ye itemstack1 = inventoryplayer.o().m();
               int l = inventoryplayer.o().b;

               for (we slot1 : this.h) {
                  if (slot1 != null
                     && a(slot1, inventoryplayer.o(), true)
                     && slot1.a(inventoryplayer.o())
                     && inventoryplayer.o().b >= this.h.size()
                     && this.b(slot1)) {
                     ye itemstack2 = itemstack1.m();
                     int j1 = slot1.e() ? slot1.d().b : 0;
                     a(this.h, this.f, itemstack2, j1);
                     if (itemstack2.b > itemstack2.e()) {
                        itemstack2.b = itemstack2.e();
                     }

                     if (itemstack2.b > slot1.a()) {
                        itemstack2.b = slot1.a();
                     }

                     l -= itemstack2.b - j1;
                     slot1.c(itemstack2);
                  }
               }

               itemstack1.b = l;
               if (itemstack1.b <= 0) {
                  itemstack1 = null;
               }

               inventoryplayer.b(itemstack1);
            }

            this.d();
         } else {
            this.d();
         }
      } else if (this.g != 0) {
         this.d();
      } else if ((par3 == 0 || par3 == 1) && (par2 == 0 || par2 == 1)) {
         if (par1 == -999) {
            if (inventoryplayer.o() != null && par1 == -999) {
               by tag = PlayerUtils.getTag(inventoryplayer.o());
               if (tag.e("personal") == 0) {
                  if (par2 == 0) {
                     par4EntityPlayer.b(inventoryplayer.o());
                     inventoryplayer.b((ye)null);
                  }

                  if (par2 == 1) {
                     par4EntityPlayer.b(inventoryplayer.o().a(1));
                     if (inventoryplayer.o().b == 0) {
                        inventoryplayer.b((ye)null);
                     }
                  }
               } else {
                  par4EntityPlayer.a(new cv().a(a.e + "Невозможно выкинуть персональный предмет."));
               }
            }
         } else if (par3 == 1) {
            if (par1 < 0) {
               return null;
            }

            we slot2 = (we)this.c.get(par1);
            if (slot2 != null && slot2.a(par4EntityPlayer)) {
               ye itemstack1 = this.b(par4EntityPlayer, par1);
               if (itemstack1 != null) {
                  int l = itemstack1.d;
                  itemstack = itemstack1.m();
                  if (slot2 != null && slot2.d() != null && slot2.d().d == l) {
                     this.a(par1, par2, true, par4EntityPlayer);
                  }
               }
            }
         } else {
            if (par1 < 0) {
               return null;
            }

            we slot2 = (we)this.c.get(par1);
            if (slot2 != null) {
               ye itemstack1 = slot2.d();
               ye itemstack4 = inventoryplayer.o();
               if (itemstack1 != null) {
                  itemstack = itemstack1.m();
               }

               if (itemstack1 == null) {
                  if (itemstack4 != null && slot2.a(itemstack4)) {
                     int k1 = par2 == 0 ? itemstack4.b : 1;
                     if (k1 > slot2.a()) {
                        k1 = slot2.a();
                     }

                     if (itemstack4.b >= k1) {
                        slot2.c(itemstack4.a(k1));
                     }

                     if (itemstack4.b == 0) {
                        inventoryplayer.b((ye)null);
                     }
                  }
               } else if (slot2.a(par4EntityPlayer)) {
                  if (itemstack4 == null) {
                     int k1x = par2 == 0 ? itemstack1.b : (itemstack1.b + 1) / 2;
                     ye itemstack3 = slot2.a(k1x);
                     inventoryplayer.b(itemstack3);
                     if (itemstack1.b == 0) {
                        slot2.c((ye)null);
                     }

                     slot2.a(par4EntityPlayer, inventoryplayer.o());
                  } else if (slot2.a(itemstack4)) {
                     if (itemstack1.d == itemstack4.d && itemstack1.k() == itemstack4.k() && ye.a(itemstack1, itemstack4)) {
                        int k1x = par2 == 0 ? itemstack4.b : 1;
                        if (k1x > slot2.a() - itemstack1.b) {
                           k1x = slot2.a() - itemstack1.b;
                        }

                        if (k1x > itemstack4.e() - itemstack1.b) {
                           k1x = itemstack4.e() - itemstack1.b;
                        }

                        itemstack4.a(k1x);
                        if (itemstack4.b == 0) {
                           inventoryplayer.b((ye)null);
                        }

                        itemstack1.b += k1x;
                     } else if (itemstack4.b <= slot2.a()) {
                        slot2.c(itemstack4);
                        inventoryplayer.b(itemstack1);
                     }
                  } else if (itemstack1.d == itemstack4.d
                     && itemstack4.e() > 1
                     && (!itemstack1.h() || itemstack1.k() == itemstack4.k())
                     && ye.a(itemstack1, itemstack4)) {
                     int k1xx = itemstack1.b;
                     if (k1xx > 0 && k1xx + itemstack4.b <= itemstack4.e()) {
                        itemstack4.b += k1xx;
                        itemstack1 = slot2.a(k1xx);
                        if (itemstack1.b == 0) {
                           slot2.c((ye)null);
                        }

                        slot2.a(par4EntityPlayer, inventoryplayer.o());
                     }
                  }
               }

               slot2.f();
            }
         }
      } else if (par3 == 2 && par2 >= 0 && par2 < 9) {
         we slot2 = (we)this.c.get(par1);
         if (slot2.a(par4EntityPlayer)) {
            ye itemstack1x = inventoryplayer.a(par2);
            boolean flag = itemstack1x == null || slot2.f == inventoryplayer && slot2.a(itemstack1x);
            int k1xx = -1;
            if (!flag) {
               k1xx = inventoryplayer.j();
               flag |= k1xx > -1;
            }

            if (slot2.e() && flag) {
               ye itemstack3 = slot2.d();
               inventoryplayer.a(par2, itemstack3.m());
               if ((slot2.f != inventoryplayer || !slot2.a(itemstack1x)) && itemstack1x != null) {
                  if (k1xx > -1) {
                     inventoryplayer.a(itemstack1x);
                     slot2.a(itemstack3.b);
                     slot2.c((ye)null);
                     slot2.a(par4EntityPlayer, itemstack3);
                  }
               } else {
                  slot2.a(itemstack3.b);
                  slot2.c(itemstack1x);
                  slot2.a(par4EntityPlayer, itemstack3);
               }
            } else if (!slot2.e() && itemstack1x != null && slot2.a(itemstack1x)) {
               inventoryplayer.a(par2, (ye)null);
               slot2.c(itemstack1x);
            }
         }
      } else if (par3 == 3 && par4EntityPlayer.bG.d && inventoryplayer.o() == null && par1 >= 0) {
         we slot2 = (we)this.c.get(par1);
         if (slot2 != null && slot2.e()) {
            ye itemstack1xx = slot2.d().m();
            itemstack1xx.b = itemstack1xx.e();
            inventoryplayer.b(itemstack1xx);
         }
      } else if (par3 == 4 && inventoryplayer.o() == null && par1 >= 0) {
         we slot2 = (we)this.c.get(par1);
         if (slot2 != null && slot2.e() && slot2.a(par4EntityPlayer)) {
            by tag = PlayerUtils.getTag(inventoryplayer.o());
            if (tag.e("personal") == 0) {
               ye itemstack1xx = slot2.a(par2 == 0 ? 1 : slot2.d().b);
               slot2.a(par4EntityPlayer, itemstack1xx);
               par4EntityPlayer.b(itemstack1xx);
            } else {
               par4EntityPlayer.a(new cv().a(a.e + "Невозможно выкинуть персональный предмет."));
            }
         }
      } else if (par3 == 6 && par1 >= 0) {
         we slot2 = (we)this.c.get(par1);
         ye itemstack1xx = inventoryplayer.o();
         if (itemstack1xx != null && (slot2 == null || !slot2.e() || !slot2.a(par4EntityPlayer))) {
            int l = par2 == 0 ? 0 : this.c.size() - 1;
            int k1xxx = par2 == 0 ? 1 : -1;

            for (int l1 = 0; l1 < 2; l1++) {
               for (int i2 = l; i2 >= 0 && i2 < this.c.size() && itemstack1xx.b < itemstack1xx.e(); i2 += k1xxx) {
                  we slot3 = (we)this.c.get(i2);
                  if (slot3.e()
                     && a(slot3, itemstack1xx, true)
                     && slot3.a(par4EntityPlayer)
                     && this.a(itemstack1xx, slot3)
                     && (l1 != 0 || slot3.d().b != slot3.d().e())) {
                     int j2 = Math.min(itemstack1xx.e() - itemstack1xx.b, slot3.d().b);
                     ye itemstack5 = slot3.a(j2);
                     itemstack1xx.b += j2;
                     if (itemstack5.b <= 0) {
                        slot3.c((ye)null);
                     }

                     slot3.a(par4EntityPlayer, itemstack5);
                  }
               }
            }
         }

         this.b();
      }

      return itemstack;
   }

   @SideOnly(Side.CLIENT)
   private void displayUpgradeGui(WeaponContainer container) {
      atv.w().a(new GuiWeaponUpgrade(container));
   }

   public boolean a(ye par1ItemStack, we par2Slot) {
      return par2Slot.f != this.craftResult && super.a(par1ItemStack, par2Slot);
   }

   @Override
   public boolean hasBackpack() {
      return this.backpackSlot.e();
   }

   @Override
   public void handleBackpackChanged(boolean hasBackpack) {
      if (this.hasBackpackSlots != hasBackpack) {
         this.hasBackpackSlots = hasBackpack;
         if (!hasBackpack && !this.isLocalWorld) {
            ArrayList inBackpack = new ArrayList();

            for (int it = 53; it < 61; it++) {
               if (this.a(it).e()) {
                  inBackpack.add(this.a(it).d());
                  this.a(it, (ye)null);
               }
            }

            Iterator var7 = super.b.iterator();

            while (var7.hasNext()) {
               ye i$ = (ye)var7.next();
               if (i$ != null && inBackpack.contains(i$)) {
                  var7.remove();
               }
            }

            for (ye stack : inBackpack) {
               PlayerUtils.addItem(this.player, stack);
            }

            this.b();
         }
      }
   }

   @Override
   public ArrayList getBackpackSlots() {
      return this.backpackSlots;
   }

   @Override
   public ArrayList getArmorSlots() {
      return this.armorSlots;
   }

   @Override
   public of getOwner() {
      return this.player;
   }

   public void b() {
      boolean hasItemsChanged = false;

      for (int i = 0; i < super.c.size(); i++) {
         ye itemstack = ((we)super.c.get(i)).d();
         ye itemstack1 = (ye)super.b.get(i);
         if (!ye.b(itemstack1, itemstack)) {
            hasItemsChanged = true;
            itemstack1 = itemstack == null ? null : itemstack.m();
            super.b.set(i, itemstack1);

            for (int j = 0; j < super.e.size(); j++) {
               ((vi)super.e.get(j)).a(this, i, itemstack1);
            }
         }
      }

      if (hasItemsChanged && this.info instanceof IPlayerServerInfo) {
         IPlayerServerInfo par2 = (IPlayerServerInfo)this.info;
         par2.updateWeightSpeed();
      }
   }

   public void b(uf par1EntityPlayer) {
      ud inventoryplayer = par1EntityPlayer.bn;
      if (inventoryplayer.o() != null && PlayerUtils.getTag(inventoryplayer.o()).e("personal") == 0) {
         par1EntityPlayer.b(inventoryplayer.o());
         inventoryplayer.b((ye)null);
      } else if (inventoryplayer.o() != null) {
         inventoryplayer.a(inventoryplayer.o());
         inventoryplayer.g = null;
         par1EntityPlayer.a(new cv().a(a.e + "Невозможно выкинуть персональный предмет."));
      }
   }

   @Override
   public boolean isSlotActive(we slot) {
      return this.hasBackpack() || !this.backpackSlots.contains(slot);
   }
}
