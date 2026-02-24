package ru.demon.pickup.handlers;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import ru.demon.util.Utils;

public class PacketHandler implements IPacketHandler {
   public void onPacketData(cm manager, ea packet, Player player) {
      if (packet.a.equals("demonmod")) {
         this.readPacket(packet, (uf)player);
      }
   }

   void readPacket(ea packet, uf player) {
      DataInputStream dis = new DataInputStream(new ByteArrayInputStream(packet.c));

      try {
         int packetID = dis.readInt();
         ByteArrayDataOutput out = ByteStreams.newDataOutput();
         if (packetID == 0) {
            int entityID = dis.readInt();
            nn ent = player.q.a(entityID);
            Utils.pickUpItem((ss)ent, player);
         }
      } catch (IOException var8) {
      }
   }
}
