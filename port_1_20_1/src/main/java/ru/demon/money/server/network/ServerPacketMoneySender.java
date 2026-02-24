package ru.demon.money.server.network;

import java.nio.charset.Charset;
import ru.demon.money.network.IOpcode;
import ru.demon.money.network.PacketHandler;

public class ServerPacketMoneySender {
   public static void sendEntityPos(nn entity) {
   }

   public static void sendMoney(uf player, int moneyValue) {
      sendToPlayer(player, ClientOpcodes.SEND_MONEY_INFO, moneyValue);
   }

   private static void sendToTrackingPlayers(nn entity, ClientOpcodes opcode, Object... data) {
      ea packet = createPacket(opcode, data);
      ((js)entity.q).q().a(entity, packet);
      if (PacketHandler.debugGroups.contains(opcode.getGroup())
         && PacketHandler.debugOpcodes.contains(opcode)
         && PacketHandler.minPriority.ordinal() <= opcode.getPriority().ordinal()) {
         System.out.println("Sending packet " + opcode.getName() + " to players that tracking entity " + entity.toString());
         PacketHandler.printArgs(data);
      }
   }

   private static void sendToTrackingPlayers(uf player, ClientOpcodes opcode, Object... data) {
      ea packet = createPacket(opcode, data);
      ((js)player.q).q().a(player, packet);
      ((jv)player).a.b(packet);
      if (PacketHandler.debugGroups.contains(opcode.getGroup())
         && PacketHandler.debugOpcodes.contains(opcode)
         && PacketHandler.minPriority.ordinal() <= opcode.getPriority().ordinal()) {
         System.out.println("Sending packet " + opcode.getName() + " to players that tracking player " + player.bu);
         PacketHandler.printArgs(data);
      }
   }

   private static void sendToPlayer(uf receiver, ClientOpcodes opcode, Object... data) {
      ((jv)receiver).a.b(createPacket(opcode, data));
      if (PacketHandler.debugGroups.contains(opcode.getGroup())
         && PacketHandler.debugOpcodes.contains(opcode)
         && PacketHandler.minPriority.ordinal() <= opcode.getPriority().ordinal()) {
         System.out.println("Sending packet " + opcode.getName() + " to player " + receiver.bu);
         PacketHandler.printArgs(data);
      }
   }

   private static ea createPacket(IOpcode opcode, Object... data) {
      StringBuffer buffer = new StringBuffer();
      buffer.append(opcode.getOrdinal()).append(":").append(data.length);

      for (int str = 0; str < data.length; str++) {
         buffer.append(":");
         buffer.append(data[str].toString().replaceAll("\\\\", "\\\\\\\\").replaceAll(":", "\\\\:"));
      }

      return new ea("money", buffer.toString().getBytes(Charset.forName("UTF-8")));
   }
}
