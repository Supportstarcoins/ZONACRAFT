package ru.stalcraft.client.render;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import ru.stalcraft.blocks.BlockConfig;
import ru.stalcraft.items.ItemArmorArtefakt;

public class RendererManager {
   public List<RenderItemArmor> renderersItemArmor = new ArrayList<>();
   public HashMap<Integer, RendererDecorative> renderersBlock = new HashMap<>();
   private atv mc;

   public RendererManager(atv mc) {
      this.mc = mc;
   }

   public void onRendererLoaded() {
      try {
         for (yc item : yc.g) {
            if (item != null && item instanceof ItemArmorArtefakt) {
               RenderItemArmor renderItemArmor = new RenderItemArmor(this, this.mc, (ItemArmorArtefakt)item);
               this.renderersItemArmor.add(renderItemArmor);
            }
         }

         for (aqz block : aqz.s) {
            if (block != null && block instanceof BlockConfig) {
               BlockConfig blockConfig = (BlockConfig)block;
               RendererDecorative renderDecor = new RendererDecorative(blockConfig.model);
               this.renderersBlock.put(blockConfig.cF, renderDecor);
            }
         }
      } catch (Exception var8) {
         var8.printStackTrace();
      }
   }
}
