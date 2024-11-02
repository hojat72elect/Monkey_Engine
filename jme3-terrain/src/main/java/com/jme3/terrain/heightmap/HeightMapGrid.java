
package com.jme3.terrain.heightmap;

import com.jme3.math.Vector3f;

/**
 *
 * @author Anthyon
 * @deprecated in favor of TerrainGridTileLoader
 */
@Deprecated
public interface HeightMapGrid {

    public HeightMap getHeightMapAt(Vector3f location);

    public void setSize(int size);

}
