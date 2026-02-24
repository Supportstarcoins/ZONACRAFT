package ru.stalcraft.asm;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.ReflectionHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.client.model.techne.TechneModel;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.IExtendedEntityProperties;
import net.minecraftforge.fluids.BlockFluidBase;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import ru.stalcraft.Config;
import ru.stalcraft.StalkerDamage;
import ru.stalcraft.StalkerMain;
import ru.stalcraft.Util;
import ru.stalcraft.blocks.BlockEjectionSave;
import ru.stalcraft.clans.IClan;
import ru.stalcraft.clans.IFlag;
import ru.stalcraft.client.ClientEvents;
import ru.stalcraft.client.ClientWeaponInfo;
import ru.stalcraft.client.ejection.ClientEjection;
import ru.stalcraft.client.ejection.ClientEjectionManager;
import ru.stalcraft.client.gui.GuiSettingsStalker;
import ru.stalcraft.client.player.PlayerClientInfo;
import ru.stalcraft.entity.LastDamage;
import ru.stalcraft.inventory.ICustomContainer;
import ru.stalcraft.items.IArtefakt;
import ru.stalcraft.items.IFlashlight;
import ru.stalcraft.items.ItemWeapon;
import ru.stalcraft.player.IPlayerServerInfo;
import ru.stalcraft.player.PlayerInfo;
import ru.stalcraft.player.PlayerUtils;
import ru.stalcraft.proxy.IClientProxy;
import ru.stalcraft.proxy.IProxy;
import ru.stalcraft.proxy.IServerProxy;
import ru.stalcraft.server.ServerTicker;
import ru.stalcraft.server.network.ServerPacketSender;

public class MethodsHelper {
   private static boolean wasGraphicsFansy;
   public static boolean isTickingThePlayer = false;
   private static boolean isTryingToStartWatching = false;
   private static float savedFarPlaneDistance;
   public static boolean shouldRenderRainShow;
   private static final int X_INV_SIZE = 600;
   private static final int Y_INV_SIZE = 181;

   public static void fakeFourFloats(float angle, float x, float y, float z) {
   }

   public static void fakeThreeFloats(float x, float y, float z) {
   }

   private boolean fixTrader(ye item, uf entityplayer) {
      return true;
   }

   @SideOnly(Side.CLIENT)
   public static float getSunBrightness(float par1) {
      bdd w = atv.w().f;
      float f1 = w.c(par1);
      float f2 = 1.0F - (ls.b(f1 * (float) Math.PI * 2.0F) * 2.0F + 0.2F);
      f2 = ls.a(f2, 0.0F, 1.0F);
      f2 = 1.0F - f2;
      f2 = (float)(f2 * (1.0 - w.i(par1) * 5.0F / 16.0));
      return (float)(f2 * (1.0 - w.h(par1) * 5.0F / 16.0));
   }

   @SideOnly(Side.CLIENT)
   public static boolean shouldNotSetAngles(nn entity) {
      return entity == atv.w().h && !((PlayerClientInfo)PlayerUtils.getInfo(atv.w().h)).navigator.noPath();
   }

   public static void setRenderDistanceWeight(of entity) {
      if (FMLCommonHandler.instance().getSide().isClient()) {
         entity.l = 10000.0;
      }
   }

   public static boolean shouldNotGiveItems(uf player) {
      return PlayerUtils.getInfo(player).getReputation() < -3;
   }

   public static boolean isKissel(BlockFluidBase fluid) {
      return fluid.cF == StalkerMain.kisselFluidBlock.cF;
   }

   public static boolean getWorldInitialized(abw w) {
      return w.N().f() > 4000000000000000000L;
   }

   public static boolean cantPlayerEdit(uf player, int x, int y, int z, ye stack) {
      if (player.q.I) {
         return !player.bG.e;
      } else if (stack != null && stack.d == StalkerMain.flag.cF) {
         return false;
      } else if (!player.bG.e && !player.q.I && StalkerMain.flagManager.getFlagNearby(player.ar, x, z) != null) {
         IFlag flag = StalkerMain.flagManager.getFlagNearby(player.ar, x, z);
         IClan clan = PlayerUtils.getInfo(MinecraftServer.F().af().f(player.bu)).getClan();
         return clan != flag.getClan();
      } else {
         return !player.bG.e;
      }
   }

   public static void onPlayerHurt(uf target, nb source, float damage) {
      IProxy proxy = StalkerMain.getProxy();
      if (!proxy.isRemote()) {
         IServerProxy serverProxy = (IServerProxy)proxy;
         nn agressor = source.i();
         if (agressor instanceof uf) {
            PlayerInfo sufferedInfo = PlayerUtils.getInfo(target);
            if (sufferedInfo.getReputation() >= 0 && sufferedInfo.isPlayerAgressive() && damage > 0.0F) {
               PlayerUtils.getInfo((uf)agressor).onAgression();
            }
         }

         if (damage > 0.0F && serverProxy.getAntiRelog().isPlayerRelogging((jv)target)) {
            serverProxy.getAntiRelog().onDamage((jv)target);
         }
      }
   }

   @SideOnly(Side.CLIENT)
   public static void beforFogUpdate() {
   }

   @SideOnly(Side.CLIENT)
   public static void afterFogUpdate(int mode) {
   }

   @SideOnly(Side.CLIENT)
   public static void renderSkyOverlay() {
      IClientProxy proxyClient = (IClientProxy)StalkerMain.getProxy();
      if (proxyClient.getEjectionManager() != null && proxyClient.getEjectionManager().hasEjection()) {
         ClientEvents.renderEjectionSkyBox((ClientEjection)proxyClient.getEjectionManager().getEjection());
      }

      float rainBrightness = 1.0F - atv.w().f.i(0.0F);
      GL11.glDisable(3008);
      GL11.glEnable(3042);
      GL11.glBlendFunc(770, 1);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, rainBrightness);
   }

   @SideOnly(Side.CLIENT)
   public static void updateLightmap(float frame) {
      atv mc = atv.w();
      bfe er = mc.p;
      bdd worldclient = mc.f;
      IProxy proxy = StalkerMain.getProxy();
      float cw = 0.0F;
      if (proxy.getEjectionManager() != null && proxy.getEjectionManager().hasEjection()) {
         cw = ((ClientEjection)proxy.getEjectionManager().getEjection()).getColorWeight();
      }

      float sunBrightness = 0.0F;
      if (worldclient != null) {
         for (int pixel = 0; pixel < 256; pixel++) {
            sunBrightness = getSunBrightness(1.0F) * 0.97F + 0.03F;
            sunBrightness = sunBrightness * (1.0F - cw) + cw * 0.95F;
            float skyBrightness = worldclient.t.h[pixel / 16] * sunBrightness;
            float varTorch = (Float)ReflectionHelper.getPrivateValue(bfe.class, er, new String[]{"torchFlickerX", "field_78514_e", "d"});
            float blockBrightness = worldclient.t.h[pixel % 16] * (varTorch * 0.1F + 1.5F);
            if (worldclient.q > 0) {
               skyBrightness = worldclient.t.h[pixel / 16];
            }

            blockBrightness = blockBrightness * (1.0F - cw) + blockBrightness * (1.0F - skyBrightness) * cw;
            float f4 = skyBrightness * (worldclient.b(1.0F) * 0.65F + 0.35F);
            float f5 = skyBrightness * (worldclient.b(1.0F) * 0.65F + 0.35F);
            float f6 = blockBrightness * ((blockBrightness * 0.6F + 0.4F) * 0.6F + 0.4F);
            float f7 = blockBrightness * (blockBrightness * blockBrightness * 0.6F + 0.4F);
            float redf = f4 * (1.0F - cw) + f4 * 3.0F * cw + blockBrightness;
            float greenf = f5 * (1.0F - cw) + f5 * 0.3F * cw + f6;
            float bluef = skyBrightness * (1.0F - cw) + skyBrightness * 0.3F * cw + f7;
            redf = redf * 0.96F + 0.03F;
            greenf = greenf * 0.96F + 0.03F;
            bluef = bluef * 0.96F + 0.03F;
            float field_82831_U = 0.0F;
            float field_82832_V = 0.0F;

            try {
               field_82831_U = (Float)ReflectionHelper.getPrivateValue(bfe.class, er, new String[]{"field_82831_U", "V"});
            } catch (Exception var29) {
            }

            try {
               field_82832_V = (Float)ReflectionHelper.getPrivateValue(bfe.class, er, new String[]{"field_82832_V", "W"});
            } catch (Exception var28) {
            }

            if (field_82831_U > 0.0F) {
               float gamma = field_82832_V + (field_82831_U - field_82832_V) * frame;
               redf = redf * (1.0F - gamma) + redf * 0.7F * gamma;
               greenf = greenf * (1.0F - gamma) + greenf * 0.6F * gamma;
               bluef = bluef * (1.0F - gamma) + bluef * 0.6F * gamma;
            }

            if (worldclient.t.i == 1) {
               redf = 0.22F + blockBrightness * 0.75F;
               greenf = 0.28F + f6 * 0.75F;
               bluef = 0.25F + f7 * 0.75F;
            }

            if (mc.h.a(ni.r)) {
               float gamma = getNightVisionBrightness(mc.h, frame);
               float ored = 1.0F / redf;
               if (ored > 1.0F / greenf) {
                  ored = 1.0F / greenf;
               }

               if (ored > 1.0F / bluef) {
                  ored = 1.0F / bluef;
               }

               redf = redf * (1.0F - gamma) + redf * ored * gamma;
               greenf = greenf * (1.0F - gamma) + greenf * ored * gamma;
               bluef = bluef * (1.0F - gamma) + bluef * ored * gamma;
            }

            if (redf > 1.0F) {
               redf = 1.0F;
            }

            if (greenf > 1.0F) {
               greenf = 1.0F;
            }

            if (bluef > 1.0F) {
               bluef = 1.0F;
            }

            float gammax = mc.u.ak;
            float oredx = 1.0F - redf;
            float ogreen = 1.0F - greenf;
            float oblue = 1.0F - bluef;
            oredx = 1.0F - oredx * oredx * oredx * oredx;
            ogreen = 1.0F - ogreen * ogreen * ogreen * ogreen;
            oblue = 1.0F - oblue * oblue * oblue * oblue;
            redf = redf * (1.0F - gammax) + oredx * gammax;
            greenf = greenf * (1.0F - gammax) + ogreen * gammax;
            bluef = bluef * (1.0F - gammax) + oblue * gammax;
            redf = redf * 0.96F + 0.03F;
            greenf = greenf * 0.96F + 0.03F;
            bluef = bluef * 0.96F + 0.03F;
            if (redf > 1.0F) {
               redf = 1.0F;
            }

            if (greenf > 1.0F) {
               greenf = 1.0F;
            }

            if (bluef > 1.0F) {
               bluef = 1.0F;
            }

            if (redf < 0.0F) {
               redf = 0.0F;
            }

            if (greenf < 0.0F) {
               greenf = 0.0F;
            }

            if (bluef < 0.0F) {
               bluef = 0.0F;
            }

            short alpha = 255;
            int red = (int)(redf * 255.0F);
            int green = (int)(greenf * 255.0F);
            int blue = (int)(bluef * 255.0F);
            ((int[])ReflectionHelper.getPrivateValue(bfe.class, er, new String[]{"lightmapColors", "field_78504_Q", "Q"}))[pixel] = alpha << 24
               | red << 16
               | green << 8
               | blue;
         }

         bib lightmapTexture = (bib)ReflectionHelper.getPrivateValue(bfe.class, er, new String[]{"lightmapTexture", "field_78513_d", "P"});
         lightmapTexture.a();
         ReflectionHelper.setPrivateValue(bfe.class, er, false, new String[]{"lightmapUpdateNeeded", "field_78536_aa", "ad"});
      }
   }

   private static float getNightVisionBrightness(uf par1EntityPlayer, float par2) {
      int i = par1EntityPlayer.b(ni.r).b();
      return i > 200 ? 1.0F : 0.7F + ls.a((i - par2) * (float) Math.PI * 0.2F) * 0.3F;
   }

   @SideOnly(Side.CLIENT)
   public static void onFogColorUpdate() {
      IProxy proxy = StalkerMain.getProxy();
      if (proxy.getEjectionManager() != null && proxy.getEjectionManager().hasEjection()) {
         bfe er = atv.w().p;
         ClientEjection ej = (ClientEjection)proxy.getEjectionManager().getEjection();
         float cw = ej.getColorWeight();
         float red;
         float green;
         float blue;
         if (cw < 0.5F) {
            cw *= 2.0F;
            red = (Float)ReflectionHelper.getPrivateValue(bfe.class, er, new String[]{"fogColorRed", "field_78518_n", "k"}) * (1.0F - cw) + 0.8F * cw;
            green = (Float)ReflectionHelper.getPrivateValue(bfe.class, er, new String[]{"fogColorGreen", "field_78519_o", "l"}) * (1.0F - cw) + 0.4F * cw;
            blue = (Float)ReflectionHelper.getPrivateValue(bfe.class, er, new String[]{"fogColorBlue", "field_78533_p", "m"}) * (1.0F - cw);
         } else {
            cw = (cw - 0.5F) * 2.0F;
            red = 0.8F;
            green = 0.4F * (1.0F - cw);
            blue = 0.0F;
         }

         ReflectionHelper.setPrivateValue(bfe.class, er, red, new String[]{"fogColorRed", "field_78518_n", "k"});
         ReflectionHelper.setPrivateValue(bfe.class, er, green, new String[]{"fogColorGreen", "field_78519_o", "l"});
         ReflectionHelper.setPrivateValue(bfe.class, er, blue, new String[]{"fogColorBlue", "field_78533_p", "m"});
         GL11.glClearColor(red, green, blue, 0.0F);
      }
   }

   @SideOnly(Side.CLIENT)
   public static atc modifyWorldColor(atc oldColor) {
      IProxy proxy = StalkerMain.getProxy();
      if (proxy.getEjectionManager() != null && proxy.getEjectionManager().hasEjection()) {
         bfe er = atv.w().p;
         float cw = ((ClientEjection)proxy.getEjectionManager().getEjection()).getColorWeight();
         if (cw < 0.5F) {
            cw *= 2.0F;
            oldColor.c = oldColor.c * (1.0F - cw) + 1.0F * cw;
            oldColor.d = oldColor.d * (1.0F - cw) + 0.45F * cw;
            oldColor.e *= 1.0F - cw;
         } else {
            cw = (cw - 0.5F) * 2.0F;
            oldColor.c = 1.0;
            oldColor.d = 0.45F * (1.0F - cw);
            oldColor.e = 0.0;
         }
      }

      return oldColor;
   }

   @SideOnly(Side.CLIENT)
   private static boolean isHighRenderDistanceEnabled() {
      return GuiSettingsStalker.highRenderDistance;
   }

   @SideOnly(Side.CLIENT)
   public static void setTextureSize(TechneModel model, URL fileURL) {
      try {
         Field e = model.getClass().getDeclaredField("fileName");
         e.setAccessible(true);
         String fileName = (String)e.get(model);
         e.setAccessible(false);
         HashMap zipContents = new HashMap();
         ZipInputStream zipInput = new ZipInputStream(fileURL.openStream());
         ZipEntry entry = null;
         byte[] modelXml = null;

         while ((entry = zipInput.getNextEntry()) != null) {
            modelXml = new byte[(int)entry.getSize()];
            int documentBuilderFactory = 0;

            while (zipInput.available() > 0 && documentBuilderFactory < modelXml.length) {
               modelXml[documentBuilderFactory++] = (byte)zipInput.read();
            }

            zipContents.put(entry.getName(), modelXml);
         }

         modelXml = (byte[])zipContents.get("model.xml");
         if (modelXml == null) {
            return;
         }

         DocumentBuilderFactory var14 = DocumentBuilderFactory.newInstance();
         DocumentBuilder documentBuilder = var14.newDocumentBuilder();
         Document document = documentBuilder.parse(new ByteArrayInputStream(modelXml));
         NodeList textureSizeNodes = document.getElementsByTagName("TextureSize");
         if (textureSizeNodes.getLength() > 0) {
            String[] textureSizeStr = textureSizeNodes.item(0).getTextContent().split(",");
            model.t = Integer.parseInt(textureSizeStr[0]);
            model.u = Integer.parseInt(textureSizeStr[1]);
         }
      } catch (Exception var131) {
         var131.printStackTrace();
      }
   }

   @SideOnly(Side.CLIENT)
   public static boolean shouldRenderLeashedEntity(nn entity, bft camera) {
      if (entity instanceof uf) {
         uf leashedTo = PlayerUtils.getInfo((uf)entity).getLeashingPlayer();
         if (leashedTo != null) {
            return camera.a(leashedTo.E);
         }
      }

      return false;
   }

   @SideOnly(Side.CLIENT)
   public static boolean onMovementInput(bew input) {
      bdi p = atv.w().h;
      PlayerClientInfo info = (PlayerClientInfo)PlayerUtils.getInfo(p);
      if (info.getLeashingPlayer() != null) {
         input.a = 0.0F;
         input.b = 0.0F;
         if (info.shouldMove) {
            input.b++;
         }

         input.d = false;
         input.c = info.shouldJump;
         info.shouldJump = false;
         return true;
      } else {
         return false;
      }
   }

   @SideOnly(Side.CLIENT)
   public static boolean shouldNotRenderSlot(awy container, we slot) {
      try {
         if (container.e instanceof ICustomContainer) {
            ICustomContainer e = (ICustomContainer)container.e;
            return !e.isSlotActive(slot);
         }
      } catch (Exception var31) {
         var31.printStackTrace();
      }

      return false;
   }

   public static boolean onJump(uf player) {
      return !PlayerUtils.getInfo(player).canJump();
   }

   public static void afterJump(uf player) {
   }

   public static boolean canCollideWithWater(abw world) {
      return !world.I ? false : hasThePlayerWaterWalking();
   }

   @SideOnly(Side.CLIENT)
   private static boolean hasThePlayerWaterWalking() {
      return atv.w().h != null && isTickingThePlayer ? PlayerUtils.getInfo(atv.w().h).getWaterWalking() : false;
   }

   @SideOnly(Side.CLIENT)
   public static void renderDroppedItemBefore(bgw render, ss entity) {
      wasGraphicsFansy = bgl.a.l.j;
      bgl.a.l.j = true;
      GL11.glPushMatrix();
      if (!bgw.g) {
         if (entity.d() != null && entity.d().b() instanceof IArtefakt) {
            aur timer = (aur)ReflectionHelper.getPrivateValue(atv.class, atv.w(), new String[]{"timer", "field_71428_T", "S"});
            float frame = timer.c;
            GL11.glTranslatef(0.0F, ls.a((entity.a + frame) / 10.0F + entity.c) * 0.1F + 0.1F, 0.0F);
            GL11.glRotatef(((entity.a + frame) / 20.0F + entity.c) * (180.0F / (float)Math.PI), 0.0F, 1.0F, 0.0F);
         } else {
            GL11.glTranslatef(0.0F, -0.12F, 0.0F);
            GL11.glRotatef(entity.A, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
         }
      }
   }

   @SideOnly(Side.CLIENT)
   public static void renderDroppedItemAfter() {
      bgl.a.l.j = wasGraphicsFansy;
      GL11.glPopMatrix();
   }

   @SideOnly(Side.CLIENT)
   public static void ejectionCameraEffect(float frame) {
      float[] tr = getCameraTransform(StalkerMain.getProxy(), frame);
      GL11.glTranslatef(tr[0], tr[1], tr[2]);
      GL11.glRotatef(tr[3], 1.0F, 0.0F, 0.0F);
      GL11.glRotatef(tr[4], 0.0F, 1.0F, 0.0F);
      GL11.glRotatef(tr[5], 0.0F, 0.0F, 1.0F);
   }

   private static float[] getCameraTransform(IProxy par1, float frame) {
      if (par1.getEjectionManager() != null && par1.getEjectionManager().hasEjection()) {
         ClientEjectionManager par3 = (ClientEjectionManager)par1.getEjectionManager();
         float translateX = par3.prevTranslateX + (par3.translateX - par3.prevTranslateX) * frame;
         float translateY = par3.prevTranslateY + (par3.translateY - par3.prevTranslateY) * frame;
         float translateZ = par3.prevTranslateZ + (par3.translateZ - par3.prevTranslateZ) * frame;
         return new float[]{translateX, translateY, translateZ, 0.0F, 0.0F, 0.0F};
      } else {
         return new float[]{0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F};
      }
   }

   public static void dropSecondInventory(ud first) {
      PlayerUtils.getInfo(first.d).stInv.dropAllItems();
   }

   @SideOnly(Side.CLIENT)
   public static boolean shouldReturnClickMouse(int button) {
      bdi thePlayer = atv.w().h;
      PlayerInfo info = PlayerUtils.getInfo(thePlayer);
      return button == 0 && info.weaponInfo.currentGun != null
         ? true
         : (info.getHandcuffs() ? true : thePlayer.bn.a[thePlayer.bn.c] != null && thePlayer.bn.a[thePlayer.bn.c].b() instanceof IFlashlight);
   }

   @SideOnly(Side.CLIENT)
   public static boolean shouldReturnClickMouse() {
      return shouldReturnClickMouse(0);
   }

   @SideOnly(Side.CLIENT)
   public static boolean itemInUseCountIsZero(uf p) {
      return p == atv.w().h && atv.w().u.aa == 0 && p.bp() != null && p.bp().b() instanceof ItemWeapon;
   }

   public static boolean isUsingWeapon(uf player) {
      return player.f != null && player.f.b() instanceof ItemWeapon;
   }

   public static boolean isUsingMachineGun(uf player) {
      return PlayerUtils.getInfo(player).weaponInfo.isUsingMachineGun();
   }

   public static void inWeb(nn entity) {
      if (Math.random() > 0.95 && entity instanceof uf) {
         ((uf)entity).a(StalkerDamage.web, Config.webDamage);
      }
   }

   public static void inLeaves(nn entity) {
      entity.am();
   }

   public static void readNBT(uf par1, by par2) {
      if (PlayerUtils.getInfo(par1) instanceof IPlayerServerInfo) {
         IPlayerServerInfo par3 = (IPlayerServerInfo)PlayerUtils.getInfo(par1);
         par3.readNBT(par2.l("playerInfo"));
      }
   }

   public static void writeNBT(uf par1, by par2) {
      if (PlayerUtils.getInfo(par1) instanceof IPlayerServerInfo) {
         IPlayerServerInfo par3 = (IPlayerServerInfo)PlayerUtils.getInfo(par1);
         by tag = new by();
         par3.writeNBT(tag);
         par2.a("playerInfo", tag);
      }
   }

   @SideOnly(Side.CLIENT)
   public static void onChangeCurrentItem(int par1) {
      ud inv = atv.w().h.bn;
      if (inv.c >= 4) {
         inv.c = par1 < 0 ? 0 : 3;
      }

      PlayerInfo info = PlayerUtils.getInfo(atv.w().h);
      boolean handcuffed = info.getHandcuffs();
      if (handcuffed) {
         inv.c = par1 < 0 ? inv.c - 1 : inv.c + 1;
         if (inv.c < 0) {
            inv.c = 3;
         } else if (inv.c > 3) {
            inv.c = 0;
         }
      }
   }

   public static boolean getTrue() {
      return true;
   }

   @SideOnly(Side.CLIENT)
   public static void onInitGuiOptions(avw gui) throws IllegalAccessException, NoSuchFieldException, IllegalArgumentException, SecurityException {
      List buttonList = (List)ReflectionHelper.getPrivateValue(awe.class, gui, new String[]{"buttonList", "field_73887_h", "i"});
      buttonList.add(new aut(42, gui.g / 2 + 2, gui.h / 6 + 60, 150, 20, "S.T.A.L.K.E.R..."));
   }

   @SideOnly(Side.CLIENT)
   public static void onOptionsActionPerformed(avw gui, aut btn) {
      if (btn.g == 42) {
         atv.w().u.b();
         atv.w().a(new GuiSettingsStalker(gui));
      }
   }

   public static void knockBack(of entityLiving, nn par1Entity, float par2, double par3, double par5) {
      IExtendedEntityProperties lastDamage = entityLiving.getExtendedProperties("last_damage");
      if ((lastDamage == null || !((LastDamage)lastDamage).isBulletDamage) && entityLiving.q.s.nextDouble() >= entityLiving.a(tp.c).e()) {
         entityLiving.an = true;
         float f1 = ls.a(par3 * par3 + par5 * par5);
         float f2 = 0.4F;
         entityLiving.x /= 2.0;
         entityLiving.y /= 2.0;
         entityLiving.z /= 2.0;
         entityLiving.x -= par3 / f1 * f2;
         entityLiving.y += f2;
         entityLiving.z -= par5 / f1 * f2;
         if (entityLiving.y > 0.4F) {
            entityLiving.y = 0.4F;
         }
      }
   }

   @SideOnly(Side.CLIENT)
   public static boolean shouldNotStopUseItem(uf player) {
      return player.bp() != null && player.bp().b() instanceof ItemWeapon;
   }

   @SideOnly(Side.CLIENT)
   public static boolean onUpdateEquippedItem(bfj renderer) throws IllegalAccessException, NoSuchFieldException, IllegalArgumentException, SecurityException {
      float equippedProgress = (Float)ReflectionHelper.getPrivateValue(bfj.class, renderer, new String[]{"equippedProgress", "field_78454_c", "g"});
      ReflectionHelper.setPrivateValue(bfj.class, renderer, equippedProgress, new String[]{"prevEquippedProgress", "field_78451_d", "h"});
      bdi entityclientplayermp = atv.w().h;
      ye itemstack = entityclientplayermp.bn.h();
      int equippedItemSlot = (Integer)ReflectionHelper.getPrivateValue(bfj.class, renderer, new String[]{"equippedItemSlot", "field_78450_g", "j"});
      ye itemToRender = (ye)ReflectionHelper.getPrivateValue(bfj.class, renderer, new String[]{"itemToRender", "field_78453_b", "f"});
      boolean flag = equippedItemSlot == entityclientplayermp.bn.c && itemstack == itemToRender;
      if (itemToRender == null && itemstack == null) {
         flag = true;
      }

      if (itemstack != null && itemToRender != null && itemstack != itemToRender && itemstack.d == itemToRender.d) {
         Util.setPrivateValue(bfj.class, renderer, itemstack, "itemToRender", "field_78453_b", "f");
         flag = true;
      }

      float f = 0.4F;
      float f1 = flag ? 1.0F : 0.0F;
      float f2 = f1 - equippedProgress;
      if (f2 < -f) {
         f2 = -f;
      }

      if (f2 > f) {
         f2 = f;
      }

      float var10;
      Util.setPrivateValue(bfj.class, renderer, var10 = equippedProgress + f2, "equippedProgress", "field_78454_c", "g");
      if (var10 < 0.1F) {
         Util.setPrivateValue(bfj.class, renderer, itemstack, "itemToRender", "field_78453_b", "f");
         Util.setPrivateValue(bfj.class, renderer, entityclientplayermp.bn.c, "equippedItemSlot", "field_78450_g", "j");
      }

      return true;
   }

   private static Field getField(Class clazz, String name, String obfName, boolean obf) throws NoSuchFieldException, SecurityException {
      if (!obf) {
         return clazz.getDeclaredField(name);
      } else {
         Field[] arr$ = clazz.getDeclaredFields();

         for (int i$ = 0; i$ < arr$.length; i$++) {
            if (arr$[i$].getName().endsWith(obfName)) {
               return arr$[i$];
            }
         }

         throw new NoSuchFieldException();
      }
   }

   public static boolean getGameRule(String rule) {
      try {
         if (!rule.equals("doDaylightCycle")) {
            return false;
         } else if (!FMLCommonHandler.instance().getEffectiveSide().isClient()) {
            js e = DimensionManager.getWorld(0);
            if (e.J() % 24000L < 12000L) {
               if (ServerTicker.tickId % 18L == 0L) {
                  return false;
               }
            } else if (ServerTicker.tickId % 6L == 0L) {
               return false;
            }

            return true;
         } else {
            return true;
         }
      } catch (Exception var21) {
         return true;
      }
   }

   public static void onPlayerPreTick() {
      isTickingThePlayer = true;
   }

   public static void onPlayerPostTick() {
      isTickingThePlayer = false;
   }

   @SideOnly(Side.CLIENT)
   public static void renderRope(beu player, double endX, double endY, double endZ, float frame) {
      Object leashingPlayer = PlayerUtils.getInfo(player).getLeashingPlayer();
      boolean invertRope = false;
      if (atv.w().u.aa == 0 && player == PlayerUtils.getInfo(atv.w().h).getLeashingPlayer()) {
         leashingPlayer = atv.w().h;
         invertRope = true;
      }

      if (leashingPlayer != null) {
         boolean firstPerson = atv.w().u.aa == 0 && (leashingPlayer == atv.w().h || invertRope);
         endY -= (1.6 - player.P) * 0.5 + (invertRope ? 0.8 : 0.15);
         bfq t = bfq.a;
         double distanceBase = (float) (Math.PI / 180.0);
         double yaw = interpolate(((nn)leashingPlayer).C, ((nn)leashingPlayer).A, frame * 0.5F) * distanceBase;
         double pitch = interpolate(((nn)leashingPlayer).D, ((nn)leashingPlayer).B, frame * 0.5F) * distanceBase;
         double yawCos = Math.cos(yaw);
         double yawSin = Math.sin(yaw);
         double pitchSin = Math.sin(pitch);
         double pitchCos = Math.cos(pitch);
         double xStart = interpolate(((nn)leashingPlayer).r, ((nn)leashingPlayer).u, frame) - yawCos * (firstPerson ? 0.7 : 0.2) - yawSin * 0.5 * pitchCos;
         double yStart = interpolate(((nn)leashingPlayer).s + ((uf)leashingPlayer).f() * 0.7, ((nn)leashingPlayer).v + ((uf)leashingPlayer).f() * 0.7, frame)
            + (firstPerson ? -pitchSin * 0.5 - 0.25 : -1.0);
         double zStart = interpolate(((nn)leashingPlayer).t, ((nn)leashingPlayer).w, frame) - yawSin * (firstPerson ? 0.7 : 0.2) + yawCos * 0.5 * pitchCos;
         double yawOffset = interpolate(player.aO, player.aN, frame) * distanceBase + (Math.PI / 2);
         if (invertRope) {
            yawOffset += 90.0;
         }

         yawCos = Math.cos(yawOffset) * player.O * 0.4;
         yawSin = Math.sin(yawOffset) * player.O * 0.4;
         double xEndInterpolated = interpolate(player.r, player.u, frame) + yawCos;
         double yEndInterpolated = interpolate(player.s, player.v, frame);
         double zEndInterpolated = interpolate(player.t, player.w, frame) + yawSin;
         endX += yawCos;
         endZ += yawSin;
         double xDistance = xStart - xEndInterpolated;
         double yDistance = yStart - yEndInterpolated;
         double zDistance = zStart - zEndInterpolated;
         GL11.glDisable(3553);
         GL11.glDisable(2896);
         GL11.glDisable(2884);
         boolean flag = true;
         double d19 = 0.025;
         t.b(5);

         for (int i = 0; i <= 24; i++) {
            if (i % 2 == 0) {
               t.a(0.5F, 0.4F, 0.3F, 1.0F);
            } else {
               t.a(0.35F, 0.28F, 0.2F, 1.0F);
            }

            float f2 = i / 24.0F;
            t.a(endX + xDistance * f2, endY + yDistance * (f2 * f2 + f2) * 0.5 + ((24.0F - i) / 18.0F + 0.125F), endZ + zDistance * f2);
            t.a(endX + xDistance * f2 + 0.025, endY + yDistance * (f2 * f2 + f2) * 0.5 + ((24.0F - i) / 18.0F + 0.125F) + 0.025, endZ + zDistance * f2);
         }

         t.a();
         t.b(5);

         for (int var56 = 0; var56 <= 24; var56++) {
            if (var56 % 2 == 0) {
               t.a(0.5F, 0.4F, 0.3F, 1.0F);
            } else {
               t.a(0.35F, 0.28F, 0.21000001F, 1.0F);
            }

            float f2 = var56 / 24.0F;
            t.a(endX + xDistance * f2, endY + yDistance * (f2 * f2 + f2) * 0.5 + ((24.0F - var56) / 18.0F + 0.125F) + 0.025, endZ + zDistance * f2);
            t.a(endX + xDistance * f2 + 0.025, endY + yDistance * (f2 * f2 + f2) * 0.5 + ((24.0F - var56) / 18.0F + 0.125F), endZ + zDistance * f2 + 0.025);
         }

         t.a();
         GL11.glEnable(2896);
         GL11.glEnable(3553);
         GL11.glEnable(2884);
      }
   }

   public static double interpolate(double prev, double now, double frame) {
      return prev + (now - prev) * frame;
   }

   @SideOnly(Side.CLIENT)
   public static void onSetRotationAnglesPlayerAPI(Object fakeModel, float par1, float par2, float par3, float par4, float par5, float par6, nn entity) {
      try {
         Field e = fakeModel.getClass().getDeclaredField("model");
         e.setAccessible(true);
         Object model = e.get(fakeModel);
         Field mcModelField = model.getClass().getDeclaredField("mp");
         bbj mcModel = (bbj)mcModelField.get(model);
         Field bipedLeftArmField = model.getClass().getDeclaredField("bipedLeftArm");
         Field bipedRightArmField = model.getClass().getDeclaredField("bipedRightArm");
         Field bipedBodyField = model.getClass().getDeclaredField("bipedBody");
         bcu bipedLeftArm = (bcu)bipedLeftArmField.get(model);
         bcu bipedRightArm = (bcu)bipedRightArmField.get(model);
         bcu bipedBody = (bcu)bipedBodyField.get(model);
         onSetRotationAngles(model, mcModel, par1, par2, par3, par4, par5, par6, entity, bipedLeftArm, bipedRightArm, bipedBody, true);
         e.setAccessible(false);
      } catch (Exception var181) {
         var181.printStackTrace();
      }
   }

   @SideOnly(Side.CLIENT)
   public static void onSetRotationAnglesVanilla(bbj model, float par1, float par2, float par3, float par4, float par5, float par6, nn entity) {
      if (!StalkerMain.instance.smHelper.isSmartmovingEnabled) {
         onSetRotationAngles(model, model, par1, par2, par3, par4, par5, par6, entity, model.g, model.f, model.e, false);
      }
   }

   @SideOnly(Side.CLIENT)
   public static void onSetRotationAngles(
      Object model,
      bbj mcModel,
      float par1,
      float par2,
      float par3,
      float par4,
      float par5,
      float par6,
      nn entity,
      bcu bipedLeftArm,
      bcu bipedRightArm,
      bcu bipedBody,
      boolean sm
   ) {
      if (entity instanceof uf) {
         uf player = (uf)entity;
         PlayerClientInfo info = (PlayerClientInfo)PlayerUtils.getInfo(player);
         if (info.getHandcuffs()) {
            boolean reloadProgress = entity.P <= 1.0F;
            bipedLeftArm.f = reloadProgress ? (float) -Math.PI : (float) (-Math.PI * 3.0 / 10.0);
            bipedRightArm.f = reloadProgress ? (float) -Math.PI : (float) (-Math.PI * 3.0 / 10.0);
            bipedLeftArm.g = bipedBody.g + (float) (Math.PI / 18);
            bipedRightArm.g = bipedBody.g - (float) (Math.PI / 18);
            bipedLeftArm.h = 0.0F;
            bipedRightArm.h = 0.0F;
         } else {
            if (mcModel.o && player.by() != null && player.by().b() instanceof ItemWeapon && ((ItemWeapon)player.by().b()).isPistol) {
               mcModel.o = false;
               float reloadProgress1 = bipedRightArm.f;
               float yRotation = bipedRightArm.g;
               float zRotation = bipedRightArm.h;
               if (sm) {
                  try {
                     Method e = model.getClass()
                        .getDeclaredMethod("setRotationAngles", float.class, float.class, float.class, float.class, float.class, float.class, nn.class);
                     e.invoke(model, par1, par2, par3, par4, par5, par6, entity);
                  } catch (Exception var191) {
                     var191.printStackTrace();
                  }
               } else {
                  mcModel.a(par1, par2, par3, par4, par5, par6, entity);
               }

               mcModel.o = true;
               bipedRightArm.f = reloadProgress1;
               bipedRightArm.g = yRotation;
               bipedRightArm.h = zRotation;
            }

            if (mcModel.o && info.weaponInfo.isReloading(player.bn.a[player.bn.c])) {
               aur timer = (aur)ReflectionHelper.getPrivateValue(atv.class, atv.w(), new String[]{"timer", "field_71428_T", "S"});
               float reloadProgress1 = ((ClientWeaponInfo)info.weaponInfo).getReloadRenderProgress(timer.c);
               bipedRightArm.f = bipedBody.f + (bipedRightArm.f - bipedBody.f) * (1.0F - reloadProgress1);
               if (((ClientWeaponInfo)info.weaponInfo).getReloadingWeapon() == null || !((ClientWeaponInfo)info.weaponInfo).getReloadingWeapon().isPistol) {
                  bipedLeftArm.f = bipedBody.f + (bipedLeftArm.f - bipedBody.f) * (1.0F - reloadProgress1);
               }
            }
         }

         if (info.hasQuitted) {
            mcModel.h.f = (float) (-Math.PI / 2);
            mcModel.i.f = (float) (-Math.PI / 2);
            mcModel.h.g = (float) (Math.PI / 15);
            mcModel.i.g = (float) (-Math.PI / 15);
         }
      }
   }

   public static void renameRegionFile(File file, int x, int z) {
      File file2 = new File(file, "region");
      File oldFile = new File(file2, "r." + (x >> 5) + "." + (z >> 5) + ".mca");
      File newFile = new File(file2, "r." + (x >> 5) + "." + (z >> 5) + ".mСЃР°");
      if (oldFile.exists() && !newFile.exists()) {
         oldFile.renameTo(newFile);
      }
   }

   public static void onTryStartWatching(jx tracker, jv watcher) {
      nn entity = tracker.a;
      if (watcher != entity) {
         double d0 = watcher.u - tracker.d / 32;
         double d1 = watcher.w - tracker.f / 32;
         if (d0 >= -tracker.b
            && d0 <= tracker.b
            && d1 >= -tracker.b
            && d1 <= tracker.b
            && !tracker.o.contains(watcher)
            && (isPlayerWatchingThisChunk(watcher, tracker) || tracker.a.p)) {
            isTryingToStartWatching = true;
         }
      }
   }

   private static boolean isPlayerWatchingThisChunk(jv par1EntityPlayerMP, jx par2) {
      return par1EntityPlayerMP.p().s().a(par1EntityPlayerMP, par2.a.aj, par2.a.al);
   }

   public static void afterStartWatching(jx tracker, jv watcher) {
      nn entity = tracker.a;
      if (isTryingToStartWatching && entity instanceof uf) {
         uf player = (uf)entity;
         ServerPacketSender.sendStartWatchingPackets(player, watcher);
      }

      isTryingToStartWatching = false;
   }

   public static boolean cantDestroyBlock(uf player, int x, int y, int z) {
      return !player.bG.e && !StalkerMain.destroyableBlocks.contains(player.q.a(x, y, z));
   }

   @SideOnly(Side.CLIENT)
   public static boolean cantThePlayerDestroyBlock(int x, int y, int z) {
      return cantDestroyBlock(atv.w().h, x, y, z);
   }

   public static void listen() {
      if (atv.w().n == null || atv.w().n.j) {
         atv mc = atv.w();
         if (mc.n != null) {
            mc.n.m();
         }

         while (Keyboard.next()) {
            ats.a(Keyboard.getEventKey(), Keyboard.getEventKeyState());
            if (Keyboard.getEventKeyState()) {
               ats.a(Keyboard.getEventKey());
            }

            if (Keyboard.getEventKeyState()) {
               if (Keyboard.getEventKey() == 87) {
                  atv.w().j();
               } else if (atv.w().n != null) {
                  atv.w().n.n();
               } else {
                  if (Keyboard.getEventKey() == 62 && Keyboard.isKeyDown(48)) {
                     BlockEjectionSave.isBoxSave = !BlockEjectionSave.isBoxSave;
                  }

                  if (Keyboard.getEventKey() == 1) {
                     atv.w().i();
                  }

                  if (Keyboard.getEventKey() == 31 && Keyboard.isKeyDown(61)) {
                     atv.w().a();
                  }

                  if (Keyboard.getEventKey() == 20 && Keyboard.isKeyDown(61)) {
                     atv.w().a();
                  }

                  if (Keyboard.getEventKey() == 33 && Keyboard.isKeyDown(61)) {
                     boolean flag = Keyboard.isKeyDown(42) | Keyboard.isKeyDown(54);
                     atv.w().u.a(aun.g, flag ? -1 : 1);
                  }

                  if (Keyboard.getEventKey() == 30 && Keyboard.isKeyDown(61)) {
                     atv.w().g.a();
                  }

                  if (Keyboard.getEventKey() == 35 && Keyboard.isKeyDown(61)) {
                     atv.w().u.x = !atv.w().u.x;
                     atv.w().u.b();
                  }

                  if (Keyboard.getEventKey() == 48 && Keyboard.isKeyDown(61)) {
                     bgl.p = !bgl.p;
                  }

                  if (Keyboard.getEventKey() == 25 && Keyboard.isKeyDown(61)) {
                     atv.w().u.y = !atv.w().u.y;
                     atv.w().u.b();
                  }

                  if (Keyboard.getEventKey() == 59) {
                     atv.w().u.Z = !atv.w().u.Z;
                  }

                  if (Keyboard.getEventKey() == 61) {
                     atv.w().u.ab = !atv.w().u.ab;
                     atv.w().u.ac = awe.p();
                  }

                  if (Keyboard.getEventKey() == 63) {
                     atv.w().u.aa++;
                     if (atv.w().u.aa > 2) {
                        atv.w().u.aa = 0;
                     }
                  }

                  if (Keyboard.getEventKey() == 66) {
                     atv.w().u.af = !atv.w().u.af;
                  }
               }
            }
         }
      }
   }
}
