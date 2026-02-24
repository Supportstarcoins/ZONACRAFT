package ru.stalcraft.client;

import cpw.mods.fml.common.IScheduledTickHandler;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.relauncher.ReflectionHelper;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import paulscode.sound.SoundSystem;
import ru.stalcraft.StalkerMain;
import ru.stalcraft.blocks.BlockAnomaly;
import ru.stalcraft.client.gui.weapon.GuiWeaponEditor;
import ru.stalcraft.client.network.ClientPacketSender;
import ru.stalcraft.client.render.RenderWeapon;
import ru.stalcraft.inventory.StalkerInventory;
import ru.stalcraft.items.ItemDetector;
import ru.stalcraft.items.ItemPNV;
import ru.stalcraft.items.ItemWeapon;
import ru.stalcraft.player.PlayerInfo;
import ru.stalcraft.player.PlayerUtils;
import ru.stalcraft.world.WorldManager;

public class ClientTicker implements IScheduledTickHandler {
   public static Thread mainThread;
   public static long tickId;
   public static boolean wallHack = false;
   public static boolean hitDamage = false;
   public atv mc;
   private static List playSoundsAtEntity;
   private String[] sounds;
   private float[] levels;
   public int savedcurrentitem;
   private int soundTickId;
   private static int latestSoundID;
   public boolean firstrun = true;
   private boolean wasIngame = false;
   private boolean startedSounds;
   private boolean hasUpMouse;
   private boolean hasOpenedOtherInventory;
   private boolean wasInCreative;
   public static float hitMarker = 0.0F;
   public static int damageLevel = 0;
   public static int hitTick;
   public static boolean isPNV = false;
   public static int tickFireMode;
   public static int weaponJammed;
   public ClientProxy proxy;
   public WorldManager worldManager;

   public ClientTicker(ClientProxy proxy) {
      this.savedcurrentitem = 0;
      this.soundTickId = 0;
      this.sounds = new String[3];
      this.levels = new float[3];
      tickId = 1L;
      playSoundsAtEntity = new ArrayList();
      this.mc = atv.w();
      this.proxy = proxy;
      this.worldManager = new WorldManager();
   }

   public void tickStart(EnumSet type, Object... tickData) {
      Iterator iterator = null;
      if (!isPNV) {
         this.mc.u.ak = 0.0F;
      }

      if (this.mc.f != null) {
         this.worldManager.onUpdate();
      }

      ClientProxy clientProxy = (ClientProxy)StalkerMain.getProxy();
      if (this.firstrun) {
         clientProxy.replaceGuiIngame();
         this.firstrun = false;
      }

      if (weaponJammed > 0) {
         weaponJammed--;
      }

      try {
         this.soundTick();
      } catch (Exception var13) {
         var13.printStackTrace();
      }

      if (hitMarker > 0.0F && hitTick <= 0) {
         hitMarker -= 0.098F;
      }

      if (tickFireMode > 0) {
         tickFireMode--;
      }

      if (hitTick > 0) {
         hitTick--;
      }

      if (this.mc.h != null) {
         uf player = this.mc.h;
         if (player != null && this.mc.n == null) {
            ye stackArmor = player.o(3);
            if (stackArmor == null || stackArmor != null && !(stackArmor.b() instanceof ItemPNV)) {
               isPNV = false;
            }
         }
      }

      if (this.proxy.clientWeaponManager != null) {
         this.proxy.clientWeaponManager.tick();
      }

      if (this.mc.A) {
         try {
            bdi var9 = atv.w().h;
            PlayerInfo player = PlayerUtils.getInfo(var9);
            if (var9.bn.h() != null && var9.bn.h().b() instanceof ItemWeapon) {
               ItemWeapon weapon = (ItemWeapon)var9.bn.h().b();
               by tag = var9.bn.h().q();
               if (weapon != null
                  && tag != null
                  && !tag.n("grenade_shooting")
                  && PlayerUtils.getTag(var9.bn.h()).e("cage") == 0
                  && var9.bn.h() != null
                  && !player.weaponInfo.isReloading(var9.bn.h())) {
                  int ammoType = tag.e("ammoType");
                  if (PlayerUtils.hasItem(var9, weapon.bulletsID[0]) && ammoType == 0
                     || !PlayerUtils.hasItem(var9, weapon.bulletsID[0]) && PlayerUtils.hasItem(var9, weapon.bulletsID[1])) {
                     ClientPacketSender.sendReloadRequest(0);
                  }

                  if (PlayerUtils.hasItem(var9, weapon.bulletsID[1]) && ammoType == 1
                     || !PlayerUtils.hasItem(var9, weapon.bulletsID[1]) && PlayerUtils.hasItem(var9, weapon.bulletsID[0])) {
                     ClientPacketSender.sendReloadRequest(1);
                  }
               } else if (tag.n("grenade_shooting") && tag.e("grenade_weapon") <= 0) {
                  ClientPacketSender.sendRelaodGrenadeRequest();
               }

               ClientWeaponInfo weaponInfo = (ClientWeaponInfo)player.weaponInfo;
               boolean isSprinting = StalkerMain.instance.smHelper.isPlayerRunning(var9);
               boolean isAiming = weaponInfo.isAiming();
               float animationSpeed = 0.225F;
               if (isSprinting) {
                  RenderWeapon.sprintingProgress = ls.a(RenderWeapon.sprintingProgress + animationSpeed, 0.0F, 1.0F);
               } else {
                  RenderWeapon.sprintingProgress = ls.a(RenderWeapon.sprintingProgress - animationSpeed, 0.0F, 1.0F);
               }

               if (isAiming) {
                  RenderWeapon.aimingProgress = ls.a(RenderWeapon.aimingProgress + animationSpeed, 0.0F, 1.0F);
               } else {
                  RenderWeapon.aimingProgress = ls.a(RenderWeapon.aimingProgress - animationSpeed, 0.0F, 1.0F);
               }

               if (!isSprinting) {
                  if (this.mc.h.bn.h() != null && weaponInfo.getLastShot() < 3 && weaponInfo.getLastShot() > 1) {
                     RenderWeapon.shotProgress = ls.a(RenderWeapon.shotProgress + animationSpeed, 0.0F, 1.0F);
                  } else {
                     RenderWeapon.shotProgress = ls.a(RenderWeapon.shotProgress - animationSpeed, 0.0F, 1.0F);
                  }
               }
            }
         } catch (Exception var14) {
            var14.printStackTrace();
         }
      }

      if (ClientProxy.isGameRunning()) {
         if (clientProxy.getEjectionManager() != null) {
            clientProxy.getEjectionManager().tick();
         }

         if (ClientProxy.shootLights != null) {
            ClientProxy.shootLights.tick();
         }
      }

      if (this.mc.h != null && PlayerUtils.getInfo(this.mc.h).weaponInfo.currentGun != null && Mouse.isButtonDown(0)) {
         StalkerMain.machineGun.a(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
      } else {
         StalkerMain.machineGun.a(0.25F, 0.0F, 0.25F, 0.75F, 0.6F, 0.75F);
      }

      if (this.mc.h != null && this.mc.h.bG.d != this.wasInCreative) {
         this.wasInCreative = this.mc.h.bG.d;

         for (int var11 = 0; var11 < BlockAnomaly.anomalies.size(); var11++) {
            aqz.s[BlockAnomaly.anomalies.get(var11)]
               .a(0.0F, 0.0F, 0.0F, this.wasInCreative ? 1.0F : 0.0F, this.wasInCreative ? 1.0F : 0.0F, this.wasInCreative ? 1.0F : 0.0F);
         }
      }

      if (this.mc.h != null && this.mc.h.by() != null && this.mc.h.by().b() instanceof ItemWeapon) {
         ye stack = this.mc.h.by();
         if (Keyboard.isKeyDown(38) && this.mc.n == null && this.mc.c.h()) {
            this.mc.a(new GuiWeaponEditor(stack));
         }
      }
   }

   public void tickEnd(EnumSet type, Object... tickData) {
      if (Keyboard.isKeyDown(210)) {
         wallHack = !wallHack;
      }

      if (Keyboard.isKeyDown(199)) {
         hitDamage = !hitDamage;
      }
   }

   public EnumSet ticks() {
      return EnumSet.of(TickType.CLIENT);
   }

   public String getLabel() {
      return "StalkerClientTicker";
   }

   private void mouseTick() {
      atv mc = atv.w();
      boolean isMouseDown = Mouse.isButtonDown(0);
      this.wasIngame = mc.A;
   }

   private void soundTick() {
      Iterator it = playSoundsAtEntity.iterator();
      SoundSystem sndSystem = this.mc.v.b;
      if (this.mc.f != null && playSoundsAtEntity.size() > 0) {
         nn entity = null;
         String soundId = null;
         String[] info = null;
         float volume = 0.0F;

         while (it.hasNext()) {
            soundId = (String)it.next();
            info = soundId.split("_");
            entity = this.mc.f.a(Integer.parseInt(info[1]));
            volume = Float.parseFloat(info[2]);
            if (entity != null) {
               if (sndSystem.playing(soundId)) {
                  sndSystem.setPosition(soundId, (float)entity.u, (float)entity.v, (float)entity.w);
                  sndSystem.setVelocity(soundId, (float)entity.x, (float)entity.y, (float)entity.z);
                  sndSystem.setVolume(soundId, volume * this.mc.u.b);
               } else {
                  it.remove();
               }
            }
         }
      }

      if (this.mc.h != null) {
         this.soundTickId++;
         if (this.soundTickId > 2) {
            this.soundTickId = 0;
         }

         if (this.soundTickId == 0) {
            this.updateSound("radiation", (ItemDetector)StalkerMain.radiationDetector, 0);
         } else if (this.soundTickId == 1) {
            this.updateSound("chemical", (ItemDetector)StalkerMain.chemicalDetector, 1);
         } else if (this.soundTickId == 2) {
            this.updateSound("biological", (ItemDetector)StalkerMain.biologicalDetector, 2);
         }
      } else {
         for (int i = 0; i < this.sounds.length; i++) {
            if (this.sounds[i] != null) {
               sndSystem.stop(this.sounds[i]);
               this.sounds[i] = null;
            }

            this.levels[i] = 0.0F;
         }
      }
   }

   private void updateSound(String name, ItemDetector detector, int id) {
      bln m = this.mc.v;
      SoundSystem sndSystem = this.mc.v.b;
      bdi p = this.mc.h;
      StalkerInventory inv = PlayerUtils.getInfo(p).stInv;
      if (inv.mainInventory[5] != null && inv.mainInventory[5].b() == detector
         || inv.mainInventory[6] != null && inv.mainInventory[6].b() == detector
         || inv.mainInventory[7] != null && inv.mainInventory[7].b() == detector) {
         float currentLevel = detector.detectLevel(p.q, p.u, p.v + p.f(), p.w);
         if (this.sounds[id] != null && currentLevel == 0.0F) {
            sndSystem.stop(this.sounds[id]);
            this.sounds[id] = null;
         } else if (this.sounds[id] == null && currentLevel > 0.0F) {
            int latestSoundID = (Integer)ReflectionHelper.getPrivateValue(bln.class, atv.w().v, new String[]{"latestSoundID", "field_77378_e", "g"});
            this.sounds[id] = "sound_" + (latestSoundID + 1) % 256;
            m.a("stalker:" + name, currentLevel, 1.0F);
            m.b.setLooping(this.sounds[id], true);
            m.b.setVolume(this.sounds[id], currentLevel * this.mc.u.b);
         } else if (this.sounds[id] != null) {
            m.b.setVolume(this.sounds[id], currentLevel * this.mc.u.b);
         }

         this.levels[id] = currentLevel;
      } else if (this.sounds[id] != null) {
         this.levels[id] = 0.0F;
         sndSystem.stop(this.sounds[id]);
         this.sounds[id] = null;
      }
   }

   public int nextTickSpacing() {
      return 1;
   }

   public static void soundPlayAtEntity(nn entity, String soundName, float volume, float pitch, boolean piority) {
      atv mc = atv.w();
      if (mc.v.c && mc.u.b != 0.0F && entity != null && soundName != null && !soundName.isEmpty()) {
         latestSoundID = (latestSoundID + 1) % 256;
         String s1 = "sound" + latestSoundID + "_" + entity.k + "_" + volume;
         blm soundpoolentry = mc.v.d.b(soundName);
         if (soundpoolentry != null && volume > 0.0F) {
            float f2 = 16.0F;
            if (volume > 1.0F) {
               f2 *= volume;
            }

            mc.v.b.newSource(piority, s1, soundpoolentry.b(), soundpoolentry.a(), false, (float)entity.u, (float)entity.v, (float)entity.w, 2, f2);
            mc.v.b.setPitch(s1, pitch);
            if (volume > 1.0F) {
               volume = 1.0F;
            }

            mc.v.b.setVolume(s1, volume * mc.u.b);
            mc.v.b.setVelocity(s1, (float)entity.x, (float)entity.y, (float)entity.z);
            mc.v.b.play(s1);
            playSoundsAtEntity.add(s1);
         }
      }
   }
}
