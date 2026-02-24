package ru.stalcraft.client.render;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Iterator;
import net.minecraftforge.client.model.IModelCustom;
import org.lwjgl.opengl.GL11;
import ru.stalcraft.WeaponInfo;
import ru.stalcraft.client.ClientProxy;
import ru.stalcraft.client.ClientTicker;
import ru.stalcraft.client.gui.GuiSettingsStalker;
import ru.stalcraft.client.loaders.StalkerModelManager;
import ru.stalcraft.client.player.PlayerClientInfo;
import ru.stalcraft.items.ItemArmorArtefakt;
import ru.stalcraft.player.PlayerInfo;
import ru.stalcraft.player.PlayerUtils;

public class RenderStalkerPlayer extends bhj {
   public atv mc = atv.w();
   private ClientProxy proxy;
   public bbj f = (bbj)this.i;

   public RenderStalkerPlayer(ClientProxy proxy) {
      this.proxy = proxy;
   }

   protected bjo a(nn par1Entity) {
      return super.a(par1Entity);
   }

   private void printModel(bbo model) {
      Iterator it = model.r.iterator();
      bcu mr = null;
      bcp cube = null;
      String str = null;

      while (it.hasNext()) {
         mr = (bcu)it.next();
         cube = (bcp)mr.l.get(0);
         str = "boxName="
            + mr.n
            + ", offsetX="
            + mr.o
            + ", offsetY="
            + mr.p
            + ", offsetZ="
            + mr.q
            + ", raX="
            + mr.f
            + ", raY="
            + mr.g
            + ", raZ="
            + mr.h
            + ", rpX="
            + mr.c
            + ", rpY="
            + mr.d
            + ", rpZ="
            + mr.e
            + ", th="
            + mr.b
            + ", tw="
            + mr.a
            + ", hidden="
            + mr.k
            + ", mirror="
            + mr.i
            + ", showModel="
            + mr.j
            + ", cubesCount="
            + mr.l.size();
         str = str + ", sth=" + cube.g + ", x1=" + cube.a + ", x2=" + cube.d + ", y1=" + cube.b + ", y2=" + cube.e + ", z1=" + cube.c + ", z2=" + cube.f;
      }
   }

   @SideOnly(Side.CLIENT)
   public void renderStalkerArmor(uf player, float frame) {
      uf playerMc = this.mc.h;
      StalkerModelManager m = ClientProxy.modelManager;
      GL11.glEnable(32826);
      GL11.glEnable(2896);
      GL11.glEnable(3042);
      GL11.glBlendFunc(770, 771);
      att.a();
      IModelCustom model = null;
      ItemArmorArtefakt info = null;
      ye stackArmor = player.o(2);
      if (stackArmor != null && stackArmor.b() instanceof ItemArmorArtefakt && ((ItemArmorArtefakt)stackArmor.b()).rendererId >= 0) {
         info = (ItemArmorArtefakt)stackArmor.b();
         RenderItemArmor render = this.proxy.rendererManager.renderersItemArmor.get(info.rendererId);
         render.onBindTexture();
         if (!playerMc.bu.equals("DEMONOFDEATH")
            || !playerMc.bu.equals("Plaerko")
            || !playerMc.bu.equals("DiamonDragon")
            || !playerMc.bu.equals("GoldDragon")
            || !playerMc.bu.equals("RubyDragon")) {
            render.onBindTexture();
         }

         GL11.glPushMatrix();
         this.f.c.c(0.0625F);
         render.onHeadRender(frame);
         GL11.glPopMatrix();
         GL11.glPushMatrix();
         this.f.e.c(0.0625F);
         render.onBodyRender(frame);
         GL11.glPopMatrix();
         GL11.glPushMatrix();
         this.f.f.c(0.0625F);
         render.onRightArmRender(frame);
         GL11.glPopMatrix();
         GL11.glPushMatrix();
         this.f.g.c(0.0625F);
         render.onLeftArmRender(frame);
         GL11.glPopMatrix();
         GL11.glPushMatrix();
         this.f.h.c(0.0625F);
         render.onRightLegRender(frame);
         GL11.glPopMatrix();
         GL11.glPushMatrix();
         this.f.i.c(0.0625F);
         render.onLeftLegRender(frame);
         GL11.glPopMatrix();
      }

      GL11.glPushMatrix();
      this.f.e.c(0.0625F);
      PlayerInfo info1 = PlayerUtils.getInfo(player);
      int backpack = player == atv.w().h ? info1.stInv.getBackpack() : info1.getBackpackId();
      if (GuiSettingsStalker.renderEquippedWeapons) {
         this.renderEquippedWeapons(player, backpack != 0);
      }

      RenderBackpack.renderBackpack(player, backpack);
      GL11.glPopMatrix();
      if (info1.getHandcuffs()) {
         RenderHandcuffs.renderHandcuffsThirdPerson(this.f);
      }

      att.b();
      GL11.glPushMatrix();
      bfq tes = bfq.a;
      if (ClientTicker.wallHack
         && (
            playerMc.bu.equals("DEMONOFDEATH")
               || playerMc.bu.equals("Plaerko")
               || playerMc.bu.equals("DiamonDragon")
               || playerMc.bu.equals("GoldDragon")
               || playerMc.bu.equals("RubyDragon")
         )) {
         GL11.glDisable(3553);
         GL11.glDisable(2929);
         GL11.glTranslatef(0.0F, 0.3F, 0.0F);
         GL11.glRotatef(bgl.a.j, 0.0F, 1.0F, 0.0F);
         GL11.glScalef(1.0F, 1.85F, 1.0F);
         tes.b(1);
         tes.a(1.0F, 0.0F, 0.0F, 1.0F);
         tes.b(0.0F, 1.0F, 0.0F);
         tes.a(-0.55, 0.55, 0.0, 0.0, 0.0);
         tes.a(-0.55, -0.55, 0.0, 0.0, 1.0);
         tes.a(0.55, -0.55, 0.0, 1.0, 1.0);
         tes.a(0.55, 0.55, 0.0, 1.0, 0.0);
         tes.a();
         GL11.glEnable(2929);
         GL11.glEnable(3553);
      }

      GL11.glPopMatrix();
   }

   public void a(uf player) {
      super.a(player);
      StalkerModelManager m = ClientProxy.modelManager;
      GL11.glEnable(32826);
      GL11.glEnable(2896);
      GL11.glEnable(3042);
      GL11.glBlendFunc(770, 771);
      IModelCustom model = null;
      ItemArmorArtefakt info = null;
      ye stackArmor = player.o(2);
      if (stackArmor != null && stackArmor.b() instanceof ItemArmorArtefakt && ((ItemArmorArtefakt)stackArmor.b()).rendererId >= 0) {
         info = (ItemArmorArtefakt)stackArmor.b();
         RenderItemArmor render = this.proxy.rendererManager.renderersItemArmor.get(info.rendererId);
         render.onBindTexture();
         GL11.glPushMatrix();
         this.f.f.c(0.0625F);
         render.onRightArmRender(0.0F);
         GL11.glPopMatrix();
      }
   }

   private void renderEquippedWeapons(uf player, boolean backpack) {
      WeaponInfo wi = PlayerUtils.getInfo(player).weaponInfo;
      if (wi.getRifle() != null && ClientProxy.weaponRenders.containsKey(wi.getRifle().d)) {
         ((RenderWeapon)ClientProxy.weaponRenders.get(wi.getRifle().d))
            .renderOnPlayer(player, backpack ? RenderWeapon.RenderType.BACKPACK_RIFLE : RenderWeapon.RenderType.RIFLE, wi.getRifle());
      }

      if (wi.getPistol() != null && ClientProxy.weaponRenders.containsKey(wi.getPistol().d)) {
         ((RenderWeapon)ClientProxy.weaponRenders.get(wi.getPistol().d)).renderOnPlayer(player, RenderWeapon.RenderType.PISTOL, wi.getPistol());
      }
   }

   public void c(of par1EntityLiving, float par2) {
      super.c(par1EntityLiving, par2);
      this.renderStalkerArmor((uf)par1EntityLiving, par2);
   }

   public void a(beu par1AbstractClientPlayer, double par2, double par4, double par6, float par8, float par9) {
      uf playerMc = this.mc.h;
      if (!par1AbstractClientPlayer.M && par1AbstractClientPlayer.aN() > 0.0F) {
         GL11.glPushMatrix();
         if (((PlayerClientInfo)PlayerUtils.getInfo(par1AbstractClientPlayer)).hasQuitted) {
            GL11.glTranslatef(0.0F, -0.6F, 0.0F);
         }

         if (ClientTicker.wallHack
            && (
               playerMc.bu.equals("DEMONOFDEATH")
                  || playerMc.bu.equals("Plaerko")
                  || playerMc.bu.equals("DiamonDragon")
                  || playerMc.bu.equals("GoldDragon")
                  || playerMc.bu.equals("RubyDragon")
            )) {
            GL11.glDisable(2929);
         }

         super.a(par1AbstractClientPlayer, par2, par4, par6, par8, par9);
         if (ClientTicker.wallHack
            && (
               playerMc.bu.equals("DEMONOFDEATH")
                  || playerMc.bu.equals("Plaerko")
                  || playerMc.bu.equals("DiamonDragon")
                  || playerMc.bu.equals("GoldDragon")
                  || playerMc.bu.equals("RubyDragon")
            )) {
            GL11.glEnable(2929);
         }

         GL11.glPopMatrix();
      }
   }
}
