package ru.stalcraft.client.clans;

public class ClientClanCaptureData {
   public String owner;
   public String invader;
   public int captureSize;

   public void parseInfo(String... data) {
      String owner = data[0];
      String invader = data[1];
      String zero = "null";
      if (owner.equals(zero)) {
         this.owner = zero;
      } else if (invader.equals(zero)) {
         this.invader = zero;
      }

      if (!owner.equals(zero)) {
         this.owner = owner;
      } else if (!invader.equals(zero)) {
         this.invader = data[1];
      }
   }
}
