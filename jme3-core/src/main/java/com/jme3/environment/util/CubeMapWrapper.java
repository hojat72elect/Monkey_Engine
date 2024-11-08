
package com.jme3.environment.util;

import com.jme3.math.*;
import com.jme3.texture.Image;
import com.jme3.texture.TextureCubeMap;
import com.jme3.texture.image.DefaultImageRaster;
import com.jme3.texture.image.MipMapImageRaster;
import com.jme3.util.BufferUtils;

import static com.jme3.math.FastMath.pow;

/**
 * Wraps a cube map and allows reading or writing pixels.
 * 
 * It uses the ImageRaster class to tailor the read/write operations.
 * 
 * @author Nehon
 */
public class CubeMapWrapper {

    private MipMapImageRaster mipMapRaster;
    private final DefaultImageRaster raster;
    private int[] sizes;
    private final Vector2f uvs = new Vector2f();
    private final Image image;

    private final ColorRGBA tmpColor = new ColorRGBA();

    /**
     * Creates a CubeMapWrapper for the given cube map
     * Note that the cube map must be initialized, and the mipmaps sizes should 
     * be set if relevant for them to be readable/writable
     * @param cubeMap the cubemap to wrap.
     */
    public CubeMapWrapper(TextureCubeMap cubeMap) {
        image = cubeMap.getImage();
        if (image.hasMipmaps()) {
            int nbMipMaps = image.getMipMapSizes().length;
            sizes = new int[nbMipMaps];
            mipMapRaster = new MipMapImageRaster(image, 0);

            for (int i = 0; i < nbMipMaps; i++) {
                sizes[i] = Math.max(1, image.getWidth() >> i);
            }
        } else {
            sizes = new int[1];
            sizes[0] = image.getWidth();
        }
        raster = new DefaultImageRaster(image, 0,0 , false);
    }

    /**
     * Reads a pixel from the cube map given the coordinate vector
     * @param vector the direction vector to fetch the texel
     * @param store the color in which to store the pixel color read.
     * @return the color of the pixel read.
     */
    public ColorRGBA getPixel(Vector3f vector, ColorRGBA store) {

        if (store == null) {
            store = new ColorRGBA();
        }

        int face = EnvMapUtils.getCubemapFaceTexCoordFromVector(vector, sizes[0], uvs, EnvMapUtils.FixSeamsMethod.Stretch);
        raster.setSlice(face);
        return raster.getPixel((int) uvs.x, (int) uvs.y, store);
    }

    /**
     * 
     * Reads a pixel from the cube map given the coordinate vector
     * @param vector the direction vector to fetch the texel
     * @param mipLevel the mip level to read from
     * @param store the color in which to store the pixel color read.
     * @return the color of the pixel read.
     */
    public ColorRGBA getPixel(Vector3f vector, float mipLevel, ColorRGBA store) {
        if (mipMapRaster == null) {
            throw new IllegalArgumentException("This cube map has no mip maps");
        }
        if (store == null) {
            store = new ColorRGBA();
        }

        int lowerMipLevel = (int) mipLevel;
        int higherMipLevel = (int) FastMath.ceil(mipLevel);
        float ratio = mipLevel - lowerMipLevel;

        int face = EnvMapUtils.getCubemapFaceTexCoordFromVector(vector, sizes[lowerMipLevel], uvs, EnvMapUtils.FixSeamsMethod.Stretch);
        mipMapRaster.setSlice(face);
        mipMapRaster.setMipLevel(lowerMipLevel);
        mipMapRaster.getPixel((int) uvs.x, (int) uvs.y, store);

        face = EnvMapUtils.getCubemapFaceTexCoordFromVector(vector, sizes[higherMipLevel], uvs, EnvMapUtils.FixSeamsMethod.Stretch);
        mipMapRaster.setSlice(face);
        mipMapRaster.setMipLevel(higherMipLevel);
        mipMapRaster.getPixel((int) uvs.x, (int) uvs.y, tmpColor);

        store.r = FastMath.interpolateLinear(ratio, store.r, tmpColor.r);
        store.g = FastMath.interpolateLinear(ratio, store.g, tmpColor.g);
        store.b = FastMath.interpolateLinear(ratio, store.b, tmpColor.b);
        store.a = FastMath.interpolateLinear(ratio, store.a, tmpColor.a);

        return store;
    }

    /**
     * Reads a pixel from the cube map given the 2D coordinates and the face to read from
     * @param x the x tex coordinate (from 0 to width)
     * @param y the y tex coordinate (from 0 to height)
     * @param face the face to read from
     * @param store the color where the result is stored.
     * @return the color read.
     */
    public ColorRGBA getPixel(int x, int y, int face, ColorRGBA store) {
        if (store == null) {
            store = new ColorRGBA();
        }
        raster.setSlice(face);
        return raster.getPixel(x, y, store);
    }

     /**
     * Reads a pixel from the cube map given the 2D coordinates and the face and 
     * the mip level to read from
     * @param x the x tex coordinate (from 0 to width)
     * @param y the y tex coordinate (from 0 to height)
     * @param face the face to read from
     * @param mipLevel the miplevel to read from
     * @param store the color where the result is stored.
     * @return the color read.
     */
    public ColorRGBA getPixel(int x, int y, int face, int mipLevel, ColorRGBA store) {
        if (mipMapRaster == null) {
            throw new IllegalArgumentException("This cube map has no mip maps");
        }
        if (store == null) {
            store = new ColorRGBA();
        }
        mipMapRaster.setSlice(face);
        mipMapRaster.setMipLevel(mipLevel);
        return mipMapRaster.getPixel(x, y, store);
    }

    /**
     * writes a pixel given the coordinates vector and the color.
     * @param vector the coordinates where to write the pixel
     * @param color the color to write
     */
    public void setPixel(Vector3f vector, ColorRGBA color) {

        int face = EnvMapUtils.getCubemapFaceTexCoordFromVector(vector, sizes[0], uvs, EnvMapUtils.FixSeamsMethod.Stretch);
        raster.setSlice(face);
        raster.setPixel((int) uvs.x, (int) uvs.y, color);
    }
    /**
     * writes a pixel given the coordinates vector, the mip level and the color.
     * @param vector the coordinates where to write the pixel
     * @param mipLevel the miplevel to write to
     * @param color the color to write
     */
    public void setPixel(Vector3f vector, int mipLevel, ColorRGBA color) {
        if (mipMapRaster == null) {
            throw new IllegalArgumentException("This cube map has no mip maps");
        }
        int face = EnvMapUtils.getCubemapFaceTexCoordFromVector(vector, sizes[mipLevel], uvs, EnvMapUtils.FixSeamsMethod.Stretch);
        mipMapRaster.setSlice(face);
        mipMapRaster.setMipLevel(mipLevel);
        mipMapRaster.setPixel((int) uvs.x, (int) uvs.y, color);
    }

    /**
     * Writes a pixel given the 2-D coordinates and the color
     * @param x the x tex coord (from 0 to width)
     * @param y the y tex coord (from 0 to height)
     * @param face the face to write to
     * @param color the color to write
     */
    public void setPixel(int x, int y, int face, ColorRGBA color) {
        raster.setSlice(face);
        raster.setPixel(x, y, color);
    }

    /**
     * Writes a pixel given the 2-D coordinates, the mip level and the color
     * @param x the x tex coord (from 0 to width)
     * @param y the y tex coord (from 0 to height)
     * @param face the face to write to
     * @param mipLevel the mip level to write to
     * @param color the color to write
     */
    public void setPixel(int x, int y, int face, int mipLevel, ColorRGBA color) {
        if (mipMapRaster == null) {
            throw new IllegalArgumentException("This cube map has no mip maps");
        }

        mipMapRaster.setSlice(face);
        mipMapRaster.setMipLevel(mipLevel);
        mipMapRaster.setPixel(x, y, color);
    }

    /**
     * Inits the mip maps of a cube map with the given number of mip maps
     * @param nbMipMaps the number of mip maps to initialize
     */
    public void initMipMaps(int nbMipMaps) {
        int maxMipMap = (int) (Math.log(image.getWidth()) / Math.log(2) + 1);
        if (nbMipMaps > maxMipMap) {
            throw new IllegalArgumentException("Max mip map number for a " + image.getWidth() + "x" + image.getHeight() + " cube map is " + maxMipMap);
        }

        sizes = new int[nbMipMaps];

        int totalSize = 0;
        for (int i = 0; i < nbMipMaps; i++) {
            int size = (int) pow(2, maxMipMap - 1 - i);
            sizes[i] = size * size * image.getFormat().getBitsPerPixel() / 8;
            totalSize += sizes[i];
        }
        
        image.setMipMapSizes(sizes);        
        image.getData().clear();
        for (int i = 0; i < 6; i++) {
            image.addData(BufferUtils.createByteBuffer(totalSize));
        }
        mipMapRaster = new MipMapImageRaster(image, 0);        
    }
}
