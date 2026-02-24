package ru.stalcraft.client.render;

import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import org.lwjgl.opengl.GL11;
import ru.stalcraft.client.ClientProxy;
import ru.stalcraft.client.clans.ClientClanCaptureData;
import ru.stalcraft.client.clans.ClientClanData;

public class RenderFlag extends bje {
   private IModelCustom model = AdvancedModelLoader.loadModel("/assets/stalker/models/flag.obj");
   private bjo texture_down = new bjo("stalker", "models/flag_down.png");
   private bjo texture_up = new bjo("stalker", "models/flag_up.png");

   public void a(asp tileentity, double x, double y, double z, float f) {
      GL11.glPushMatrix();
      GL11.glBlendFunc(770, 771);
      GL11.glTranslatef((float)x + 0.5F, (float)y, (float)z + 0.5F);
      atv.w().N.a(this.texture_down);
      this.model.renderPart("palka");
      atv.w().N.a(this.texture_up);
      this.model.renderPart("flag");
      GL11.glPopMatrix();
      ClientClanCaptureData captureData = ClientProxy.captureData;
      float captureSizeXZ = captureData.captureSize;
      float captureSizeY = 25.0F;
      if (captureSizeXZ > 0.0F) {
         GL11.glPushMatrix();
         GL11.glDisable(3008);
         GL11.glEnable(3042);
         GL11.glBlendFunc(770, 771);
         GL11.glDisable(2896);
         GL11.glLineWidth(2.0F);
         GL11.glDisable(3553);
         GL11.glDepthMask(false);
         GL11.glTranslated(x + 0.5, y, z + 0.5);
         float f1 = -0.05F;
         GL11.glColor4d(0.0, 0.8, 0.0, 1.0);
         this.drawOutlinedBoundingBox(asx.a().a(-captureSizeXZ, 0.0, -captureSizeXZ, captureSizeXZ, captureSizeY, captureSizeXZ).b(f1, f1, f1));
         GL11.glDepthMask(true);
         GL11.glEnable(2896);
         GL11.glEnable(3553);
         GL11.glDisable(3042);
         GL11.glEnable(3008);
         GL11.glPopMatrix();
      }

      if (ClientProxy.clanData.flagCaptureTime == ClientProxy.clanData.flagCapture) {
         captureData.captureSize = 0;
      }

      ClientClanData.ClientClanLand land = this.getLand(tileentity.l, tileentity.m, tileentity.n);
      avi fontrenderer = bgl.a.a();
      if (land != null) {
         GL11.glPushMatrix();
         String baseName = land.name;
         String clanOwnerName = land.ownerName;
         double scale = 0.035F;
         int slideY = 0;
         GL11.glTranslated(x + 0.5, y + 6.0, z + 0.5);
         GL11.glRotatef(-bgl.a.j, 0.0F, 1.0F, 0.0F);
         GL11.glRotatef(bgl.a.k, 1.0F, 0.0F, 0.0F);
         GL11.glScaled(-scale, -scale, scale);
         GL11.glBlendFunc(770, 771);
         bfq tessellator = bfq.a;
         int j = fontrenderer.a(baseName) >> 1;
         int i = fontrenderer.a(clanOwnerName) >> 1;
         if (j != 0) {
            GL11.glDisable(2896);
            tessellator.b();
            tessellator.a(0.0F, 0.0F, 0.0F, 0.6275F);
            tessellator.a(-j - i - 1, slideY - i - 1, 0.0);
            tessellator.a(-j - i - 1, slideY + i + 8, 0.0);
            tessellator.a(j + i + 1, slideY + i + 8, 0.0);
            tessellator.a(j + i + 1, slideY - i - 1, 0.0);
            tessellator.a();
            fontrenderer.b(baseName, -j, slideY - 10, 1627389951);
            fontrenderer.b(clanOwnerName, -i, slideY + 2, 1627389951);
            GL11.glEnable(2896);
         }

         GL11.glPopMatrix();
      }
   }

   private void drawOutlinedBoundingBox(asx par1AxisAlignedBB) {
      bfq tessellator = bfq.a;
      tessellator.b(3);
      tessellator.a(par1AxisAlignedBB.a, par1AxisAlignedBB.b, par1AxisAlignedBB.c);
      tessellator.a(par1AxisAlignedBB.d, par1AxisAlignedBB.b, par1AxisAlignedBB.c);
      tessellator.a(par1AxisAlignedBB.d, par1AxisAlignedBB.b, par1AxisAlignedBB.f);
      tessellator.a(par1AxisAlignedBB.a, par1AxisAlignedBB.b, par1AxisAlignedBB.f);
      tessellator.a(par1AxisAlignedBB.a, par1AxisAlignedBB.b, par1AxisAlignedBB.c);
      tessellator.a();
      tessellator.b(3);

      for (int i = 0; i < par1AxisAlignedBB.e; i++) {
         float y = i * 0.99F;
         tessellator.a(par1AxisAlignedBB.a, par1AxisAlignedBB.e - y, par1AxisAlignedBB.c);
         tessellator.a(par1AxisAlignedBB.d, par1AxisAlignedBB.e - y, par1AxisAlignedBB.c);
         tessellator.a(par1AxisAlignedBB.d, par1AxisAlignedBB.e - y, par1AxisAlignedBB.f);
         tessellator.a(par1AxisAlignedBB.a, par1AxisAlignedBB.e - y, par1AxisAlignedBB.f);
         tessellator.a(par1AxisAlignedBB.a, par1AxisAlignedBB.e - y, par1AxisAlignedBB.c);
      }

      tessellator.a();

      for (int i = 0; i < par1AxisAlignedBB.d * 2.05F; i++) {
         float x = i * 0.99F;
         tessellator.b(1);
         tessellator.a(par1AxisAlignedBB.a + x * 0.975F, par1AxisAlignedBB.b, par1AxisAlignedBB.c);
         tessellator.a(par1AxisAlignedBB.a + x * 0.975F, par1AxisAlignedBB.e, par1AxisAlignedBB.c);
         tessellator.a(par1AxisAlignedBB.a + x * 0.975F, par1AxisAlignedBB.b, par1AxisAlignedBB.f);
         tessellator.a(par1AxisAlignedBB.a + x * 0.975F, par1AxisAlignedBB.e, par1AxisAlignedBB.f);
         tessellator.a();
         GL11.glPushMatrix();
         tessellator.b(1);
         GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
         tessellator.a(par1AxisAlignedBB.a + x * 0.975F, par1AxisAlignedBB.b, par1AxisAlignedBB.c);
         tessellator.a(par1AxisAlignedBB.a + x * 0.975F, par1AxisAlignedBB.e, par1AxisAlignedBB.c);
         tessellator.a(par1AxisAlignedBB.a + x * 0.975F, par1AxisAlignedBB.b, par1AxisAlignedBB.f);
         tessellator.a(par1AxisAlignedBB.a + x * 0.975F, par1AxisAlignedBB.e, par1AxisAlignedBB.f);
         tessellator.a();
         GL11.glPopMatrix();
      }
   }

   public ClientClanData.ClientClanLand getLand(double x, double y, double z) {
      ClientClanData.ClientClanLand land = null;
      ClientClanData clanData = ClientProxy.clanData;
      if (clanData.lands != null && clanData.lands.size() > 0) {
         for (ClientClanData.ClientClanLand currentLand : clanData.lands) {
            if (currentLand.x == x && currentLand.y == y && currentLand.z == z) {
               land = currentLand;
            }
         }
      }

      return land;
   }
}
