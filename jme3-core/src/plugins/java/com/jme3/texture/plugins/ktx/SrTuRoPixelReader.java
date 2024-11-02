
package com.jme3.texture.plugins.ktx;

import java.io.DataInput;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * reads the pixels of an image whose origin is the bottom left corner
 *
 * @author Nehon
 */
public class SrTuRoPixelReader implements PixelReader {

    @Override
    public int readPixels(int pixelWidth, int pixelHeight, byte[] pixelData, ByteBuffer buffer, DataInput in) throws IOException {
        int pixelRead = 0;
        for (int row = 0; row < pixelHeight; row++) {
            for (int pixel = 0; pixel < pixelWidth; pixel++) {
                in.readFully(pixelData);
                buffer.put(pixelData);
                pixelRead += pixelData.length;
            }
        }
        return pixelRead;
    }

}
