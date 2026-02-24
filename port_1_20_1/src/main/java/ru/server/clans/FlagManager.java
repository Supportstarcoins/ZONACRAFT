package ru.stalcraft.server.clans;

import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.server.MinecraftServer;
import ru.stalcraft.BlockPos;
import ru.stalcraft.clans.ClanMember;
import ru.stalcraft.clans.ClanRank;
import ru.stalcraft.clans.IClan;
import ru.stalcraft.clans.IFlagManager;
import ru.stalcraft.clans.IFlagsLand;
import ru.stalcraft.player.PlayerUtils;
import ru.stalcraft.server.CommonProxy;

public class FlagManager implements IFlagManager {
   private ArrayList flagsToCheck = new ArrayList();
   private ArrayList<Flag> flags = new ArrayList<>();
   private ArrayList flagsLands = new ArrayList();
   private int nextFlagId;
   public int rent = 0;
   public int maxRentTimer;

   void onUpdate(abw world) {
   }

   void readNBT(by tag) {
      cg flagsList = tag.m("flags");

      for (int flagsLandsList = 0; flagsLandsList < flagsList.c(); flagsLandsList++) {
         this.flags.add(new Flag((by)flagsList.b(flagsLandsList)));
      }

      cg var6 = tag.m("flags_lands");

      for (int i = 0; i < var6.c(); i++) {
         by t = (by)var6.b(i);
         this.flagsLands.add(new FlagsLand(t.e("dimension"), t.e("x1"), t.e("x2"), t.e("z1"), t.e("z2"), t.e("rent"), t.e("maxRentTimer")));
      }

      this.nextFlagId = tag.e("next_flag_id");
   }

   by writeNBT() {
      by tag = new by();
      cg flagsList = new cg();

      for (Flag i$ : this.flags) {
         flagsList.a(i$.writeNBT());
      }

      tag.a("flags", flagsList);
      cg flagsLandsList1 = new cg();

      for (FlagsLand land : this.flagsLands) {
         by landTag = new by();
         landTag.a("dimension", land.dimension);
         landTag.a("x1", land.x1);
         landTag.a("x2", land.x2);
         landTag.a("z1", land.z1);
         landTag.a("z2", land.z2);
         landTag.a("rent", land.rent);
         landTag.a("maxRentTimer", land.maxRentTimer);
         flagsLandsList1.a(landTag);
      }

      tag.a("flags_lands", flagsLandsList1);
      tag.a("next_flag_id", this.nextFlagId);
      return tag;
   }

   @Override
   public void onFlagPlace(abw w, String flagName, int x, int y, int z, jv player) {
      boolean hasFlags = false;
      Flag var11 = new Flag(flagName, ++this.nextFlagId, w.t.i, x, y, z, (FlagsLand)this.getLand(w.t.i, x, z));
      this.flags.add(var11);
      player.a("Флаг установлен.");
   }

   public ArrayList getClanFlags(IClan clan) {
      ArrayList clanFlags = new ArrayList();

      for (Flag flag : this.flags) {
         clanFlags.add(flag);
      }

      return clanFlags;
   }

   public ArrayList getFlags(IClan clan) {
      ArrayList clanFlags = new ArrayList();

      for (Flag flag : this.flags) {
         if (clan.equals(flag.owner)) {
            clanFlags.add(flag);
         }
      }

      return clanFlags;
   }

   @Override
   public IFlagsLand getLand(int dimension, int x, int z) {
      for (FlagsLand land : this.flagsLands) {
         if (land.isBlockInLand(dimension, x, z)) {
            return land;
         }
      }

      return null;
   }

   public boolean removeFlagsLand(int dimension, int x, int z) {
      FlagsLand land = (FlagsLand)this.getLand(dimension, x, z);
      if (land != null) {
         this.flagsLands.remove(land);
         return true;
      } else {
         return false;
      }
   }

   @Override
   public void onBlockFlagRemoved(int dimension, int x, int y, int z) {
      Iterator it = this.flags.iterator();

      while (it.hasNext()) {
         Flag flag = (Flag)it.next();
         if (flag.dimension == dimension && flag.x == x && flag.y == y && flag.z == z) {
            it.remove();
         }
      }
   }

   @Override
   public boolean canPlaceFlagHere(int dimension, int x, int z) {
      if (this.getLand(dimension, x, z) == null) {
         return false;
      } else {
         for (Flag flag : this.flags) {
            if (dimension == flag.dimension && Math.abs(flag.x - x) <= 32 && Math.abs(flag.z - z) <= 32) {
               return true;
            }
         }

         return true;
      }
   }

   public void addFlagsLand(FlagsLand land) {
      this.flagsLands.add(land);
   }

   @Override
   public void addFlagToCheck(int dimension, int x, int y, int z) {
      this.flagsToCheck.add(new BlockPos(dimension, x, y, z));
   }

   public void trySetFlagName(uf player, int id, String name) {
      Flag flag = this.getFlagById(id);
      if (this.isPlayerOwner(player, flag) && name.length() <= 16) {
         flag.setName(name);
      }
   }

   public void tryRemoveFlag(uf player, int id) {
      Flag flag = this.getFlagById(id);
      if (this.isPlayerOwner(player, flag)) {
         js world = MinecraftServer.F().a(flag.dimension);
         if (world != null) {
            world.c(flag.x, flag.y, flag.z, 0);
         }
      }
   }

   public void onClanLeave(Clan clan, ClanMember member) {
      this.leaveAllClanLands(clan, member);
   }

   public void onClanDissolution(Clan clan) {
      ArrayList clanFlags = this.getClanFlags(clan);
      Iterator it = clanFlags.iterator();

      while (it.hasNext()) {
         Flag flag = (Flag)it.next();
         js world = MinecraftServer.F().a(flag.dimension);
         if (world != null) {
            world.c(flag.x, flag.y, flag.z, 0);
            it.remove();
         }
      }
   }

   @Override
   public void tryJoinClanLand(uf player, int x, int y, int z) {
      Clan clan = (Clan)PlayerUtils.getInfo(player).getClan();
      Flag flag = this.getFlagByPos(player.q.t.i, x, y, z);
      if (clan == null) {
         player.a("Вы не состоите в клане!");
      } else if (clan != flag.owner) {
         player.a("Эта база не принадлежит вашему клану!");
      } else {
         ClanMember member = clan.getClanMember(player);
         this.leaveAllClanLands(clan, member);
         player.a("Теперь вы возрождаетесь на этой базе.");
         flag.addMember(member);
      }
   }

   private void leaveAllClanLands(Clan clan, ClanMember member) {
      for (Flag flag : this.getClanFlags(clan)) {
         flag.removeMember(member);
      }
   }

   private boolean isPlayerOwner(uf player, Flag flag) {
      if (flag == null) {
         return false;
      } else {
         Clan clan = (Clan)PlayerUtils.getInfo(player).getClan();
         return clan.getClanMember(player).rank == ClanRank.MEMBER ? false : flag.owner == clan;
      }
   }

   public Flag getFlagById(int id) {
      for (Flag flag : this.flags) {
         if (flag.id == id) {
            return flag;
         }
      }

      return null;
   }

   public Flag getFlagNearby(int dimension, int x, int z) {
      for (Flag flag : this.flags) {
         if (flag.dimension == dimension && Math.abs(flag.x - x) <= 16 && Math.abs(flag.z - z) <= 16) {
            return flag;
         }
      }

      return null;
   }

   public Flag getFlagByPlayer(uf player) {
      Clan clan = (Clan)PlayerUtils.getInfo(player).getClan();
      if (clan == null) {
         return null;
      } else {
         ClanMember clanMember = clan.getClanMember(player);
         if (clanMember == null) {
            return null;
         } else {
            for (Flag flag : this.flags) {
               if (flag.owner == clan && flag.isMember(clanMember)) {
                  return flag;
               }
            }

            return null;
         }
      }
   }

   public Flag getFlagByPos(int dimension, int x, int y, int z) {
      for (Flag flag : this.flags) {
         if (flag.dimension == dimension && flag.x == x && flag.y == y && flag.z == z) {
            return flag;
         }
      }

      return null;
   }

   public static FlagManager instance() {
      return CommonProxy.clanManager.flagManager;
   }
}
