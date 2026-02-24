package ru.stalcraft.items;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import java.util.List;
import org.lwjgl.input.Mouse;
import ru.stalcraft.StalkerMain;
import ru.stalcraft.client.network.ClientPacketSender;
import ru.stalcraft.player.PlayerUtils;

public class ItemCommand extends yc {
   public List description;
   public String commands;
   public int durability;
   public int coolDown;

   public ItemCommand(int id, String name, String icon, List description, String commands, int durability) {
      super(id);
      this.description = description;
      this.commands = commands;
      this.durability = durability;
      super.d("stalker:" + icon);
      super.b("commandItem" + id);
      super.a(StalkerMain.tab);
      LanguageRegistry.addName(this, name);
      GameRegistry.registerItem(this, "commandItem" + id);
   }

   public void a(ye stack, abw world, nn entity, int id, boolean inUse) {
      by tag = PlayerUtils.getTag(stack);
      if (inUse) {
         if (!tag.n("setCommandItemNBT")) {
            tag.a("durability", this.durability);
            tag.a("setCommandItemNBT", true);
         }

         if (world.I) {
            boolean isRightClick = Mouse.isButtonDown(1);
            if (isRightClick && this.coolDown <= 0) {
               ClientPacketSender.sendCommandItem(this.commands);
               this.coolDown = 3;
            }

            if (this.coolDown > 0) {
               this.coolDown--;
            }
         }
      }
   }

   public void a(ye stack, uf player, List list, boolean inUse) {
      list.addAll(this.description);
   }
}
