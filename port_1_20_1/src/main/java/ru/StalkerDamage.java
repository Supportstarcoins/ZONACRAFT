package ru.stalcraft;

public class StalkerDamage extends nb {
   protected String deathMessage;
   public static nb radiation = new StalkerDamage("radiation", " погиб от радиации").j();
   public static nb chemical = new StalkerDamage("chemical", " погиб от термического заражения").j();
   public static nb biological = new StalkerDamage("biological", " погиб от биологического заражения").j();
   public static nb psy = new StalkerDamage("psy", " погиб от ПСИ-излучения").j();
   public static nb blackhole = new StalkerDamage("blackhole", " разорвало воронкой").j();
   public static nb web = new StalkerDamage("web", " погиб в колючей проволоке").j();
   public static nb ejection = new StalkerDamage("ejection", " погиб от выброса").j();
   public static nb electra = new StalkerDamage("electra", " убило электрой").j();
   public static nb carousel = new StalkerDamage("carousel", " разорвало каруселью").j();
   public static nb coach = new StalkerDamage("coach", " погиб из-за тренера").j();
   public static nb kissel = new StalkerDamage("kissel", " растворился в киселе").j();
   public static nb steam = new StalkerDamage("steam", " сварился в паре").j();
   public static nb trampoline = new StalkerDamage("steam", " разорвало батутом").j();

   protected StalkerDamage(String name, String message) {
      super(name);
      this.deathMessage = message;
   }

   public static nb causeBulletDamage(nn par1) {
      return new EntityDamageSourceBullet("bullet", par1);
   }

   public cv b(of par1) {
      return new cv().a(par1.an() + this.deathMessage);
   }
}
