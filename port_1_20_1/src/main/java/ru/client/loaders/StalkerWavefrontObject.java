package ru.stalcraft.client.loaders;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraftforge.client.model.IModelCustom;
import net.minecraftforge.client.model.ModelFormatException;
import net.minecraftforge.client.model.obj.Face;
import net.minecraftforge.client.model.obj.GroupObject;
import net.minecraftforge.client.model.obj.TextureCoordinate;
import net.minecraftforge.client.model.obj.Vertex;

@SideOnly(Side.CLIENT)
public class StalkerWavefrontObject implements IModelCustom {
   private static Pattern face_V_VT_VN_Pattern = Pattern.compile("(p( \\d+/\\d+/\\d+){3,4} *\\n)|(p( \\d+/\\d+/\\d+){3,4} *$)");
   private static Pattern face_V_VT_Pattern = Pattern.compile("(p( \\d+/\\d+){3,4} *\\n)|(p( \\d+/\\d+){3,4} *$)");
   private static Pattern face_V_VN_Pattern = Pattern.compile("(p( \\d+//\\d+){3,4} *\\n)|(p( \\d+//\\d+){3,4} *$)");
   private static Pattern face_V_Pattern = Pattern.compile("(p( \\d+){3,4} *\\n)|(p( \\d+){3,4} *$)");
   private static Pattern objvertexPattern = Pattern.compile("(v( (\\-){0,1}\\d+\\.\\d+){3,4} *\\n)|(v( (\\-){0,1}\\d+\\.\\d+){3,4} *$)");
   private static Pattern objvertexNormalPattern = Pattern.compile("(vn( (\\-){0,1}\\d+\\.\\d+){3,4} *\\n)|(vn( (\\-){0,1}\\d+\\.\\d+){3,4} *$)");
   private static Pattern objtextureCoordinatePattern = Pattern.compile("(vt( (\\-){0,1}\\d+\\.\\d+){2,3} *\\n)|(vt( (\\-){0,1}\\d+\\.\\d+){2,3} *$)");
   private static Pattern objface_V_VT_VN_Pattern = Pattern.compile("(f( \\d+/\\d+/\\d+){3,4} *\\n)|(f( \\d+/\\d+/\\d+){3,4} *$)");
   private static Pattern objface_V_VT_Pattern = Pattern.compile("(f( \\d+/\\d+){3,4} *\\n)|(f( \\d+/\\d+){3,4} *$)");
   private static Pattern objface_V_VN_Pattern = Pattern.compile("(f( \\d+//\\d+){3,4} *\\n)|(f( \\d+//\\d+){3,4} *$)");
   private static Pattern objface_V_Pattern = Pattern.compile("(f( \\d+){3,4} *\\n)|(f( \\d+){3,4} *$)");
   private static Pattern groupObjectPattern = Pattern.compile("([go]( [\\w\\d]+) *\\n)|([go]( [\\w\\d]+) *$)");
   private static Matcher vertexMatcher;
   private static Matcher vertexNormalMatcher;
   private static Matcher textureCoordinateMatcher;
   private static Matcher face_V_VT_VN_Matcher;
   private static Matcher face_V_VT_Matcher;
   private static Matcher face_V_Matcher;
   private static Matcher groupObjectMatcher;
   public ArrayList vertices = new ArrayList();
   public ArrayList vertexNormals = new ArrayList();
   public ArrayList textureCoordinates = new ArrayList();
   public ArrayList groupObjects = new ArrayList();
   private GroupObject currentGroupObject;
   private String fileName;

   public StalkerWavefrontObject(String fileName, URL resource) throws ModelFormatException {
      this.fileName = fileName;

      try {
         this.loadObjModel(resource.openStream());
      } catch (IOException var4) {
         throw new ModelFormatException("IO Exception reading model format", var4);
      }
   }

   public StalkerWavefrontObject(String filename, InputStream inputStream) throws ModelFormatException {
      this.fileName = filename;
      this.loadObjModel(inputStream);
   }

   private void loadObjModel(InputStream inputStream) throws ModelFormatException {
      BufferedReader reader = null;
      String currentLine = null;
      int lineCount = 0;
      String[] vertexKey = new String[]{"mnsdf ", "kundn ", "kdybs ", "kdhsbb "};
      String[] vertexNormalKey = new String[]{"mnsdff ", "kunddn ", "kdybbs ", "kdhssbb "};
      String[] textureCoordinate = new String[]{"mmnsdff ", "kuunddn ", "kdyybbs ", "kdhhssbb "};

      try {
         reader = new BufferedReader(new InputStreamReader(inputStream));

         while ((currentLine = reader.readLine()) != null) {
            lineCount++;
            currentLine = currentLine.replaceAll("\\s+", " ").trim();
            if (!currentLine.startsWith("#") && currentLine.length() != 0) {
               if (currentLine.startsWith("m ")
                  || currentLine.startsWith("v ")
                  || currentLine.startsWith(vertexKey[0])
                  || currentLine.startsWith(vertexKey[1])
                  || currentLine.startsWith(vertexKey[2])
                  || currentLine.startsWith(vertexKey[3])) {
                  Vertex e = this.parseVertex(currentLine, lineCount);
                  if (e != null) {
                     this.vertices.add(e);
                  }
               } else if (currentLine.startsWith("mn ")
                  || currentLine.startsWith("vn ")
                  || currentLine.startsWith(vertexNormalKey[0])
                  || currentLine.startsWith(vertexNormalKey[1])
                  || currentLine.startsWith(vertexNormalKey[2])
                  || currentLine.startsWith(vertexNormalKey[3])) {
                  Vertex e = this.parseVertexNormal(currentLine, lineCount);
                  if (e != null) {
                     this.vertexNormals.add(e);
                  }
               } else if (currentLine.startsWith("mt ")
                  || currentLine.startsWith("vt ")
                  || currentLine.startsWith(textureCoordinate[0])
                  || currentLine.startsWith(textureCoordinate[1])
                  || currentLine.startsWith(textureCoordinate[2])
                  || currentLine.startsWith(textureCoordinate[3])) {
                  TextureCoordinate var18 = this.parseTextureCoordinate(currentLine, lineCount);
                  if (var18 != null) {
                     this.textureCoordinates.add(var18);
                  }
               } else if (!currentLine.startsWith("p ") && !currentLine.startsWith("f ")) {
                  if (currentLine.startsWith("g ") | currentLine.startsWith("o ")) {
                     GroupObject var20x = this.parseGroupObject(currentLine, lineCount);
                     if (var20x != null && this.currentGroupObject != null) {
                        this.groupObjects.add(this.currentGroupObject);
                     }

                     this.currentGroupObject = var20x;
                  }
               } else {
                  if (this.currentGroupObject == null) {
                     this.currentGroupObject = new GroupObject("Default");
                  }

                  Face var19 = this.parseFace(currentLine, lineCount);
                  if (var19 != null) {
                     this.currentGroupObject.faces.add(var19);
                  }
               }
            }
         }

         this.groupObjects.add(this.currentGroupObject);
      } catch (IOException var201) {
         throw new ModelFormatException("IO Exception reading model format", var201);
      } finally {
         try {
            reader.close();
         } catch (IOException var19) {
         }

         try {
            inputStream.close();
         } catch (IOException var18) {
         }
      }
   }

   public void renderAll() {
      bfq tessellator = bfq.a;
      if (this.currentGroupObject != null) {
         tessellator.b(this.currentGroupObject.glDrawingMode);
      } else {
         tessellator.b(4);
      }

      this.tessellateAll(tessellator);
      tessellator.a();
   }

   public void tessellateAll(bfq tessellator) {
      for (GroupObject groupObject : this.groupObjects) {
         groupObject.render(tessellator);
      }
   }

   public void renderOnly(String... groupNames) {
      for (GroupObject groupObject : this.groupObjects) {
         for (String groupName : groupNames) {
            if (groupName.equalsIgnoreCase(groupObject.name)) {
               groupObject.render();
            }
         }
      }
   }

   public void tessellateOnly(bfq tessellator, String... groupNames) {
      for (GroupObject groupObject : this.groupObjects) {
         for (String groupName : groupNames) {
            if (groupName.equalsIgnoreCase(groupObject.name)) {
               groupObject.render(tessellator);
            }
         }
      }
   }

   public void renderPart(String partName) {
      for (GroupObject groupObject : this.groupObjects) {
         if (partName.equalsIgnoreCase(groupObject.name)) {
            groupObject.render();
         }
      }
   }

   public void tessellatePart(bfq tessellator, String partName) {
      for (GroupObject groupObject : this.groupObjects) {
         if (partName.equalsIgnoreCase(groupObject.name)) {
            groupObject.render(tessellator);
         }
      }
   }

   public void renderAllExcept(String... excludedGroupNames) {
      for (GroupObject groupObject : this.groupObjects) {
         boolean skipPart = false;

         for (String excludedGroupName : excludedGroupNames) {
            if (excludedGroupName.equalsIgnoreCase(groupObject.name)) {
               skipPart = true;
            }
         }

         if (!skipPart) {
            groupObject.render();
         }
      }
   }

   public void tessellateAllExcept(bfq tessellator, String... excludedGroupNames) {
      for (GroupObject groupObject : this.groupObjects) {
         boolean exclude = false;

         for (String excludedGroupName : excludedGroupNames) {
            if (excludedGroupName.equalsIgnoreCase(groupObject.name)) {
               exclude = true;
            }
         }

         if (!exclude) {
            groupObject.render(tessellator);
         }
      }
   }

   private Vertex parseVertex(String line, int lineCount) throws ModelFormatException {
      Object vertex = null;
      line = line.substring(line.indexOf(" ") + 1);
      String[] tokens = line.split(" ");

      try {
         return (Vertex)(
            tokens.length == 2
               ? new Vertex(Float.parseFloat(tokens[0]), Float.parseFloat(tokens[1]))
               : (tokens.length == 3 ? new Vertex(Float.parseFloat(tokens[0]), Float.parseFloat(tokens[1]), Float.parseFloat(tokens[2])) : vertex)
         );
      } catch (NumberFormatException var6) {
         throw new ModelFormatException(String.format("Number formatting error at line %d", lineCount), var6);
      }
   }

   private Vertex parseVertexNormal(String line, int lineCount) throws ModelFormatException {
      Object vertexNormal = null;
      line = line.substring(line.indexOf(" ") + 1);
      String[] tokens = line.split(" ");
      return (Vertex)(tokens.length == 3 ? new Vertex(Float.parseFloat(tokens[0]), Float.parseFloat(tokens[1]), Float.parseFloat(tokens[2])) : vertexNormal);
   }

   private TextureCoordinate parseTextureCoordinate(String line, int lineCount) throws ModelFormatException {
      Object textureCoordinate = null;
      line = line.substring(line.indexOf(" ") + 1);
      String[] tokens = line.split(" ");

      try {
         return (TextureCoordinate)(
            tokens.length == 2
               ? new TextureCoordinate(Float.parseFloat(tokens[0]), 1.0F - Float.parseFloat(tokens[1]))
               : (
                  tokens.length == 3
                     ? new TextureCoordinate(Float.parseFloat(tokens[0]), 1.0F - Float.parseFloat(tokens[1]), Float.parseFloat(tokens[2]))
                     : textureCoordinate
               )
         );
      } catch (NumberFormatException var6) {
         throw new ModelFormatException(String.format("Number formatting error at line %d", lineCount), var6);
      }
   }

   private Face parseFace(String line, int lineCount) throws ModelFormatException {
      Face face = null;
      face = new Face();
      String trimmedLine = line.substring(line.indexOf(" ") + 1);
      String[] tokens = trimmedLine.split(" ");
      String[] subTokens = null;
      if (tokens.length == 3) {
         if (this.currentGroupObject.glDrawingMode == -1) {
            this.currentGroupObject.glDrawingMode = 4;
         } else if (this.currentGroupObject.glDrawingMode != 4) {
            throw new ModelFormatException(
               "Error parsing entry ('"
                  + line
                  + "', line "
                  + lineCount
                  + ") in file '"
                  + this.fileName
                  + "' - Invalid number of points for face (expected 4, found "
                  + tokens.length
                  + ")"
            );
         }
      } else if (tokens.length == 4) {
         if (this.currentGroupObject.glDrawingMode == -1) {
            this.currentGroupObject.glDrawingMode = 7;
         } else if (this.currentGroupObject.glDrawingMode != 7) {
            throw new ModelFormatException(
               "Error parsing entry ('"
                  + line
                  + "', line "
                  + lineCount
                  + ") in file '"
                  + this.fileName
                  + "' - Invalid number of points for face (expected 3, found "
                  + tokens.length
                  + ")"
            );
         }
      }

      if (isValidFace_V_VT_VN_Line(line)) {
         face.vertices = new Vertex[tokens.length];
         face.textureCoordinates = new TextureCoordinate[tokens.length];
         face.vertexNormals = new Vertex[tokens.length];

         for (int i = 0; i < tokens.length; i++) {
            subTokens = tokens[i].split("/");
            face.vertices[i] = (Vertex)this.vertices.get(Integer.parseInt(subTokens[0]) - 1);
            face.textureCoordinates[i] = (TextureCoordinate)this.textureCoordinates.get(Integer.parseInt(subTokens[1]) - 1);
            face.vertexNormals[i] = (Vertex)this.vertexNormals.get(Integer.parseInt(subTokens[2]) - 1);
         }

         face.faceNormal = face.calculateFaceNormal();
      } else if (isValidFace_V_VT_Line(line)) {
         face.vertices = new Vertex[tokens.length];
         face.textureCoordinates = new TextureCoordinate[tokens.length];

         for (int i = 0; i < tokens.length; i++) {
            subTokens = tokens[i].split("/");
            face.vertices[i] = (Vertex)this.vertices.get(Integer.parseInt(subTokens[0]) - 1);
            face.textureCoordinates[i] = (TextureCoordinate)this.textureCoordinates.get(Integer.parseInt(subTokens[1]) - 1);
         }

         face.faceNormal = face.calculateFaceNormal();
      } else if (isValidFace_V_VN_Line(line)) {
         face.vertices = new Vertex[tokens.length];
         face.vertexNormals = new Vertex[tokens.length];

         for (int i = 0; i < tokens.length; i++) {
            subTokens = tokens[i].split("//");
            face.vertices[i] = (Vertex)this.vertices.get(Integer.parseInt(subTokens[0]) - 1);
            face.vertexNormals[i] = (Vertex)this.vertexNormals.get(Integer.parseInt(subTokens[1]) - 1);
         }

         face.faceNormal = face.calculateFaceNormal();
      } else {
         if (!isValidFace_V_Line(line)) {
            throw new ModelFormatException("Error parsing entry ('" + line + "', line " + lineCount + ") in file '" + this.fileName + "' - Incorrect format");
         }

         face.vertices = new Vertex[tokens.length];

         for (int i = 0; i < tokens.length; i++) {
            face.vertices[i] = (Vertex)this.vertices.get(Integer.parseInt(tokens[i]) - 1);
         }

         face.faceNormal = face.calculateFaceNormal();
      }

      return face;
   }

   private GroupObject parseGroupObject(String line, int lineCount) throws ModelFormatException {
      GroupObject group = null;
      if (isValidGroupObjectLine(line)) {
         String trimmedLine = line.substring(line.indexOf(" ") + 1);
         if (trimmedLine.length() > 0) {
            group = new GroupObject(trimmedLine);
         }

         return group;
      } else {
         throw new ModelFormatException("Error parsing entry ('" + line + "', line " + lineCount + ") in file '" + this.fileName + "' - Incorrect format");
      }
   }

   private static boolean isValidFace_V_VT_VN_Line(String line) {
      return face_V_VT_VN_Pattern.matcher(line).matches() || objface_V_VT_VN_Pattern.matcher(line).matches();
   }

   private static boolean isValidFace_V_VT_Line(String line) {
      return face_V_VT_Pattern.matcher(line).matches() || objface_V_VT_Pattern.matcher(line).matches();
   }

   private static boolean isValidFace_V_VN_Line(String line) {
      return face_V_VN_Pattern.matcher(line).matches() || objface_V_VN_Pattern.matcher(line).matches();
   }

   private static boolean isValidFace_V_Line(String line) {
      return face_V_Pattern.matcher(line).matches() || objface_V_Pattern.matcher(line).matches();
   }

   private static boolean isValidFaceLine(String line) {
      return isValidFace_V_VT_VN_Line(line) || isValidFace_V_VT_Line(line) || isValidFace_V_VN_Line(line) || isValidFace_V_Line(line);
   }

   private static boolean isValidGroupObjectLine(String line) {
      return groupObjectPattern.matcher(line).matches();
   }

   public String getType() {
      return "obj";
   }
}
