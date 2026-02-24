package ru.stalcraft.server.clans;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.server.MinecraftServer;
import ru.stalcraft.Logger;
import ru.stalcraft.clans.ClanMember;
import ru.stalcraft.clans.IClan;
import ru.stalcraft.clans.IFlag;
import ru.stalcraft.inventory.ContainerWarehouse;
import ru.stalcraft.items.ItemArmorArtefakt;
import ru.stalcraft.items.ItemBullet;
import ru.stalcraft.items.ItemMedicine;
import ru.stalcraft.items.ItemWeapon;
import ru.stalcraft.player.PlayerUtils;
import ru.stalcraft.server.network.ServerPacketSender;
import ru.stalcraft.tile.TileEntityFlag;

public class Flag implements IFlag {
   private static int nextTickId = 0;
   private int rentTimer;
   private final int maxRentTimer;
   private final int rent;
   public int captureSize;
   public int captureTime;
   public int captureRate;
   public int capture;
   public boolean isCapture;
   public ArrayList<uf> invaders = new ArrayList<>();
   private ArrayList members = new ArrayList();
   public Clan owner = null;
   public Clan invader = null;
   private String name;
   public String loc;
   public final int dimension;
   public final int x;
   public final int y;
   public final int z;
   public final int id;
   private final int tickId;
   public String captureDay;
   public String captureTimes;
   public String captureTimeMunute;
   public int baseWareX;
   public int baseWareY;
   public int baseWareZ;
   public List<BaseContent> baseActiveContent = new ArrayList<>();
   public List<BaseContent> baseContents;
   public int[] prices = new int[135];
   public int[] cooldowns = new int[135];
   public int tagBaseContentCount;
   public int tagPriceCount;
   public int tagCoolDownCount;

   public Flag(by tag) {
      this.baseContents = new ArrayList<>();
      this.tagPriceCount = tag.e("tagPriceCount");

      for (int i = 0; i < this.tagPriceCount; i++) {
         this.prices[i] = tag.e("prices" + String.valueOf(i));
      }

      this.tagCoolDownCount = tag.e("tagCoolDownCount");

      for (int i = 0; i < this.tagCoolDownCount; i++) {
         this.cooldowns[i] = tag.e("cooldowns" + String.valueOf(i));
      }

      this.tagBaseContentCount = tag.e("tagBaseContentCount");

      for (int i = 0; i < this.tagBaseContentCount; i++) {
         this.baseContents.add(new BaseContent(tag.e("itemID" + String.valueOf(i)), this.prices[i], this.cooldowns[i]));
      }

      if (tag.b("clan")) {
         this.owner = ClanManager.instance().getClan(tag.i("clan"));
      }

      if (tag.b("invader")) {
         this.invader = ClanManager.instance().getClan(tag.i("invader"));
      }

      this.name = tag.i("name");
      this.loc = tag.i("loc");
      this.dimension = tag.e("dimension");
      this.x = tag.e("x");
      this.y = tag.e("y");
      this.z = tag.e("z");
      this.id = tag.e("id");
      this.maxRentTimer = tag.e("maxRentTimer");
      this.rent = tag.e("rent");
      this.capture = tag.e("capture");
      this.captureTime = tag.e("captureTime");
      this.captureRate = tag.e("captureRate");
      this.captureSize = tag.e("captureSize");
      this.isCapture = tag.n("");
      this.loc = tag.i("locationName");
      this.captureDay = tag.i("captureDay");
      this.captureTimes = tag.i("captureTimes");
      this.captureTimeMunute = tag.i("captureTimeMunute");
      this.baseWareX = tag.e("baseWareX");
      this.baseWareY = tag.e("baseWareY");
      this.baseWareZ = tag.e("baseWareZ");
      this.tickId = nextTickId++ % 20;
   }

   public Flag(String name, int id, int dimension, int x, int y, int z, FlagsLand land) {
      this.baseContents = new ArrayList<>();
      this.name = name;
      this.dimension = dimension;
      this.x = x;
      this.y = y;
      this.z = z;
      this.id = id;
      this.maxRentTimer = land.maxRentTimer;
      this.rent = land.rent;
      this.tickId = nextTickId++ % 20;
      this.captureSize = 0;
      this.captureTime = 0;
      this.captureRate = 0;
      this.isCapture = false;
   }

   public by writeNBT() {
      by tag = new by();

      for (int i = 0; i < this.baseContents.size(); i++) {
         BaseContent baseContent = this.baseContents.get(i);
         tag.a("itemID" + String.valueOf(i), baseContent.itemID);
      }

      for (int i = 0; i < this.baseContents.size(); i++) {
         int price = this.prices[i];
         tag.a("prices" + String.valueOf(i), price);
      }

      for (int i = 0; i < this.baseContents.size(); i++) {
         int cooldown = this.cooldowns[i];
         tag.a("cooldowns" + String.valueOf(i), cooldown);
      }

      tag.a("tagPriceCount", this.prices.length);
      tag.a("tagBaseContentCount", this.baseContents.size());
      tag.a("tagCoolDownCount", this.cooldowns.length);
      if (this.owner != null) {
         tag.a("clan", this.owner.name);
      }

      if (this.invader != null) {
         tag.a("invader", this.invader.name);
      }

      tag.a("name", this.name);
      tag.a("dimension", this.dimension);
      tag.a("x", this.x);
      tag.a("y", this.y);
      tag.a("z", this.z);
      tag.a("id", this.id);
      tag.a("rent", this.rent);
      tag.a("maxRentTimer", this.maxRentTimer);
      tag.a("capture", this.capture);
      tag.a("captureTime", this.captureTime);
      tag.a("captureRate", this.captureRate);
      tag.a("captureSize", this.captureSize);
      tag.a("isCapture", this.isCapture);
      tag.a("locationName", this.loc);
      tag.a("captureDay", this.captureDay);
      tag.a("captureTimes", this.captureTimes);
      tag.a("captureTimeMunute", this.captureTimeMunute);
      tag.a("baseWareX", this.baseWareX);
      tag.a("baseWareY", this.baseWareY);
      tag.a("baseWareZ", this.baseWareZ);
      cg membersList = new cg();

      for (ClanMember member : this.members) {
         by memberTag = new by();
         memberTag.a("username", member.username);
         membersList.a(memberTag);
      }

      tag.a("members", membersList);
      return tag;
   }

   public boolean isMember(ClanMember member) {
      return this.members.contains(member);
   }

   public void addMember(ClanMember member) {
      this.members.add(member);
   }

   public void removeMember(ClanMember member) {
      this.members.remove(member);
   }

   public int getMembersCount() {
      return this.members.size();
   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public void setCapture(float f) {
      if (this.capture < this.captureTime) {
         this.capture = (int)(this.capture + f);
      }
   }

   public void onUpdate(abw world, FlagManager flagManager) {
      asp tile = world.r(this.x, this.y, this.z);
      Logger.console(tile);
      if (tile == null || !(tile instanceof TileEntityFlag)) {
         flagManager.onBlockFlagRemoved(this.dimension, this.x, this.y, this.z);
      } else if (tile != null && this.getClan() != null && tile != null && this.baseContents.size() > 0) {
         MinecraftServer mcServer = MinecraftServer.F();
         int baseContentSize = this.baseContents.size();
         if (baseContentSize > 0) {
            int contents = mcServer.f_().s.nextInt(baseContentSize);
            int valid = 0;

            for (int i = 0; i < this.baseActiveContent.size(); i++) {
               BaseContent baseContent = this.baseContents.get(contents);
               BaseContent currentBaseContent = this.baseActiveContent.get(i);
               if (currentBaseContent.itemID == baseContent.itemID) {
                  valid += currentBaseContent.cooldown;
               }
            }

            if (this.baseActiveContent.size() <= 0) {
               valid = 0;
            }

            if (this.baseActiveContent.size() < 81 && valid <= 0) {
               this.baseActiveContent.add(new BaseContent(this.baseContents.get(contents).itemID, 0, 0));
            }
         }

         for (BaseContent baseContent : this.baseActiveContent) {
            for (int ix = 0; ix < this.baseContents.size(); ix++) {
               BaseContent currentBaseContent = this.baseContents.get(ix);
               if (baseContent.itemID == currentBaseContent.itemID) {
                  baseContent.price = this.prices[ix];
               }
            }
         }

         for (BaseContent baseContent : this.baseActiveContent) {
            for (int ixx = 0; ixx < this.baseContents.size(); ixx++) {
               BaseContent currentBaseContent = this.baseContents.get(ixx);
               if (baseContent.itemID == currentBaseContent.itemID && !baseContent.isCoolDown) {
                  baseContent.cooldown = this.cooldowns[ixx];
                  baseContent.isCoolDown = true;
               }
            }
         }

         for (BaseContent baseContent : this.baseActiveContent) {
            if (baseContent.cooldown > 0) {
               baseContent.cooldown--;
            }
         }

         if (baseContentSize > 0) {
            Clan clan = (Clan)this.getClan();

            for (uf player : clan.getOnlineMembers()) {
               if (!player.bG.d && player.bp instanceof ContainerWarehouse) {
                  ContainerWarehouse container = (ContainerWarehouse)player.bp;

                  for (int ixxx = 0; ixxx < this.baseActiveContent.size(); ixxx++) {
                     ye itemStack = container.inventory.contents[ixxx];
                     if (itemStack == null) {
                        BaseContent content = this.baseActiveContent.get(ixxx);
                        yc item = yc.g[content.itemID];
                        int stackSize = item instanceof ItemBullet ? 64 : (item instanceof ItemMedicine ? 8 : 1);
                        if (item != null) {
                           ye stack = new ye(item, stackSize);
                           by itemStackNBT = PlayerUtils.getTag(stack);
                           if (itemStackNBT.e("setClan") == 0) {
                              itemStackNBT.a("clan", PlayerUtils.getInfo(player).getClan().getName());
                           }

                           itemStackNBT.a("prices", content.price);
                           if (item instanceof ItemWeapon || item instanceof ItemArmorArtefakt) {
                              itemStackNBT.a("personal", 1);
                           }

                           container.inventory.contents[ixxx] = stack;
                           ServerPacketSender.sendUpdateTag(player, ixxx, content.price);
                        }
                     }
                  }
               }
            }
         }
      }
   }

   @Override
   public IClan getClan() {
      return this.owner;
   }

   @Override
   public int getPosZ() {
      return this.x;
   }

   @Override
   public int getPosY() {
      return this.y;
   }

   @Override
   public int getPosX() {
      return this.z;
   }
}
