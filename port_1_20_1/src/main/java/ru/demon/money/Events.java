package ru.demon.money;

import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import ru.demon.money.client.player.PlayerClientInfo;
import ru.demon.money.server.player.PlayerServerInfo;

public class Events {
   @ForgeSubscribe
   public void onEntityJointWorld(EntityJoinWorldEvent event) {
      if (event.entity != null && event.entity instanceof uf) {
         uf player = (uf)event.entity;
         abw worldObj = player.q;
         MoneyMod.instance.playerUtils.setPlayerInfo(new PlayerClientInfo(player));
         MoneyMod.instance.playerUtils.setPlayerInfo(new PlayerServerInfo(player));
      }
   }
}
