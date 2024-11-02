
package com.jme3.scene.plugins.gltf;

import com.jme3.plugins.json.JsonElement;
import com.jme3.asset.AssetKey;

/**
 * Material adapter for the Unlit pipeline
 * @author Markil 3
 */
public class UnlitExtensionLoader implements ExtensionLoader {

    private final UnlitMaterialAdapter materialAdapter = new UnlitMaterialAdapter();

    @Override
    public Object handleExtension(GltfLoader loader, String parentName, JsonElement parent, JsonElement extension, Object input) {
        MaterialAdapter adapter = materialAdapter;
        AssetKey key = loader.getInfo().getKey();
        //check for a custom adapter for spec/gloss pipeline
        if (key instanceof GltfModelKey) {
            GltfModelKey gltfKey = (GltfModelKey) key;
            MaterialAdapter ma = gltfKey.getAdapterForMaterial("unlit");
            if (ma != null) {
                adapter = ma;
            }
        }

        adapter.init(loader.getInfo().getManager());

        return adapter;
    }
}
