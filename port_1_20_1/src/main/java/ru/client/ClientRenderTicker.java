package ru.stalcraft.client;

import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.relauncher.ReflectionHelper;
import java.util.EnumSet;
import ru.stalcraft.entity.EntityCorpse;
import ru.stalcraft.items.ItemSkin;

public class ClientRenderTicker implements ITickHandler {
   public void tickStart(EnumSet type, Object... tickData) {
      ClientProxy.modelManager.tick();
      atv mc = atv.w();
      if (mc.f != null) {
         int l = mc.f.e.size();
         nn entity = null;
         ye stack = null;

         for (int i = 0; i < l; i++) {
            entity = (nn)mc.f.e.get(i);
            if (entity instanceof uf) {
               stack = ((uf)entity).bn.b[1];
               if (stack != null && yc.g[stack.d] instanceof ItemSkin) {
                  ReflectionHelper.setPrivateValue(
                     beu.class, (beu)entity, ((ItemSkin)yc.g[stack.d]).texture, new String[]{"locationSkin", "field_110312_d", "d"}
                  );
               } else {
                  ReflectionHelper.setPrivateValue(beu.class, (beu)entity, beu.b, new String[]{"locationSkin", "field_110312_d", "d"});
               }
            }

            if (entity instanceof EntityCorpse) {
               stack = ((EntityCorpse)entity).inventory.mainInventory[37];
               if (stack != null && yc.g[stack.d] instanceof ItemSkin) {
                  ((EntityCorpse)entity).locationSkin = ((ItemSkin)yc.g[stack.d]).texture;
               } else {
                  ((EntityCorpse)entity).locationSkin = beu.b;
               }
            }
         }
      }
   }

   public void tickEnd(EnumSet type, Object... tickData) {
   }

   public EnumSet ticks() {
      return EnumSet.of(TickType.RENDER);
   }

   public String getLabel() {
      return "StalkerRenderTicker";
   }
}
