
package com.jme3.scene.plugins.gltf;

import com.jme3.plugins.json.JsonArray;
import com.jme3.plugins.json.JsonElement;
import com.jme3.asset.AssetLoadException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Nehon on 20/08/2017.
 */
public class CustomContentManager {
    static volatile Class<? extends ExtrasLoader> defaultExtraLoaderClass = UserDataLoader.class;
    private ExtrasLoader defaultExtraLoaderInstance;


    private final static Logger logger = Logger.getLogger(CustomContentManager.class.getName());

    private GltfModelKey key;
    private GltfLoader gltfLoader;

    
    static final Map<String, Class<? extends ExtensionLoader>> defaultExtensionLoaders = new ConcurrentHashMap<>();
    static {
        defaultExtensionLoaders.put("KHR_materials_pbrSpecularGlossiness", PBRSpecGlossExtensionLoader.class);
        defaultExtensionLoaders.put("KHR_lights_punctual", LightsPunctualExtensionLoader.class);
        defaultExtensionLoaders.put("KHR_materials_unlit", UnlitExtensionLoader.class);
        defaultExtensionLoaders.put("KHR_texture_transform", TextureTransformExtensionLoader.class);
        defaultExtensionLoaders.put("KHR_materials_emissive_strength", PBREmissiveStrengthExtensionLoader.class);

    }
    
    private final Map<String, ExtensionLoader> loadedExtensionLoaders = new HashMap<>();

    public CustomContentManager() {
    }
    
    /**
     * Returns the default extras loader.
     * @return the default extras loader.
     */
    public ExtrasLoader getDefaultExtrasLoader() {
        if (defaultExtraLoaderClass == null) { 
            defaultExtraLoaderInstance = null; // do not hold reference
            return null;
        }

        if (defaultExtraLoaderInstance != null
                && defaultExtraLoaderInstance.getClass() != defaultExtraLoaderClass) {
            defaultExtraLoaderInstance = null; // reset instance if class changed
        }

        try {
            defaultExtraLoaderInstance = defaultExtraLoaderClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            logger.log(Level.WARNING, "Could not instantiate default extras loader", e);
            defaultExtraLoaderInstance = null;
        }

        return defaultExtraLoaderInstance;
    }

    void init(GltfLoader gltfLoader) {
        this.gltfLoader = gltfLoader;

        if (gltfLoader.getInfo().getKey() instanceof GltfModelKey) {
            this.key = (GltfModelKey) gltfLoader.getInfo().getKey();
        }

        JsonArray extensionUsed = gltfLoader.getDocRoot().getAsJsonArray("extensionsUsed");
        if (extensionUsed != null) {
            for (JsonElement extElem : extensionUsed) {
                String ext = extElem.getAsString();
                if (ext != null) {
                    if (defaultExtensionLoaders.get(ext) == null && (this.key != null && this.key.getExtensionLoader(ext) == null)) {
                        logger.log(Level.WARNING, "Extension " + ext + " is not supported, please provide your own implementation in the GltfModelKey");
                    }
                }
            }
        }
        JsonArray extensionRequired = gltfLoader.getDocRoot().getAsJsonArray("extensionsRequired");
        if (extensionRequired != null) {
            for (JsonElement extElem : extensionRequired) {
                String ext = extElem.getAsString();
                if (ext != null) {
                    if (defaultExtensionLoaders.get(ext) == null && (this.key != null && this.key.getExtensionLoader(ext) == null)) {
                        logger.log(Level.SEVERE, "Extension " + ext + " is mandatory for this file, the loaded scene result will be unexpected.");
                    }
                }
            }
        }
    }

    public <T> T readExtensionAndExtras(String name, JsonElement el, T input) throws AssetLoadException, IOException {
        T output = readExtension(name, el, input);
        output = readExtras(name, el, output);
        return output;
    }

    @SuppressWarnings("unchecked")
    private <T> T readExtension(String name, JsonElement el, T input) throws AssetLoadException, IOException {
        JsonElement extensions = el.getAsJsonObject().getAsJsonObject("extensions");
        if (extensions == null) {
            return input;
        }

        for (Map.Entry<String, JsonElement> ext : extensions.getAsJsonObject().entrySet()) {
            ExtensionLoader loader = null;

            if (key != null) {
                loader = key.getExtensionLoader(ext.getKey());
            }

            if (loader == null) {
                loader = loadedExtensionLoaders.get(ext.getKey());
                if (loader == null) {
                    try {
                        Class<? extends ExtensionLoader> clz = defaultExtensionLoaders.get(ext.getKey());
                        if (clz != null) {
                            loader = clz.getDeclaredConstructor().newInstance();
                        }
                    } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
                        logger.log(Level.WARNING, "Could not instantiate loader", e);
                    }

                    if (loader != null) {
                        loadedExtensionLoaders.put(ext.getKey(), loader);
                    }
                }
            }
            

            if (loader == null) {
                logger.log(Level.WARNING, "Could not find loader for extension " + ext.getKey());
                continue;
            }

            try {
                return (T) loader.handleExtension(gltfLoader, name, el, ext.getValue(), input);
            } catch (ClassCastException e) {
                throw new AssetLoadException("Extension loader " + loader.getClass().getName() + " for extension " + ext.getKey() + " is incompatible with type " + input.getClass(), e);
            }
        }

        return input;
    }

    @SuppressWarnings("unchecked")
    private <T> T readExtras(String name, JsonElement el, T input) throws AssetLoadException {
        ExtrasLoader loader = null;

        if (key != null) { // try to get the extras loader from the model key if available
            loader = key.getExtrasLoader();
        }
 
        if (loader == null) { // if no loader was found, use the default extras loader
            loader = getDefaultExtrasLoader();
        }

        if (loader == null) { // if default loader is not set or failed to instantiate, skip extras
            return input;
        }
           
        JsonElement extras = el.getAsJsonObject().getAsJsonObject("extras");
        if (extras == null) {
            return input;
        }

        try {
            return (T) loader.handleExtras(gltfLoader, name, el, extras, input);
        } catch (ClassCastException e) {
            throw new AssetLoadException("Extra loader " + loader.getClass().getName() + " for " + name + " is incompatible with type " + input.getClass(), e);
        }

    }


}
