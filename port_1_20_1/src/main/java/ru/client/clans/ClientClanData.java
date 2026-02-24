package ru.stalcraft.client.clans;

import java.util.ArrayList;
import java.util.HashSet;

public class ClientClanData {
   public ClientClanData.ClientClanLand land;
   public int thePlayerRank;
   public String thePlayerClan = null;
   public String leader = "";
   public String logo = "";
   public int landsCount;
   public int reputatuion;
   public int deathCount;
   public int membersCount;
   public int onlineMembersCount;
   public int removeTime;
   public int money;
   public int salaryState;
   public String rules = "";
   public ArrayList lands = new ArrayList();
   public ArrayList ClanLands = new ArrayList();
   public ArrayList members = new ArrayList();
   public ArrayList clans = new ArrayList();
   public HashSet enemies = new HashSet();
   public int flagPrevCapture = -1;
   public int flagCapture = -1;
   public int flagCaptureTime = 0;
   public int loyalePoints;

   public void resetData() {
      this.thePlayerRank = -1;
      this.leader = "";
      this.logo = "";
      this.landsCount = 0;
      this.reputatuion = 0;
      this.deathCount = 0;
      this.membersCount = 0;
      this.onlineMembersCount = 0;
      this.removeTime = -1;
      this.flagCapture = -1;
      this.flagCaptureTime = 0;
      this.rules = "";
      this.lands.clear();
      this.members.clear();
      this.clans.clear();
   }

   public void parseFlagCapture(String[] data) {
      this.flagPrevCapture = this.flagCapture;
      this.flagCapture = Integer.parseInt(data[0]);
      this.flagCaptureTime = Integer.parseInt(data[1]);
   }

   public void parseInfo(String[] data) {
      this.logo = data[0];
      this.landsCount = Integer.parseInt(data[1]);
      this.reputatuion = Integer.parseInt(data[2]);
      this.membersCount = Integer.parseInt(data[3]);
      this.onlineMembersCount = Integer.parseInt(data[4]);
      this.removeTime = Integer.parseInt(data[5]);
      this.leader = data[6];
      this.money = Integer.parseInt(data[7]);
      this.salaryState = Integer.parseInt(data[8]);
      this.loyalePoints = Integer.parseInt(data[9]);
   }

   public void clearRules() {
      this.rules = "";
   }

   public void clearMembers() {
      this.members.clear();
   }

   public void clearClans() {
      this.clans.clear();
   }

   public void clearLands() {
      this.lands.clear();
   }

   public void clearEnemies() {
      this.enemies.clear();
   }

   public void addRules(String newRules) {
      this.rules = this.rules + newRules;
   }

   public void parseEnemies(String[] data) {
      for (String str : data) {
         this.enemies.add(str);
      }
   }

   public void parseLands(String[] data) {
      for (int i = 0; i + 12 <= data.length; i += 12) {
         this.land = new ClientClanData.ClientClanLand();
         this.land.name = data[i];
         this.land.id = Integer.parseInt(data[i + 1]);
         this.land.x = Integer.parseInt(data[i + 2]);
         this.land.z = Integer.parseInt(data[i + 3]);
         this.land.membersCount = Integer.parseInt(data[i + 4]);
         this.land.isThePlayerMember = Boolean.parseBoolean(data[i + 5]);
         this.land.ownerName = data[i + 6];
         this.land.invaderName = data[i + 7];
         this.land.y = Integer.parseInt(data[i + 8]);
         this.land.loc = data[i + 9];
         this.land.captureDay = data[i + 10];
         this.land.captureTimes = data[i + 11];
         this.land.captureTimeMunute = data[i + 12];
         this.lands.add(this.land);
      }
   }

   public void setLoc(String loc) {
      this.land.loc = loc;
   }

   public void parseMembers(String[] data) {
      for (int i = 0; i + 4 <= data.length; i += 4) {
         ClientClanData.ClientClanMember member = new ClientClanData.ClientClanMember();
         member.username = data[i];
         member.rank = Integer.parseInt(data[i + 1]);
         member.online = data[i + 2].equals("1");
         member.loyalePoint = Integer.parseInt(data[i + 3]);
         this.members.add(member);
      }
   }

   public void parseClans(String[] data) {
      for (int i = 0; i + 5 <= data.length; i += 5) {
         ClientClanData.ClientOtherClan clan = new ClientClanData.ClientOtherClan();
         clan.name = data[i];
         clan.leader = data[i + 1];
         clan.warState = Integer.parseInt(data[i + 2]);
         clan.membersCount = Integer.parseInt(data[i + 3]);
         clan.landsCount = this.ClanLands.size();
         this.clans.add(clan);
      }
   }

   public void parseCommonData(String[] data) {
      this.thePlayerClan = data[0];
      this.thePlayerRank = Integer.parseInt(data[1]);
      if (this.thePlayerClan.isEmpty()) {
         this.clearEnemies();
      }
   }

   public class ClientClanLand implements ClientClanData.IListable {
      public String name;
      public String ownerName;
      public String invaderName;
      public int id;
      public int x;
      public int y;
      public int z;
      public int membersCount;
      public boolean isThePlayerMember;
      public String loc;
      public String captureDay;
      public String captureTimes;
      public String captureTimeMunute;

      @Override
      public String getString() {
         return this.name;
      }

      @Override
      public int getColor() {
         return this.ownerName.equals("")
            ? -1
            : (this.ownerName.equals(ClientClanData.this.thePlayerClan) ? 3500 : (ClientClanData.this.enemies.contains(this.ownerName) ? 11145489 : 327234));
      }
   }

   public class ClientClanMember implements ClientClanData.IListable {
      public String username;
      public int rank;
      public boolean online;
      public int loyalePoint;

      @Override
      public String getString() {
         return this.username;
      }

      @Override
      public int getColor() {
         return 16777215;
      }
   }

   public class ClientOtherClan implements ClientClanData.IListable {
      public String name;
      public String leader;
      public int warState;
      public int membersCount;
      public int landsCount;
      public String fraction = "Одиночки";

      @Override
      public String getString() {
         return this.name;
      }

      @Override
      public int getColor() {
         if (!this.name.equals(ClientClanData.this.thePlayerClan)) {
            if (this.warState == -1) {
               return 1157649;
            } else {
               return this.warState >= 0 ? 11145489 : 11145489;
            }
         } else {
            return 3500;
         }
      }
   }

   public interface IListable {
      String getString();

      int getColor();
   }
}
