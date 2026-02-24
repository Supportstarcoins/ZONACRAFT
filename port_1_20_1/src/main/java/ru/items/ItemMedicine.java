package ru.stalcraft.items;

import cpw.mods.fml.common.registry.LanguageRegistry;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import ru.stalcraft.StalkerMain;
import ru.stalcraft.player.PlayerInfo;
import ru.stalcraft.player.PlayerUtils;

public class ItemMedicine extends yc {
   private int heal;
   private int[] contaminationModifiers;
   private boolean removePoison;
   private List description;
   private List info;
   private String textureName;
   public final String sound;
   public int poisonProtect;
   public int coolDown;
   public int regeneration;
   public int duration;

   public ItemMedicine(
      int id,
      String textureName,
      String localizedName,
      int heal,
      int[] contaminationModifiers,
      boolean removePoison,
      int poisonProtect,
      String sound,
      int coolDown,
      List description,
      int regeneration,
      int duration
   ) {
      super(id);
      this.heal = heal;
      this.contaminationModifiers = contaminationModifiers;
      this.removePoison = removePoison;
      this.poisonProtect = poisonProtect;
      this.description = description;
      this.regeneration = regeneration;
      this.duration = duration;
      this.info = this.getAdditionalLines();
      this.textureName = textureName;
      this.sound = "stalker:" + sound;
      this.a(StalkerMain.tab);
      this.b(textureName);
      this.coolDown = coolDown;
      LanguageRegistry.addName(this, localizedName);
   }

   private List getAdditionalLines() {
      ArrayList lines = new ArrayList();
      a green_color = a.c;
      a red_color = a.c;
      NumberFormat nf = NumberFormat.getInstance();
      nf.setMaximumFractionDigits(3);
      this.duration /= 20;
      lines.add(green_color + "Время действия: " + nf.format((long)this.duration) + " сек.");
      if (this.heal != 0) {
         lines.add(green_color + "Мгновенное лечение: " + this.heal);
      }

      if (this.contaminationModifiers[0] != 0) {
         lines.add(green_color + "Радиация: " + this.getParametrValue(this.contaminationModifiers[0], false) + this.contaminationModifiers[0]);
      }

      if (this.contaminationModifiers[1] != 0) {
         lines.add(green_color + "Термозащита: " + this.getParametrValue(this.contaminationModifiers[1], true) + this.contaminationModifiers[1] * 5);
      }

      if (this.contaminationModifiers[2] != 0) {
         lines.add(green_color + "Биозащита: " + this.getParametrValue(this.contaminationModifiers[2], true) + this.contaminationModifiers[2] * 5);
      }

      if (this.contaminationModifiers[3] < 0) {
         lines.add(green_color + "Пси-атака: " + this.getParametrValue(this.contaminationModifiers[3], false) + this.contaminationModifiers[3] * 5);
      }

      if (this.poisonProtect > 0) {
         lines.add(green_color + "Защита от кровотечения: " + this.getParametrValue(this.poisonProtect, true) + this.poisonProtect * 10 + "%");
      }

      lines.add(
         green_color
            + (this.regeneration > 0 ? "Регенерация: " : "Урон в секунду: ")
            + this.getParametrValue(this.regeneration, true)
            + this.regeneration * 0.5F
            + "%"
      );
      return lines;
   }

   public void a(ye stack, uf player, List list, boolean inUse) {
      list.addAll(this.description);
      list.addAll(this.info);
   }

   public void useHealing(uf player) {
      player.f(this.heal);
      PlayerInfo info = PlayerUtils.getInfo(player);

      for (int i = 0; i < 4; i++) {
         info.cont.removeEffect(i, -this.contaminationModifiers[i]);
      }

      if (this.removePoison) {
         player.k(19);
      }
   }

   public void a(mt par1IconRegister) {
      super.cz = par1IconRegister.a("stalker:" + this.textureName);
   }

   public String getParametrValue(int value, boolean validate) {
      return value > 0 ? (validate ? "+" : "-") : (validate ? "-" : "+");
   }
}
