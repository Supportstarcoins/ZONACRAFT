package ru.stalcraft.player;

import ru.stalcraft.tile.TileEntityMachineGun;

public interface IPlayerServerInfo {
   void startEjection();

   void shooterMachineGun(TileEntityMachineGun var1);

   void shootMachineGun(TileEntityMachineGun var1);

   void reloadRequestMachineGun(TileEntityMachineGun var1);

   void reloadFinishMachineGun(TileEntityMachineGun var1);

   void addItemSafe(uf var1, ye var2);

   void updateWeightSpeed();

   void readNBT(by var1);

   void writeNBT(by var1);

   void activeEffectEnergy();
}
