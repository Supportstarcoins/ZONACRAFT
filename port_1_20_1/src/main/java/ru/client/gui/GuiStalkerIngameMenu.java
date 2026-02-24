package ru.stalcraft.client.gui;

import ru.stalcraft.client.ClientProxy;
import ru.stalcraft.client.gui.shop.ClientShopData;

public class GuiStalkerIngameMenu extends awe {
   private int updateCounter2;
   private int updateCounter;

   public void A_() {
      this.updateCounter2 = 0;
      this.i.clear();
      byte b0 = -16;
      boolean flag = true;
      this.i.add(new aut(1, this.g / 2 - 100, this.h / 4 + 148 + b0, bkb.a("menu.returnToMenu")));
      if (!this.f.A()) {
         ((aut)this.i.get(0)).f = bkb.a("menu.disconnect");
      }

      this.i.add(new aut(4, this.g / 2 - 100, this.h / 4 + 24 + b0, bkb.a("menu.returnToGame")));
      this.i.add(new aut(0, this.g / 2 - 100, this.h / 4 + 122 + b0, 98, 20, bkb.a("menu.options")));
      aut guibutton;
      this.i.add(guibutton = new aut(7, this.g / 2 + 2, this.h / 4 + 122 + b0, 98, 20, bkb.a("menu.shareToLan")));
      this.i.add(new aut(5, this.g / 2 - 100, this.h / 4 + 48 + b0, 98, 20, bkb.a("gui.achievements")));
      this.i.add(new aut(6, this.g / 2 + 2, this.h / 4 + 48 + b0, 98, 20, bkb.a("gui.stats")));
      this.i.add(new aut(100, this.g / 2, this.h / 4 + 72 + b0, 98, 20, "Кейсы"));
      this.i.add(new aut(101, this.g / 2 - 100, this.h / 4 + 72 + b0, 98, 20, "Магазин"));
      this.i.add(new aut(102, this.g / 2 - 100, this.h / 4 + 96 + b0, "Персональный склад"));
      guibutton.h = this.f.B() && !this.f.C().c();
   }

   protected void a(aut par1GuiButton) {
      switch (par1GuiButton.g) {
         case 0:
            this.f.a(new avw(this, this.f.u));
            break;
         case 1:
            par1GuiButton.h = false;
            this.f.y.a(la.j, 1);
            this.f.f.F();
            this.f.a((bdd)null);
            this.f.a(new blt());
         case 2:
         case 3:
         default:
            break;
         case 4:
            this.f.a((awe)null);
            this.f.g();
            this.f.v.f();
            break;
         case 5:
            this.f.a(new awq(super.f.y));
            break;
         case 6:
            this.f.a(new awr(this, super.f.y));
            break;
         case 7:
            this.f.a(new awj(this));
            break;
         case 100:
            ClientShopData.OpenGui();
            break;
         case 101:
            ClientProxy.clientShopManager.OpenGui(super.f);
      }
   }

   public void c() {
      super.c();
      this.updateCounter++;
   }

   public void a(int par1, int par2, float par3) {
      this.e();
      this.a(this.o, "Game menu", this.g / 2, 40, 16777215);
      super.a(par1, par2, par3);
   }
}
