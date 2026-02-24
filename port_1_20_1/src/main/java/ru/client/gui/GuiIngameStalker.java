package ru.stalcraft.client.gui;

import cpw.mods.fml.common.FMLCommonHandler;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import net.minecraftforge.client.GuiIngameForge;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.Chat;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.client.event.RenderGameOverlayEvent.Post;
import net.minecraftforge.client.event.RenderGameOverlayEvent.Pre;
import net.minecraftforge.client.event.RenderGameOverlayEvent.Text;
import net.minecraftforge.common.MinecraftForge;
import net.smart.moving.SmartMovingContext;
import net.smart.moving.SmartMovingFactory;
import net.smart.moving.SmartMovingSelf;
import noppes.npcs.entity.EntityNPCHumanMale;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import ru.demon.pickup.handlers.DemonKeyHandler;
import ru.stalcraft.Contamination;
import ru.stalcraft.blocks.BlockBaseWarehouse;
import ru.stalcraft.client.ClientProxy;
import ru.stalcraft.client.ClientTicker;
import ru.stalcraft.client.ClientWeaponInfo;
import ru.stalcraft.client.clans.ClientClanCaptureData;
import ru.stalcraft.client.effects.EffectsEngine;
import ru.stalcraft.client.gui.weapon.GuiWeaponEditor;
import ru.stalcraft.client.player.PlayerClientInfo;
import ru.stalcraft.client.render.RenderItemInGui;
import ru.stalcraft.entity.EntityCorpse;
import ru.stalcraft.items.ItemMedicine;
import ru.stalcraft.items.ItemWeapon;
import ru.stalcraft.items.ItemWeaponGrenadeM203;
import ru.stalcraft.items.ItemWeaponGrenadeVOG;
import ru.stalcraft.player.PlayerInfo;
import ru.stalcraft.player.PlayerUtils;

public class GuiIngameStalker extends GuiIngameForge {
   private static bjo VIGNETTE = new bjo("textures/misc/vignette.png");
   private static bjo WIDGITS = new bjo("textures/gui/widgets.png");
   private static bjo PUMPKIN_BLUR = new bjo("textures/misc/pumpkinblur.png");
   private static bjo STALKER_GUI = new bjo("stalker", "textures/hud.png");
   private static bjo capture = new bjo("stalker", "textures/clans/capture.png");
   private static bjo notification = new bjo("stalker", "textures/notification.png");
   public static bjo PNV = new bjo("stalker", "textures/pnv.png");
   public static bjo biologic = new bjo("stalker", "textures/biological.png");
   public static bjo thermal = new bjo("stalker", "textures/thermal.png");
   public static bjo psycho = new bjo("stalker", "textures/psycho.png");
   public static bjo bleeding = new bjo("stalker", "textures/bleeding.png");
   public static bjo hitmarker = new bjo("stalker", "textures/hitmarker.png");
   private static final int WHITE = 16777215;
   public static boolean renderHelmet = true;
   public static boolean renderPortal = true;
   public static boolean renderHotbar = true;
   public static boolean renderCrosshairs = true;
   public static boolean renderBossHealth = true;
   public static boolean renderHealth = true;
   public static boolean renderArmor = true;
   public static boolean renderFood = true;
   public static boolean renderHealthMount = true;
   public static boolean renderAir = true;
   public static boolean renderExperiance = true;
   public static boolean renderJumpBar = true;
   public static boolean renderObjective = true;
   public static int left_height = 39;
   public static int right_height = 39;
   private final float[] alpha1Levels = new float[]{0.0F, 0.3F, 0.45F, 0.6F};
   private final float[] alpha2Levels = new float[]{0.0F, 0.45F, 0.6F, 0.75F};
   private awf res = null;
   private avi fontrenderer = null;
   private RenderGameOverlayEvent eventParent;
   private static final String MC_VERSION = new c((b)null).a();
   public float fireModAlpha = 0.0F;
   public RenderItemInGui renderItemInGui = new RenderItemInGui();
   public int notificationPosX = -156;

   public GuiIngameStalker(atv mc) {
      super(mc);
   }

   public void a(float partialTicks, boolean hasScreen, int mouseX, int mouseY) {
      this.res = new awf(super.g.u, super.g.d, super.g.e);
      this.eventParent = new RenderGameOverlayEvent(partialTicks, this.res, mouseX, mouseY);
      int width = this.res.a();
      int height = this.res.b();
      renderHealthMount = super.g.h.o instanceof of;
      renderFood = super.g.h.o == null;
      renderJumpBar = super.g.h.u();
      right_height = 39;
      left_height = 39;
      if (!this.pre(ElementType.ALL)) {
         this.fontrenderer = super.g.l;
         super.g.p.c();
         GL11.glEnable(3042);
         GL11.glDisable(2896);
         if (atv.s()) {
            this.a(super.g.h.d(partialTicks), width, height);
         } else {
            GL11.glBlendFunc(770, 771);
         }

         if (renderHelmet) {
            this.renderHelmet(this.res, partialTicks, hasScreen, mouseX, mouseY);
         }

         if (renderPortal && !super.g.h.a(ni.k)) {
            this.renderPortal(width, height, partialTicks);
         }

         PlayerInfo info = PlayerUtils.getInfo(super.g.h);
         Contamination cont = info.cont;
         if (cont.getLevel(1) > 0) {
            this.renderContaminationHUD(thermal, width, height);
         }

         if (cont.getLevel(2) > 0) {
            this.renderContaminationHUD(biologic, width, height);
         }

         if (cont.getLevel(3) > 0) {
            this.renderContaminationHUD(psycho, width, height);
         }

         this.renderBleendingHUD(bleeding, width, height, ls.a(info.player.aN(), 0.0F, 7.5F));
         if (ClientTicker.isPNV) {
            this.renderPNV(width, height);
         }

         if (this.g.t != null) {
            ata mouseOver = this.g.t;
            if (mouseOver.g instanceof of) {
               nn entity = mouseOver.g;
               boolean isValid = false;
               if (entity instanceof EntityNPCHumanMale || mouseOver.g instanceof uf) {
                  super.a(this.g.l, entity.an(), width / 2, height / 2 + 10, -1);
                  isValid = true;
               } else if (entity instanceof EntityCorpse) {
                  EntityCorpse entityCorpse = (EntityCorpse)entity;
                  super.a(this.g.l, entityCorpse.username, width / 2, height / 2 + 10, -1);
                  isValid = true;
               }

               if (isValid) {
                  super.a(this.g.l, Keyboard.getKeyName(DemonKeyHandler.pickUpKey.d) + " - ВЗАИМОДЕЙСТВОВАТЬ", width / 2 - 7, height / 2 + 18, -1);
               }
            }

            if (mouseOver.g == null && mouseOver.a == atb.a) {
               aqz block = aqz.s[this.g.f.a(mouseOver.b, mouseOver.c, mouseOver.d)];
               if (block instanceof BlockBaseWarehouse) {
                  super.a(this.g.l, "Склад базы", width / 2, height / 2 + 8, -1);
                  super.a(this.g.l, Keyboard.getKeyName(DemonKeyHandler.pickUpKey.d) + " - ОСМОТРЕТЬ", width / 2, height / 2 + 18, -1);
               }
            }
         }

         if (!super.g.c.a()) {
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            super.n = -90.0F;
            super.f.setSeed(super.i * 312871);
            if (!((ClientWeaponInfo)info.weaponInfo).isAiming() && renderCrosshairs || this.g.n instanceof GuiWeaponEditor) {
               this.renderCrosshairs(width, height);
            }

            atv mc = atv.w();
            awf scale = new awf(mc.u, mc.d, mc.e);
            bfq tessellator = bfq.a;
            int xCenter = scale.a() / 2;
            int offset = scale.b() * 2;
            mc.N.a(new bjo("stalker", "textures/hitmarker.png"));
            int damageLevel = ClientTicker.damageLevel;
            if (damageLevel == 2) {
               this.drawTexturedModalRect(width / 2 - 30, height / 2 - 30, 66, 60, 121, 128, 768, 0.5, ClientTicker.hitMarker);
            } else if (damageLevel == 1) {
               this.drawTexturedModalRect(width / 2 - 22, height / 2 - 23, 237, 75, 90, 92, 768, 0.5, ClientTicker.hitMarker);
            } else if (damageLevel == 0) {
               this.drawTexturedModalRect(width / 2 - 11, height / 2 - 12, 394, 96, 45, 46, 768, 0.5, ClientTicker.hitMarker);
            }

            if (super.g.c.b()) {
               GL11.glEnable(3042);
               GL11.glBlendFunc(770, 771);
               super.g.N.a(STALKER_GUI);
               int objective = 0;
               if (super.g.h.i(5)) {
                  this.drawEffect(0, objective++);
               }

               if (super.g.h.i(10)) {
                  this.drawEffect(1, objective++);
               }

               if (super.g.h.i(1)) {
                  this.drawEffect(2, objective++);
               }

               bdi p = super.g.h;
               this.b(this.res.a() - 6 - 111, this.res.b() - 6 - 63, 79, 0, 111, 69);
               SmartMovingSelf moving = (SmartMovingSelf)SmartMovingFactory.getInstance(mc.h);
               float maxExhaustion = SmartMovingContext.Client.getMaximumExhaustion();
               float exhaustion = Math.min(moving.exhaustion, maxExhaustion);
               float maxStillJumpCharge = (Float)SmartMovingContext.Config._jumpChargeMaximum.value;
               float stillJumpCharge = Math.min(moving.jumpCharge, maxStillJumpCharge);
               float maxRunJumpCharge = (Float)SmartMovingContext.Config._headJumpChargeMaximum.value;
               float runJumpCharge = Math.min(moving.headJumpCharge, maxRunJumpCharge);
               if (!(stillJumpCharge > 0.0F) && !(runJumpCharge > 0.0F)) {
                  boolean var70 = false;
               } else {
                  boolean var10000 = true;
               }

               float maxJumpCharge = stillJumpCharge > runJumpCharge ? maxStillJumpCharge : maxRunJumpCharge;
               float jumpCharge = Math.max(stillJumpCharge, runJumpCharge);
               this.drawTexturedModalRect(this.res.a() - 6 - 82, this.res.b() - 6 - 56, 155, 148, (int)(mc.h.aN() * 6.9F), 16, 512, 0.5);
               this.drawTexturedModalRect(this.res.a() - 6 - 82, this.res.b() - 6 - 45, 155, 171, (int)(138.0F - exhaustion * 1.3F), 10, 512, 0.5);
               this.drawTexturedModalRect(this.res.a() - 6 - 82, this.res.b() - 6 - 38, 155, 184, (int)(jumpCharge * 6.9F), 10, 512, 0.5);
               PlayerClientInfo clientInfo = (PlayerClientInfo)info;

               for (int i = 0; i < 2; i++) {
                  for (int paramId = 0; paramId < clientInfo.clientPlayerParameters.length; paramId++) {
                     int param = clientInfo.clientPlayerParameters[paramId];
                     if (param > 0) {
                        this.drawTexturedModalRect(90 + i * 17, height - 22, 288 + i * 32, 449, 31, 40, 512, 0.5);
                     }
                  }
               }

               objective = 0;

               for (int reputation = 0; reputation < 3; reputation++) {
                  if (cont.getLevel(reputation) > 0) {
                     this.drawRightIcon(reputation, cont.getLevel(reputation), objective++);
                  }
               }

               if (cont.getLevel(3) > 0) {
                  this.drawRightIcon(3, 3, objective++);
               }

               if (super.g.h.bI().a() == 0) {
                  this.drawRightIcon(4, 4, objective++);
               } else if (super.g.h.bI().a() <= 10) {
                  this.drawRightIcon(4, 3, objective++);
               } else if (super.g.h.bI().a() <= 14) {
                  this.drawRightIcon(4, 2, objective++);
               } else if (super.g.h.bI().a() <= 16) {
                  this.drawRightIcon(4, 1, objective++);
               }

               if (super.g.h.i(19)) {
                  if (super.g.h.b(ni.u).c() == 0) {
                     this.drawRightIcon(5, 1, objective++);
                     this.renderBleendingHUD(bleeding, width, height, 0.5F);
                  } else if (super.g.h.b(ni.u).c() == 1) {
                     this.drawRightIcon(5, 2, objective++);
                     this.renderBleendingHUD(bleeding, width, height, 0.35F);
                  } else {
                     this.drawRightIcon(5, 3, objective++);
                     this.renderBleendingHUD(bleeding, width, height, 0.0F);
                  }
               }

               float weightSpeed = info.getWeightSpeed();
               if (weightSpeed >= 0.9F) {
                  this.drawRightIcon(6, 1, objective++);
               } else if (weightSpeed >= 0.6F) {
                  this.drawRightIcon(6, 2, objective++);
               } else if (weightSpeed >= 0.2F) {
                  this.drawRightIcon(6, 3, objective++);
               } else if (weightSpeed >= 0.1F) {
                  this.drawRightIcon(6, 4, objective++);
               }

               GL11.glDisable(3042);
               if (info.weaponInfo.currentGun != null) {
                  String deathScoreStr = String.valueOf(info.weaponInfo.currentGun.bulletsInCage);
                  super.g.l.b(deathScoreStr, this.res.a() - 97 - super.g.l.a(deathScoreStr) / 2, this.res.b() - 25 + 1, 16777215);
               } else if (super.g.h.bn.h() != null && super.g.h.bn.h().b() instanceof ItemWeapon) {
                  ItemWeapon var19 = (ItemWeapon)super.g.h.bn.h().b();
                  int grenadeSize = 0;

                  for (ye stack : super.g.h.bn.a) {
                     if (stack != null) {
                        if (var19.indexGrenadeLauncher == 1) {
                           if (stack.b() instanceof ItemWeaponGrenadeM203) {
                              grenadeSize += stack.b;
                           }
                        } else if (stack.b() instanceof ItemWeaponGrenadeVOG) {
                           grenadeSize += stack.b;
                        }
                     }
                  }

                  String var21 = String.valueOf(grenadeSize);
                  super.g.l.b(var21, this.res.a() - 91 - super.g.l.a(var21) / 2, this.res.b() - 33, 16777215);
                  by tag = PlayerUtils.getTag(super.g.h.bn.h());
                  String[] fireMod = new String[]{"А", "О", "Г"};
                  String[] fireMods = new String[]{"Автоматический", "Одиночный", "Гранатомет"};
                  int mod = tag.e("fireModes");
                  int ammo = mod <= 1 ? tag.e("cage") : tag.e("grenade_weapon");
                  int ammoType = tag.e("ammoType");
                  if (ClientTicker.tickFireMode > 16) {
                     this.fireModAlpha = 1.0F;
                  } else if (this.fireModAlpha > 0.0F) {
                     this.fireModAlpha -= 0.01F;
                  }

                  GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                  this.drawCenteredString(mc.l, fireMods[mod], width / 2, height / 2 + 215, -1, this.fireModAlpha);
                  GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                  if (ClientTicker.weaponJammed > 0) {
                     super.a(mc.l, a.m + "Оружие заклинило, нужно перезарядить", width / 2, height / 2 + 215, -1);
                  }

                  super.g
                     .l
                     .b(ammo + "/" + (ammoType > 0 ? "Б" : "С"), this.res.a() - 92 - super.g.l.a(String.valueOf(mod)) / 2 - 4, this.res.b() - 17, 16777215);
                  super.g.l.b(fireMod[mod], this.res.a() - 42 - super.g.l.a(String.valueOf(fireMod[mod])) / 2 - 4, this.res.b() - 8, 16777215);
               }

               String deathScoreStr = String.valueOf(info.getDeathScore());
               super.g.l.b(deathScoreStr, this.res.a() - 59 - super.g.l.a(deathScoreStr) / 2, this.res.b() - 33, 16777215);
            }

            if (renderHotbar) {
               this.renderHotbar(width, height, partialTicks);
            }
         }

         this.renderSleepFade(width, height);
         this.renderToolHightlight(width, height);
         this.renderHUDText(width, height);
         this.renderRecordOverlay(width, height, partialTicks);
         ate var22 = super.g.f.X().a(1);
         if (renderObjective && var22 != null) {
            this.a(var22, height, width, this.fontrenderer);
         }

         GL11.glEnable(3042);
         GL11.glBlendFunc(770, 771);
         GL11.glDisable(3008);
         this.renderChat(width, height);
         this.renderPlayerList(width, height);
         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
         GL11.glDisable(2896);
         GL11.glEnable(3008);
         this.post(ElementType.ALL);
      }

      if (ClientProxy.clanData.flagCapture >= 0) {
         GL11.glEnable(3042);
         GL11.glBlendFunc(770, 771);
         this.g.N.a(GuiIngameStalker.capture);
         this.drawTexturedModalRect(width / 2 - 206, height / 2 - 230, 0, 0, 423, 55, 512, 0.5);
         this.drawTexturedModalRect(width / 2 + 5, height / 2 - 230, 0, 56, 423, 55, 512, 0.5);
         int captureTime = ClientProxy.clanData.flagCaptureTime / 2;
         int cap = ClientProxy.clanData.flagCapture;
         float capture = cap * 100.0F / captureTime / 100.0F;
         int captureVector = ClientProxy.clanData.flagCaptureTime / 2;
         ClientClanCaptureData captureInfo = ClientProxy.captureData;
         if (ClientProxy.clanData.flagCapture > captureVector * 0.5F) {
            this.drawTexturedModalRect(width / 2 - 115, height / 2 - 228, 1, 115, (int)(481.0F * (-1.0F + capture)), 31, 512, 0.5);
            super.a(this.g.l, captureInfo.invader, width / 2 - 158, height / 2 - 229, -1);
            super.a(this.g.l, captureInfo.invader, width / 2 + 171, height / 2 - 229, -1);
         } else {
            this.drawTexturedModalRect(width / 2 - 115, height / 2 - 228, 1, 115, (int)(481.0F * (1.0F - capture * 2.0F)), 31, 512, 0.5);
            super.a(this.g.l, captureInfo.invader, width / 2 - 158, height / 2 - 229, -1);
            super.a(this.g.l, captureInfo.owner, width / 2 + 171, height / 2 - 229, -1);
         }

         GL11.glDisable(3042);
      }

      this.g.N.a(notification);
      PlayerClientInfo infox = (PlayerClientInfo)PlayerUtils.getInfo(this.g.h);
      boolean isBase = false;
      if (this.g.h != null && infox != null) {
         if (infox.yesNoTick <= 0 && infox.inviteTimeValid <= 0) {
            if (this.notificationPosX > 0) {
               this.notificationPosX -= 25;
            }
         } else {
            if (this.notificationPosX < 156) {
               this.notificationPosX += 2;
            }

            isBase = infox.yesNoTick > 0;
         }

         if (this.notificationPosX > 156) {
            this.notificationPosX = 156;
         }
      }

      this.drawTexturedModalRect(-156 + this.notificationPosX, height / 2 - 140, 0, 0, 311, 178, 512, 0.5);
      String base = "Сохранение на базе";
      if (!isBase) {
         base = "Вам предлогает обмен: " + infox.inviterName;
      }

      super.b(this.g.l, base, -136 + this.notificationPosX, height / 2 - 125, -1);
      super.b(
         this.g.l, "Нажмите кнопку " + Keyboard.getKeyName(DemonKeyHandler.accept.d) + " чтобы принять", -136 + this.notificationPosX, height / 2 - 100, -1
      );
      super.b(this.g.l, "Нажмите кнопку " + Keyboard.getKeyName(DemonKeyHandler.deny.d) + " для отмены", -136 + this.notificationPosX, height / 2 - 90, -1);
      this.renderIconMedicine(height);
      if (ClientTicker.wallHack) {
         super.a(this.g.l, "WallHack: active", 32, 1, -1);
      }

      if (ClientTicker.hitDamage) {
         int posY = ClientTicker.wallHack ? 10 : 0;
         super.a(this.g.l, "HitDamage: true", 31, posY, -1);
      }
   }

   private void renderPNV(int width, int height) {
      this.g.N.a(PNV);
      GL11.glTexParameteri(3553, 10241, 9729);
      GL11.glTexParameteri(3553, 10240, 9729);
      bfq tessellator = bfq.a;
      tessellator.b();
      this.g.u.ak = 10.0F;
      tessellator.a(0.0F, 100.0F, 0.5F, 0.4F);
      double minX = 0.0;
      double minY = 0.0;
      double maxX = 1.0;
      double maxY = 1.0;
      double screenRatio = (double)width / height;
      double textureRatio = 1.7777777777777777;
      if (screenRatio > textureRatio) {
         minY = (1.0 - textureRatio / screenRatio) / 2.0;
         maxY = 1.0 - (1.0 - textureRatio / screenRatio) / 2.0;
      } else if (textureRatio > screenRatio) {
         minX = (1.0 - screenRatio / textureRatio) / 2.0;
         maxX = 1.0 - (1.0 - screenRatio / textureRatio) / 2.0;
      }

      tessellator.a(0.0, height, super.n, minX, maxY);
      tessellator.a(width, height, super.n, maxX, maxY);
      tessellator.a(width, 0.0, super.n, maxX, minY);
      tessellator.a(0.0, 0.0, super.n, minX, minY);
      tessellator.a();
   }

   public void renderContaminationHUD(bjo hud, int width, int height) {
      this.g.N.a(hud);
      GL11.glBlendFunc(770, 771);
      GL11.glTexParameteri(3553, 10241, 9729);
      GL11.glTexParameteri(3553, 10240, 9729);
      bfq tessellator = bfq.a;
      tessellator.b();
      tessellator.a(1.0F, 1.0F, 1.0F, 1.0F);
      double minX = 0.0;
      double minY = 0.0;
      double maxX = 1.0;
      double maxY = 1.0;
      double screenRatio = (double)width / height;
      double textureRatio = 1.7777777777777777;
      if (screenRatio > textureRatio) {
         minY = (1.0 - textureRatio / screenRatio) / 2.0;
         maxY = 1.0 - (1.0 - textureRatio / screenRatio) / 2.0;
      } else if (textureRatio > screenRatio) {
         minX = (1.0 - screenRatio / textureRatio) / 2.0;
         maxX = 1.0 - (1.0 - screenRatio / textureRatio) / 2.0;
      }

      tessellator.a(0.0, height, super.n, minX, maxY);
      tessellator.a(width, height, super.n, maxX, maxY);
      tessellator.a(width, 0.0, super.n, maxX, minY);
      tessellator.a(0.0, 0.0, super.n, minX, minY);
      tessellator.a();
   }

   public void renderBleendingHUD(bjo hud, int width, int height, float alpha) {
      this.g.N.a(hud);
      float prevAlpha = 1.0F / (alpha * 1.1F);
      GL11.glBlendFunc(770, 771);
      GL11.glTexParameteri(3553, 10241, 9729);
      GL11.glTexParameteri(3553, 10240, 9729);
      bfq tessellator = bfq.a;
      tessellator.b();
      tessellator.a(1.0F, 1.0F, 1.0F, ls.a(prevAlpha, 0.0F, 1.0F));
      double minX = 0.0;
      double minY = 0.0;
      double maxX = 1.0;
      double maxY = 1.0;
      double screenRatio = (double)width / height;
      double textureRatio = 1.7777777777777777;
      if (screenRatio > textureRatio) {
         minY = (1.0 - textureRatio / screenRatio) / 2.0;
         maxY = 1.0 - (1.0 - textureRatio / screenRatio) / 2.0;
      } else if (textureRatio > screenRatio) {
         minX = (1.0 - screenRatio / textureRatio) / 2.0;
         maxX = 1.0 - (1.0 - screenRatio / textureRatio) / 2.0;
      }

      tessellator.a(0.0, height, super.n, minX, maxY);
      tessellator.a(width, height, super.n, maxX, maxY);
      tessellator.a(width, 0.0, super.n, maxX, minY);
      tessellator.a(0.0, 0.0, super.n, minX, minY);
      tessellator.a();
   }

   public void drawTexturedModalRect(double width, double weight, int minU, int minV, int maxU, int maxV, int textureSize, double scale) {
      double d = 1.0 / textureSize;
      bfq tessellator = bfq.a;
      tessellator.b();
      tessellator.a(width + 0.0, weight + maxV * scale, super.n, (minU + 0) * d, (minV + maxV) * d);
      tessellator.a(width + maxU * scale, weight + maxV * scale, super.n, (minU + maxU) * d, (minV + maxV) * d);
      tessellator.a(width + maxU * scale, weight + 0.0, super.n, (minU + maxU) * d, (minV + 0) * d);
      tessellator.a(width + 0.0, weight + 0.0, super.n, (minU + 0) * d, (minV + 0) * d);
      tessellator.a();
   }

   public void drawTexturedModalRect(double width, double weight, int minU, int minV, int maxU, int maxV, int textureSize, double scale, float hitMarker) {
      double d = 1.0 / textureSize;
      GL11.glEnable(3042);
      GL11.glBlendFunc(770, 771);
      bfq tessellator = bfq.a;
      tessellator.b();
      tessellator.a(0.6F, 0.0F, 0.0F, hitMarker);
      tessellator.a(width + 0.0, weight + maxV * scale, super.n, (minU + 0) * d, (minV + maxV) * d);
      tessellator.a(width + maxU * scale, weight + maxV * scale, super.n, (minU + maxU) * d, (minV + maxV) * d);
      tessellator.a(width + maxU * scale, weight + 0.0, super.n, (minU + maxU) * d, (minV + 0) * d);
      tessellator.a(width + 0.0, weight + 0.0, super.n, (minU + 0) * d, (minV + 0) * d);
      tessellator.a();
      GL11.glDisable(3042);
   }

   public awf getResolution() {
      return this.res;
   }

   protected void renderHotbar(int width, int height, float partialTicks) {
      if (!this.pre(ElementType.HOTBAR)) {
         this.res = new awf(super.g.u, super.g.d, super.g.e);
         GL11.glDisable(3042);
         super.g.C.a("actionBar");
         GL11.glEnable(32826);
         att.c();
         this.a(super.g.h.bn.c, this.res.a() - 6 - 31, this.res.b() - 6 - 22, partialTicks);
         att.a();
         GL11.glDisable(32826);
         super.g.C.b();
         this.post(ElementType.HOTBAR);
      }
   }

   public void renderIconMedicine(int height) {
      GL11.glEnable(32826);
      att.c();

      for (int i2 = 0; i2 < 4; i2++) {
         int i4 = 3 + i2 * 20 + 2;
         int j2 = height - 16 - 3;
         this.a(i2 + 44, i4, j2, this.g.S.c);
      }

      att.a();
      GL11.glDisable(32826);
   }

   protected void renderCrosshairs(int width, int height) {
      if (!this.pre(ElementType.CROSSHAIRS)) {
         this.bind(avk.m);
         GL11.glEnable(3042);
         GL11.glBlendFunc(775, 769);
         this.b(width / 2 - 7, height / 2 - 7, 0, 0, 16, 16);
         GL11.glDisable(3042);
         this.post(ElementType.CROSSHAIRS);
      }
   }

   protected void renderHelmet(awf res, float partialTicks, boolean hasScreen, int mouseX, int mouseY) {
      if (!this.pre(ElementType.HELMET)) {
         ye itemstack = super.g.h.bn.f(3);
         if (super.g.u.aa == 0 && itemstack != null && itemstack.b() != null) {
            if (itemstack.d == aqz.bf.cF) {
               this.b(res.a(), res.b());
            } else {
               itemstack.b().renderHelmetOverlay(itemstack, super.g.h, res, partialTicks, hasScreen, mouseX, mouseY);
            }
         }

         this.post(ElementType.HELMET);
      }
   }

   protected void renderPortal(int width, int height, float partialTicks) {
      if (!this.pre(ElementType.PORTAL)) {
         float f1 = super.g.h.bO + (super.g.h.bN - super.g.h.bO) * partialTicks;
         if (f1 > 0.0F) {
            this.b(f1, width, height);
         }

         this.post(ElementType.PORTAL);
      }
   }

   protected void renderSleepFade(int width, int height) {
      if (super.g.h.bE() > 0) {
         super.g.C.a("sleep");
         GL11.glDisable(2929);
         GL11.glDisable(3008);
         int sleepTime = super.g.h.bE();
         float opacity = sleepTime / 100.0F;
         if (opacity > 1.0F) {
            opacity = 1.0F - (sleepTime - 100) / 10.0F;
         }

         int color = (int)(220.0F * opacity) << 24 | 1052704;
         a(0, 0, width, height, color);
         GL11.glEnable(3008);
         GL11.glEnable(2929);
         super.g.C.b();
      }
   }

   protected void renderJumpBar(int width, int height) {
      this.bind(avk.m);
      if (!this.pre(ElementType.JUMPBAR)) {
         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
         super.g.C.a("jumpBar");
         float charge = super.g.h.bN();
         boolean barWidth = true;
         int x = width / 2 - 91;
         int filled = (int)(charge * 183.0F);
         int top = height - 32 + 3;
         this.b(x, top, 0, 84, 182, 5);
         if (filled > 0) {
            this.b(x, top, 0, 89, filled, 5);
         }

         super.g.C.b();
         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
         this.post(ElementType.JUMPBAR);
      }
   }

   protected void renderToolHightlight(int width, int height) {
      if (super.g.u.D) {
         super.g.C.a("toolHighlight");
         if (super.q > 0 && super.r != null) {
            String name = super.r.s();
            int opacity = (int)(super.q * 256.0F / 10.0F);
            if (opacity > 255) {
               opacity = 255;
            }

            if (opacity > 0) {
               int y = height - 59;
               if (!super.g.c.b()) {
                  y += 14;
               }

               GL11.glPushMatrix();
               GL11.glEnable(3042);
               GL11.glBlendFunc(770, 771);
               avi font = super.r.b().getFontRenderer(super.r);
               if (font != null) {
                  int x = (width - font.a(name)) / 2;
                  font.a(name, x, y, 16777215 | opacity << 24);
               } else {
                  int x = (width - this.fontrenderer.a(name)) / 2;
                  this.fontrenderer.a(name, x, y, 16777215 | opacity << 24);
               }

               GL11.glDisable(3042);
               GL11.glPopMatrix();
            }
         }

         super.g.C.b();
      }
   }

   protected void renderHUDText(int width, int height) {
      super.g.C.a("forgeHudText");
      ArrayList left = new ArrayList();
      ArrayList right = new ArrayList();
      if (super.g.p()) {
         long event = super.g.f.I();
         if (event >= 120500L) {
            right.add(bu.a("demo.demoExpired"));
         } else {
            right.add(String.format(bu.a("demo.remainingTime"), ma.a((int)(120500L - event))));
         }
      }

      if (super.g.u.ab) {
         super.g.C.a("debug");
         GL11.glPushMatrix();
         left.add("Minecraft " + MC_VERSION + " (" + super.g.E + ")");
         left.add(super.g.l());
         left.add(super.g.m());
         left.add(super.g.o());
         left.add(super.g.n());
         left.add("PE: " + EffectsEngine.instance.emittersRendered + ", P: " + EffectsEngine.instance.particlesRendered);
         left.add(null);
         long event = Runtime.getRuntime().maxMemory();
         long msg = Runtime.getRuntime().totalMemory();
         long free = Runtime.getRuntime().freeMemory();
         long used = msg - free;
         right.add("Used memory: " + used * 100L / event + "% (" + used / 1024L / 1024L + "MB) of " + event / 1024L / 1024L + "MB");
         right.add("Allocated memory: " + msg * 100L / event + "% (" + msg / 1024L / 1024L + "MB)");
         int x1 = ls.c(super.g.h.u);
         int y = ls.c(super.g.h.v);
         int z = ls.c(super.g.h.w);
         float yaw = super.g.h.A;
         int heading = ls.c(super.g.h.A * 4.0F / 360.0F + 0.5) & 3;
         left.add(String.format("x: %.5f (%d) // c: %d (%d)", super.g.h.u, x1, x1 >> 4, x1 & 15));
         left.add(String.format("y: %.3f (feet pos, %.3f eyes pos)", super.g.h.E.b, super.g.h.v));
         left.add(String.format("z: %.5f (%d) // c: %d (%d)", super.g.h.w, z, z >> 4, z & 15));
         left.add(String.format("f: %d (%s) / %f", heading, r.c[heading], ls.g(yaw)));
         if (super.g.f != null && super.g.f.f(x1, y, z)) {
            adr i$ = super.g.f.d(x1, z);
            left.add(
               String.format(
                  "lc: %d b: %s bl: %d sl: %d rl: %d",
                  i$.h() + 15,
                  i$.a(x1 & 15, z & 15, super.g.f.u()).y,
                  i$.a(ach.b, x1 & 15, y, z & 15),
                  i$.a(ach.a, x1 & 15, y, z & 15),
                  i$.c(x1 & 15, y, z & 15, 0)
               )
            );
         } else {
            left.add(null);
         }

         left.add(String.format("ws: %.3f, fs: %.3f, g: %b, fl: %d", super.g.h.bG.b(), super.g.h.bG.a(), super.g.h.F, super.g.f.f(x1, z)));
         right.add(null);

         for (String s : FMLCommonHandler.instance().getBrandings().subList(1, FMLCommonHandler.instance().getBrandings().size())) {
            right.add(s);
         }

         GL11.glPopMatrix();
         super.g.C.b();
      }

      Text var20 = new Text(this.eventParent, left, right);
      if (!MinecraftForge.EVENT_BUS.post(var20)) {
         for (int x = 0; x < left.size(); x++) {
            String var21 = (String)left.get(x);
            if (var21 != null) {
               this.fontrenderer.a(var21, 2, 2 + x * 10, 16777215);
            }
         }

         for (int var221 = 0; var221 < right.size(); var221++) {
            String var21 = (String)right.get(var221);
            if (var21 != null) {
               int w = this.fontrenderer.a(var21);
               this.fontrenderer.a(var21, width - w - 10, 2 + var221 * 10, 16777215);
            }
         }
      }

      super.g.C.b();
      this.post(ElementType.TEXT);
   }

   protected void renderRecordOverlay(int width, int height, float partialTicks) {
      if (super.o > 0) {
         super.g.C.a("overlayMessage");
         float hue = super.o - partialTicks;
         int opacity = (int)(hue * 256.0F / 20.0F);
         if (opacity > 255) {
            opacity = 255;
         }

         if (opacity > 0) {
            GL11.glPushMatrix();
            GL11.glTranslatef(width / 2, height - 48, 0.0F);
            GL11.glEnable(3042);
            GL11.glBlendFunc(770, 771);
            int color = super.p ? Color.HSBtoRGB(hue / 50.0F, 0.7F, 0.6F) & 16777215 : 16777215;
            this.fontrenderer.b(super.j, -this.fontrenderer.a(super.j) / 2, -4, color | opacity << 24);
            GL11.glDisable(3042);
            GL11.glPopMatrix();
         }

         super.g.C.b();
      }
   }

   protected void renderChat(int width, int height) {
      super.g.C.a("chat");
      Chat event = new Chat(this.eventParent, 0, height - 48);
      if (!MinecraftForge.EVENT_BUS.post(event)) {
         GL11.glPushMatrix();
         GL11.glTranslatef(event.posX, event.posY, 0.0F);
         super.h.a(super.i);
         GL11.glPopMatrix();
         this.post(ElementType.CHAT);
         super.g.C.b();
      }
   }

   protected void renderPlayerList(int width, int height) {
      ate scoreobjective = super.g.f.X().a(0);
      bcw handler = super.g.h.a;
      if (super.g.u.T.e && (!super.g.A() || handler.c.size() > 1 || scoreobjective != null)) {
         if (this.pre(ElementType.PLAYER_LIST)) {
            return;
         }

         super.g.C.a("playerList");
         List players = handler.c;
         int maxPlayers = handler.d;
         int rows = maxPlayers;
         boolean columns = true;

         int var22;
         for (var22 = 1; rows > 20; rows = (maxPlayers + var22 - 1) / var22) {
            var22++;
         }

         int columnWidth = 300 / var22;
         if (columnWidth > 150) {
            columnWidth = 150;
         }

         int left = (width - var22 * columnWidth) / 2;
         byte border = 10;
         a(left - 1, border - 1, left + columnWidth * var22, border + 9 * rows, Integer.MIN_VALUE);

         for (int i = 0; i < maxPlayers; i++) {
            int xPos = left + i % var22 * columnWidth;
            int yPos = border + i / var22 * 9;
            a(xPos, yPos, xPos + columnWidth - 1, yPos + 8, 553648127);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glEnable(3008);
            if (i < players.size()) {
               bdj player = (bdj)players.get(i);
               atf team = super.g.f.X().i(player.a);
               String displayName = atf.a(team, player.a);
               this.fontrenderer.a(displayName, xPos, yPos, 16777215);
               if (scoreobjective != null) {
                  int pingIndex = xPos + this.fontrenderer.a(displayName) + 5;
                  int ping = xPos + columnWidth - 12 - 5;
                  if (ping - pingIndex > 5) {
                     atg score = scoreobjective.a().a(player.a, scoreobjective);
                     String scoreDisplay = a.o + "" + score.c();
                     this.fontrenderer.a(scoreDisplay, ping - this.fontrenderer.a(scoreDisplay), yPos, 16777215);
                  }
               }

               GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
               super.g.J().a(avk.m);
               byte var23 = 4;
               int ping = player.b;
               if (ping < 0) {
                  var23 = 5;
               } else if (ping < 150) {
                  var23 = 0;
               } else if (ping < 300) {
                  var23 = 1;
               } else if (ping < 600) {
                  var23 = 2;
               } else if (ping < 1000) {
                  var23 = 3;
               }

               super.n += 100.0F;
               this.b(xPos + columnWidth - 12, yPos, 0, 176 + var23 * 8, 10, 8);
               super.n -= 100.0F;
            }
         }

         this.post(ElementType.PLAYER_LIST);
      }
   }

   protected boolean pre(ElementType type) {
      return MinecraftForge.EVENT_BUS.post(new Pre(this.eventParent, type));
   }

   protected void post(ElementType type) {
      MinecraftForge.EVENT_BUS.post(new Post(this.eventParent, type));
   }

   protected void bind(bjo res) {
      super.g.J().a(res);
   }

   private void drawGradientRect(
      int x, int y, int width, int heigth, float red1, float blue1, float green1, float alpha1, float red2, float blue2, float green2, float alpha2
   ) {
      GL11.glDisable(3553);
      GL11.glEnable(3042);
      GL11.glDisable(3008);
      GL11.glBlendFunc(770, 771);
      GL11.glShadeModel(7425);
      bfq tessellator = bfq.a;
      tessellator.b();
      tessellator.a(red1, blue1, green1, alpha1);
      tessellator.a(width, y, super.n);
      tessellator.a(x, y, super.n);
      tessellator.a(red2, blue2, green2, alpha2);
      tessellator.a(x, heigth, super.n);
      tessellator.a(width, heigth, super.n);
      tessellator.a();
      GL11.glShadeModel(7424);
      GL11.glDisable(3042);
      GL11.glEnable(3008);
      GL11.glEnable(3553);
   }

   private void renderQuad(bfq par1Tessellator, int x, int y, int width, int height, int color) {
      par1Tessellator.b();
      par1Tessellator.d(color);
      par1Tessellator.a(x + 0, y + 0, 0.0);
      par1Tessellator.a(x + 0, y + height, 0.0);
      par1Tessellator.a(x + width, y + height, 0.0);
      par1Tessellator.a(x + width, y + 0, 0.0);
      par1Tessellator.a();
   }

   public void drawEffect(int effectId, int screenId) {
      awf sr = new awf(super.g.u, super.g.d, super.g.e);
      this.b(8 + screenId * 18, sr.b() - 24 - 18, effectId * 15, 0, 15, 18);
   }

   public void drawRightIcon(int effectId, int level, int screenId) {
      awf sr = new awf(super.g.u, super.g.d, super.g.e);
      this.b(sr.a() - 6 - 19, sr.b() - 6 - 63 - 6 - (screenId + 1) * 18, (level - 1) * 19, effectId * 18 + 18, 19, 18);
   }

   public int getEnd(int now, int max) {
      return max <= 0 ? 0 : (int)(69.0F * now / max);
   }

   protected void a(int par1, int par2, int par3, float par4) {
      ye itemstack;
      if (par1 < 36) {
         itemstack = super.g.h.bn.a[par1];
      } else {
         itemstack = PlayerUtils.getInfo(super.g.h).stInv.mainInventory[par1 - 36];
      }

      if (itemstack != null) {
         float f1 = itemstack.c - par4;
         if (f1 > 0.0F) {
            GL11.glPushMatrix();
            float f2 = 1.0F + f1 / 5.0F;
            GL11.glTranslatef(par2 + 8, par3 + 12, 0.0F);
            GL11.glScalef(1.0F / f2, (f2 + 1.0F) / 2.0F, 1.0F);
            GL11.glTranslatef(-(par2 + 8), -(par3 + 12), 0.0F);
         }

         if (itemstack.b() instanceof ItemMedicine) {
            this.renderItemInGui.renderItemIntoGUIMedicine(super.g.l, super.g.J(), itemstack, par2, par3);
         } else {
            avj.e.b(super.g.l, super.g.J(), itemstack, par2, par3);
         }

         if (f1 > 0.0F) {
            GL11.glPopMatrix();
         }

         avj.e.c(super.g.l, super.g.J(), itemstack, par2, par3);
      }
   }

   public void drawCenteredString(avi par1FontRenderer, String par2Str, int par3, int par4, int par5, float alpha) {
      this.drawStringWithShadow(par1FontRenderer, par2Str, par3 - par1FontRenderer.a(par2Str) / 2, par4, par5, alpha);
   }

   public int drawStringWithShadow(avi par1FontRenderer, String par1Str, int par2, int par3, int par4, float alpha) {
      return this.drawString(par1FontRenderer, par1Str, par2, par3, par4, true, alpha);
   }

   public int drawString(avi par1FontRenderer, String par1Str, int par2, int par3, int par4, boolean par5, float alpha) {
      par1FontRenderer.e();
      if (par1FontRenderer.l) {
         par1Str = par1FontRenderer.c(par1Str);
      }

      int l;
      if (par5) {
         l = this.renderString(par1FontRenderer, par1Str, par2 + 1, par3 + 1, par4, true, alpha);
         l = Math.max(l, this.renderString(par1FontRenderer, par1Str, par2, par3, par4, false, alpha));
      } else {
         l = this.renderString(par1FontRenderer, par1Str, par2, par3, par4, false, alpha);
      }

      return l;
   }

   private int renderString(avi par1FontRenderer, String par1Str, int par2, int par3, int par4, boolean par5, float alpha) {
      if (par1Str == null) {
         return 0;
      } else {
         if ((par4 & -67108864) == 0) {
            par4 |= -16777215;
         }

         if (par5) {
            par4 = (par4 & 16579836) >> 2 | par4 & 16777216;
         }

         float red = (par4 >> 16 & 0xFF) / 255.0F;
         float green = (par4 & 0xFF) / 255.0F;
         float blue = (par4 << 0 & 0xFF) / 255.0F;
         par1FontRenderer.p = (par4 >> 24 & 0xFF) / 255.0F;
         GL11.glColor4f(red, blue, green, alpha);
         par1FontRenderer.i = par2;
         par1FontRenderer.j = par3;
         par1FontRenderer.a(par1Str, par5);
         return (int)par1FontRenderer.i;
      }
   }
}
