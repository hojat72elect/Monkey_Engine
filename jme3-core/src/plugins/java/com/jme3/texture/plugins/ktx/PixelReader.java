
package com.jme3.texture.plugins.ktx;

import java.io.DataInput;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 *
 * Interface used to read a set of pixels in a KTX file
 * @author Nehon
 */
public interface PixelReader {

    public int readPixels(int pixelWidth, int pixelHeight, byte[] pixelData, ByteBuffer buffer, DataInput in) throws IOException;
}
