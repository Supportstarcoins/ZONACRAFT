package ru.stalcraft.client.effects.particles.attributes;

import cpw.mods.fml.relauncher.ReflectionHelper;
import ru.stalcraft.client.effects.particles.Particle;

public class LightmaskCoordAttribute extends Attribute {
   public LightmaskCoordAttribute() {
      super("brightness", 3);
   }

   @Override
   public void writeToBuffer(Particle particle, float frame) {
      int l = particle.getTessellatorBrightness();
      int[] lightmapColors = (int[])ReflectionHelper.getPrivateValue(bfe.class, atv.w().p, new String[]{"lightmapColors", "field_78504_Q", "Q"});
      int b = lightmapColors[l / 65536 + l % 65536 / 16];
      super.buffer.put((b >> 16 & 0xFF) / 255.0F);
      super.buffer.put((b >> 8 & 0xFF) / 255.0F);
      super.buffer.put((b & 0xFF) / 255.0F);
   }
}
