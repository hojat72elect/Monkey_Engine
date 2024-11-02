
package com.jme3.texture;

import com.jme3.texture.image.ColorSpace;
import com.jme3.util.BufferUtils;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import org.junit.Test;

/**
 * Verify that setMultiSamples(1) can be applied to any Image. This was issue
 * #2250 at GitHub.
 *
 * @author Stephen Gold
 */
public class TestIssue2250 {

    /**
     * Test setMultiSamples() on an Image with a data buffer.
     */
    @Test
    public void testIssue2250WithData() {
        int width = 8;
        int height = 8;
        int numBytes = 4 * width * height;
        ByteBuffer data = BufferUtils.createByteBuffer(numBytes);
        Image image1 = new Image(
                Image.Format.RGBA8, width, height, data, ColorSpace.Linear);

        image1.setMultiSamples(1);
    }

    /**
     * Test setMultiSamples() on an Image with mip maps.
     */
    @Test
    public void testIssue2250WithMips() {
        int width = 8;
        int height = 8;
        int depth = 1;
        int[] mipMapSizes = {256, 64, 16, 4};

        ArrayList<ByteBuffer> data = new ArrayList<>();
        Image image2 = new Image(Image.Format.RGBA8, width, height, depth, data,
                mipMapSizes, ColorSpace.Linear);

        image2.setMultiSamples(1);
    }
}
