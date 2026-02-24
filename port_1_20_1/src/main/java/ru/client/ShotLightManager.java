package ru.stalcraft.client;

import atomicstryker.dynamiclights.client.DynamicLights;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import ru.stalcraft.client.gui.GuiSettingsStalker;
import ru.stalcraft.entity.EntityShot;

public class ShotLightManager {
   private HashMap lights = new HashMap();

   public void tick() {
      Iterator it = this.lights.entrySet().iterator();
      Entry entry = null;
      int newValue = 0;
      ClientBlockPos pos = null;
      abw world = null;

      while (it.hasNext()) {
         entry = (Entry)it.next();
         newValue = (Integer)entry.getValue() - 1;
         if (newValue == 0) {
            pos = (ClientBlockPos)entry.getKey();
            world = pos.getWorld();
            if (world != null) {
               world.c(ach.b, pos.x, pos.y, pos.z);
            }

            it.remove();
         } else {
            entry.setValue(newValue);
         }
      }
   }

   public static void addLight(EntityShot shot) {
      if (GuiSettingsStalker.dynamicLights) {
         DynamicLights.addLightSource(new ShotLight(shot));
      }
   }
}
