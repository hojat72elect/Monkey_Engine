
package com.jme3.texture.plugins.ktx;

import java.io.DataInput;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * reads the pixels of an image whose origin is the top left corner
 *
 * @author Nehon
 */
public class SrTdRiPixelReader implements PixelReader {

    @Override
    public int readPixels(int pixelWidth, int pixelHeight, byte[] pixelData, ByteBuffer buffer, DataInput in) throws IOException {
        int pixelRead = 0;
        for (int row = pixelHeight - 1; row >= 0; row--) {
            for (int pixel = 0; pixel < pixelWidth; pixel++) {
                in.readFully(pixelData);
                for (int i = 0; i < pixelData.length; i++) {
                    buffer.put((row * pixelWidth + pixel) * pixelData.length + i, pixelData[i]);
                }
                pixelRead += pixelData.length;
            }
        }
        return pixelRead;
    }

}
