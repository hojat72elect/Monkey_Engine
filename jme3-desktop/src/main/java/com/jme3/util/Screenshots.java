
package com.jme3.util;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;
import java.awt.image.WritableRaster;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public final class Screenshots {
    
    /**
     * A private constructor to inhibit instantiation of this class.
     */
    private Screenshots() {
    }

    public static void convertScreenShot2(IntBuffer bgraBuf, BufferedImage out){
        WritableRaster wr = out.getRaster();
        DataBufferInt db = (DataBufferInt) wr.getDataBuffer();
        
        int[] cpuArray = db.getData();
        
        bgraBuf.clear();
        bgraBuf.get(cpuArray);
        
//        int width  = wr.getWidth();
//        int height = wr.getHeight();
//
//        // flip the components the way AWT likes them
//        for (int y = 0; y < height / 2; y++){
//            for (int x = 0; x < width; x++){
//                int inPtr  = (y * width + x);
//                int outPtr = ((height-y-1) * width + x);
//                int pixel = cpuArray[inPtr];
//                cpuArray[inPtr] = cpuArray[outPtr];
//                cpuArray[outPtr] = pixel;
//            }
//        }
    }
    
    /**
     * Flips the image along the Y axis and converts BGRA to ABGR
     * 
     * @param bgraBuf (not null, modified)
     * @param out (not null) 
     */
    public static void convertScreenShot(ByteBuffer bgraBuf, BufferedImage out){
        WritableRaster wr = out.getRaster();
        DataBufferByte db = (DataBufferByte) wr.getDataBuffer();

        byte[] cpuArray = db.getData();

        // copy native memory to java memory
        bgraBuf.clear();
        bgraBuf.get(cpuArray);
        bgraBuf.clear();

        int width  = wr.getWidth();
        int height = wr.getHeight();

        // flip the components the way AWT likes them
        
        // calculate half of height such that all rows of the array are written to
        // e.g. for odd heights, write 1 more scanline
        int heightdiv2ceil = height % 2 == 1 ? (height / 2) + 1 : height / 2;
        for (int y = 0; y < heightdiv2ceil; y++){
            for (int x = 0; x < width; x++){
                int inPtr  = (y * width + x) * 4;
                int outPtr = ((height-y-1) * width + x) * 4;

                byte b1 = cpuArray[inPtr+0];
                byte g1 = cpuArray[inPtr+1];
                byte r1 = cpuArray[inPtr+2];
                byte a1 = cpuArray[inPtr+3];

                byte b2 = cpuArray[outPtr+0];
                byte g2 = cpuArray[outPtr+1];
                byte r2 = cpuArray[outPtr+2];
                byte a2 = cpuArray[outPtr+3];

                cpuArray[outPtr+0] = a1;
                cpuArray[outPtr+1] = b1;
                cpuArray[outPtr+2] = g1;
                cpuArray[outPtr+3] = r1;

                cpuArray[inPtr+0] = a2;
                cpuArray[inPtr+1] = b2;
                cpuArray[inPtr+2] = g2;
                cpuArray[inPtr+3] = r2;
            }
        }
    }
}
