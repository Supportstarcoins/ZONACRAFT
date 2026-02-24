package ru.stalcraft.items;

import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ru.stalcraft.StalkerMain;

public class ItemDetector extends yc {
   private int[] blockIds;
   private final int radius = 5;
   private String textureName;
   private static final int R = 15;

   public ItemDetector(int id, int[] blockIds, String textureName, String localizedName) {
      super(id);
      this.blockIds = blockIds;
      this.textureName = textureName;
      this.a(StalkerMain.tab);
      this.b(textureName);
      LanguageRegistry.addName(this, localizedName);
   }

   public float detectLevel(abw world, double x, double y, double z) {
      float currentLevel = 0.0F;
      int posX = (int)x;
      int posY = (int)y;
      int posZ = (int)z;

      for (int curX = posX - 15; curX <= posX + 15; curX++) {
         for (int curY = posY - 15; curY <= posY + 15; curY++) {
            for (int curZ = posZ - 15; curZ <= posZ + 15; curZ++) {
               currentLevel = (float)(currentLevel + this.getBlockLevel(world, curX, curY, curZ) / Math.max(1.0, this.distance(x, y, z, curX, curY, curZ)));
            }
         }
      }

      return currentLevel;
   }

   private double distance(double xPlayer, double yPlayer, double zPlayer, int xBlock, int yBlock, int zBlock) {
      double deltaX = xBlock - xPlayer;
      double deltaY = yBlock - yPlayer;
      double deltaZ = zBlock - zPlayer;
      return Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ);
   }

   private int getBlockLevel(abw world, int posX, int posY, int posZ) {
      int blockId = world.a(posX, posY, posZ);

      for (int i = 0; i < this.blockIds.length; i++) {
         if (blockId == this.blockIds[i]) {
            return i + 1;
         }
      }

      return 0;
   }

   @SideOnly(Side.CLIENT)
   public void a(mt par1IconRegister) {
      super.cz = par1IconRegister.a("stalker:" + this.textureName);
   }
}
