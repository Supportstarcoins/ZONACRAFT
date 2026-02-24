package ru.stalcraft.server;

import cpw.mods.fml.relauncher.ReflectionHelper;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.EntityInteractEvent;
import net.minecraftforge.event.entity.player.PlayerDropsEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;
import net.minecraftforge.event.world.WorldEvent.Load;
import net.minecraftforge.event.world.WorldEvent.Unload;
import ru.stalcraft.Logger;
import ru.stalcraft.StalkerDamage;
import ru.stalcraft.StalkerMain;
import ru.stalcraft.entity.EntityCorpse;
import ru.stalcraft.entity.LastDamage;
import ru.stalcraft.player.PlayerInfo;
import ru.stalcraft.player.PlayerUtils;
import ru.stalcraft.proxy.IServerProxy;
import ru.stalcraft.server.clans.ClanManager;
import ru.stalcraft.server.clans.FlagsLand;
import ru.stalcraft.server.player.PlayerSavedDrop;
import ru.stalcraft.server.player.PlayerServerInfo;

public class ServerEvents {
   private static long BASE_TIME = 4000000000000000000L;
   private static final float MESSAGE_RADIUS_SQ = 4096.0F;
   private static final String RED = "§4";
   private static final String GREEN = "§2";
   private static final String PURPLE = "§5";
   private static final String WHITE = "§f";
   private static final String NPC_INTERFACE = "noppes.npcs.EntityNPCInterface";
   private static final String ROLE_TRADER = "noppes.npcs.roles.RoleTrader";
   private int landProgress = 0;
   private int clickSize;
   public final List receivers = new ArrayList();
   private int x1;
   private int x2;
   private int z1;
   private int z2;

   @ForgeSubscribe
   public void loadWorld(Load e) {
   }

   @ForgeSubscribe
   public void onLivingUpdate(LivingUpdateEvent e) {
      if (!e.entityLiving.q.I && e.entityLiving instanceof uf) {
         PlayerUtils.getInfo((uf)e.entityLiving).onUpdate();
      }
   }

   @ForgeSubscribe
   public void constructingEntity(EntityConstructing par1) {
      if (par1.entity instanceof of && par1.entity.getExtendedProperties("last_damage") == null) {
         par1.entity.registerExtendedProperties("last_damage", new LastDamage(true));
      }
   }

   @ForgeSubscribe
   public void livingDead(LivingDeathEvent par1) {
      if (par1.entityLiving instanceof jv) {
         PlayerServerInfo par4 = (PlayerServerInfo)PlayerUtils.getInfo((jv)par1.entityLiving);
         PlayerSavedDrop.initDrop((uf)par1.entityLiving);
         if (par1.source.o != StalkerDamage.blackhole.o) {
            par1.entityLiving.q.d(new EntityCorpse((jv)par1.entityLiving));
         }

         ((ServerContamination)par4.cont).removeEffects();
         par4.onDeath();
      }
   }

   @ForgeSubscribe
   public void saveWorld(Unload e) {
      if (!StalkerMain.getProxy().isRemote()) {
         ((IServerProxy)StalkerMain.getProxy()).getAntiRelog().onSaveAll();
         if (CommonProxy.clanSaveHandler != null) {
            CommonProxy.clanSaveHandler.saveClans(CommonProxy.clanManager);
         }
      }
   }

   @ForgeSubscribe
   public void onDamage(LivingAttackEvent e) {
   }

   @ForgeSubscribe
   public void onDrop(PlayerDropsEvent e) {
   }

   @ForgeSubscribe
   public void onFall(LivingFallEvent e) {
   }

   @ForgeSubscribe
   public void onChat(ServerChatEvent e) {
      e.setCanceled(true);
      List receivers = this.getMessageReceivers(e.player);
      int l = receivers.size();
      jv player = null;

      for (int i = 0; i < l; i++) {
         player = (jv)receivers.get(i);
         player.a(this.buildMessage(e.player, player, e.message, e.message.startsWith("% ")));
      }
   }

   private List getMessageReceivers(uf sender) {
      hn scm = MinecraftServer.F().af();
      if (scm.e(sender.bu)) {
         return scm.a;
      } else {
         ArrayList receivers = new ArrayList();

         for (uf player : scm.a) {
            if (sender.ar == player.ar && sender.e(player) < 4096.0 || scm.e(player.bu)) {
               receivers.add(player);
            }
         }

         return receivers;
      }
   }

   private String buildMessage(uf sender, uf receiver, String message, boolean isClanMessage) {
      StringBuilder sb = new StringBuilder();
      PlayerInfo senderInfo = PlayerUtils.getInfo(sender);
      PlayerInfo receiverInfo = PlayerUtils.getInfo(receiver);
      if (!isClanMessage && senderInfo.getClan() != null) {
         boolean areClansEnemies = receiverInfo.getClan() != null && senderInfo.getClan().isClanEnemy(receiverInfo.getClan());
         if (areClansEnemies) {
            sb.append("§5").append("[").append(senderInfo.getClan().getName()).append("] ");
         } else if (!areClansEnemies) {
            sb.append("§1").append("[").append(senderInfo.getClan().getName()).append("] ");
         }
      }

      if (senderInfo.getReputation() < 0) {
         sb.append("§4");
      } else if (senderInfo.isPlayerAgressive()) {
         sb.append("§5");
      } else {
         sb.append("§1");
      }

      if (isClanMessage) {
         message = message.substring(2);
      }

      sb.append("<").append(sender.bu).append("> ").append(isClanMessage ? "§2" : "§f").append(message);
      return sb.toString();
   }

   @ForgeSubscribe
   public void onEntityInteract(EntityInteractEvent e) {
   }

   @ForgeSubscribe
   public void onInteract(PlayerInteractEvent e) {
      if (e.action == Action.RIGHT_CLICK_BLOCK
         && !e.entityPlayer.q.I
         && e.entityPlayer.bG.d
         && e.entityPlayer.by() != null
         && e.entityPlayer.by().d == StalkerMain.flagAxe.cv) {
         if (ClanManager.instance().flagManager.getLand(e.entityPlayer.ar, e.x, e.z) != null) {
            e.entityPlayer.a("Пересекает другую зону флага!");
            this.landProgress = 0;
            return;
         }

         this.landProgress++;
         switch (this.landProgress) {
            case 1:
               this.x1 = e.x;
               this.z1 = e.z;
               break;
            case 2:
               this.x2 = e.x;
               this.z2 = e.z;
               ClanManager.instance().flagManager.addFlagsLand(new FlagsLand(e.entityPlayer.ar, this.x1, this.x2, this.z1, this.z2, 0, 0));
               e.entityPlayer.a("Новая зона флага создана!");
               this.landProgress = 0;
         }

         Logger.console(e.x + " " + e.z + " ");
      }
   }

   @ForgeSubscribe
   public void onEntityJoin(EntityJoinWorldEvent e) {
      if (e.entity instanceof ol) {
         ReflectionHelper.setPrivateValue(nn.class, e.entity, true, new String[]{"invulnerable", "field_83001_bt", "h"});
      }
   }

   public static boolean canPlaceBlock(String username, int x, int y, int z, int itemID, int metadata) {
      return true;
   }
}
