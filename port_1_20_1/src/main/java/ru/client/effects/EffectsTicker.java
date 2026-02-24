package ru.stalcraft.client.effects;

import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;
import java.util.EnumSet;

public class EffectsTicker implements ITickHandler {
   atv mc = atv.w();

   public void tickStart(EnumSet type, Object... tickData) {
      if (this.mc.n == null || !this.mc.n.f() || EffectsEngine.instance != null) {
         EffectsEngine.instance.tick();
      }
   }

   public void tickEnd(EnumSet type, Object... tickData) {
   }

   public EnumSet ticks() {
      return EnumSet.of(TickType.CLIENT);
   }

   public String getLabel() {
      return "ParticlesTicker";
   }
}
