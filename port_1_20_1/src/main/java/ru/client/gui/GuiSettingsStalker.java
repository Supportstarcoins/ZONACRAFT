package ru.stalcraft.client.gui;

import ru.stalcraft.StalkerMain;
import ru.stalcraft.client.ClientProxy;
import ru.stalcraft.client.effects.EffectsEngine;

public class GuiSettingsStalker extends awe {
   public static boolean useWeaponModels = true;
   public static boolean renderSleeves = true;
   public static boolean autoReload = true;
   public static boolean renderEquippedWeapons = true;
   public static boolean dynamicLights = true;
   public static boolean highRenderDistance = true;
   public static boolean shaderRendering = true;
   public static int particleRenderDistance = 64;
   public static boolean advancedShot = true;
   private awe parentGuiScreen;
   protected String screenTitle = "S.T.A.L.K.E.R. - Настройки";
   private aut weaponModelsButton;
   private aut renderSleevesButton;
   private aut autoReloadButton;
   private aut renderEquippedWeaponsButton;
   private aut useFlahslightButton;
   private aut entityRenderDistance;
   private aut shaderRenderingBtn;
   private aut advancedShotBtn;
   private awk particleRenderDistanceSlider;

   public GuiSettingsStalker(awe par1GuiScreen) {
      this.parentGuiScreen = par1GuiScreen;
   }

   public void A_() {
      super.i.clear();
      super.i.add(new aut(0, super.g / 2 - 100, super.h / 6 + 168, 200, 20, "Готово"));
      this.weaponModelsButton = new aut(1, super.g / 2 - 152, super.h / 6, 150, 20, "");
      this.renderSleevesButton = new aut(2, super.g / 2 + 2, super.h / 6, 150, 20, "");
      this.autoReloadButton = new aut(3, super.g / 2 - 152, super.h / 6 + 24, 150, 20, "");
      this.renderEquippedWeaponsButton = new aut(4, super.g / 2 + 2, super.h / 6 + 24, 150, 20, "");
      this.useFlahslightButton = new aut(5, super.g / 2 - 152, super.h / 6 + 48, 150, 20, "");
      this.entityRenderDistance = new aut(6, super.g / 2 + 2, super.h / 6 + 48, 150, 20, "");
      this.shaderRenderingBtn = new aut(7, super.g / 2 - 152, super.h / 6 + 72, 150, 20, "");
      this.advancedShotBtn = new aut(8, super.g / 2 + 2, super.h / 6 + 72, 150, 20, "");
      super.i.add(this.weaponModelsButton);
      super.i.add(this.renderSleevesButton);
      super.i.add(this.autoReloadButton);
      super.i.add(this.renderEquippedWeaponsButton);
      super.i.add(this.useFlahslightButton);
      super.i.add(this.entityRenderDistance);
      super.i.add(this.shaderRenderingBtn);
      super.i.add(this.advancedShotBtn);
      this.updateButtonNames();
   }

   private void updateButtonNames() {
      this.weaponModelsButton.f = "3D модели: " + (useWeaponModels ? "вкл." : "выкл.");
      this.renderSleevesButton.f = "Гильзы: " + (renderSleeves ? "вкл." : "выкл.");
      this.autoReloadButton.f = "Автоперезарядка: " + (autoReload ? "вкл." : "выкл.");
      this.renderEquippedWeaponsButton.f = "Оружие на спине: " + (renderEquippedWeapons ? "вкл." : "выкл.");
      this.useFlahslightButton.f = "Динамическое освещение: " + (dynamicLights ? "вкл." : "выкл.");
      this.entityRenderDistance.f = "Дальняя прорисовка сущностей: " + (highRenderDistance ? "вкл." : "выкл.");
      this.shaderRenderingBtn.f = "Шейдеры частиц: " + (shaderRendering ? "вкл." : "выкл.");
      this.advancedShotBtn.f = "Улучшенная вспышка: " + (advancedShot ? "вкл." : "выкл.");
   }

   protected void a(aut btn) {
      if (btn.g == 1) {
         useWeaponModels = !useWeaponModels;
      } else if (btn.g == 2) {
         renderSleeves = !renderSleeves;
      } else if (btn.g == 3) {
         autoReload = !autoReload;
      } else if (btn.g == 4) {
         renderEquippedWeapons = !renderEquippedWeapons;
      } else if (btn.g == 5) {
         dynamicLights = !dynamicLights;
      } else if (btn.g == 6) {
         highRenderDistance = !highRenderDistance;
         if (atv.w().f != null) {
            for (nn entity : atv.w().f.e) {
               if (entity instanceof of) {
                  entity.l = highRenderDistance ? 10000.0 : 1.0;
               }
            }
         }
      } else if (btn.g == 7) {
         shaderRendering = !shaderRendering;
         EffectsEngine.instance.shouldUseShaders = shaderRendering;
      } else if (btn.g == 8) {
         advancedShot = !advancedShot;
      }

      this.updateButtonNames();
      ClientProxy par1 = (ClientProxy)StalkerMain.getProxy();
      ClientProxy.mcconfig.get("general", "use_weapons_models", true).set(useWeaponModels);
      ClientProxy.mcconfig.get("general", "render_sleeves", true).set(renderSleeves);
      ClientProxy.mcconfig.get("general", "auto_reload", true).set(autoReload);
      ClientProxy.mcconfig.get("general", "render_equipped_items", true).set(renderEquippedWeapons);
      ClientProxy.mcconfig.get("general", "use_flashlight", true).set(dynamicLights);
      ClientProxy.mcconfig.get("general", "high_render_distance", true).set(highRenderDistance);
      ClientProxy.mcconfig.get("general", "shader_rendering", true).set(shaderRendering);
      ClientProxy.mcconfig.get("general", "advanced_shot", true).set(advancedShot);
      ClientProxy.mcconfig.get("general", "particle_render_distance", 64).set(particleRenderDistance);
      ClientProxy.mcconfig.save();
      if (btn.g == 0) {
         super.f.a(this.parentGuiScreen);
      }
   }

   public void a(int par1, int par2, float par3) {
      this.e();
      super.a(par1, par2, par3);
   }
}
