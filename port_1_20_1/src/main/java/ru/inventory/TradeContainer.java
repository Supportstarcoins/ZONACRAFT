package ru.stalcraft.inventory;

import java.util.ArrayList;
import java.util.Iterator;
import ru.stalcraft.client.network.ClientPacketSender;
import ru.stalcraft.player.PlayerInfo;
import ru.stalcraft.player.PlayerUtils;

public class TradeContainer extends uy implements ICustomContainer {
   public vk craftMatrix = new vk(this, 2, 2);
   public mo craftResult = new wc();
   public boolean isLocalWorld = false;
   protected final uf player;
   public uf trader;
   public final PlayerInfo info;
   private boolean hasBackpackSlots = false;
   public ArrayList backpackSlots = new ArrayList();
   private ArrayList hotbarSlots = new ArrayList();
   private TradeInventory tradeInventory;
   public ye[] stacks = new ye[20];

   public TradeContainer(ud par1InventoryPlayer, boolean par2, uf par3EntityPlayer, uf trader) {
      this.player = par3EntityPlayer;
      this.trader = trader;
      this.info = PlayerUtils.getInfo(par3EntityPlayer);
      this.isLocalWorld = par2;
      int var3x = 0;
      int var6 = 0;

      for (int var7 = 0; var7 < 3; var7++) {
         for (int var11 = 0; var11 < 9; var11++) {
            this.a(new we(par3EntityPlayer.bn, var11 + var7 * 9 + 9, -4 + var11 * 18, 165 + var7 * 18));
         }
      }

      for (int var8 = 0; var8 < 9; var8++) {
         this.a(new we(par3EntityPlayer.bn, var8, -4 + var8 * 18, 223));
      }

      this.tradeInventory = new TradeInventory();

      for (int var9 = 0; var9 < 4; var9++) {
         for (int var12 = 0; var12 < 5; var12++) {
            this.a(new we(this.tradeInventory, var9 + var12 * 9, -4 + var9 * 18, 43 + var12 * 18));
         }
      }

      for (int var10 = 0; var10 < 4; var10++) {
         for (int var13 = 0; var13 < 5; var13++) {
            this.a(new SlotTrader(this.tradeInventory, 4 + var10 + var13 * 9, 86 + var10 * 18, 43 + var13 * 18, false));
         }
      }

      this.a(this.craftMatrix);
      this.hasBackpackSlots = this.hasBackpack();
   }

   public void a(mo par1IInventory) {
      if (this.player != null) {
         this.craftResult.a(0, aaf.a().a(this.craftMatrix, this.player.q));
      }
   }

   public boolean a(uf par1EntityPlayer) {
      return true;
   }

   public ye b(uf par1EntityPlayer, int par2) {
      return null;
   }

   public ye a(int par1, int par2, int par3, uf par4EntityPlayer) {
      if (!(this.a(par1) instanceof SlotTrader)) {
         return par1 >= 0 && this.a(par1) != null && !this.isSlotActive(this.a(par1)) ? null : super.a(par1, par2, par3, par4EntityPlayer);
      } else if (!((SlotTrader)this.a(par1)).thePlayerSlot) {
         return null;
      } else {
         return par1 >= 0 && this.a(par1) != null && !this.isSlotActive(this.a(par1)) ? null : super.a(par1, par2, par3, par4EntityPlayer);
      }
   }

   public boolean a(ye par1ItemStack, we par2Slot) {
      return par2Slot.f != this.craftResult && super.a(par1ItemStack, par2Slot);
   }

   @Override
   public boolean hasBackpack() {
      return this.info.stInv.mainInventory[12] != null;
   }

   public we a(int par1) {
      return par1 >= 0 && this.c.size() > 0 ? (we)this.c.get(par1) : null;
   }

   public void a(int par1, ye par2ItemStack) {
      we slot = this.a(par1);
      if (slot != null) {
         slot.c(par2ItemStack);
      }
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
   public of getOwner() {
      return this.player;
   }

   public void b() {
      boolean hasItemsChanged = false;

      for (int tag = 0; tag < super.c.size(); tag++) {
         ye itemstack = ((we)super.c.get(tag)).d();
         ye itemstack1 = (ye)super.b.get(tag);
         if (!ye.b(itemstack1, itemstack)) {
            hasItemsChanged = true;
            itemstack1 = itemstack == null ? null : itemstack.m();
            super.b.set(tag, itemstack1);

            for (int j = 0; j < super.e.size(); j++) {
               ((vi)super.e.get(j)).a(this, tag, itemstack1);
            }
         }
      }
   }

   @Override
   public boolean isSlotActive(we slot) {
      return this.hasBackpack() || !this.backpackSlots.contains(slot);
   }

   public void b(uf player) {
      super.b(player);

      for (int j = 0; j < 20; j++) {
         ye stack = this.tradeInventory.contents[j];
         if (stack != null) {
            for (int i = 0; i < player.bn.a.length; i++) {
               ye stackInventory = player.bn.a[i];
               if (stackInventory == null) {
                  this.tradeInventory.contents[j] = null;
                  player.bn.a[i] = stack;
                  stack = null;
               }
            }
         }
      }
   }

   @Override
   public ArrayList getArmorSlots() {
      return new ArrayList();
   }

   public void updateContainer() {
      for (int i = 0; i < 20; i++) {
         we slot = (we)super.c.get(i);
         ye stack = this.tradeInventory.contents[i];
         if (stack != null) {
            by tag = stack.q();
            yc item = stack.b();
            int itemID = item.cv;
            ClientPacketSender.sendUpdateContent(slot.getSlotIndex(), this.trader.k, itemID);
         } else {
            ClientPacketSender.sendUpdateContent(slot.getSlotIndex(), this.trader.k);
         }
      }

      for (int ix = 0; ix < 20; ix++) {
         ye stack = this.stacks[ix];
         if (stack != null) {
            this.tradeInventory.contents[20 + ix] = stack;
         }
      }
   }
}
