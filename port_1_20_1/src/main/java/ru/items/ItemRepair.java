package ru.stalcraft.items;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import org.lwjgl.input.Mouse;
import ru.stalcraft.StalkerMain;
import ru.stalcraft.proxy.IClientProxy;

public class ItemRepair extends yc {
   public int type;

   public ItemRepair(int id, int type) {
      super(id);
      this.type = type;
      String name = "Ремкомплект для оружия";
      String icon = "repair_kit_weapon";
      if (type == 1) {
         name = "Ремкомплект для брони";
         icon = "repair_kit_armor";
      }

      super.a(StalkerMain.tab);
      super.d("stalker:" + icon);
      super.b(icon);
      GameRegistry.registerItem(this, "repait_kit" + type * id);
      LanguageRegistry.addName(this, name);
   }

   public void a(ye stack, abw world, nn entity, int id, boolean isHand) {
      if (isHand && world.I) {
         boolean isRightClick = Mouse.isButtonDown(1);
         if (isRightClick && atv.w().n == null) {
            IClientProxy proxy = (IClientProxy)StalkerMain.getProxy();
            proxy.onGuiRepair((uf)entity, this.type);
         }
      }
   }
}
