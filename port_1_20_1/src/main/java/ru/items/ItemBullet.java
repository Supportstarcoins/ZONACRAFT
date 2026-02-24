package ru.stalcraft.items;

import cpw.mods.fml.common.registry.LanguageRegistry;
import java.util.List;
import org.lwjgl.input.Keyboard;
import ru.stalcraft.StalkerMain;

public class ItemBullet extends yc {
   private final List description;
   private final String textureName;
   private static int nextId = 0;
   public int piercing;

   public ItemBullet(int id, String name, String textureName, List description, int stackSize, int piercing) {
      super(id - 256);
      this.b("bullet" + ++nextId);
      this.a(StalkerMain.tab);
      this.textureName = textureName;
      LanguageRegistry.addName(this, name);
      this.description = description;
      super.cw = Math.max(1, Math.min(64, stackSize));
      this.piercing = piercing;
   }

   public void a(mt par1IconRegister) {
      super.cz = par1IconRegister.a("stalker:" + this.textureName);
   }

   public void a(ye stack, uf par2EntityPlayer, List par3List, boolean par4) {
      if (stack.s().contains("Патрон") || stack.s().contains("Аккумулятор") || stack.s().contains("аккумулятор")) {
         if (this.piercing > 0) {
            par3List.add(a.c + "Бронебойный патрон");
            par3List.add(a.c + "Бронебойность: +" + this.piercing + "%");
         } else {
            par3List.add(a.p + "Стандартный патрон");
         }
      }

      if (!Keyboard.isKeyDown(42) && !Keyboard.isKeyDown(54)) {
         par3List.add("Зажмите <Shift> для просмотра подробностей");
      } else {
         par3List.addAll(this.description);
      }
   }
}
