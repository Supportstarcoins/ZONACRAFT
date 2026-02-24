package ru.stalcraft.client;

import cpw.mods.fml.relauncher.ReflectionHelper;
import java.util.Iterator;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.client.event.FOVUpdateEvent;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.client.event.RenderPlayerEvent.Post;
import net.minecraftforge.client.event.RenderPlayerEvent.Specials.Pre;
import net.minecraftforge.client.event.sound.SoundLoadEvent;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.EntityEvent.EnteringChunk;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.world.WorldEvent.Load;
import org.lwjgl.opengl.GL11;
import ru.stalcraft.ItemsConfig;
import ru.stalcraft.StalkerMain;
import ru.stalcraft.asm.MethodsHelper;
import ru.stalcraft.client.clans.TagData;
import ru.stalcraft.client.effects.EffectsEngine;
import ru.stalcraft.client.ejection.ClientEjection;
import ru.stalcraft.client.gui.GuiGameOverStalker;
import ru.stalcraft.client.gui.GuiStalkerConnecting;
import ru.stalcraft.client.gui.GuiStalkerIngameMenu;
import ru.stalcraft.client.gui.GuiStalkerMenu;
import ru.stalcraft.client.network.ClientPacketSender;
import ru.stalcraft.client.particles.EntityParticleEmitter;
import ru.stalcraft.client.particles.ParticleLivingEmitter;
import ru.stalcraft.client.player.PlayerClientInfo;
import ru.stalcraft.items.IFlashlight;
import ru.stalcraft.items.ItemWeapon;
import ru.stalcraft.player.PlayerUtils;

public class ClientEvents {
   public static boolean isTickingPlayer = false;
   boolean soundsLoaded = false;
   private static float radiansToDegrees = 180.0F / (float)Math.PI;
   public int isSensivityUpdate;
   public float prevSensivity;
   public float sensivity;
   public boolean isPrevSensivity;

   @ForgeSubscribe
   public void onEnitityChunk(EnteringChunk e) {
      if (e.entity.q.I && e.entity != null && e.entity instanceof of && !EffectsEngine.instance.emittersLiving.containsKey(e.entity.k)) {
         ParticleLivingEmitter p = new ParticleLivingEmitter(new EntityParticleEmitter(e.entity));
         EffectsEngine.instance.emittersLiving.put(e.entity.k, p);
         EffectsEngine.instance.addParticleEmitter(p);
      }
   }

   @ForgeSubscribe
   public void onLivingUpdate(LivingUpdateEvent e) {
      if (e.entityLiving.q.I && e.entityLiving instanceof uf) {
         PlayerUtils.getInfo((uf)e.entityLiving).onUpdate();
      }
   }

   @ForgeSubscribe
   public void onClick(MouseEvent e) {
      atv mc = atv.w();
      if (mc.A && mc.h != null && PlayerUtils.getInfo(mc.h).getHandcuffs()) {
         e.setCanceled(true);
      } else if ((e.button == 0 || e.button == 1) && mc.A && mc.h != null && mc.h.by() != null && mc.h.by().b() instanceof IFlashlight) {
         e.setCanceled(true);
      } else if (e.button == 1 && e.buttonstate && mc.A && mc.t != null && mc.t.g instanceof uf && mc.h.by() == null) {
         ClientPacketSender.sendRightClickRequest((uf)mc.t.g);
         e.setCanceled(true);
      }

      if ((e.button == 0 || e.button == 1) && mc.h != null && mc.h.by() != null && mc.h.by().b() instanceof ItemWeapon) {
         e.setCanceled(true);
      }

      if (e.button == 0 && mc.h != null && PlayerUtils.getInfo(mc.h).weaponInfo.currentGun != null) {
         e.setCanceled(true);
      }
   }

   @ForgeSubscribe
   public void onSpecialsPreRender(Pre e) {
      if (PlayerUtils.getInfo(e.entityPlayer).getHandcuffs()) {
         e.renderItem = false;
      }
   }

   @ForgeSubscribe
   public void renderPlayer(net.minecraftforge.client.event.RenderPlayerEvent.Pre par1) {
      bbj par2 = (bbj)ReflectionHelper.getPrivateValue(bhj.class, par1.renderer, 1);
      bbj par3 = (bbj)ReflectionHelper.getPrivateValue(bhj.class, par1.renderer, 2);
      bbj par4 = (bbj)ReflectionHelper.getPrivateValue(bhj.class, par1.renderer, 3);
      if (par1.entityPlayer.by() != null && par1.entityPlayer.by().b() instanceof ItemWeapon) {
         par2.o = true;
      } else {
         par2.o = false;
      }
   }

   @ForgeSubscribe
   public void onGuiOpen(GuiOpenEvent e) {
      if (e.gui instanceof axv && PlayerUtils.getInfo(atv.w().h).getHandcuffs()) {
         e.setCanceled(true);
      } else if (e.gui instanceof axv) {
         if (!atv.w().c.h()) {
            e.setCanceled(true);
            ClientPacketSender.sendOpenGuiInventory();
         }

         ClientPacketSender.sendUpdateContainer();
      } else if (e.gui instanceof avc && !(e.gui instanceof GuiGameOverStalker)) {
         e.setCanceled(true);
         GuiGameOverStalker gui = new GuiGameOverStalker();
         int ticksToLie = 0;
         PlayerClientInfo info = (PlayerClientInfo)PlayerUtils.getInfo(atv.w().h);
         if (info.getForceCooldown() > 0) {
            ticksToLie += info.getForceCooldown();
            info.setForceCooldown(-1);
         }

         gui.deathTimer = ticksToLie;
         atv.w().a(gui);
      } else if (e.gui instanceof blt && !(e.gui instanceof GuiStalkerMenu)) {
         e.setCanceled(true);
         atv.w().a(new GuiStalkerMenu());
      } else if (e.gui instanceof bcy && !(e.gui instanceof GuiStalkerConnecting)) {
         e.gui = new GuiStalkerMenu();
      } else {
         if (e.gui instanceof avn) {
            return;
         }

         if (e.gui instanceof avy) {
            e.gui = new GuiStalkerIngameMenu();
         }
      }
   }

   @ForgeSubscribe
   public void onBlockHighlight(DrawBlockHighlightEvent e) {
      if (!atv.w().c.h()) {
         e.setCanceled(true);
      }
   }

   @ForgeSubscribe
   public void onFovUpdate(FOVUpdateEvent e) {
      atv mc = atv.w();
      if (mc.h != null && mc.h.bn.h() != null && mc.h.bn.h().b() instanceof ItemWeapon) {
         if (((ClientWeaponInfo)PlayerUtils.getInfo(mc.h).weaponInfo).isAiming() && ((ItemWeapon)mc.h.bn.h().b()).integrateSight
            || ((ClientWeaponInfo)PlayerUtils.getInfo(mc.h).weaponInfo).isAiming() && mc.h.bn.h().q().n("sight")) {
            e.newfov = e.fov / ((ItemWeapon)mc.h.bn.h().b()).zoom * 0.75F;
            if (!this.isPrevSensivity) {
               this.prevSensivity = mc.u.c;
            }

            if (!this.isPrevSensivity && this.prevSensivity > 0.0F) {
               this.sensivity = mc.u.c - 0.2F;
               mc.u.c = this.sensivity;
               this.isPrevSensivity = true;
               this.isSensivityUpdate = 1;
            }
         }

         if (!((ClientWeaponInfo)PlayerUtils.getInfo(mc.h).weaponInfo).isAiming() && this.isPrevSensivity && this.isSensivityUpdate > 0) {
            this.sensivity = this.prevSensivity;
            mc.u.c = this.sensivity;
            this.isPrevSensivity = false;
            this.isSensivityUpdate--;
         }
      }
   }

   @ForgeSubscribe
   public void onPlayerPreRender(net.minecraftforge.client.event.RenderPlayerEvent.Pre e) {
      MethodsHelper.renderRope(
         (beu)e.entityPlayer,
         MethodsHelper.interpolate(e.entity.r, e.entity.u, e.partialRenderTick) - bgl.b,
         MethodsHelper.interpolate(e.entity.s, e.entity.v, e.partialRenderTick) - bgl.c,
         MethodsHelper.interpolate(e.entity.t, e.entity.w, e.partialRenderTick) - bgl.d,
         e.partialRenderTick
      );
   }

   @ForgeSubscribe
   public void onPlayerPostRender(Post e) {
      bgl rm = bgl.a;
      double x = this.interpolate(e.entity.r, e.entity.u, e.partialRenderTick);
      double y = this.interpolate(e.entity.s, e.entity.v, e.partialRenderTick);
      double z = this.interpolate(e.entity.t, e.entity.w, e.partialRenderTick);
      this.passSpecialRender(e.entityPlayer, x - bgl.b, y - bgl.c, z - bgl.d);
   }

   private boolean shouldRenderLivingLabel(uf entity) {
      atv mc = atv.w();
      return mc.t != null && mc.t.g == entity && atv.r() && entity != bgl.a.h && !entity.d(mc.h) && entity.n == null;
   }

   private void passSpecialRender(uf entity, double par2, double par4, double par6) {
      if (this.shouldRenderLivingLabel(entity)) {
         String nameStr = entity.bu;
         TagData tag = (TagData)ClientProxy.tags.get(entity.bu);
         if (tag == null) {
            return;
         }

         String clanStr = ((TagData)ClientProxy.tags.get(entity.bu)).clan;
         if (!clanStr.isEmpty()) {
            clanStr = "<" + clanStr + ">";
         }

         int nameColor = tag.reputation >= 0 ? (tag.isAgressive ? 8388863 : 1179409) : 11145489;
         int clanColor = !tag.clan.isEmpty() && ClientProxy.clanData.enemies.contains(tag.clan) ? 11145489 : 1179409;
         this.renderLivingLabel(entity, par2, par4, par6, nameStr, clanStr, nameColor, clanColor);
      }
   }

   private void renderLivingLabel(uf entity, double par2, double par4, double par6, String nameStr, String clanStr, int nameColor, int clanColor) {
      if (entity.bh()) {
         this.renderLivigLabelAt(entity, par2, par4 - 1.5, par6, nameStr, clanStr, nameColor, clanColor);
      } else if (entity.ah()) {
         this.renderLivigLabelAt(entity, par2, par4 - 0.25, par6, nameStr, clanStr, nameColor, clanColor);
      } else {
         this.renderLivigLabelAt(entity, par2, par4, par6, nameStr, clanStr, nameColor, clanColor);
      }
   }

   private void renderLivigLabelAt(uf entity, double par3, double par5, double par7, String nameStr, String clanStr, int nameColor, int clanColor) {
      avi fontrenderer = bgl.a.a();
      float f = 1.6F;
      float f1 = 0.016666668F * f;
      GL11.glPushMatrix();
      GL11.glTranslatef((float)par3 + 0.0F, (float)par5 + entity.P + 0.5F, (float)par7);
      GL11.glNormal3f(0.0F, 1.0F, 0.0F);
      GL11.glRotatef(-bgl.a.j, 0.0F, 1.0F, 0.0F);
      GL11.glRotatef(bgl.a.k, 1.0F, 0.0F, 0.0F);
      GL11.glScalef(-f1, -f1, f1);
      GL11.glDisable(2896);
      GL11.glDepthMask(false);
      GL11.glDisable(2929);
      GL11.glEnable(3042);
      GL11.glBlendFunc(770, 771);
      bfq tessellator = bfq.a;
      GL11.glDisable(3553);
      byte b0 = 0;
      int width = Math.max(fontrenderer.a(nameStr), fontrenderer.a(clanStr));
      if (!clanStr.isEmpty()) {
         b0 = (byte)(b0 - 9);
         tessellator.b();
         int j = width / 2;
         tessellator.a(0.0F, 0.0F, 0.0F, 0.5F);
         tessellator.a(-j - 1, -1.0, 0.0);
         tessellator.a(-j - 1, 8.0, 0.0);
         tessellator.a(j + 1, 8.0, 0.0);
         tessellator.a(j + 1, -1.0, 0.0);
         tessellator.a();
         GL11.glEnable(3553);
         fontrenderer.b(clanStr, -fontrenderer.a(clanStr) / 2, 0, 553648127);
         GL11.glEnable(2929);
         GL11.glDepthMask(true);
         fontrenderer.b(clanStr, -fontrenderer.a(clanStr) / 2, 0, clanColor);
         GL11.glDepthMask(false);
         GL11.glDisable(2929);
         GL11.glDisable(3553);
      }

      tessellator.b();
      int j = width / 2;
      tessellator.a(0.0F, 0.0F, 0.0F, 0.5F);
      tessellator.a(-j - 1, -1 + b0, 0.0);
      tessellator.a(-j - 1, 8 + b0, 0.0);
      tessellator.a(j + 1, 8 + b0, 0.0);
      tessellator.a(j + 1, -1 + b0, 0.0);
      tessellator.a();
      tessellator.a(1.0F, 1.0F, 1.0F, 1.0F);
      GL11.glEnable(3553);
      fontrenderer.b(nameStr, -fontrenderer.a(nameStr) / 2, b0, 553648127);
      GL11.glEnable(2929);
      GL11.glDepthMask(true);
      fontrenderer.b(nameStr, -fontrenderer.a(nameStr) / 2, b0, nameColor);
      GL11.glEnable(2896);
      GL11.glDisable(3042);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      GL11.glPopMatrix();
   }

   @ForgeSubscribe
   public void onSoundsLoaded(SoundLoadEvent e) {
      e.manager.a("stalker:instal.ogg");
      e.manager.a("stalker:firemode.ogg");
      e.manager.a("stalker:hitmarker.ogg");
      e.manager.a("stalker:radiation.ogg");
      e.manager.a("stalker:medicine.ogg");
      e.manager.a("stalker:chemical.ogg");
      e.manager.a("stalker:biological.ogg");
      e.manager.a("stalker:machinegun_reload.ogg");
      e.manager.a("stalker:machinegun_shoot.ogg");
      e.manager.a("stalker:machinegun_hit.ogg");
      e.manager.a("stalker:ejection.ogg");
      e.manager.a("stalker:ejection_start.ogg");
      e.manager.a("stalker:ejection_end.ogg");
      e.manager.a("stalker:lightturrel_shoot.ogg");
      e.manager.a("stalker:lightturrel_hit.ogg");
      e.manager.a("stalker:middleturrel_shoot.ogg");
      e.manager.a("stalker:middleturrel_hit.ogg");
      e.manager.a("stalker:heavyturrel_shoot.ogg");
      e.manager.a("stalker:heavyturrel_hit.ogg");
      e.manager.a("stalker:blackhole.ogg");
      e.manager.a("stalker:blackhole_active.ogg");
      e.manager.a("stalker:carousel.ogg");
      e.manager.a("stalker:coach.ogg");
      e.manager.a("stalker:coach_active.ogg");
      e.manager.a("stalker:electra.ogg");
      e.manager.a("stalker:electra_hit.ogg");
      e.manager.a("stalker:kissel.ogg");
      e.manager.a("stalker:lighter.ogg");
      e.manager.a("stalker:steam.ogg");
      e.manager.a("stalker:trampoline.ogg");
      e.manager.a("stalker:electra_hit.ogg");
      e.manager.a("stalker:kissel_hit.ogg");
      e.manager.a("stalker:steam_hit.ogg");
      e.manager.a("stalker:trampoline_hit.ogg");
      e.manager.a("stalker:medicine_a.ogg");
      e.manager.a("stalker:medicine_b.ogg");
      e.manager.a("stalker:medicine_c.ogg");
      e.manager.a("stalker:bandage.ogg");
      e.manager.a("stalker:radiation_protector.ogg");
      e.manager.a("stalker:biological_protector.ogg");
      e.manager.a("stalker:psycho_protector.ogg");
      e.manager.a("stalker:novokaine.ogg");
      Iterator i$ = ItemsConfig.getSounds().iterator();

      while (i$.hasNext()) {
         e.manager.a("stalker:" + (String)i$.next() + ".ogg");
      }

      i$ = ItemsConfig.medicineSound.iterator();

      while (i$.hasNext()) {
         e.manager.a("stalker:" + (String)i$.next() + ".ogg");
      }
   }

   @ForgeSubscribe
   public void onWorldRendering(RenderWorldLastEvent e) {
   }

   private double interpolate(double prev, double current, double frame) {
      return prev + (current - prev) * frame;
   }

   private float handleRotationFloat(of par1EntityLiving, float par2) {
      return par1EntityLiving.ac + par2;
   }

   public static void renderEjectionSkyBox(ClientEjection e) {
      GL11.glPushAttrib(16640);
      GL11.glDisable(3553);
      float[] color = e.getSkyColor();
      GL11.glColor4f(color[0], color[1], color[2], color[3]);
      GL11.glEnable(3042);
      GL11.glBlendFunc(774, 768);
      GL11.glDisable(2896);
      GL11.glPushMatrix();
      bfq t = bfq.a;
      t.b();
      t.a(100.0, 100.0, 100.0);
      t.a(100.0, -100.0, 100.0);
      t.a(-100.0, -100.0, 100.0);
      t.a(-100.0, 100.0, 100.0);
      t.a();
      t.b();
      t.a(100.0, 100.0, -100.0);
      t.a(-100.0, 100.0, -100.0);
      t.a(-100.0, -100.0, -100.0);
      t.a(100.0, -100.0, -100.0);
      t.a();
      t.b();
      t.a(100.0, 100.0, 100.0);
      t.a(-100.0, 100.0, 100.0);
      t.a(-100.0, 100.0, -100.0);
      t.a(100.0, 100.0, -100.0);
      t.a();
      t.b();
      t.a(100.0, -100.0, 100.0);
      t.a(100.0, -100.0, -100.0);
      t.a(-100.0, -100.0, -100.0);
      t.a(-100.0, -100.0, 100.0);
      t.a();
      t.b();
      t.a(100.0, 100.0, 100.0);
      t.a(100.0, 100.0, -100.0);
      t.a(100.0, -100.0, -100.0);
      t.a(100.0, -100.0, 100.0);
      t.a();
      t.b();
      t.a(-100.0, 100.0, 100.0);
      t.a(-100.0, -100.0, 100.0);
      t.a(-100.0, -100.0, -100.0);
      t.a(-100.0, 100.0, -100.0);
      t.a();
      GL11.glPopMatrix();
      GL11.glEnable(2896);
      GL11.glDisable(3042);
      GL11.glEnable(3553);
      GL11.glPopAttrib();
   }

   @ForgeSubscribe
   public void onWorldLoad(Load e) {
      if (StalkerMain.getProxy().isRemote()) {
         ((ClientProxy)StalkerMain.getProxy()).initWorldStatics();
      }
   }

   @ForgeSubscribe
   public void onItemTool(ItemTooltipEvent e) {
      PlayerClientInfo par2 = (PlayerClientInfo)PlayerUtils.getInfo(e.entityPlayer);
      if (par2.isClan(e.itemStack)) {
         e.toolTip.add(1, a.k + "Группировка: " + PlayerUtils.getTag(e.itemStack).i("clan"));
      }

      if (par2.isNoDrop(e.itemStack) && !par2.isPersonal(e.itemStack) && !par2.isClan(e.itemStack)) {
         e.toolTip.add(1, a.k + "Невыпадающий предмет");
      }

      if (par2.isPersonal(e.itemStack)) {
         if (!par2.isClan(e.itemStack)) {
            e.toolTip.add(1, a.k + "Персональный при получении");
         } else {
            e.toolTip.add(2, a.k + "Персональный при получении");
         }
      }

      if (par2.isPlayerOwner(e.itemStack)) {
         if (!par2.isClan(e.itemStack)) {
            e.toolTip.add(1, a.k + "Владелец: " + PlayerUtils.getTag(e.itemStack).i("personalOwner"));
         } else {
            e.toolTip.add(2, a.k + "Владелец: " + PlayerUtils.getTag(e.itemStack).i("personalOwner"));
         }
      }

      if (par2.isOwner(e.itemStack)) {
         e.toolTip.add(2, a.k + "Приобрел: " + e.itemStack.q().i("owner"));
      }
   }
}
