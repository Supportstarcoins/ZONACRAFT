package ru.stalcraft.items;

import java.util.ArrayList;
import ru.stalcraft.player.IPlayerServerInfo;
import ru.stalcraft.player.PlayerUtils;

public class ItemEnergy extends ItemMedicine {
   public ItemEnergy(int id) {
      super(id, "instal", "Энергетик", 0, new int[]{0, 0, 0, 0}, false, 0, "instal", 0, new ArrayList(), 0, 0);
   }

   @Override
   public void useHealing(uf player) {
      super.useHealing(player);
      ((IPlayerServerInfo)PlayerUtils.getInfo(player)).activeEffectEnergy();
   }
}
