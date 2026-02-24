package ru.stalcraft.client.render;

import org.lwjgl.opengl.GL11;
import ru.stalcraft.player.PlayerInfo;
import ru.stalcraft.player.PlayerUtils;

public class RenderItemInGui extends bgw {
   public void renderItemIntoGUIMedicine(avi par1FontRenderer, bim par2TextureManager, ye par3ItemStack, int par4, int par5) {
      this.renderItemIntoGUI(par1FontRenderer, par2TextureManager, par3ItemStack, par4, par5, false);
      GL11.glDisable(3553);
      GL11.glEnable(3042);
      GL11.glBlendFunc(768, 1);
      PlayerInfo info = PlayerUtils.getInfo(atv.w().h);
      int cooldown = ls.a((int)(info.medicineCooldown * 0.1F), 0, 16);
      this.drawTexturedModalRect(par4 + 1, par5, 0, 0, 57, 62, 128, 0.25, 0.5F, 16 - cooldown);
      GL11.glDisable(3042);
      GL11.glEnable(3553);
   }

   public void drawTexturedModalRect(
      double width, double weight, int minU, int minV, int maxU, int maxV, int textureSize, double scale, float alpha, int cooldown
   ) {
      double d = 1.0 / textureSize;
      GL11.glPushMatrix();
      bfq tessellator = bfq.a;
      tessellator.b();
      tessellator.a(alpha, alpha, alpha, 0.95F);
      tessellator.a(width + 0.0, weight + maxV * scale, super.f, (minU + 0) * d, (minV + maxV) * d);
      tessellator.a(width + maxU * scale, weight + maxV * scale, super.f, (minU + maxU) * d, (minV + maxV) * d);
      tessellator.a(width + maxU * scale, weight + cooldown, super.f, (minU + maxU) * d, (minV + 0) * d);
      tessellator.a(width + 0.0, weight + cooldown, super.f, (minU + 0) * d, (minV + 0) * d);
      tessellator.a();
      GL11.glPopMatrix();
   }
}
