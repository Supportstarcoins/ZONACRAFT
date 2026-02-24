package ru.stalcraft.tile;

import cpw.mods.fml.relauncher.ReflectionHelper;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import ru.stalcraft.Config;
import ru.stalcraft.StalkerDamage;

public class TileEntityCoach extends TileEntityAnomaly {
   private HashMap targets = new HashMap();
   private boolean isSoundActive;
   private String activeSound = "stalker:coach_active";
   private String playingSound;

   public void addTarget(uf player) {
      if (!this.targets.containsKey(player)) {
         this.targets.put(player, 0);
      }
   }

   public void removeTarget(uf player) {
      this.targets.remove(player);
   }

   @Override
   public void h() {
      super.h();
      if (super.k.I) {
         boolean it = this.targets.size() > 0;
         bln entry = atv.w().v;
         if (!it || this.playingSound != null && entry.b.playing(this.playingSound)) {
            if (this.playingSound != null && entry.b.playing(this.playingSound) && !this.isSoundActive) {
               entry.b.stop(this.playingSound);
            }
         } else {
            int latestSoundID = (Integer)ReflectionHelper.getPrivateValue(bln.class, atv.w().v, new String[]{"latestSoundID", "field_77378_e", "g"});
            this.playingSound = "sound_" + (latestSoundID + 1) % 256;
            entry.a(this.activeSound, super.l + 0.5F, super.m + 0.5F, super.n + 0.5F, 1.0F, 1.0F);
         }
      } else {
         Iterator it1 = this.targets.entrySet().iterator();

         while (it1.hasNext()) {
            Entry entry1 = (Entry)it1.next();
            entry1.setValue(Math.max(0, (Integer)entry1.getValue() - 1));
            if (!((uf)entry1.getKey()).M && ((uf)entry1.getKey()).q == super.k) {
               double distance = Math.sqrt(this.a(((uf)entry1.getKey()).u, ((uf)entry1.getKey()).v, ((uf)entry1.getKey()).w));
               if (distance > 50.0) {
                  it1.remove();
               } else if (distance > 5.0 && (Integer)entry1.getValue() < 1) {
                  ((uf)entry1.getKey()).a(StalkerDamage.coach, Config.coachDamage);
                  int distanceRate = 10 - (int)(distance / 5.0);
                  entry1.setValue(5 + distanceRate);
               }
            } else {
               it1.remove();
            }
         }
      }
   }

   @Override
   public boolean canUpdate() {
      return true;
   }

   @Override
   protected Class getEmitterClass() {
      return null;
   }
}
