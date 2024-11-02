
package com.jme3.export;

import com.jme3.asset.AssetLoader;
import com.jme3.asset.AssetManager;

public interface JmeImporter extends AssetLoader {
    public InputCapsule getCapsule(Savable id);
    public AssetManager getAssetManager();
    
    /**
     * Returns the version number written in the header of the J3O/XML
     * file.
     * 
     * @return Global version number for the file
     */
    public int getFormatVersion();
}
