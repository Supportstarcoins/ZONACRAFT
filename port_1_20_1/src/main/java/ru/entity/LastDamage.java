package ru.stalcraft.entity;

import net.minecraftforge.common.IExtendedEntityProperties;

public class LastDamage implements IExtendedEntityProperties {
   public final boolean isBulletDamage;

   public LastDamage(boolean par1) {
      this.isBulletDamage = par1;
   }

   public void saveNBTData(by compound) {
   }

   public void loadNBTData(by compound) {
   }

   public void init(nn entity, abw world) {
   }
}
