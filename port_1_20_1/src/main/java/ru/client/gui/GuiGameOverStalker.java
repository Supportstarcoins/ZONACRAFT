package ru.stalcraft.client.gui;

public class GuiGameOverStalker extends avc {
   public int deathTimer;
   private int a = 0;

   public void a(int par1, int par2, float par3) {
      super.a(par1, par2, par3);
   }

   public void c() {
      super.c();

      for (aut button : super.i) {
         if (button.g == 1) {
            button.h = true;
         }

         if (button.g == 2) {
            button.h = true;
         }
      }
   }
}
