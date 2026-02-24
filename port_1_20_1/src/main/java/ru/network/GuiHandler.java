package ru.stalcraft.network;

import cpw.mods.fml.common.network.IGuiHandler;
import ru.stalcraft.client.gui.GuiArmorUpgrade;
import ru.stalcraft.client.gui.GuiInventoryStalker;
import ru.stalcraft.client.gui.GuiOtherPlayerInventory;
import ru.stalcraft.client.gui.GuiWeaponUpgrade;
import ru.stalcraft.client.gui.clans.GuiBaseWarehouse;
import ru.stalcraft.entity.EntityCorpse;
import ru.stalcraft.inventory.ArmorContainer;
import ru.stalcraft.inventory.ContainerWarehouse;
import ru.stalcraft.inventory.CorpseContainer;
import ru.stalcraft.inventory.HandcuffsContainer;
import ru.stalcraft.inventory.ICustomContainer;
import ru.stalcraft.inventory.InventoryWarehouse;
import ru.stalcraft.inventory.WeaponContainer;
import ru.stalcraft.inventory.WeaponRepairContainer;
import ru.stalcraft.player.PlayerUtils;

public class GuiHandler implements IGuiHandler {
   public static final int WEAPON_GUI_ID = 1;
   public static final int CORPSE_GUI_ID = 2;
   public static final int HANDCUFFS_GUI_ID = 3;

   public Object getServerGuiElement(int ID, uf player, abw world, int x, int y, int z) {
      switch (ID) {
         case 1:
            return new WeaponContainer(player.bn, player.q.I, player, x);
         case 2:
            nn corpse = world.a(x);
            if (corpse instanceof EntityCorpse) {
               return new CorpseContainer((EntityCorpse)corpse, world.I);
            }

            return null;
         case 3:
            nn containerOwner = world.a(x);
            if (player instanceof uf) {
               return new HandcuffsContainer((uf)containerOwner, player, world.I);
            }

            return null;
         case 4:
            return PlayerUtils.getInfo(player).inventoryContainer;
         case 5:
            return new WeaponRepairContainer(player, 0);
         case 6:
            return new ArmorContainer(player.bn, player.q.I, player, x);
         case 7:
            return new ContainerWarehouse(player, new InventoryWarehouse());
         default:
            return null;
      }
   }

   public Object getClientGuiElement(int ID, uf player, abw world, int x, int y, int z) {
      switch (ID) {
         case 1:
            player.bp = new WeaponContainer(player.bn, world.I, player, x);
            return new GuiWeaponUpgrade((WeaponContainer)player.bp);
         case 2:
            nn entity = world.a(x);
            if (entity instanceof EntityCorpse) {
               player.bp = new CorpseContainer((EntityCorpse)entity, world.I);
               return new GuiOtherPlayerInventory(player.bp, (ICustomContainer)player.bp);
            }

            return null;
         case 3:
            nn containerOwner = world.a(x);
            if (player instanceof uf) {
               player.bp = new HandcuffsContainer((uf)containerOwner, player, world.I);
               return new GuiOtherPlayerInventory(player.bp, (ICustomContainer)player.bp);
            }

            return null;
         case 4:
            return new GuiInventoryStalker(player);
         case 5:
            player.bp = new WeaponRepairContainer(player, 0);
            return null;
         case 6:
            player.bp = new ArmorContainer(player.bn, world.I, player, x);
            return new GuiArmorUpgrade((ArmorContainer)player.bp);
         case 7:
            player.bp = new ContainerWarehouse(player, new InventoryWarehouse());
            return new GuiBaseWarehouse(player, (ContainerWarehouse)player.bp, x, y, z);
         default:
            return null;
      }
   }
}
