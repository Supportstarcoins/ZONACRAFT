package ru.stalcraft.entity;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.List;
import ru.stalcraft.StalkerMain;
import ru.stalcraft.inventory.CorpseInventory;
import ru.stalcraft.items.ItemWeapon;
import ru.stalcraft.server.network.ServerPacketSender;

public class EntityCorpse extends og implements IEntityAdditionalSpawnData {
   private static final int BACKPACK = 28;
   private static final int PISTOL = 29;
   private static final int RIFLE = 30;
   public String username;
   public int ticksDead;
   public float rotationFall;
   public float prevRotationFall;
   private float fallSpeed;
   private float startPitch;
   private float endLeftHand;
   private float endRightHand;
   public float leftHandRotation;
   public float rightHandRotation;
   public float prevLeftHandRotation;
   public float prevRightHandRotation;
   public boolean isFallingFinished;
   private bic downloadedSkin;
   public bjo locationSkin;
   public CorpseInventory inventory;
   private int currentItem;
   private int emptyInventoryTimer;
   private boolean isInventoryEmpty;
   public List openedContainers = new ArrayList();
   public Object particleEmitter;

   public EntityCorpse(abw par1World) {
      super(par1World);
      super.O = 1.0F;
      this.inventory = new CorpseInventory(this);
      super.e = new float[]{0.0F, 0.0F, 0.0F, 0.0F, 0.0F};
      if (!par1World.I) {
         this.afterServerInit();
      }

      this.a(super.O, 0.25F);
   }

   public EntityCorpse(uf player) {
      this(player.q);
      this.username = player.bu;
      this.b(player.u, player.v, player.w, player.A, player.B);
      super.aP = player.aP;
      if (player.P < 1.0F) {
         this.fallSpeed = 0.1F;
         this.rotationFall = 90.0F;
         this.finishFalling();
      } else {
         this.fallSpeed = super.q.s.nextBoolean() ? -0.1F : 0.1F;
      }

      this.startPitch = player.B;
      this.endRightHand = super.q.s.nextFloat() * 20.0F;
      this.endLeftHand = -super.q.s.nextFloat() * 20.0F;
      super.aN = super.aO = player.aN;
      this.inventory = new CorpseInventory(this, player);
      this.currentItem = player.bn.c;
      this.afterServerInit();
   }

   protected void afterServerInit() {
      this.updateObjects();
      this.updateInventoryEmpty();
   }

   public void a() {
      super.a();
      this.v().a(28, 5);
      this.v().a(29, 5);
      this.v().a(30, 5);
   }

   public boolean ar() {
      return true;
   }

   public void l_() {
      super.l_();
      if (!super.q.I) {
         if (this.isInventoryOpened()) {
            this.updateInventoryEmpty();
         }

         if (this.isInventoryEmpty) {
            this.emptyInventoryTimer++;
         }
      }

      if (!super.q.I && (super.ac > 18000 || this.emptyInventoryTimer > 6000)) {
         this.x();
      }

      if (!this.isFallingFinished) {
         this.updateFalling();
         if (Math.abs(this.rotationFall) >= 90.0F) {
            this.finishFalling();
         }
      }

      this.updateRotations();
      if (!super.q.I) {
         this.updateObjects();
      }

      this.updateEquipment();
   }

   public void a(by tag) {
      super.a(tag);
      this.username = tag.i("username");
      this.ticksDead = tag.e("ticks_dead");
      this.prevRotationFall = this.rotationFall = tag.g("rotation_fall");
      this.fallSpeed = tag.g("fall_speed");
      this.endRightHand = tag.g("end_right_hand");
      this.endLeftHand = tag.g("end_left_hand");
      this.rightHandRotation = this.prevRightHandRotation = tag.g("right_hand_rotation");
      this.leftHandRotation = this.prevLeftHandRotation = tag.g("left_hand_rotation");
      this.isFallingFinished = tag.n("is_falling_finished");
      this.currentItem = tag.e("current_item");
      this.emptyInventoryTimer = tag.e("empty_inv_timer");
      this.inventory.readFromNBT(tag.m("corpse_inventory"));
      this.updateInventoryEmpty();
   }

   public void b(by tag) {
      super.b(tag);
      tag.a("username", this.username);
      tag.a("ticks_dead", this.ticksDead);
      tag.a("rotation_fall", this.rotationFall);
      tag.a("fall_speed", this.fallSpeed);
      tag.a("start_pitch", this.startPitch);
      tag.a("end_right_hand", this.endRightHand);
      tag.a("end_left_hand", this.endLeftHand);
      tag.a("right_hand_rotation", this.rightHandRotation);
      tag.a("left_hand_rotation", this.leftHandRotation);
      tag.a("is_falling_finished", this.isFallingFinished);
      tag.a("current_item", this.currentItem);
      tag.a("corpse_inventory", this.inventory.writeToNBT(new cg()));
      tag.a("empty_inv_timer", this.emptyInventoryTimer);
   }

   public void writeSpawnData(ByteArrayDataOutput data) {
      data.writeInt(this.ticksDead);
      data.writeFloat(this.rotationFall);
      data.writeFloat(this.fallSpeed);
      data.writeFloat(this.startPitch);
      data.writeFloat(this.endRightHand);
      data.writeFloat(this.endLeftHand);
      data.writeFloat(super.aN);
      data.writeBoolean(this.isFallingFinished);
      data.writeInt(this.currentItem);
      data.writeUTF(this.username);
   }

   public void readSpawnData(ByteArrayDataInput data) {
      this.ticksDead = data.readInt();
      this.prevRotationFall = this.rotationFall = data.readFloat();
      this.fallSpeed = data.readFloat();
      this.startPitch = data.readFloat();
      this.endRightHand = data.readFloat();
      this.endLeftHand = data.readFloat();
      super.aO = super.aN = data.readFloat();
      this.isFallingFinished = data.readBoolean();
      this.currentItem = data.readInt();
      this.username = data.readUTF();
      uf player = super.q.a(this.username);
      if (player != null) {
         if (player.A != 0.0F) {
            super.A = player.A;
         }

         if (player.aP != 0.0F) {
            super.aP = player.aP;
         }

         if (player.B != 0.0F) {
            this.startPitch = player.B;
         }

         if (player.aN != 0.0F) {
            super.aN = player.aN;
         }

         this.setupSkin(player);
      } else {
         this.setupSkin(this.username);
      }

      int i = ls.c(super.u / 16.0);
      int j = ls.c(super.w / 16.0);
      super.q.e(i, j).a(this);
   }

   @SideOnly(Side.CLIENT)
   private void setupSkin(uf player) {
      this.locationSkin = ((beu)player).r();
      this.downloadedSkin = ((beu)player).p();
   }

   @SideOnly(Side.CLIENT)
   private void setupSkin(String username) {
      this.locationSkin = beu.f(username);
      this.downloadedSkin = beu.a(this.locationSkin, username);
   }

   private void updateFalling() {
      this.prevRotationFall = this.rotationFall;
      if (this.fallSpeed > 0.0F) {
         this.rotationFall = Math.min(90.0F, this.rotationFall + this.fallSpeed);
         this.fallSpeed += 0.75F;
      } else {
         this.rotationFall = Math.max(-90.0F, this.rotationFall + this.fallSpeed);
         this.fallSpeed -= 0.75F;
      }
   }

   private void updateRotations() {
      this.prevRightHandRotation = this.rightHandRotation;
      this.prevLeftHandRotation = this.leftHandRotation;
      float progress = this.getFallProgress();
      this.rightHandRotation = this.endRightHand * progress;
      this.leftHandRotation = this.endLeftHand * progress;
      super.B = this.startPitch * (1.0F - progress);
   }

   private void finishFalling() {
      this.isFallingFinished = true;
      this.prevRotationFall = this.rotationFall;
      this.prevRightHandRotation = this.rightHandRotation;
      this.prevLeftHandRotation = this.leftHandRotation;
      double x;
      double z;
      if (this.rotationFall < 0.0F) {
         x = super.u + ls.a(super.aN / 180.0F * (float) Math.PI) * 0.9;
         z = super.w + -ls.b(super.aN / 180.0F * (float) Math.PI) * 0.9;
      } else {
         x = super.u + -ls.a(super.aN / 180.0F * (float) Math.PI) * 0.9;
         z = super.w + ls.b(super.aN / 180.0F * (float) Math.PI) * 0.9;
      }

      this.b(x, super.v, z);
      super.U = super.u;
      super.W = super.w;
   }

   public float S() {
      return this.isFallingFinished ? 0.0F : super.S();
   }

   private float getFallProgress() {
      return Math.abs(this.rotationFall) / 90.0F;
   }

   protected boolean bc() {
      return true;
   }

   public void e(float par1, float par2) {
      super.x = 0.0;
      super.z = 0.0;
      super.e(par1, par2);
   }

   public bjo getTexture() {
      return this.locationSkin;
   }

   public void a(double par1, double par3, double par5, float par7, float par8, int par9) {
      if (super.A == 0.0F) {
         super.A = par7;
      }

      if (super.B == 0.0F) {
         super.B = par8;
      }
   }

   public void openGui(uf player) {
      if (!super.q.I) {
         player.openGui(StalkerMain.instance, 2, player.q, super.k, 0, 0);
         ServerPacketSender.sendWindowId(player, player.bp.d);
      }
   }

   private void updateObjects() {
      ye backpack = this.getBackpack();
      if (super.ah.f(28) != backpack) {
         if (backpack == null) {
            super.ah.a(28, 5);
            super.ah.h(28);
         } else {
            super.ah.b(28, backpack);
         }
      }

      ye pistol = this.getPistol();
      if (super.ah.f(29) != pistol) {
         if (pistol == null) {
            super.ah.a(29, 5);
            super.ah.h(29);
         } else {
            super.ah.b(29, pistol);
         }
      }

      ye rifle = this.getRifle();
      if (super.ah.f(30) != rifle) {
         if (rifle == null) {
            super.ah.a(30, 5);
            super.ah.h(30);
         } else {
            super.ah.b(30, rifle);
         }
      }
   }

   public ye getBackpack() {
      return super.q.I ? super.ah.f(28) : this.inventory.mainInventory[52];
   }

   public ye getPistol() {
      if (super.q.I) {
         return super.ah.f(29);
      } else {
         for (int i = 0; i < 4; i++) {
            if (i != this.currentItem
               && this.inventory.mainInventory[i] != null
               && this.inventory.mainInventory[i].b() instanceof ItemWeapon
               && ((ItemWeapon)this.inventory.mainInventory[i].b()).isPistol) {
               return this.inventory.mainInventory[i];
            }
         }

         return null;
      }
   }

   public ye getRifle() {
      if (super.q.I) {
         return super.ah.f(30);
      } else {
         for (int i = 0; i < 4; i++) {
            if (i != this.currentItem
               && this.inventory.mainInventory[i] != null
               && this.inventory.mainInventory[i].b() instanceof ItemWeapon
               && !((ItemWeapon)this.inventory.mainInventory[i].b()).isPistol) {
               return this.inventory.mainInventory[i];
            }
         }

         return null;
      }
   }

   public ye n(int par1) {
      return par1 == 0 ? this.inventory.mainInventory[this.currentItem] : this.inventory.mainInventory[par1 + 35];
   }

   public void c(int par1, ye par2ItemStack) {
      if (par1 == 0) {
         this.inventory.mainInventory[this.currentItem] = par2ItemStack;
      } else {
         this.inventory.mainInventory[par1 + 35] = par2ItemStack;
      }
   }

   private void updateEquipment() {
      for (int i = 0; i < 5; i++) {
         this.ae()[i] = this.n(i);
      }
   }

   protected void bw() {
   }

   public void updateInventoryEmpty() {
      boolean empty = true;

      for (ye stack : this.inventory.mainInventory) {
         if (stack != null) {
            empty = false;
            break;
         }
      }

      this.isInventoryEmpty = empty;
   }

   private boolean isInventoryOpened() {
      return this.openedContainers.size() != 0;
   }

   protected boolean i(double par1, double par3, double par5) {
      return true;
   }
}
