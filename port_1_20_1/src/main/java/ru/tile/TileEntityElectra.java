package ru.stalcraft.tile;

import atomicstryker.dynamiclights.client.DynamicLights;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import ru.stalcraft.StalkerDamage;
import ru.stalcraft.client.LighterLight;
import ru.stalcraft.client.gui.GuiSettingsStalker;
import ru.stalcraft.client.network.ClientPacketSender;
import ru.stalcraft.client.particles.ElectraParticleEmitter;
import ru.stalcraft.entity.EntityElectraLight;
import ru.stalcraft.items.EntityBolt;

public class TileEntityElectra extends TileEntityExtendedAnomaly {
   public List<ParticleDisk> particleDisk = new ArrayList<>();
   public int particleDiskSpawnTick;
   public int particleGlowSpawnTick;
   public int ticksSleep;

   @Override
   public void h() {
      super.h();
      Iterator it = this.particleDisk.iterator();

      while (it.hasNext()) {
         ParticleDisk particleDisk = (ParticleDisk)it.next();
         if (!particleDisk.isDead && this.ticksSleep <= 0) {
            particleDisk.tick();
         } else {
            it.remove();
         }
      }

      if (this.ticksSleep <= 0) {
         this.particleDiskSpawnTick++;
         if (this.particleDiskSpawnTick > 2 && super.k.s.nextInt(3) == 0) {
            this.particleDisk.add(new ParticleDisk());
            this.particleDiskSpawnTick = 0;
         }
      }

      for (nn entity : this.az().a(nn.class, asx.a(this.l - 2.0, this.m, this.n - 2.0, this.l + 2.0, this.m + 1.5, this.n + 2.0))) {
         if (this.ticksSleep <= 0) {
            if (super.k.I && entity instanceof of || entity instanceof uf || entity instanceof EntityBolt) {
               ((ElectraParticleEmitter)super.particleEmitter).setSplash();
               float var10002 = super.l + 0.5F;
               float var10003 = super.m + 0.5F;
               atv.w().v.a("stalker:electra_hit", var10002, var10003, super.n + 0.5F, 1.0F, 1.0F);
               if (this.ticksSleep <= 0 && GuiSettingsStalker.dynamicLights) {
                  EntityElectraLight entityLight = new EntityElectraLight(this);
                  atv.w().f.d(entityLight);
                  DynamicLights.addLightSource(new LighterLight(entityLight));
               }

               this.ticksSleep = 60;
            }

            if (super.k.I && entity instanceof uf) {
               ClientPacketSender.sendElectraAttackPlayer();
               this.ticksSleep = 60;
            } else if (!super.k.I && entity instanceof of && !(entity instanceof uf)) {
               entity.a(StalkerDamage.electra, 12.0F);
               this.ticksSleep = 60;
            } else if (!super.k.I && entity instanceof EntityBolt) {
               this.ticksSleep = 60;
            }
         }
      }

      if (this.ticksSleep > 0) {
         this.ticksSleep--;
         if (this.ticksSleep > 50) {
            ((ElectraParticleEmitter)super.particleEmitter).setSplash();
         }
      }
   }

   @Override
   protected Class getEmitterClass() {
      return ElectraParticleEmitter.class;
   }
}
