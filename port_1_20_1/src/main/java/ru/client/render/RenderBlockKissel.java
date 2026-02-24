package ru.stalcraft.client.render;

import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fluids.RenderBlockFluid;

public class RenderBlockKissel extends RenderBlockFluid {
   public static RenderBlockKissel instance = new RenderBlockKissel();

   public float getFluidHeightForRender(acf world, int x, int y, int z, BlockFluidBase block) {
      return world.a(x, y + 1, z) == block.cF ? 1.0F : 0.1F;
   }
}
