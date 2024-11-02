
package com.jme3.scene.plugins.ogre.matext;

import com.jme3.asset.AssetKey;
import com.jme3.material.MaterialList;

/**
 * <code>OgreMaterialKey</code> allows specifying material extensions, which map
 * from Ogre3D base materials to jME3 materials
 */
public class OgreMaterialKey extends AssetKey<MaterialList> {

    private MaterialExtensionSet matExts;

    public OgreMaterialKey(String name) {
        super(name);
    }

    public OgreMaterialKey() {
        super();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final OgreMaterialKey other = (OgreMaterialKey) obj;
        if (!super.equals(other)) {
            return false;
        }
        if (this.matExts != other.matExts && (this.matExts == null || !this.matExts.equals(other.matExts))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 71 * hash + (super.hashCode());
        hash = 71 * hash + (this.matExts != null ? this.matExts.hashCode() : 0);
        return hash;
    }
    
    /**
     * Set the {@link MaterialExtensionSet} to use for mapping base materials to
     * jME3 matdefs when loading. Set to
     * <code>null</code> to disable this functionality.
     *
     * @param matExts The {@link MaterialExtensionSet} to use
     */
    public void setMaterialExtensionSet(MaterialExtensionSet matExts) {
        this.matExts = matExts;
    }

    /**
     * Returns the {@link MaterialExtensionSet} previously set using
     * {@link OgreMaterialKey#setMaterialExtensionSet(com.jme3.scene.plugins.ogre.matext.MaterialExtensionSet)
     * } method.
     *
     * @return the {@link MaterialExtensionSet}
     */
    public MaterialExtensionSet getMaterialExtensionSet() {
        return matExts;
    }
}
