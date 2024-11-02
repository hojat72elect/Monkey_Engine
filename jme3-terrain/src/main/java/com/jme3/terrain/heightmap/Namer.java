
package com.jme3.terrain.heightmap;

/**
 *
 * 
 */
public interface Namer {

    /**
     * Gets a name for a heightmap tile given its cell id
     * @param x
     * @param y
     * @return a tile name
     */
    public String getName(int x, int y);

}
