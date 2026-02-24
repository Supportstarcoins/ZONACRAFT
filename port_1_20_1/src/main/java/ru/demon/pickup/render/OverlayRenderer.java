package ru.demon.pickup.render;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.ForgeSubscribe;
import org.lwjgl.input.Keyboard;
import ru.demon.pickup.handlers.DemonKeyHandler;
import ru.demon.util.PacketUtil;

@SideOnly(Side.CLIENT)
public class OverlayRenderer extends OverlayRenderUtils {
   private atv mc = atv.w();
   public static ss item;

   @ForgeSubscribe
   public void renderOverlay(RenderGameOverlayEvent event) {
      item = this.getEntityItem();
      if (item != null) {
         ye is = item.d();
         avi font = is.b().getFontRenderer(is) == null ? this.mc.l : is.b().getFontRenderer(is);
         String displayName = is.s();
         String press = Keyboard.getKeyName(DemonKeyHandler.pickUpKey.d) + " - ПОДОБРАТЬ ";
         font.a(displayName, event.resolution.a() / 2 - font.a(displayName) / 2, event.resolution.b() / 2 + 30, 16777215, false);
         font.a(press, event.resolution.a() / 2 - (font.a(press) / 2 - 5), event.resolution.b() / 2 + 40, 16777215, false);
      }
   }

   public ss getEntityItem() {
      int range = 3;
      atc v = this.mc.h.aa().a();

      for (int i = 1; i < range; i++) {
         asx aabb = asx.a(
            this.mc.h.u + v.c * i - 0.18,
            this.mc.h.v + v.d * i - 0.45,
            this.mc.h.w + v.e * i - 0.18,
            this.mc.h.u + v.c * i + 0.18,
            this.mc.h.v + v.d * i + 0.45,
            this.mc.h.w + v.e * i + 0.18
         );
         List list = this.mc.h.q.a(ss.class, aabb);
         if (list.iterator().hasNext()) {
            ss el = (ss)list.get(0);
            item = el;
            return el;
         }
      }

      return null;
   }

   public static void pickupItem() {
      if (item != null) {
         PacketUtil.writeToServer(0, item.k);
      }
   }
}
