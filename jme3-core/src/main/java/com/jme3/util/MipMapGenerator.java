
package com.jme3.util;

import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.texture.Image;
import com.jme3.texture.image.ImageRaster;

import java.nio.ByteBuffer;
import java.util.ArrayList;

public class MipMapGenerator {

    private MipMapGenerator() {
    }

    public static Image scaleImage(Image inputImage, int outputWidth, int outputHeight) {
        int size = outputWidth * outputHeight * inputImage.getFormat().getBitsPerPixel() / 8;
        ByteBuffer buffer = BufferUtils.createByteBuffer(size);
        Image outputImage = new Image(inputImage.getFormat(),
                                      outputWidth,
                                      outputHeight,
                                      buffer,
                                      inputImage.getColorSpace());

        ImageRaster input = ImageRaster.create(inputImage, 0, 0, false);
        ImageRaster output = ImageRaster.create(outputImage, 0, 0, false);

        float xRatio = ((float) (input.getWidth()  - 1)) / output.getWidth();
        float yRatio = ((float) (input.getHeight() - 1)) / output.getHeight();

        ColorRGBA outputColor = new ColorRGBA(0, 0, 0, 0);
        ColorRGBA bottomLeft = new ColorRGBA();
        ColorRGBA bottomRight = new ColorRGBA();
        ColorRGBA topLeft = new ColorRGBA();
        ColorRGBA topRight = new ColorRGBA();

        for (int y = 0; y < outputHeight; y++) {
            for (int x = 0; x < outputWidth; x++) {
                float x2f = x * xRatio;
                float y2f = y * yRatio;

                int x2 = (int) x2f;
                int y2 = (int) y2f;

                input.getPixel(x2,     y2,     bottomLeft);
                input.getPixel(x2 + 1, y2,     bottomRight);
                input.getPixel(x2,     y2 + 1, topLeft);
                input.getPixel(x2 + 1, y2 + 1, topRight);

                outputColor.set(bottomLeft).addLocal(bottomRight)
                           .addLocal(topLeft).addLocal(topRight);
                outputColor.multLocal(1f / 4f);
                output.setPixel(x, y, outputColor);
            }
        }
        return outputImage;
    }

    public static Image resizeToPowerOf2(Image original){
        int potWidth = FastMath.nearestPowerOfTwo(original.getWidth());
        int potHeight = FastMath.nearestPowerOfTwo(original.getHeight());
        return scaleImage(original, potWidth, potHeight);
    }

    public static void generateMipMaps(Image image){
        int width = image.getWidth();
        int height = image.getHeight();

        Image current = image;
        ArrayList<ByteBuffer> output = new ArrayList<>();
        int totalSize = 0;

        while (height >= 1 || width >= 1){
            output.add(current.getData(0));
            totalSize += current.getData(0).capacity();

            if (height == 1 || width == 1) {
                break;
            }

            height /= 2;
            width  /= 2;

            current = scaleImage(current, width, height);
        }

        ByteBuffer combinedData = BufferUtils.createByteBuffer(totalSize);
        int[] mipSizes = new int[output.size()];
        for (int i = 0; i < output.size(); i++){
            ByteBuffer data = output.get(i);
            data.clear();
            combinedData.put(data);
            mipSizes[i] = data.capacity();
        }
        combinedData.flip();

        // insert mip data into image
        image.setData(0, combinedData);
        image.setMipMapSizes(mipSizes);
    }
}
