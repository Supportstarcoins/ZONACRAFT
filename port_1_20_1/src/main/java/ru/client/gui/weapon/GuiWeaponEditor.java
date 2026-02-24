package ru.stalcraft.client.gui.weapon;

import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.IItemRenderer.ItemRenderType;
import ru.stalcraft.client.ClientWeaponInfo;
import ru.stalcraft.client.render.RenderWeapon;

public class GuiWeaponEditor extends awe {
   public ye weaponStack;
   public RenderWeapon weaponRenderer;
   public aut buttonPlus;
   public avf input;
   public boolean plus;
   public float value;
   public boolean isAiming;

   public GuiWeaponEditor(ye weaponStack) {
      this.weaponStack = weaponStack;
      this.weaponRenderer = (RenderWeapon)MinecraftForgeClient.getItemRenderer(weaponStack, ItemRenderType.EQUIPPED_FIRST_PERSON);
      this.plus = true;
      this.value = 0.1F;
      this.isAiming = false;
   }

   public void A_() {
      super.A_();
      super.i.add(new aut(0, super.g / 2 - 280, super.h / 2 - 25, 60, 20, "PosZ"));
      super.i.add(new aut(1, super.g / 2 - 280, super.h / 2 - 50, 60, 20, "PosY"));
      super.i.add(new aut(2, super.g / 2 - 280, super.h / 2 - 75, 60, 20, "PosX"));
      super.i.add(new aut(3, super.g / 2 - 200, super.h / 2 - 75, 60, 20, "PosLeftHandX"));
      super.i.add(new aut(4, super.g / 2 - 200, super.h / 2 - 50, 60, 20, "PosLeftHandY"));
      super.i.add(new aut(5, super.g / 2 - 200, super.h / 2 - 25, 60, 20, "PosLeftHandZ"));
      super.i.add(new aut(6, super.g / 2 - 120, super.h / 2 - 75, 60, 20, "PosRightHandX"));
      super.i.add(new aut(7, super.g / 2 - 120, super.h / 2 - 50, 60, 20, "PosRightHandY"));
      super.i.add(new aut(8, super.g / 2 - 120, super.h / 2 - 25, 60, 20, "PosRightHandZ"));
      super.i.add(new aut(9, super.g / 2 - 280, super.h / 2, 60, 20, "AimingPosX"));
      super.i.add(new aut(10, super.g / 2 - 280, super.h / 2 + 25, 60, 20, "AimingPosY"));
      super.i.add(new aut(11, super.g / 2 - 280, super.h / 2 + 50, 60, 20, "AimingPosZ"));
      super.i.add(new aut(12, super.g / 2 - 200, super.h / 2, 60, 20, "AimingRotX"));
      super.i.add(new aut(13, super.g / 2 - 200, super.h / 2 + 25, 60, 20, "AimingRotY"));
      super.i.add(new aut(14, super.g / 2 - 200, super.h / 2 + 50, 60, 20, "AimingRotZ"));
      super.i.add(new aut(80, super.g / 2 - 120, super.h / 2 + 75, 60, 20, "flashPosX"));
      super.i.add(new aut(81, super.g / 2 - 200, super.h / 2 + 75, 60, 20, "flashPosY"));
      super.i.add(new aut(82, super.g / 2 - 280, super.h / 2 + 75, 60, 20, "flashPosZ"));
      super.i.add(new aut(84, super.g / 2 - 280, super.h / 2 + 100, 60, 20, "flashSize"));
      super.i.add(new aut(83, super.g / 2 - 40, super.h / 2 + 75, 60, 20, "aim"));
      this.buttonPlus = new aut(15, super.g / 2 - 110, super.h / 2 + 50, 15, 20, "");
      super.i.add(this.buttonPlus);
      super.i.add(new aut(100, super.g / 2 - 120, super.h / 2 - 100, 60, 20, "equippedPosX"));
      super.i.add(new aut(101, super.g / 2 - 200, super.h / 2 - 100, 60, 20, "equippedPosY"));
      super.i.add(new aut(102, super.g / 2 - 280, super.h / 2 - 100, 60, 20, "equippedPosZ"));
      this.input = new avf(super.o, super.g / 2 - 37, super.h / 2 - 30, 74, 14);
      this.input.f(16);
      this.input.b(true);
   }

   public void a(int x, int y, float size) {
      super.a(x, y, size);
      super.a(this.f.l, "" + this.weaponRenderer.posX, super.g / 2 - 210, super.h / 2 - 70, -1);
      super.a(this.f.l, "" + this.weaponRenderer.posY, super.g / 2 - 210, super.h / 2 - 45, -1);
      super.a(this.f.l, "" + this.weaponRenderer.posZ, super.g / 2 - 210, super.h / 2 - 20, -1);
      super.a(this.f.l, "" + this.weaponRenderer.posLeftHandX, super.g / 2 - 130, super.h / 2 - 70, -1);
      super.a(this.f.l, "" + this.weaponRenderer.posLeftHandY, super.g / 2 - 130, super.h / 2 - 45, -1);
      super.a(this.f.l, "" + this.weaponRenderer.posLeftHandZ, super.g / 2 - 130, super.h / 2 - 20, -1);
      super.a(this.f.l, "" + this.weaponRenderer.posRightHandX, super.g / 2 - 50, super.h / 2 - 70, -1);
      super.a(this.f.l, "" + this.weaponRenderer.posRightHandY, super.g / 2 - 50, super.h / 2 - 45, -1);
      super.a(this.f.l, "" + this.weaponRenderer.posRightHandZ, super.g / 2 - 50, super.h / 2 - 20, -1);
      super.a(this.f.l, "" + this.weaponRenderer.posX, super.g / 2 - 210, super.h / 2 - 70, -1);
      super.a(this.f.l, "" + this.weaponRenderer.posY, super.g / 2 - 210, super.h / 2 - 45, -1);
      super.a(this.f.l, "" + this.weaponRenderer.posZ, super.g / 2 - 210, super.h / 2 - 20, -1);
      super.a(this.f.l, "" + this.weaponRenderer.aimPosX, super.g / 2 - 210, super.h / 2 + 5, -1);
      super.a(this.f.l, "" + this.weaponRenderer.aimPosY, super.g / 2 - 210, super.h / 2 + 30, -1);
      super.a(this.f.l, "" + this.weaponRenderer.aimPosZ, super.g / 2 - 210, super.h / 2 + 55, -1);
      super.a(this.f.l, "" + this.weaponRenderer.aimRotX, super.g / 2 - 130, super.h / 2 + 5, -1);
      super.a(this.f.l, "" + this.weaponRenderer.aimRotY, super.g / 2 - 130, super.h / 2 + 30, -1);
      super.a(this.f.l, "" + this.weaponRenderer.aimRotZ, super.g / 2 - 130, super.h / 2 + 55, -1);
      super.a(this.f.l, "" + this.weaponRenderer.flashPosZ, super.g / 2 - 209, super.h / 2 + 80, -1);
      super.a(this.f.l, "" + this.weaponRenderer.flashPosY, super.g / 2 - 130, super.h / 2 + 80, -1);
      super.a(this.f.l, "" + this.weaponRenderer.flashPosX, super.g / 2 - 51, super.h / 2 + 80, -1);
      super.a(this.f.l, "" + this.weaponRenderer.size, super.g / 2 - 198, super.h / 2 + 105, -1);
      super.a(this.f.l, "" + this.weaponRenderer.equippedPosX, super.g / 2 - 210, super.h / 2 - 95, -1);
      super.a(this.f.l, "" + this.weaponRenderer.equippedPosY, super.g / 2 - 130, super.h / 2 - 95, -1);
      super.a(this.f.l, "" + this.weaponRenderer.equippedPosZ, super.g / 2 - 50, super.h / 2 - 95, -1);
      this.input.f();
   }

   public void a(aut button) {
      RenderWeapon weaponRenderer = this.weaponRenderer;
      ClientWeaponInfo weaponInfo = weaponRenderer.clientWeaponInfo;
      if (button.g == 2) {
         weaponRenderer.posX = this.plus ? weaponRenderer.posX + this.value : weaponRenderer.posX - this.value;
      }

      if (button.g == 1) {
         weaponRenderer.posY = this.plus ? weaponRenderer.posY + this.value : weaponRenderer.posY - this.value;
      }

      if (button.g == 0) {
         weaponRenderer.posZ = this.plus ? weaponRenderer.posZ + this.value : weaponRenderer.posZ - this.value;
      }

      if (button.g == 3) {
         weaponRenderer.posLeftHandX = this.plus ? weaponRenderer.posLeftHandX + this.value : weaponRenderer.posLeftHandX - this.value;
      }

      if (button.g == 5) {
         weaponRenderer.posLeftHandY = this.plus ? weaponRenderer.posLeftHandY + this.value : weaponRenderer.posLeftHandY - this.value;
      }

      if (button.g == 4) {
         weaponRenderer.posLeftHandZ = this.plus ? weaponRenderer.posLeftHandZ + this.value : weaponRenderer.posLeftHandZ - this.value;
      }

      if (button.g == 6) {
         weaponRenderer.posRightHandX = this.plus ? weaponRenderer.posRightHandX + this.value : weaponRenderer.posRightHandX - this.value;
      }

      if (button.g == 8) {
         weaponRenderer.posRightHandY = this.plus ? weaponRenderer.posRightHandY + this.value : weaponRenderer.posRightHandY - this.value;
      }

      if (button.g == 7) {
         weaponRenderer.posRightHandZ = this.plus ? weaponRenderer.posRightHandZ + this.value : weaponRenderer.posRightHandZ - this.value;
      }

      if (button.g == 9) {
         weaponRenderer.aimPosX = this.plus ? weaponRenderer.aimPosX + this.value : weaponRenderer.aimPosX - this.value;
      }

      if (button.g == 11) {
         weaponRenderer.aimPosY = this.plus ? weaponRenderer.aimPosY + this.value : weaponRenderer.aimPosY - this.value;
      }

      if (button.g == 10) {
         weaponRenderer.aimPosZ = this.plus ? weaponRenderer.aimPosZ + this.value : weaponRenderer.aimPosZ - this.value;
      }

      if (button.g == 12) {
         weaponRenderer.aimRotX = this.plus ? weaponRenderer.aimRotX + this.value : weaponRenderer.aimRotX - this.value;
      }

      if (button.g == 14) {
         weaponRenderer.aimRotY = this.plus ? weaponRenderer.aimRotX + this.value : weaponRenderer.aimRotY - this.value;
      }

      if (button.g == 13) {
         weaponRenderer.aimRotZ = this.plus ? weaponRenderer.aimRotZ + this.value : weaponRenderer.aimRotZ - this.value;
      }

      if (button.g == 12) {
         weaponRenderer.aimRotX = this.plus ? weaponRenderer.aimRotX + this.value : weaponRenderer.aimRotX - this.value;
      }

      if (button.g == 14) {
         weaponRenderer.aimRotY = this.plus ? weaponRenderer.aimRotX + this.value : weaponRenderer.aimRotY - this.value;
      }

      if (button.g == 13) {
         weaponRenderer.aimRotZ = this.plus ? weaponRenderer.aimRotZ + this.value : weaponRenderer.aimRotZ - this.value;
      }

      if (button.g == 80) {
         weaponRenderer.flashPosX = this.plus ? weaponRenderer.flashPosX + this.value : weaponRenderer.flashPosX - this.value;
      }

      if (button.g == 81) {
         weaponRenderer.flashPosY = this.plus ? weaponRenderer.flashPosY + this.value : weaponRenderer.flashPosY - this.value;
      }

      if (button.g == 82) {
         weaponRenderer.flashPosZ = this.plus ? weaponRenderer.flashPosZ + this.value : weaponRenderer.flashPosZ - this.value;
      }

      if (button.g == 84) {
         weaponRenderer.size = this.plus ? weaponRenderer.size + this.value : weaponRenderer.size - this.value;
      }

      if (button.g == 15) {
         this.plus = !this.plus;
      }

      if (button.g == 83) {
         this.isAiming = !this.isAiming;
         weaponInfo.setAiming(this.isAiming);
      }

      if (button.g == 102) {
         weaponRenderer.equippedPosX = this.plus ? weaponRenderer.equippedPosX + this.value : weaponRenderer.equippedPosX - this.value;
      }

      if (button.g == 101) {
         weaponRenderer.equippedPosY = this.plus ? weaponRenderer.equippedPosY + this.value : weaponRenderer.equippedPosY - this.value;
      }

      if (button.g == 100) {
         weaponRenderer.equippedPosZ = this.plus ? weaponRenderer.equippedPosZ + this.value : weaponRenderer.equippedPosZ - this.value;
      }
   }

   public void c() {
      super.c();
      if (this.plus) {
         this.buttonPlus.f = "+";
      } else {
         this.buttonPlus.f = "-";
      }

      if (!this.input.b().trim().matches("[a-zA-Z\\s]*") && !this.input.b().trim().equals("ю") && !this.input.b().trim().equals("?")) {
         this.value = Float.parseFloat(this.input.b().trim());
      }

      this.input.a();
   }

   protected void a(int par1, int par2, int par3) {
      super.a(par1, par2, par3);
   }

   protected void a(char par1, int par2) {
      super.a(par1, par2);
      this.input.a(par1, par2);
   }

   public boolean f() {
      return false;
   }
}
