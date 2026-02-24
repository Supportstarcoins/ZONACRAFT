package ru.stalcraft.config;

import cpw.mods.fml.relauncher.Side;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import ru.stalcraft.StalkerMain;
import ru.stalcraft.blocks.BlockConfig;
import ru.stalcraft.tile.TileEntityDecorative;

public class Config {
   public List<Class<? extends asp>> tileEntityRendering = new ArrayList<>();
   public HashMap tileEntityModels = new HashMap();

   public void readConfig(String cfgName, Side side) {
      try {
         BufferedReader bufferReader = new BufferedReader(
            new InputStreamReader(StalkerMain.class.getResourceAsStream("/assets/stalker/config/" + cfgName + ".cfg"), "UTF-8")
         );
         String currentLine = null;

         while ((currentLine = bufferReader.readLine()) != null) {
            BufferedReader bufferReaderBlock = new BufferedReader(
               new InputStreamReader(StalkerMain.class.getResourceAsStream("/assets/stalker/config/" + currentLine + ".cfg"), "UTF-8")
            );
            String currentLineBlock = null;
            int id = 0;
            String name = null;
            String model = null;
            String icon = null;
            boolean isModelBlock = false;
            boolean isCollided = true;
            int collidedRendererDistance = 2;

            while ((currentLineBlock = bufferReaderBlock.readLine()) != null) {
               id = this.getInt(currentLineBlock, "id", id);
               name = this.getStr(currentLineBlock, "name", name);
               model = this.getStr(currentLineBlock, "model", model);
               icon = this.getStr(currentLineBlock, "icon", icon);
               isModelBlock = this.getBoolean(currentLineBlock, "isModelBlock", isModelBlock);
               isCollided = this.getBoolean(currentLineBlock, "isCollided", isCollided);
               collidedRendererDistance = this.getInt(currentLineBlock, "collidedRendererDistance", collidedRendererDistance);
            }

            Class tileEntity = TileEntityDecorative.class;
            new BlockConfig(tileEntity, id, name, model, icon, isModelBlock, isCollided, collidedRendererDistance);
            this.tileEntityRendering.add(tileEntity);
            this.tileEntityModels.put(tileEntity, model);
            bufferReaderBlock.close();
         }

         bufferReader.close();
      } catch (Exception var15) {
         var15.printStackTrace();
      }
   }

   public int getInt(String line, String valueName, int value) {
      if (line.startsWith(valueName + ": ")) {
         String[] values = line.split(" ");
         return Integer.parseInt(values[1].trim());
      } else {
         return value;
      }
   }

   public String getStr(String line, String valueName, String value) {
      if (line.startsWith(valueName + ": ")) {
         String[] values = line.split(" ");
         return String.valueOf(values[1].trim());
      } else {
         return value;
      }
   }

   public boolean getBoolean(String line, String valueName, boolean value) {
      if (line.startsWith(valueName + ": ")) {
         String[] values = line.split(" ");
         return Boolean.parseBoolean(values[1].trim());
      } else {
         return value;
      }
   }
}
