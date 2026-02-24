package ru.stalcraft.entity;

import ru.stalcraft.StalkerMain;
import ru.stalcraft.items.ItemExplosive;

public class EntityExplosive extends nn {
   private static final int SIDE_PLACED = 28;
   private static final int LIFETIME = 200;
   private static final int BLOCK_X = 29;
   private static final int BLOCK_Y = 30;
   private static final int BLOCK_Z = 31;
   private int prevBlockId;
   private int prevBlockMetadata;
   private int prevDownBlockMetadata;
   private boolean hasReadPrevValues = false;

   public EntityExplosive(abw par1World) {
      super(par1World);
      super.Z = true;
      this.a(0.001F, 0.001F);
      super.N = 0.0F;
      super.P = 0.007F;
      super.O = 0.001F;
      super.l = Double.MAX_VALUE;
      super.ah.a(28, new Integer(0));
      super.ah.a(29, new Integer(0));
      super.ah.a(30, new Integer(0));
      super.ah.a(31, new Integer(0));
   }

   public void l_() {
      super.l_();
      if (!super.q.I) {
         int x = super.ah.c(29);
         int y = super.ah.c(30);
         int z = super.ah.c(31);
         int blockId = super.q.a(x, y, z);
         int blockData = super.q.h(x, y, z);
         int downBlockData = super.q.h(x, y - 1, z);
         if (!this.hasReadPrevValues) {
            this.hasReadPrevValues = true;
            this.prevBlockId = blockId;
            this.prevBlockMetadata = blockData;
            this.prevDownBlockMetadata = downBlockData;
         }

         if (blockId != this.prevBlockId || !ItemExplosive.isBlockExplosible(blockId)) {
            this.x();
            return;
         }

         this.prevBlockId = blockId;
         if (blockId == StalkerMain.stalkerDoor.cF
            && (blockData != this.prevBlockMetadata || downBlockData != this.prevDownBlockMetadata && super.q.a(x, y - 1, z) == StalkerMain.stalkerDoor.cF)) {
            this.doExplosion(x, y, z);
         }

         this.prevBlockMetadata = blockData;
         this.prevDownBlockMetadata = downBlockData;
         if (super.ac >= 200) {
            this.doExplosion(x, y, z);
         }
      }
   }

   private void doExplosion(int x, int y, int z) {
      if (!super.M) {
         super.q.a(x, y, z, false);
         super.q.a(this, super.u, super.v, super.w, 1.0F, false);
         this.x();
      }
   }

   public void applyAttributes(int sidePlaced, int blockX, int blockY, int blockZ) {
      super.ah.b(28, sidePlaced);
      super.ah.b(29, blockX);
      super.ah.b(30, blockY);
      super.ah.b(31, blockZ);
   }

   protected void a() {
   }

   protected void a(by tag) {
      this.applyAttributes(tag.e("sidePlaced"), tag.e("blockX"), tag.e("blockY"), tag.e("blockZ"));
   }

   protected void b(by tag) {
      tag.a("sidePlaced", super.ah.c(28));
      tag.a("blockX", super.ah.c(29));
      tag.a("blockY", super.ah.c(30));
      tag.a("blockZ", super.ah.c(31));
   }

   public int getSidePlaced() {
      return super.ah.c(28);
   }

   public void a(double par1, double par3, double par5, float par7, float par8, int par9) {
      this.b(par1, par3, par5);
      this.b(par7, par8);
   }

   public boolean a(nb par1DamageSource, float par2) {
      if (this.ar()) {
         return false;
      } else {
         this.x();
         float f = 0.1F;
         ss entityitem = new ss(super.q, super.u, super.v, super.w, new ye(StalkerMain.explosive.cv, 1, 0));
         entityitem.g((super.ab.nextFloat() - 0.5F) * f, super.ab.nextFloat() * f / 2.0F, (super.ab.nextFloat() - 0.5F) * f);
         entityitem.b = 10;
         super.q.d(entityitem);
         return true;
      }
   }
}
