
package com.jme3.cursors.plugins;

import java.nio.IntBuffer;

/**
 * A Jme representation of the LWJGL Cursor class.
 *
 * Created Jun 6, 2012 12:12:38 PM
 * 
 * @author MadJack
 */
public class JmeCursor {

    private int width;
    private int height;
    private int xHotSpot;
    private int yHotSpot;
    private int numImages;
    private IntBuffer imagesData;
    private IntBuffer imagesDelay;

    /**
     * Queries the cursor's height. Note that
     * the coordinate system is the same as OpenGL. 0, 0 being lower left.
     * @return the height in pixel.
     */
    public int getHeight() {
        return height;
    }

    /**
     * Queries the cursor's images' data.
     * @return An {@link IntBuffer} containing the cursor's image(s) data in
     * sequence.
     */
    public IntBuffer getImagesData() {
        return imagesData;
    }

    /**
     * Queries the cursor's delay for each frame.
     * @return An {@link IntBuffer} containing the cursor's delay in
     * sequence. The delay is expressed in milliseconds.
     */
    public IntBuffer getImagesDelay() {
        return imagesDelay;
    }

    /**
     * Queries the number of images contained in the cursor. Static cursors should
     * contain only 1 image.
     * @return The number of image(s) composing the cursor. 1 if the cursor is
     * static.
     */
    public int getNumImages() {
        return numImages;
    }

    /**
     * Queries the cursor's width. Note that
     * the coordinate system is the same as OpenGL. 0, 0 being lower left.
     * @return the width of the cursor in pixel.
     */
    public int getWidth() {
        return width;
    }

    /**
     * Queries the cursor's X hotspot coordinate. Note that
     * the coordinate system is the same as OpenGL. 0, 0 being lower left.
     * @return the coordinate on the cursor's X axis where the hotspot is located.
     */
    public int getXHotSpot() {
        return xHotSpot;
    }

    /**
     * Queries the cursor's Y hotspot coordinate. Note that
     * the coordinate system is the same as OpenGL. 0, 0 being lower left.
     * @return the coordinate on the cursor's Y axis where the hotspot is located.
     */
    public int getYHotSpot() {
        return yHotSpot;
    }

    /**
     * Sets the cursor's height.
     * @param height The height of the cursor in pixels. Note that all images
     * in a cursor have to be the same size.
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * Sets the cursor's image(s) data. Each image data should be consecutively
     * stored in the {@link IntBuffer} if more tha one image is contained in the
     * cursor.
     * @param imagesData the cursor's image(s) data. Each image data should be consecutively
     * stored in the {@link IntBuffer} if more than one image is contained in the
     * cursor.
     */
    public void setImagesData(IntBuffer imagesData) {
        this.imagesData = imagesData;
    }

    /**
     * Sets the cursor image delay for each frame of an animated cursor. If the
     * cursor has no animation and consist of only 1 image, null is expected.
     *
     * @param imagesDelay the desired delay amount for each frame
     */
    public void setImagesDelay(IntBuffer imagesDelay) {
        this.imagesDelay = imagesDelay;
    }

    /**
     * Sets the number of images in the cursor.
     * @param numImages number of images in the cursor.
     */
    public void setNumImages(int numImages) {
        this.numImages = numImages;
    }

    /**
     * Sets the cursor's width.
     * @param width The width of the cursor in pixels. Note that all images
     * in a cursor have to be the same size.
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * Sets the cursor's X coordinate for its hotspot.
     * @param xHotSpot the cursor's X axis coordinate for its hotspot. Note that
     * the coordinate system is the same as OpenGL. 0, 0 being lower left.
     */
    public void setxHotSpot(int xHotSpot) {
        this.xHotSpot = xHotSpot;
    }

    /**
     * Sets the cursor's Y axis coordinate for its hotspot.
     * @param yHotSpot the cursor's Y axis coordinate for its hotspot. Note that
     * the coordinate system is the same as OpenGL. 0, 0 being lower left.
     */
    public void setyHotSpot(int yHotSpot) {
        this.yHotSpot = yHotSpot;
    }


}
