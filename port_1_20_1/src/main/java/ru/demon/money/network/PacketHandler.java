package ru.demon.money.network;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.relauncher.Side;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class PacketHandler implements IPacketHandler {
   int colon = ":".charAt(0);
   char slash = "\\".charAt(0);
   public static Set debugGroups = new HashSet();
   public static Set debugOpcodes = new HashSet();
   public static DebugPriority minPriority = DebugPriority.LOW;
   private static HashMap<Integer, IOpcode[]> opcodes = new HashMap<>();

   public void onPacketData(cm manager, ea packet, Player player) {
      Side side = FMLCommonHandler.instance().getEffectiveSide();

      try {
         DataInputStream e = new DataInputStream(new ByteArrayInputStream(packet.c));
         new StringBuilder();
         byte[] bytes = new byte[e.available()];

         for (int info = 0; info < bytes.length; info++) {
            bytes[info] = e.readByte();
         }

         String var20 = new String(bytes, "UTF-8");
         String[] opcodeAndData = var20.split(":", 3);
         int opcode = Integer.valueOf(opcodeAndData[0]);
         int argsCount = Integer.valueOf(opcodeAndData[1]);
         String dataStr = opcodeAndData.length == 3 ? opcodeAndData[2] : "";
         String[] data;
         if (argsCount == 0) {
            data = new String[0];
         } else {
            data = new String[argsCount];
            int cOpcode = 0;
            int endIndex = -1;

            for (int i = 0; i < argsCount; i++) {
               int slashCount;
               do {
                  endIndex = dataStr.indexOf(this.colon, endIndex + 1);
                  slashCount = 0;
                  if (endIndex == -1) {
                     endIndex = dataStr.length();
                  } else {
                     int j = endIndex - 1;

                     while (j >= 0 && dataStr.charAt(j--) == this.slash) {
                        slashCount++;
                     }
                  }
               } while (slashCount % 2 != 0);

               data[i] = dataStr.substring(cOpcode, endIndex);
               cOpcode = endIndex + 1;
            }
         }

         for (int cOpcode = 0; cOpcode < data.length; cOpcode++) {
            data[cOpcode] = data[cOpcode].replaceAll("\\\\:", ":").replaceAll("\\\\\\\\", "\\\\");
         }

         if (side == Side.CLIENT) {
            ((IOpcodeClient[])opcodes.get(0))[opcode].handle(data);
         } else {
            IOpcodeServer var21 = ((IOpcodeServer[])opcodes.get(1))[opcode];
            if (debugGroups.contains(var21.getGroup()) && debugOpcodes.contains(var21) && minPriority.ordinal() <= var21.getPriority().ordinal()) {
               System.out.println("Received packet " + var21.getName() + " from player " + ((uf)player).bu);
               printArgs(data);
            }

            var21.handle((jv)player, data);
         }
      } catch (Exception var18) {
         var18.printStackTrace();
      }
   }

   public static void printArgs(String... args) {
      if (args.length > 0) {
         StringBuilder sb = new StringBuilder();
         sb.append("Arguments: ");

         for (int i = 0; i < args.length; i++) {
            sb.append(i + 1 + ": \"" + args[i] + "\"");
            if (i + 1 < args.length) {
               sb.append(", ");
            }
         }

         System.out.println(sb.toString());
      }
   }

   public static void printArgs(Object... args) {
      String[] strArgs = new String[args.length];

      for (int i = 0; i < args.length; i++) {
         strArgs[i] = args[i].toString();
      }

      printArgs(strArgs);
   }

   public static void addPackets(IOpcode[] par2) {
      int packetsId = par2[0] instanceof IOpcodeClient ? 0 : 1;
      opcodes.put(packetsId, par2);
   }
}
