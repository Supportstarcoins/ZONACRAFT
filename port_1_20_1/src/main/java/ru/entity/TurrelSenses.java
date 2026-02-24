package ru.stalcraft.entity;

import java.util.ArrayList;
import java.util.List;

public class TurrelSenses {
   EntityTurrel entityObj;
   List seenEntities = new ArrayList();
   List unseenEntities = new ArrayList();

   public TurrelSenses(EntityTurrel entity) {
      this.entityObj = entity;
   }

   public void clearSensingCache() {
      this.seenEntities.clear();
      this.unseenEntities.clear();
   }

   public boolean canSee(nn par1Entity) {
      if (this.seenEntities.contains(par1Entity)) {
         return true;
      } else if (this.unseenEntities.contains(par1Entity)) {
         return false;
      } else {
         this.entityObj.q.C.a("canSee");
         boolean flag = this.entityObj.canEntityBeSeen(par1Entity);
         this.entityObj.q.C.b();
         if (flag) {
            this.seenEntities.add(par1Entity);
         } else {
            this.unseenEntities.add(par1Entity);
         }

         return flag;
      }
   }
}
