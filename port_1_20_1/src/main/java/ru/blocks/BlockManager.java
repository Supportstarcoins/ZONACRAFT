package ru.stalcraft.blocks;

import cpw.mods.fml.relauncher.Side;
import ru.stalcraft.config.Config;

public class BlockManager {
   public Config configBlocks = new Config();

   public void loadBlock(Side side) {
      this.configBlocks.readConfig("blocksList", side);
   }
}
