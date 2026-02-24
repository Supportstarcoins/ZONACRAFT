package ru.stalcraft.asm;

import java.io.IOException;
import java.io.InputStream;

public class MicInputStream extends InputStream {
   private InputStream baseStream;
   private int byteReading = 0;
   private boolean isReadingMic;
   private final byte[] png = new byte[]{80, 78, 71};
   private final byte[] mic = new byte[]{77, 73, 67};

   public MicInputStream(bjo loc, InputStream baseStream) {
      this.baseStream = baseStream;
      this.isReadingMic = loc.a().matches(".*\\.[mM][iI][cC]$");
   }

   @Override
   public int read() throws IOException {
      int value = this.baseStream.read();
      if (value > -1) {
         if (this.isReadingMic && this.byteReading > 0 && this.byteReading < 4) {
            return this.png[this.byteReading - 1];
         }

         this.byteReading++;
      }

      return value;
   }

   @Override
   public int available() throws IOException {
      return this.baseStream.available();
   }

   @Override
   public void close() throws IOException {
      this.baseStream.close();
   }

   @Override
   public void mark(int readLimit) {
      this.baseStream.mark(readLimit);
   }

   @Override
   public boolean markSupported() {
      return this.baseStream.markSupported();
   }

   @Override
   public int read(byte[] b) throws IOException {
      int value = this.baseStream.read(b);
      if (this.isReadingMic) {
         for (int i = 0; i < value && i < 4; i++) {
            if (i + this.byteReading > 0 && i + this.byteReading < 4 && i > -1 && b.length > i) {
               b[i] = this.png[i - 1 + this.byteReading];
            }
         }
      }

      this.byteReading += value;
      return value;
   }

   @Override
   public int read(byte[] b, int off, int len) throws IOException {
      int value = this.baseStream.read(b, off, len);
      if (this.isReadingMic && this.byteReading < 4) {
         for (int i = 0; i < value && i < 4; i++) {
            if (i + this.byteReading > 0 && i + this.byteReading < 4) {
               int byteToSet = i + off;
               if (byteToSet > -1 && b.length > byteToSet) {
                  b[byteToSet] = this.png[i - 1 + this.byteReading];
               }
            }
         }
      }

      this.byteReading += value;
      return value;
   }

   @Override
   public void reset() throws IOException {
      this.baseStream.reset();
   }

   @Override
   public long skip(long n) throws IOException {
      this.byteReading = (int)(this.byteReading + n);
      return this.baseStream.skip(n);
   }
}
