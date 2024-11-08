
package com.jme3.scene.plugins.gltf;

import com.jme3.asset.ModelKey;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * An optional key to use when loading a glTF file
 * It allows you to specify custom data loader, replacing the default ones.
 *
 * MaterialAdapters: Allows you to map glTF standard material model to a non-stock material.
 * ExtensionLoaders: Allows you to provide or override a loader for a given glTF extension.
 * ExtrasLoader: Allows you to load any extras, application specific data of the glTF file.
 *
 * For more information, please see glTF 2.0 specifications
 * https://github.com/KhronosGroup/glTF/blob/master/specification/2.0/README.md
 *
 * Created by Nehon on 08/08/2017.
 */
public class GltfModelKey extends ModelKey {

    private Map<String, MaterialAdapter> materialAdapters = new HashMap<>();
    private static Map<String, ExtensionLoader> extensionLoaders = new HashMap<>();
    private boolean keepSkeletonPose = false;
    private ExtrasLoader extrasLoader;

    public GltfModelKey(String name) {
        super(name);
    }

    public GltfModelKey() {
    }

    /**
     * Registers a MaterialAdapter for the given materialName.
     * The materialName must be "pbrMetallicRoughness" or any name from KHR_materials glTF Extension (for example "pbrSpecularGlossiness" for "KHR_materials_pbrSpecularGlossiness" extension)
     *
     * @param gltfMaterialName the name of the gltf material
     * @param adapter          the material adapter
     */
    public void registerMaterialAdapter(String gltfMaterialName, MaterialAdapter adapter) {
        materialAdapters.put(gltfMaterialName, adapter);
    }

    /**
     * Registers an extension loader for the given extension name.
     * For more information on extension please see glTF 2.0 extensions registry
     * https://github.com/KhronosGroup/glTF/blob/master/extensions/README.md
     *
     * @param extensionName the name of the extension
     * @param loader        the Extension loader
     */
    public void registerExtensionLoader(String extensionName, ExtensionLoader loader) {
        extensionLoaders.put(extensionName, loader);
    }

    public MaterialAdapter getAdapterForMaterial(String gltfMaterialName) {
        return materialAdapters.get(gltfMaterialName);
    }

    public ExtensionLoader getExtensionLoader(String extensionName) {
        return extensionLoaders.get(extensionName);
    }

    public boolean isKeepSkeletonPose() {
        return keepSkeletonPose;
    }

    public void setKeepSkeletonPose(boolean keepSkeletonPose) {
        this.keepSkeletonPose = keepSkeletonPose;
    }

    public ExtrasLoader getExtrasLoader() {
        return extrasLoader;
    }

    /**
     * Sets the ExtrasLoader for reading any extra information from the gltf file.
     *
     * @param extrasLoader the desired loader
     */
    public void setExtrasLoader(ExtrasLoader extrasLoader) {
        this.extrasLoader = extrasLoader;
    }
    
    @Override
    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }
        if (getClass() != object.getClass()) {
            return false;
        }
        final GltfModelKey other = (GltfModelKey)object;
        if (!super.equals(other)) {
            return false;
        }
        if (!Objects.equals(materialAdapters, other.materialAdapters)
                || !Objects.equals(extrasLoader, other.extrasLoader)) {
            return false;
        }
        return keepSkeletonPose == other.keepSkeletonPose;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + materialAdapters.hashCode();
        hash = 37 * hash + Objects.hashCode(this.extrasLoader);
        hash = 37 * hash + (this.keepSkeletonPose ? 1 : 0);
        return hash;
    }
    
}
