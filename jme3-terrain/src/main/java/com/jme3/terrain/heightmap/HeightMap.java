
package com.jme3.terrain.heightmap;

/**
 *
 * @author cghislai
 */
public interface HeightMap {

    /**
     * <code>getHeightMap</code> returns the entire grid of height data.
     *
     * @return the grid of height data.
     */
    float[] getHeightMap();

    float[] getScaledHeightMap();

    /**
     * <code>getInterpolatedHeight</code> returns the height of a point that
     * does not fall directly on the height posts.
     *
     * @param x
     * the x coordinate of the point.
     * @param z
     * the y coordinate of the point.
     * @return the interpolated height at this point.
     */
    float getInterpolatedHeight(float x, float z);

    /**
     * <code>getScaledHeightAtPoint</code> returns the scaled value at the
     * point provided.
     *
     * @param x
     * the x (east/west) coordinate.
     * @param z
     * the z (north/south) coordinate.
     * @return the scaled value at (x, z).
     */
    float getScaledHeightAtPoint(int x, int z);

    /**
     * <code>getSize</code> returns the size of one side the height map. Where
     * the area of the height map is size x size.
     *
     * @return the size of a single side.
     */
    int getSize();

    /**
     * <code>getTrueHeightAtPoint</code> returns the non-scaled value at the
     * point provided.
     *
     * @param x
     * the x (east/west) coordinate.
     * @param z
     * the z (north/south) coordinate.
     * @return the value at (x,z).
     */
    float getTrueHeightAtPoint(int x, int z);

    /**
     * <code>load</code> populates the height map data. This is dependent on
     * the subclass's implementation.
     *
     * @return true if the load was successful, false otherwise.
     */
    boolean load();

    /**
     * <code>setHeightAtPoint</code> sets the height value for a given
     * coordinate. It is recommended that the height value be within the 0 - 255
     * range.
     *
     * @param height
     * the new height for the coordinate.
     * @param x
     * the x (east/west) coordinate.
     * @param z
     * the z (north/south) coordinate.
     */
    void setHeightAtPoint(float height, int x, int z);

    /**
     * <code>setHeightScale</code> sets the scale of the height values.
     * Typically, the height is a little too extreme and should be scaled to a
     * smaller value (i.e. 0.25), to produce cleaner slopes.
     *
     * @param scale
     * the scale to multiply height values by.
     */
    void setHeightScale(float scale);

    /**
     * <code>setFilter</code> sets the erosion value for the filter. This
     * value must be between 0 and 1, where 0.2 - 0.4 produces arguably the best
     * results.
     *
     * @param filter
     * the erosion value.
     * @throws Exception
     * if filter is less than 0 or greater than 1.
     */
    void setMagnificationFilter(float filter) throws Exception;

    /**
     * <code>setSize</code> sets the size of the terrain where the area is
     * size x size.
     *
     * @param size
     * the new size of the terrain.
     * @throws Exception
     * if the size is less than or equal to zero.
     */
    void setSize(int size) throws Exception;

    /**
     * <code>unloadHeightMap</code> clears the data of the height map. This
     * insures it is ready for reloading.
     */
    void unloadHeightMap();

}
