package ru.stalcraft.items;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraftforge.common.EnumHelper;
import ru.stalcraft.StalkerMain;

public class ItemPNV extends wh {
   public ItemPNV(int id) {
      super(id, EnumHelper.addArmorMaterial("STALKERPNV" + id, -1, new int[]{0, 0, 0, 0}, 15), 0, 0);
      super.a(StalkerMain.tab);
      super.d("stalker:ItemPNV");
      super.b("ItemPNV");
      GameRegistry.registerItem(this, "ItemPNV");
      LanguageRegistry.addName(this, "Прибор ночного виденья");
   }

   @SideOnly(Side.CLIENT)
   public String getArmorTexture(ye stack, nn entity, int slot, int layer) {
      return "stalker:textures/armor/empty.png";
   }

   @SideOnly(Side.CLIENT)
   public boolean b() {
      return false;
   }

   public boolean a(ye par1ItemStack) {
      return false;
   }

   public int getEntityLifespan(ye itemStack, abw world) {
      return 288000;
   }
}
