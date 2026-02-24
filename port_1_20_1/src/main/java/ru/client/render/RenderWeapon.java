package ru.stalcraft.client.render;

import cpw.mods.fml.relauncher.ReflectionHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.IItemRenderer.ItemRenderType;
import net.minecraftforge.client.IItemRenderer.ItemRendererHelper;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import ru.stalcraft.StalkerMain;
import ru.stalcraft.client.ClientProxy;
import ru.stalcraft.client.ClientWeaponInfo;
import ru.stalcraft.client.gui.GuiSettingsStalker;
import ru.stalcraft.client.gui.weapon.GuiWeaponEditor;
import ru.stalcraft.client.loaders.StalkerModelManager;
import ru.stalcraft.client.models.ModelHand;
import ru.stalcraft.client.particles.ParticleFlashSmoke;
import ru.stalcraft.client.particles.ParticleTracer;
import ru.stalcraft.items.ItemArmorArtefakt;
import ru.stalcraft.items.ItemWeapon;
import ru.stalcraft.player.PlayerUtils;

@SideOnly(Side.CLIENT)
public class RenderWeapon implements IItemRenderer {
   public bjo bullet_trace = new bjo("stalker:textures/bullet_trace.png");
   public static IModelCustom modelTrace = AdvancedModelLoader.loadModel("/assets/stalker/models/tracer.obj");
   public ClientProxy proxy;
   public IModelCustom weaponModel;
   public int weaponModelBody = -1;
   public int weaponModelGrenade = -1;
   public int weaponModelSight = -1;
   public int weaponModelSilencer = -1;
   public int weaponModelIronSight = -1;
   public ClientWeaponInfo clientWeaponInfo;
   protected static float x;
   protected static float y;
   protected static float z;
   protected static float x1;
   protected static float y1;
   protected static float z1;
   protected static float zoom = 2.0F;
   private static atv mc = atv.w();
   private String modelName;
   private bjo texture;
   protected ModelHand hand = new ModelHand();
   private bjo blank = new bjo("stalker", "textures/blank.png");
   public bjo[] particleIcon = new bjo[3];
   private bjo aimTexture = null;
   private bjo aimTextureSight = null;
   private static float[] fpTransform = new float[]{-0.355F, -0.06F, 0.234F, -0.6F, -15.4F, -3.85F};
   private static float[] playerTransform = new float[]{0.66F, 1.3F, 0.78F, -76.8F, 14.1F, 45.3F};
   private static float[] oldLivingTransform = new float[]{-0.49F, 1.08F, 0.57F, 29.0F, 233.7F, 33.6F};
   private static float[] newLivingTransform = new float[]{0.23F, 0.84F, 0.76F, 9.1F, 316.6F, 0.0F};
   public float aimPosX;
   public float aimPosY;
   public float aimPosZ;
   public float aimRotX;
   public float aimRotY;
   public float aimRotZ;
   public float posX;
   public float posY;
   public float posZ;
   public float posLeftHandX;
   public float posLeftHandY;
   public float posLeftHandZ;
   public float posRightHandX;
   public float posRightHandY;
   public float posRightHandZ;
   public float flashPosX;
   public float flashPosY;
   public float flashPosZ;
   public float equippedPosX;
   public float equippedPosY;
   public float equippedPosZ;
   private static boolean written = false;
   public ItemWeapon weapon;
   public List<ParticleFlashSmoke> particlesSmoke = new ArrayList<>();
   public List<ParticleFlashSmoke> particlesEquipedSmoke = new ArrayList<>();
   public List<ParticleTracer> particlesTracer = new ArrayList<>();
   public static float sprintingProgress = 0.0F;
   public static float aimingProgress = 0.0F;
   public static float shotProgress = 0.0F;
   public boolean integrateSight;
   public float size;

   public RenderWeapon(ClientProxy proxy, ItemWeapon weapon) {
      this.proxy = proxy;
      this.weapon = weapon;
      this.modelName = weapon.modelName;
      this.texture = new bjo("stalker", "models/weapons/" + weapon.modelTexture + ".png");
      this.posX = weapon.posX;
      this.posY = weapon.posY;
      this.posZ = weapon.posZ;
      this.integrateSight = weapon.integrateSight;
      this.posLeftHandX = weapon.posLeftHandX;
      this.posLeftHandY = weapon.posLeftHandY;
      this.posLeftHandZ = weapon.posLeftHandZ;
      this.posRightHandX = weapon.posRightHandX;
      this.posRightHandY = weapon.posRightHandY;
      this.posRightHandZ = weapon.posRightHandZ;
      this.aimPosX = weapon.aimPosX;
      this.aimPosY = weapon.aimPosY;
      this.aimPosZ = weapon.aimPosZ;
      this.aimRotX = weapon.aimRotX;
      this.aimRotY = weapon.aimRotY;
      this.aimRotZ = weapon.aimRotZ;
      this.size = weapon.flashSize;
      this.flashPosX = weapon.flashPosX;
      this.flashPosY = weapon.flashPosY;
      this.flashPosZ = weapon.flashPosZ;
      this.equippedPosX = weapon.equippedPosX;
      this.equippedPosY = weapon.equippedPosY;
      this.equippedPosZ = weapon.equippedPosZ;
      if (weapon.aimingTexture != null && !weapon.aimingTexture.isEmpty()) {
         this.aimTexture = new bjo("stalker", "textures/" + weapon.aimingTexture + ".png");
      }

      if (weapon.aimingTextureSight != null && !weapon.aimingTextureSight.isEmpty()) {
         this.aimTextureSight = new bjo("stalker", "textures/" + weapon.aimingTextureSight + ".png");
      }

      for (int i = 0; i < 3; i++) {
         int j = i + 1;
         this.particleIcon[i] = new bjo("stalker:textures/particles/weapon/flash" + j + ".png");
      }

      StalkerModelManager m = ClientProxy.modelManager;

      try {
         this.weaponModel = StalkerModelManager.addModel("weapons", this.modelName);
         this.weaponModelBody = GL11.glGenLists(1);
         GL11.glNewList(this.weaponModelBody, 4864);
         this.weaponModel.renderAllExcept(new String[]{"flashlight", "sight", "silencer", "grenade_launcher", "iron_sight"});
         GL11.glEndList();
         this.weaponModelGrenade = GL11.glGenLists(1);
         GL11.glNewList(this.weaponModelGrenade, 4864);
         this.weaponModel.renderPart("grenade_launcher");
         GL11.glEndList();
         this.weaponModelSight = GL11.glGenLists(1);
         GL11.glNewList(this.weaponModelSight, 4864);
         this.weaponModel.renderPart("sight");
         GL11.glEndList();
         this.weaponModelSilencer = GL11.glGenLists(1);
         GL11.glNewList(this.weaponModelSilencer, 4864);
         this.weaponModel.renderPart("silencer");
         GL11.glEndList();
         if (weapon.ironSight) {
            this.weaponModelIronSight = GL11.glGenLists(1);
            GL11.glNewList(this.weaponModelIronSight, 4864);
            this.weaponModel.renderPart("iron_sight");
            GL11.glEndList();
         }
      } catch (Exception var5) {
         var5.printStackTrace();
      }
   }

   public boolean handleRenderType(ye item, ItemRenderType type) {
      return type != ItemRenderType.INVENTORY
         && (type != ItemRenderType.EQUIPPED || GuiSettingsStalker.useWeaponModels)
         && (type != ItemRenderType.ENTITY || GuiSettingsStalker.useWeaponModels);
   }

   public boolean shouldUseRenderHelper(ItemRenderType type, ye item, ItemRendererHelper helper) {
      return type != ItemRenderType.ENTITY;
   }

   private void renderModel(ye stack) {
      StalkerModelManager m = ClientProxy.modelManager;
      m.tryLoadTexture(this.texture);
      if (m.tryBindTexture(this.texture)) {
         GL11.glCallList(this.weaponModelBody);
         by tag = PlayerUtils.getTag(stack);
         boolean isSight = tag.n("sight");
         if (tag.n("silencer")) {
            GL11.glCallList(this.weaponModelSilencer);
         }

         if (isSight) {
            GL11.glCallList(this.weaponModelSight);
         }

         if (tag.n("grenade_launcher")) {
            GL11.glCallList(this.weaponModelGrenade);
         }

         if (((ItemWeapon)stack.b()).ironSight && !isSight) {
            GL11.glCallList(this.weaponModelIronSight);
         }
      }
   }

   public void renderItem(ItemRenderType type, ye item, Object... data) {
      GL11.glPushMatrix();
      att.a();
      ClientWeaponInfo e = (ClientWeaponInfo)PlayerUtils.getInfo(mc.h).weaponInfo;
      this.clientWeaponInfo = e;
      boolean isSight = item.q().n("sight");

      try {
         if (type == ItemRenderType.ENTITY) {
            GL11.glPushMatrix();
            GL11.glTranslatef(0.0F, -0.175F, 0.0F);
            GL11.glRotatef(90.0F, 0.0F, 0.0F, 1.0F);
            GL11.glScalef(2.65F, 2.65F, 2.65F);
            this.renderModel(item);
            GL11.glPopMatrix();
         } else if (type == ItemRenderType.EQUIPPED) {
            GL11.glPushMatrix();
            if (data[1] instanceof uf) {
               GL11.glTranslatef(playerTransform[0] + this.equippedPosX, playerTransform[1] + this.equippedPosY, playerTransform[2] + this.equippedPosZ);
               GL11.glRotatef(playerTransform[3], 1.0F, 0.0F, 0.0F);
               GL11.glRotatef(playerTransform[4], 0.0F, 1.0F, 0.0F);
               GL11.glRotatef(playerTransform[5], 0.0F, 0.0F, 1.0F);
               GL11.glScalef(4.0F, 4.0F, 4.0F);
            } else {
               GL11.glTranslatef(newLivingTransform[0], newLivingTransform[1], newLivingTransform[2]);
               GL11.glRotatef(newLivingTransform[3], 1.0F, 0.0F, 0.0F);
               GL11.glRotatef(newLivingTransform[4], 0.0F, 1.0F, 0.0F);
               GL11.glRotatef(newLivingTransform[5], 0.0F, 0.0F, 1.0F);
               GL11.glScalef(2.0F, 2.0F, 2.0F);
            }

            this.renderModel(item);
            GL11.glPopMatrix();
         } else if (type == ItemRenderType.EQUIPPED_FIRST_PERSON) {
            boolean isSprinting = StalkerMain.instance.smHelper.isPlayerRunning(mc.h);
            GL11.glTranslatef(fpTransform[0], fpTransform[1], fpTransform[2] - 0.05F);
            GL11.glRotatef(fpTransform[3] + 1.5F, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(fpTransform[4] + 0.5F, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(fpTransform[5], 0.0F, 0.0F, 1.0F);
            this.setupTranslation(e);
            GL11.glPushMatrix();
            GL11.glRotatef(-30.0F, 0.0F, 1.0F, 0.0F);
            GL11.glTranslatef(this.posX - 0.3F, this.posY + 1.475F, this.posZ + 1.1F);
            this.setupRotation(e);
            GL11.glScalef(2.75F, 2.75F, 2.75F);
            if (!this.integrateSight && !isSight || aimingProgress < 0.95F) {
               this.renderModel(item);
            }

            GL11.glPopMatrix();
            if (!this.integrateSight && !isSight || aimingProgress < 0.95F) {
               GL11.glPushMatrix();
               GL11.glRotatef(-90.0F, 1.0F, 0.0F, 0.0F);
               GL11.glRotatef(0.0F, 0.0F, 1.0F, 0.0F);
               GL11.glRotatef(-32.0F, 0.0F, 0.0F, 1.0F);
               GL11.glTranslatef(-0.3F + this.posRightHandX, -1.83F + this.posRightHandY, 1.05F + this.posRightHandZ);
               GL11.glScalef(1.0F, 1.45F, 1.0F);
               this.setupRotation(e);
               this.hand.render(mc.h, 1);
               this.renderRightArm();
               GL11.glPopMatrix();
               if (!this.weapon.isPistol) {
                  GL11.glPushMatrix();
                  GL11.glRotatef(-84.0F, 1.0F, 0.0F, 0.0F);
                  GL11.glRotatef(-1.95F, 0.0F, 1.0F, 0.0F);
                  GL11.glRotatef(-72.25F, 0.0F, 0.0F, 1.0F);
                  GL11.glTranslatef(-0.345F + this.posLeftHandX, -1.45F + this.posLeftHandY, 1.1F + this.posLeftHandZ);
                  GL11.glScalef(0.96F, 1.9F, 0.96F);
                  this.setupRotation(e);
                  this.hand.render(mc.h, 1);
                  this.renderLeftArm();
                  GL11.glPopMatrix();
               }
            }

            if (mc.h.bG.d) {
               this.listenFirstPerson();
            }
         }
      } catch (NullPointerException var7) {
         var7.printStackTrace();
      }

      GL11.glPushMatrix();
      if (type == ItemRenderType.EQUIPPED_FIRST_PERSON) {
         if (!e.isAiming() && !this.integrateSight || !isSight) {
            this.renderShot();
            this.renderBulletTrace();
         }
      } else if (type == ItemRenderType.EQUIPPED) {
      }

      GL11.glPopMatrix();
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      att.b();
      GL11.glPopMatrix();
      GL11.glPushMatrix();
      if (type == ItemRenderType.EQUIPPED_FIRST_PERSON && e.isAiming() && (this.integrateSight || isSight)) {
         GL11.glTranslatef(-1.15F, 1.8F, 0.169F);
         GL11.glRotatef(-45.0F, 0.0F, 1.0F, 0.0F);
         GL11.glScalef(2.75F, 2.75F, 2.75F);
         if (aimingProgress >= 0.75F) {
            this.renderAim(this.aimTexture);
         }
      }

      GL11.glPopMatrix();
   }

   private void renderBulletTrace() {
      GL11.glPushMatrix();
      GL11.glTranslatef(0.17F + this.flashPosX, 1.432F + this.flashPosY, -0.875F + this.flashPosZ);
      GL11.glRotatef(-26.0F, 0.0F, 1.0F, 0.0F);
      GL11.glRotatef(-85.0F, 1.0F, 0.0F, 0.0F);
      GL11.glEnable(3042);
      GL11.glDepthMask(false);
      GL11.glBlendFunc(770, 1);
      bfq t = bfq.a;
      if (aimingProgress == 0.0F) {
         for (ParticleTracer particle : this.particlesTracer) {
            mc.N.a(this.bullet_trace);
            float alpha = particle.alpha;
            float motionX = particle.motionX;
            t.b();
            t.a(1.0F, 1.0F, 1.0F, 1.0F * alpha);
            t.b(0.0F, 1.0F, 0.0F);
            t.a(-0.25, 4.225 + motionX, 0.0, 0.0, 0.0);
            t.a(-0.25, -4.225 - motionX, 0.0, 0.0, 1.0);
            t.a(0.25, -0.25 - motionX, 0.0, 1.0, 1.0);
            t.a(0.25, 0.25 + motionX, 0.0, 1.0, 0.0);
            t.a();
            GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
            t.b();
            t.a(1.0F, 1.0F, 1.0F, 1.0F * alpha);
            t.b(0.0F, 1.0F, 0.0F);
            t.a(-0.25, 4.225 + motionX, 0.0, 0.0, 0.0);
            t.a(-0.25, -4.225 - motionX, 0.0, 0.0, 1.0);
            t.a(0.25, -0.25 - motionX, 0.0, 1.0, 1.0);
            t.a(0.25, 0.25 + motionX, 0.0, 1.0, 0.0);
            t.a();
            GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
            t.b();
            t.a(1.0F, 1.0F, 1.0F, 1.0F * alpha);
            t.b(0.0F, 1.0F, 0.0F);
            t.a(-0.25, 4.225 + motionX, 0.0, 0.0, 0.0);
            t.a(-0.25, -4.225 - motionX, 0.0, 0.0, 1.0);
            t.a(0.25, -0.25 - motionX, 0.0, 1.0, 1.0);
            t.a(0.25, 0.25 + motionX, 0.0, 1.0, 0.0);
            t.a();
            GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
            t.b();
            t.a(1.0F, 1.0F, 1.0F, 1.0F * alpha);
            t.b(0.0F, 1.0F, 0.0F);
            t.a(-0.25, 4.225 + motionX, 0.0, 0.0, 0.0);
            t.a(-0.25, -4.225 - motionX, 0.0, 0.0, 1.0);
            t.a(0.25, -0.25 - motionX, 0.0, 1.0, 1.0);
            t.a(0.25, 0.25 + motionX, 0.0, 1.0, 0.0);
            t.a();
         }
      }

      GL11.glDepthMask(true);
      GL11.glDisable(3042);
      GL11.glDisable(3042);
      GL11.glPopMatrix();
   }

   public void renderRightArm() {
      atv mc = atv.w();
      uf player = mc.h;
      GL11.glBlendFunc(770, 771);
      ItemArmorArtefakt info = null;
      ye stackArmor = player.o(2);
      if (stackArmor != null && stackArmor.b() instanceof ItemArmorArtefakt && ((ItemArmorArtefakt)stackArmor.b()).rendererId >= 0) {
         info = (ItemArmorArtefakt)stackArmor.b();
         RenderItemArmor render = this.proxy.rendererManager.renderersItemArmor.get(info.rendererId);
         if (render != null) {
            render.onBindTexture();
            GL11.glPushMatrix();
            GL11.glTranslatef(-0.25F, -0.05F, 0.13F);
            GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
            GL11.glScalef(1.0F, 1.15F, 1.0F);
            render.onRightArmRender();
            GL11.glPopMatrix();
         }
      }
   }

   public void renderLeftArm() {
      atv mc = atv.w();
      uf player = mc.h;
      GL11.glBlendFunc(770, 771);
      ItemArmorArtefakt info = null;
      ye stackArmor = player.o(2);
      if (stackArmor != null && stackArmor.b() instanceof ItemArmorArtefakt && ((ItemArmorArtefakt)stackArmor.b()).rendererId >= 0) {
         info = (ItemArmorArtefakt)stackArmor.b();
         RenderItemArmor render = this.proxy.rendererManager.renderersItemArmor.get(info.rendererId);
         if (render != null) {
            render.onBindTexture();
            GL11.glPushMatrix();
            GL11.glTranslatef(-0.016F, -0.03F, 0.0F);
            GL11.glTranslatef(0.51F, 0.0F, 0.13F);
            GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
            GL11.glScalef(0.99F, 1.03F, 0.99F);
            render.onLeftArmRender();
            GL11.glPopMatrix();
         }
      }
   }

   private void renderShot() {
      GL11.glPushMatrix();
      GL11.glEnable(3042);
      GL11.glDisable(3008);
      GL11.glDepthMask(false);
      GL11.glBlendFunc(770, 771);
      GL11.glTranslatef(0.17F + this.flashPosX, 1.432F + this.flashPosY, -0.875F + this.flashPosZ);
      GL11.glRotatef(-35.0F, 0.0F, 1.0F, 0.0F);
      GL11.glScalef(0.25F, 0.25F, 0.25F);

      for (ParticleFlashSmoke particle : this.particlesSmoke) {
         bfq t = bfq.a;
         int iconIndex = mc.f.s.nextInt(2);
         mc.N.a(this.particleIcon[iconIndex]);
         t.b();
         t.a(30.0F, 30.0F, 30.0F, 1.0F * particle.alpha);
         double rotation = (45.0 - particle.prevRotation + (double)(particle.rotation - particle.prevRotation) * mc.S.c) * Math.PI / 180.0;
         float cos = ls.b((float)rotation);
         float sin = ls.a((float)rotation);
         if (!particle.tick) {
            cos = 1.0F;
            sin = 1.0F;
         }

         t.b(0.0F, 1.0F, 0.0F);
         t.a(-particle.size * sin, particle.size * cos, 0.0, 0.0, 0.0);
         t.a(-particle.size * cos, -particle.size * sin, 0.0, 0.0, 1.0);
         t.a(particle.size * sin, -particle.size * cos, 0.0, 1.0, 1.0);
         t.a(particle.size * cos, particle.size * sin, 0.0, 1.0, 0.0);
         t.a();
      }

      GL11.glDepthMask(true);
      GL11.glEnable(3008);
      GL11.glDisable(3042);
      GL11.glPopMatrix();
   }

   public void renderOnPlayer(of p, RenderWeapon.RenderType renderType, ye stack) {
      GL11.glPushMatrix();
      GL11.glScalef(1.45F, 1.45F, 1.45F);
      att.a();
      if (renderType == RenderWeapon.RenderType.BACKPACK_RIFLE) {
         GL11.glTranslatef(-0.145F, 0.3F, 0.15F);
         GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
         GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
         GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
      } else if (renderType == RenderWeapon.RenderType.RIFLE) {
         GL11.glTranslatef(-0.03F, 0.3F, p.n(3) == null ? 0.1F : 0.15F);
         GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
         GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
         GL11.glRotatef(115.0F, 1.0F, 0.0F, 0.0F);
      } else if (renderType == RenderWeapon.RenderType.PISTOL) {
         GL11.glTranslatef(0.18F, 0.35F, 0.0F);
         GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
         GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
         GL11.glRotatef(-45.0F, 1.0F, 0.0F, 0.0F);
      }

      this.renderModel(stack);
      att.b();
      GL11.glPopMatrix();
   }

   private void renderAim(bjo texture) {
      bdi entityclientplayermp = atv.w().h;
      bma.a(bma.b, 240.0F, 240.0F);
      GL11.glDisable(2896);
      GL11.glMatrixMode(5890);
      GL11.glPushMatrix();
      atv.w().N.a(this.blank);
      int tex = atv.w().N.b(this.blank).b();
      GL11.glViewport(0, 0, mc.d, mc.e);
      GL11.glBindTexture(3553, tex);
      int width = mc.d;
      int height = mc.e;
      float zoom = 1.5F;
      int size = (int)(Math.min(width, height) / zoom);
      GL11.glCopyTexImage2D(3553, 0, 6407, width / 2 - size / 2, height / 2 - size / 2, size, size, 0);
      GL11.glTexParameteri(3553, 10240, 9729);
      GL11.glPopMatrix();
      GL11.glMatrixMode(5888);
      GL11.glPushMatrix();
      bfq t = bfq.a;
      GL11.glScalef(0.22F, 0.22F, 0.22F);
      float[] x = new float[8];
      float[] y = new float[8];

      for (int i = 0; i < 8; i++) {
         double j = Math.toRadians(i * 45);
         x[i] = (float)Math.cos(j);
         y[i] = (float)Math.sin(j);
      }

      t.b();
      t.a(x[7], y[7], 0.0, (x[7] + 1.0F) / 2.0F, (y[7] + 1.0F) / 2.0F);
      t.a(x[0], y[0], 0.0, (x[0] + 1.0F) / 2.0F, (y[0] + 1.0F) / 2.0F);
      t.a(x[1], y[1], 0.0, (x[1] + 1.0F) / 2.0F, (y[1] + 1.0F) / 2.0F);
      t.a(x[2], y[2], 0.0, (x[2] + 1.0F) / 2.0F, (y[2] + 1.0F) / 2.0F);
      t.a();
      t.b();
      t.a(x[6], y[6], 0.0, (x[6] + 1.0F) / 2.0F, (y[6] + 1.0F) / 2.0F);
      t.a(x[7], y[7], 0.0, (x[7] + 1.0F) / 2.0F, (y[7] + 1.0F) / 2.0F);
      t.a(x[2], y[2], 0.0, (x[2] + 1.0F) / 2.0F, (y[2] + 1.0F) / 2.0F);
      t.a(x[3], y[3], 0.0, (x[3] + 1.0F) / 2.0F, (y[3] + 1.0F) / 2.0F);
      t.a();
      t.b();
      t.a(x[5], y[5], 0.0, (x[5] + 1.0F) / 2.0F, (y[5] + 1.0F) / 2.0F);
      t.a(x[6], y[6], 0.0, (x[6] + 1.0F) / 2.0F, (y[6] + 1.0F) / 2.0F);
      t.a(x[3], y[3], 0.0, (x[3] + 1.0F) / 2.0F, (y[3] + 1.0F) / 2.0F);
      t.a(x[4], y[4], 0.0, (x[4] + 1.0F) / 2.0F, (y[4] + 1.0F) / 2.0F);
      t.a();
      GL11.glTexParameteri(3553, 10240, 9728);
      atv.w().N.a(texture);
      GL11.glScalef(2.0F, 2.0F, 2.0F);
      GL11.glEnable(3042);
      int var16 = mc.f.h(ls.c(entityclientplayermp.u), ls.c(entityclientplayermp.v), ls.c(entityclientplayermp.w), 0);
      int var14 = var16 % 65536;
      int k = var16 / 65536;
      bma.a(bma.b, var14 / 1.0F, k / 1.0F);
      GL11.glBlendFunc(770, 771);
      t.b();
      t.a(-1.0, 1.0, 0.001, 0.0, 0.0);
      t.a(-1.0, -1.0, 0.001, 0.0, 1.0);
      t.a(1.0, -1.0, 0.001, 1.0, 1.0);
      t.a(1.0, 1.0, 0.001, 1.0, 0.0);
      t.a();
      GL11.glPopMatrix();
      GL11.glEnable(2896);
   }

   private void setupRotation(ClientWeaponInfo info) {
      if (mc.h.bn.h() != null && info.isReloading(mc.h.bn.h())) {
         aur timer = (aur)ReflectionHelper.getPrivateValue(atv.class, atv.w(), new String[]{"timer", "field_71428_T", "S"});
         float progress = info.getReloadRenderProgress(timer.c);
         GL11.glRotatef(-65.0F * progress, 1.0F, 0.0F, 0.0F);
      }

      if (mc.h.bn.h() != null && mc.h.bn.h().q().e("grenadeLauncher_reloadTime") > 0) {
         aur timer = (aur)ReflectionHelper.getPrivateValue(atv.class, atv.w(), new String[]{"timer", "field_71428_T", "S"});
         float progress = info.getReloadRenderProgress(mc.h.by(), timer.c);
         GL11.glRotatef(-65.0F * progress, 1.0F, 0.0F, 0.0F);
      }
   }

   private void setupTranslation(ClientWeaponInfo info) {
      boolean isSprinting = StalkerMain.instance.smHelper.isPlayerRunning(mc.h);
      boolean isAiming = info.isAiming() && mc.n == null || info.isAiming() && mc.n instanceof GuiWeaponEditor;
      boolean isSight = mc.h.by() != null && mc.h.by().q() != null && mc.h.by().q().n("sight");
      float progress = 0.0F;
      aur timer = (aur)ReflectionHelper.getPrivateValue(atv.class, atv.w(), new String[]{"timer", "field_71428_T", "S"});
      float animationSpeed = 0.225F;
      if ((mc.h.bn.h() == null || !info.isReloading(mc.h.bn.h())) && (mc.h.bn.h() == null || mc.h.bn.h().q().e("grenadeLauncher_reloadTime") <= 0)) {
         if (mc.h.bn.h() == null || info.getLastShot() >= 3) {
            progress = ls.a(progress - timer.c * 0.05F, 0.0F, 1.0F);
         } else if (!isSprinting && info.getLastShot() > 1) {
            progress = ls.a(progress + timer.c * 0.05F, 0.0F, 1.0F);
            if (mc.n == null && !mc.h.bn.h().q().n("silencer")) {
               if (this.particlesSmoke.size() <= 3 && !isSight && !this.integrateSight
                  || this.particlesSmoke.size() <= 3 && isSight && !info.isAiming()
                  || this.particlesSmoke.size() <= 1 && this.integrateSight && !info.isAiming()) {
                  this.particlesSmoke.add(new ParticleFlashSmoke(this.size, true));
               }

               this.particlesTracer.add(new ParticleTracer());
            }
         }
      } else {
         progress = info.getReloadRenderProgress(timer.c);
         GL11.glTranslatef(0.0F, 0.0F, progress / 2.0F);
      }

      if (mc.n instanceof GuiWeaponEditor && this.particlesSmoke.size() <= 0) {
         this.particlesSmoke.add(new ParticleFlashSmoke(this.size, false));
         this.particlesTracer.add(new ParticleTracer());
      }

      float sprinting = 0.0F;
      if (!this.weapon.isPistol) {
         sprinting = sprintingProgress;
         sprinting = ls.a(sprinting + (mc.A ? timer.c * (isSprinting ? animationSpeed : -animationSpeed) : 0.0F), 0.0F, 1.0F);
      }

      float aiming = aimingProgress;
      aiming = ls.a(aiming + (mc.A ? timer.c * (isAiming ? animationSpeed : -animationSpeed) : 0.0F), 0.0F, 1.0F);
      Iterator it = this.particlesSmoke.iterator();

      while (it.hasNext()) {
         ParticleFlashSmoke particle = (ParticleFlashSmoke)it.next();
         if (particle.isDead) {
            it.remove();
         } else {
            particle.tick();
         }
      }

      it = this.particlesTracer.iterator();

      while (it.hasNext()) {
         ParticleTracer particle = (ParticleTracer)it.next();
         if (particle.isDead) {
            it.remove();
         } else {
            particle.tick();
         }
      }

      if (isAiming) {
         GL11.glTranslatef(0.0F, 0.0F, progress * 0.3F);
      } else {
         GL11.glTranslatef(0.0F, 0.0F, progress * 0.61F);
      }

      GL11.glTranslatef(-sprinting * 1.15F, 0.0F, -sprinting * 0.75F);
      if (!this.integrateSight && mc.h.by() != null && mc.h.by().q() != null && !isSight) {
         GL11.glTranslatef(aiming * this.aimPosX, 0.05F + aiming * this.aimPosY, aiming * this.aimPosZ);
         GL11.glRotatef(aiming * this.aimRotX, 1.0F, 0.0F, 0.0F);
         GL11.glRotatef(aiming * this.aimRotY, 0.0F, 1.0F, 0.0F);
         GL11.glRotatef(aiming * this.aimRotZ, 0.0F, 0.0F, 1.0F);
      }

      if (this.integrateSight || mc.h.by() != null && isSight) {
         GL11.glTranslatef(aiming * -0.557F, 0.05F, 0.0F);
         GL11.glRotatef(aiming * -0.5F, 1.0F, 0.0F, 0.0F);
      }

      GL11.glRotatef(sprinting * 65.0F, 0.0F, 1.0F, 0.0F);
   }

   private void listenFirstPerson() {
      if (Keyboard.isKeyDown(75)) {
         this.posX -= 0.01F;
      }

      if (Keyboard.isKeyDown(77)) {
         this.posX += 0.01F;
      }

      if (Keyboard.isKeyDown(80)) {
         this.posY -= 0.01F;
      }

      if (Keyboard.isKeyDown(72)) {
         this.posY += 0.01F;
      }

      if (Keyboard.isKeyDown(71)) {
         this.posZ -= 0.01F;
      }

      if (Keyboard.isKeyDown(73)) {
         this.posZ += 0.01F;
      }

      if (Keyboard.isKeyDown(76)) {
         this.posX = 0.0F;
         this.posY = 0.0F;
         this.posZ = 0.0F;
      }

      if (Keyboard.isKeyDown(82) && !written) {
         System.out.println("posX: " + this.posX);
         System.out.println("posY: " + this.posY);
         System.out.println("posZ: " + this.posZ);
         written = true;
      } else if (!Keyboard.isKeyDown(82)) {
         written = false;
      }
   }

   private void listenAiming() {
      if (Keyboard.isKeyDown(75)) {
         this.aimRotX -= 0.1F;
      }

      if (Keyboard.isKeyDown(77)) {
         this.aimRotX += 0.1F;
      }

      if (Keyboard.isKeyDown(80)) {
         this.aimPosY -= 0.01F;
      }

      if (Keyboard.isKeyDown(72)) {
         this.aimPosY += 0.01F;
      }

      if (Keyboard.isKeyDown(71)) {
         this.aimPosZ -= 0.01F;
      }

      if (Keyboard.isKeyDown(73)) {
         this.aimPosZ += 0.01F;
      }

      if (Keyboard.isKeyDown(76)) {
         this.aimRotX = 0.0F;
         this.aimPosY = 0.0F;
         this.aimPosZ = 0.0F;
      }

      if (Keyboard.isKeyDown(82) && !written) {
         System.out.println("aimRotX: " + this.aimRotX);
         System.out.println("aimPosY: " + this.aimPosY);
         System.out.println("aimPosZ: " + this.aimPosZ);
         written = true;
      } else if (!Keyboard.isKeyDown(82)) {
         written = false;
      }
   }

   private void listenThirdPerson() {
      if (Keyboard.isKeyDown(78)) {
         if (Keyboard.isKeyDown(79)) {
            oldLivingTransform[0] = oldLivingTransform[0] + 0.01F;
         }

         if (Keyboard.isKeyDown(80)) {
            oldLivingTransform[1] = oldLivingTransform[1] + 0.01F;
         }

         if (Keyboard.isKeyDown(81)) {
            oldLivingTransform[2] = oldLivingTransform[2] + 0.01F;
         }

         if (Keyboard.isKeyDown(75)) {
            oldLivingTransform[3] = oldLivingTransform[3] + 0.1F;
         }

         if (Keyboard.isKeyDown(76)) {
            oldLivingTransform[4] = oldLivingTransform[4] + 0.1F;
         }

         if (Keyboard.isKeyDown(77)) {
            oldLivingTransform[5] = oldLivingTransform[5] + 0.1F;
         }
      } else if (Keyboard.isKeyDown(12)) {
         if (Keyboard.isKeyDown(79)) {
            oldLivingTransform[0] = oldLivingTransform[0] - 0.01F;
         }

         if (Keyboard.isKeyDown(80)) {
            oldLivingTransform[1] = oldLivingTransform[1] - 0.01F;
         }

         if (Keyboard.isKeyDown(81)) {
            oldLivingTransform[2] = oldLivingTransform[2] - 0.01F;
         }

         if (Keyboard.isKeyDown(75)) {
            oldLivingTransform[3] = oldLivingTransform[3] - 0.1F;
         }

         if (Keyboard.isKeyDown(76)) {
            oldLivingTransform[4] = oldLivingTransform[4] - 0.1F;
         }

         if (Keyboard.isKeyDown(77)) {
            oldLivingTransform[5] = oldLivingTransform[5] - 0.1F;
         }
      }

      System.out
         .println(
            oldLivingTransform[0]
               + ", "
               + oldLivingTransform[1]
               + ", "
               + oldLivingTransform[2]
               + ", "
               + oldLivingTransform[3]
               + ", "
               + oldLivingTransform[4]
               + ", "
               + oldLivingTransform[5]
         );
   }

   public static enum RenderType {
      RIFLE("RIFLE", 0),
      BACKPACK_RIFLE("BACKPACK_RIFLE", 1),
      PISTOL("PISTOL", 2);

      private static final RenderWeapon.RenderType[] $VALUES = new RenderWeapon.RenderType[]{RIFLE, BACKPACK_RIFLE, PISTOL};

      private RenderType(String var1, int var2) {
      }
   }
}
