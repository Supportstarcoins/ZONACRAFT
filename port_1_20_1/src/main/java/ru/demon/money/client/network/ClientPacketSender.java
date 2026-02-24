package ru.demon.money.client.network;

import cpw.mods.fml.common.network.PacketDispatcher;
import java.nio.charset.Charset;

public class ClientPacketSender {
   public static void sendAddingMoney(int value) {
      sendServer(ServerOpcodes.SEND_ADDING_MONEY, value);
   }

   private static void sendServer(ServerOpcodes opcode, Object... data) {
      PacketDispatcher.sendPacketToServer(createPacket(opcode, data));
   }

   private static ea createPacket(ServerOpcodes opcode, Object... data) {
      StringBuffer buffer = new StringBuffer();
      buffer.append(opcode.getOrdinal()).append(":").append(data.length);

      for (int str = 0; str < data.length; str++) {
         buffer.append(":");
         buffer.append(data[str].toString().replaceAll("\\\\", "\\\\\\\\").replaceAll(":", "\\\\:"));
      }

      String var5 = buffer.toString();
      ea packet = new ea();
      packet.a = "money";
      packet.c = var5.getBytes(Charset.forName("UTF-8"));
      packet.b = packet.c.length;
      return packet;
   }

   public static void sendRepair(int id) {
   }
}
