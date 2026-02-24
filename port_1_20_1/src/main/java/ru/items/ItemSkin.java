package ru.stalcraft.items;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraftforge.common.EnumHelper;
import ru.stalcraft.StalkerMain;

public class ItemSkin extends wh {
   public bjo texture;

   public ItemSkin(int id, String name, String texture) {
      super(id, EnumHelper.addArmorMaterial("STALKERSKIN" + id, -1, new int[]{0, 0, 0, 0}, 15), 2, 2);
      super.a(StalkerMain.tab);
      super.b("stalkerskin=" + id);
      this.d("stalker:skin");
      if (FMLCommonHandler.instance().getSide().isClient()) {
         this.texture = new bjo("stalker", "textures/skins/" + texture + ".png");
      }

      GameRegistry.registerItem(this, name);
      LanguageRegistry.addName(this, name);
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
