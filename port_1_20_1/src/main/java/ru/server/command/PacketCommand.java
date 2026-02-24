package ru.stalcraft.server.command;

import net.minecraft.server.MinecraftServer;
import ru.stalcraft.network.DebugGroup;
import ru.stalcraft.network.DebugPriority;
import ru.stalcraft.network.PacketHandler;
import ru.stalcraft.server.network.ClientOpcode;

public class PacketCommand extends z {
   public String c() {
      return "packetlog";
   }

   public void b(ad cs, String[] args) {
      jv par3 = MinecraftServer.F().af().f(cs.c_());
      if (par3 == null || MinecraftServer.F().af().e(par3.bu)) {
         if (args.length == 0) {
            cs.a(new cv().a("/packetlog <add/remove/priority>"));
         } else {
            if (args[0].equals("priority")) {
               if (args.length == 1) {
                  cs.a(new cv().a(this.getPriorityUsage()));
                  return;
               }

               DebugPriority remove = null;

               try {
                  remove = DebugPriority.valueOf(args[1]);
               } catch (Exception var11) {
               }

               if (remove == null) {
                  cs.a(new cv().a(this.getPriorityUsage()));
                  return;
               }

               PacketHandler.minPriority = remove;
               cs.a(new cv().a("Минимальный приоритет установлен."));
            }

            if (args[0].equals("remove") || args[0].equals("add")) {
               boolean var12 = args[0].equals("remove");
               if (args.length == 1 || !args[1].equals("group") && !args[1].equals("packet")) {
                  cs.a(new cv().a("/packetlog <add/remove> <group/packet> <name>"));
                  return;
               }

               if (args[1].equals("group")) {
                  if (args.length == 2) {
                     cs.a(new cv().a(this.getGroupUsage()));
                     return;
                  }

                  if (args[2].equals("all")) {
                     if (var12) {
                        PacketHandler.debugGroups.clear();
                     } else {
                        for (DebugGroup opcode1 : DebugGroup.values()) {
                           PacketHandler.debugGroups.add(opcode1);
                        }
                     }
                  } else {
                     DebugGroup var13 = null;

                     try {
                        var13 = DebugGroup.valueOf(args[2]);
                     } catch (Exception var10) {
                     }

                     if (var13 == null) {
                        cs.a(new cv().a(this.getGroupUsage()));
                        return;
                     }

                     if (var12) {
                        PacketHandler.debugGroups.remove(var13);
                        cs.a(new cv().a("Группа удалена."));
                     } else {
                        PacketHandler.debugGroups.add(var13);
                        cs.a(new cv().a("Группа добавлена."));
                     }
                  }
               } else {
                  if (args.length == 2) {
                     cs.a(new cv().a("/packetlog <add/remove> packet <name>"));
                     return;
                  }

                  if (args[2].equals("all")) {
                     if (var12) {
                        PacketHandler.debugOpcodes.clear();
                     } else {
                        for (ClientOpcode var18 : ClientOpcode.values()) {
                           PacketHandler.debugOpcodes.add(var18);
                        }
                     }
                  } else {
                     Object var16 = null;
                     if (var16 == null) {
                        try {
                           var16 = ClientOpcode.valueOf(args[2]);
                        } catch (Exception var9) {
                        }
                     }

                     if (var16 == null) {
                        cs.a(new cv().a("no such packet!"));
                        return;
                     }

                     if (var12) {
                        PacketHandler.debugOpcodes.remove(var16);
                        cs.a(new cv().a("Пакет удален."));
                     } else {
                        PacketHandler.debugOpcodes.add(var16);
                        cs.a(new cv().a("Пакет добавлен."));
                     }
                  }
               }
            }
         }
      }
   }

   private String getPriorityUsage() {
      StringBuilder sb = new StringBuilder();
      sb.append("/packetlog priority <");
      DebugPriority[] priorities = DebugPriority.values();

      for (int i = 0; i < priorities.length; i++) {
         sb.append(priorities[i]);
         if (i + 1 < priorities.length) {
            sb.append("/");
         }
      }

      sb.append(">");
      return sb.toString();
   }

   private String getGroupUsage() {
      StringBuilder sb = new StringBuilder();
      sb.append("/packetlog <add/remove> group <");
      DebugGroup[] groups = DebugGroup.values();

      for (int i = 0; i < groups.length; i++) {
         sb.append(groups[i]);
         if (i + 1 < groups.length) {
            sb.append("/");
         }
      }

      sb.append(">");
      return sb.toString();
   }

   public String c(ad icommandsender) {
      return "";
   }
}
