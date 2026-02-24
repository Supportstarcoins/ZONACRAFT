package ru.demon.util;

import cpw.mods.fml.common.network.PacketDispatcher;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

public class PacketUtil {
   public static void writeToServer(int... params) {
      ByteArrayOutputStream bos = new ByteArrayOutputStream(8);
      DataOutputStream out = new DataOutputStream(bos);

      try {
         for (int par : params) {
            out.writeInt(par);
         }
      } catch (Exception var7) {
         var7.printStackTrace();
      }

      sendToServer(bos);
   }

   static void sendToServer(ByteArrayOutputStream bos) {
      ea packet = new ea();
      packet.a = "demonmod";
      packet.c = bos.toByteArray();
      packet.b = bos.size();
      PacketDispatcher.sendPacketToServer(packet);
   }
}
