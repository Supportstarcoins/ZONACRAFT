package ru.stalcraft.items;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import ru.stalcraft.StalkerMain;
import ru.stalcraft.client.ClientProxy;
import ru.stalcraft.entity.EntityDecorative;

public class ItemDecorative extends yc {
   public IModelCustom model;

   public ItemDecorative(int id) {
      super(id);
      super.b("Test");
      super.a(StalkerMain.tab);
      super.setNoRepair();
      GameRegistry.registerItem(this, "Test");
      LanguageRegistry.instance();
      LanguageRegistry.addName(this, "Test");
      this.model = AdvancedModelLoader.loadModel("/assets/stalker/models/campfire.obj");
   }

   public boolean a(ye stack, uf player, abw world, int par4, int par5, int par6, int par7, float par8, float par9, float par10) {
      EntityDecorative entityDecorative = new EntityDecorative(player, this.cv);
      if (world.I) {
         ClientProxy.entityModelManager.addEntityModel(this.cv, this.model, new bjo("stalker:models/campfire.png"));
      }

      world.d(entityDecorative);
      return false;
   }
}
