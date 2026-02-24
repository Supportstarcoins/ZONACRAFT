package ru.stalcraft.proxy;

public interface IClientProxy extends IProxy {
   void registerRenderers();

   void onFlagGui(int var1, int var2, int var3);

   void onGuiRepair(uf var1, int var2);

   void onBaseWarehouseGui();
}
