package ru.stalcraft.client.particles;

import java.util.ArrayList;
import java.util.Iterator;
import ru.stalcraft.client.effects.particles.ParticleEmitter;
import ru.stalcraft.client.effects.particles.ParticleIcon;
import ru.stalcraft.client.gui.GuiSettingsStalker;
import ru.stalcraft.tile.TileEntityTrampoline;

public class TrampolineParticleEmitter extends ParticleEmitter {
   private static final int PARTICLES_COUNT = 50;
   private static final int DUST_PARTICLES_COUNT = 250;
   private static ParticleIcon[] stoneIcons;
   private static ParticleIcon[] dustIcons;
   private static ParticleIcon[] leafTextureCoords;
   private ArrayList stones;

   public TrampolineParticleEmitter(TileEntityTrampoline trampoline) {
      super(trampoline);
      super.setCenter(super.centerX + 0.5, super.centerY, super.centerZ + 0.5);
      super.setSize(-2.0, -2.0, -2.0, 2.0, 3.0, 2.0);
      this.stones = new ArrayList();

      for (int i = 0; i < 50; i++) {
         this.stones.add(new TrampolineStoneParticle(this, stoneIcons[this.world.s.nextInt(stoneIcons.length)]));
      }

      this.particles.addAll(this.stones);
   }

   @Override
   public void tick() {
      super.tick();
      super.renderDistanceSq = GuiSettingsStalker.particleRenderDistance * GuiSettingsStalker.particleRenderDistance;
      super.addParticle(new FunnelParticleLeaf(this, leafTextureCoords[super.world.s.nextInt(leafTextureCoords.length)]));
   }

   @Override
   public boolean isValid() {
      return super.mc.f != null && super.mc.f.g.contains(super.emmiter);
   }

   @Override
   public void reset() {
      super.reset();
      super.particles.addAll(this.stones);
   }

   public static void registerIcons(mt ir) {
      stoneIcons = new ParticleIcon[8];
      dustIcons = new ParticleIcon[8];

      for (int i = 0; i < 8; i++) {
         stoneIcons[i] = (ParticleIcon)ir.a("stalker:stone/stone" + (i + 1));
         dustIcons[i] = (ParticleIcon)ir.a("stalker:dust/dust" + (i + 1));
      }

      leafTextureCoords = new ParticleIcon[2];
      leafTextureCoords[0] = (ParticleIcon)ir.a("stalker:leaf/leaf1");
      leafTextureCoords[1] = (ParticleIcon)ir.a("stalker:leaf/leaf2");
   }

   public void onActivate() {
      Iterator it = this.stones.iterator();

      for (int i = 0; i < 250; i++) {
         super.particles.add(new TrampolineDustParticle(this, dustIcons[super.world.s.nextInt(dustIcons.length)]));
      }

      TrampolineStoneParticle stone = null;

      while (it.hasNext()) {
         stone = (TrampolineStoneParticle)it.next();
         stone.jump();
      }
   }
}
