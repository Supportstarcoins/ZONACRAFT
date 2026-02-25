package ru.stalcraft.client;

import atomicstryker.dynamiclights.client.DynamicLights;
import ru.stalcraft.client.gui.GuiSettingsStalker;
import ru.stalcraft.client.network.ClientPacketSender;
import ru.stalcraft.client.particles.ElectraParticleEmitter;
import ru.stalcraft.entity.EntityElectraLight;
import ru.stalcraft.sound.StalkerSounds;
import ru.stalcraft.tile.TileEntityElectra;

public final class ElectraClientFxHooks {
   private ElectraClientFxHooks() {
   }

   public static void setSplash(Object particleEmitter) {
      if (particleEmitter instanceof ElectraParticleEmitter) {
         ((ElectraParticleEmitter)particleEmitter).setSplash();
      }
   }

   public static void playHitSound(float x, float y, float z) {
      atv.w().v.a(StalkerSounds.ELECTRA_HIT, x, y, z, 1.0F, 1.0F);
   }

   public static void spawnDynamicLight(TileEntityElectra tileEntityElectra) {
      if (GuiSettingsStalker.dynamicLights) {
         EntityElectraLight entityLight = new EntityElectraLight(tileEntityElectra);
         atv.w().f.d(entityLight);
         DynamicLights.addLightSource(new LighterLight(entityLight));
      }
   }

   public static void sendElectraAttackPlayer() {
      ClientPacketSender.sendElectraAttackPlayer();
   }

   public static Class getEmitterClass() {
      return ElectraParticleEmitter.class;
   }
}
