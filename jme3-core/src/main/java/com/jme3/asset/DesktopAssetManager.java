
package com.jme3.asset;

import com.jme3.asset.cache.AssetCache;
import com.jme3.audio.AudioData;
import com.jme3.audio.AudioKey;
import com.jme3.font.BitmapFont;
import com.jme3.material.Material;
import com.jme3.post.FilterPostProcessor;
import com.jme3.renderer.Caps;
import com.jme3.scene.Spatial;
import com.jme3.shader.Glsl100ShaderGenerator;
import com.jme3.shader.Glsl150ShaderGenerator;
import com.jme3.shader.Glsl300ShaderGenerator;
import com.jme3.shader.ShaderGenerator;
import com.jme3.system.JmeSystem;
import com.jme3.texture.Texture;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * <code>AssetManager</code> is the primary method for managing and loading
 * assets inside jME.
 *
 * @author Kirill Vainer
 */
public class DesktopAssetManager implements AssetManager {

    private static final Logger logger = Logger.getLogger(AssetManager.class.getName());
    private ShaderGenerator shaderGenerator;

    private final ImplHandler handler = new ImplHandler(this);

    final private CopyOnWriteArrayList<AssetEventListener> eventListeners =
            new CopyOnWriteArrayList<>();

    @Deprecated
    final private List<ClassLoader> classLoaders = Collections.synchronizedList(new ArrayList<>());

    public DesktopAssetManager() {
        this(null);
    }

    public DesktopAssetManager(boolean usePlatformConfig) {
        this(usePlatformConfig ? JmeSystem.getPlatformAssetConfigURL() : null);
    }

    public DesktopAssetManager(URL configFile) {
        if (configFile != null) {
            loadConfigFile(configFile);
        }
        logger.fine("DesktopAssetManager created.");
    }

    private void loadConfigFile(URL configFile) {
        try {
            AssetConfig.loadText(this, configFile);
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "Failed to load asset config", ex);
        }
    }

    @Deprecated
    @Override
    public void addClassLoader(ClassLoader loader) {
        classLoaders.add(loader);
    }

    @Deprecated
    @Override
    public void removeClassLoader(ClassLoader loader) {
        classLoaders.remove(loader);
    }

    @Deprecated
    @Override
    public List<ClassLoader> getClassLoaders() {
        return Collections.unmodifiableList(classLoaders);
    }
    @Override
    public void addAssetEventListener(AssetEventListener listener) {
        eventListeners.add(listener);
    }

    @Override
    public void removeAssetEventListener(AssetEventListener listener) {
        eventListeners.remove(listener);
    }

    @Override
    public void clearAssetEventListeners() {
        eventListeners.clear();
    }

    public void setAssetEventListener(AssetEventListener listener) {
        eventListeners.clear();
        eventListeners.add(listener);
    }

    @Override
    public void registerLoader(Class<? extends AssetLoader> loader, String ... extensions) {
        handler.addLoader(loader, extensions);
        if (logger.isLoggable(Level.FINER)) {
            logger.log(Level.FINER, "Registered loader: {0} for extensions {1}",
                    new Object[]{loader.getSimpleName(), Arrays.toString(extensions)});
        }
    }

    @SuppressWarnings("unchecked")
    public void registerLoader(String clsName, String ... extensions) {
        Class<? extends AssetLoader> clazz = null;
        try {
            clazz = (Class<? extends AssetLoader>) Class.forName(clsName);
        } catch (ClassNotFoundException | NoClassDefFoundError ex) {
            logger.log(Level.WARNING, "Failed to find loader: " + clsName, ex);
        }
        if (clazz != null) {
            registerLoader(clazz, extensions);
        }
    }

    @Override
    public void unregisterLoader(Class<? extends AssetLoader> loaderClass) {
        handler.removeLoader(loaderClass);
        if (logger.isLoggable(Level.FINER)) {
            logger.log(Level.FINER, "Unregistered loader: {0}",
                    loaderClass.getSimpleName());
        }
    }

    @Override
    public void registerLocator(String rootPath, Class<? extends AssetLocator> locatorClass) {
        handler.addLocator(locatorClass, rootPath);
        if (logger.isLoggable(Level.FINER)) {
            logger.log(Level.FINER, "Registered locator: {0}",
                    locatorClass.getSimpleName());
        }
    }

    @SuppressWarnings("unchecked")
    public void registerLocator(String rootPath, String clsName) {
        Class<? extends AssetLocator> clazz = null;
        try {
            clazz = (Class<? extends AssetLocator>) Class.forName(clsName);
        } catch (ClassNotFoundException ex) {
            logger.log(Level.WARNING, "Failed to find locator: " + clsName, ex);
        } catch (NoClassDefFoundError ex) {
            logger.log(Level.WARNING, "Failed to find loader: " + clsName, ex);
        }
        if (clazz != null) {
            registerLocator(rootPath, clazz);
        }
    }

    @Override
    public void unregisterLocator(String rootPath, Class<? extends AssetLocator> clazz) {
        handler.removeLocator(clazz, rootPath);
        if (logger.isLoggable(Level.FINER)) {
            logger.log(Level.FINER, "Unregistered locator: {0}",
                    clazz.getSimpleName());
        }
    }

    @Override
    public AssetInfo locateAsset(AssetKey<?> key) {
        AssetInfo info = handler.tryLocate(key);
        if (info == null) {
            logger.log(Level.WARNING, "Cannot locate resource: {0}", key);
        }
        return info;
    }

    @Override
    public <T> T getFromCache(AssetKey<T> key) {
        AssetCache cache = handler.getCache(key.getCacheType());
        if (cache != null) {
            T asset = cache.getFromCache(key);
            if (asset != null) {
                // Since getFromCache fills the load stack, it has to be popped
                cache.notifyNoAssetClone();
            }
            return asset;
        } else {
            throw new IllegalArgumentException("Key " + key + " specifies no cache.");
        }
    }

    @Override
    public <T> void addToCache(AssetKey<T> key, T asset) {
        AssetCache cache = handler.getCache(key.getCacheType());
        if (cache != null) {
            cache.addToCache(key, asset);
            cache.notifyNoAssetClone();
        } else {
            throw new IllegalArgumentException("Key " + key + " specifies no cache.");
        }
    }

    @Override
    public <T> boolean deleteFromCache(AssetKey<T> key) {
        AssetCache cache = handler.getCache(key.getCacheType());
        if (cache != null) {
            return cache.deleteFromCache(key);
        } else {
            throw new IllegalArgumentException("Key " + key + " specifies no cache.");
        }
    }

    @Override
    public void clearCache() {
        handler.clearCache();
        if (logger.isLoggable(Level.FINER)) {
            logger.log(Level.FINER, "All asset caches cleared.");
        }
    }

    /**
     * Loads an asset that has already been located.
     * @param <T> The asset type
     * @param key The asset key
     * @param info The AssetInfo from the locator
     * @param proc AssetProcessor to use, or null to disable processing
     * @param cache The cache to store the asset in, or null to disable caching
     * @return The loaded asset
     *
     * @throws AssetLoadException If failed to load asset due to exception or
     * other error.
     */
    @SuppressWarnings("unchecked")
    protected <T> T loadLocatedAsset(AssetKey<T> key, AssetInfo info, AssetProcessor proc, AssetCache cache) {
        AssetLoader loader = handler.aquireLoader(key);
        Object obj;
        try {
            handler.establishParentKey(key);
            obj = loader.load(info);
        } catch (IOException ex) {
            throw new AssetLoadException("An exception has occurred while loading asset: " + key, ex);
        } finally {
            handler.releaseParentKey(key);
        }
        if (obj == null) {
            throw new AssetLoadException("Error occurred while loading asset \""
                    + key + "\" using " + loader.getClass().getSimpleName());
        } else {
            if (logger.isLoggable(Level.FINER)) {
                logger.log(Level.FINER, "Loaded {0} with {1}",
                        new Object[]{key, loader.getClass().getSimpleName()});
            }

            if (proc != null) {
                // do processing on asset before caching
                obj = proc.postProcess(key, obj);
            }

            if (cache != null) {
                // At this point, obj should be of type T
                cache.addToCache(key, (T) obj);
            }

            for (AssetEventListener listener : eventListeners) {
                listener.assetLoaded(key);
            }

            return (T) obj;
        }
    }

    /**
     * Clones the asset using the given processor and registers the clone
     * with the cache.
     *
     * @param <T> The asset type
     * @param key The asset key
     * @param obj The asset to clone / register, must implement
     *     {@link CloneableSmartAsset}.
     * @param proc The processor which will generate the clone, cannot be null
     * @param cache The cache to register the clone with, cannot be null.
     * @return The cloned asset, cannot be the same as the given asset since
     *     it is a clone.
     *
     * @throws IllegalStateException If asset does not implement
     * {@link CloneableSmartAsset}, if the cache is null, or if the
     * processor did not clone the asset.
     */
    @SuppressWarnings("unchecked")
    protected <T> T registerAndCloneSmartAsset(AssetKey<T> key, T obj, AssetProcessor proc, AssetCache cache) {
        // object obj is the original asset
        // create an instance for user
        if (proc == null) {
            throw new IllegalStateException("Asset implements "
                    + "CloneableSmartAsset but doesn't "
                    + "have processor to handle cloning");
        } else {
            T clone = (T) proc.createClone(obj);
            if (cache != null && clone != obj) {
                cache.registerAssetClone(key, clone);
            } else {
                throw new IllegalStateException("Asset implements "
                        + "CloneableSmartAsset but doesn't have cache or "
                        + "was not cloned");
            }
            return clone;
        }
    }

    @Override
    public <T> T loadAssetFromStream(AssetKey<T> key, InputStream inputStream) {
        if (key == null) {
            throw new IllegalArgumentException("key cannot be null");
        }

        for (AssetEventListener listener : eventListeners) {
            listener.assetRequested(key);
        }

        AssetProcessor proc = handler.getProcessor(key.getProcessorType());
        StreamAssetInfo info = new StreamAssetInfo(this, key, inputStream);
        return loadLocatedAsset(key, info, proc, null);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T loadAsset(AssetKey<T> key) {
        if (key == null)
            throw new IllegalArgumentException("key cannot be null");

        for (AssetEventListener listener : eventListeners) {
            listener.assetRequested(key);
        }

        AssetCache cache = handler.getCache(key.getCacheType());
        AssetProcessor proc = handler.getProcessor(key.getProcessorType());

        Object obj = cache != null ? cache.getFromCache(key) : null;
        if (obj == null) {
            // Asset not in cache, load it from file system.
            AssetInfo info = handler.tryLocate(key);
            if (info == null) {
                if (handler.getParentKey() != null) {
                    // Inform event listener that an asset has failed to load.
                    // If the parent AssetLoader chooses not to propagate
                    // the exception, this is the only means of finding
                    // that something went wrong.
                    for (AssetEventListener listener : eventListeners) {
                        listener.assetDependencyNotFound(handler.getParentKey(), key);
                    }
                }
                throw new AssetNotFoundException(key.toString());
            }

            obj = loadLocatedAsset(key, info, proc, cache);
        }

        T clone = (T) obj;

        if (obj instanceof CloneableSmartAsset) {
            clone = registerAndCloneSmartAsset(key, clone, proc, cache);
        }

        return clone;
    }

    @Override
    public Object loadAsset(String name) {
        return loadAsset(new AssetKey<>(name));
    }

    @Override
    public Texture loadTexture(TextureKey key) {
        return loadAsset(key);
    }

    @Override
    public Material loadMaterial(String name) {
        return loadAsset(new MaterialKey(name));
    }

    @Override
    public Texture loadTexture(String name) {
        TextureKey key = new TextureKey(name, true);
        key.setGenerateMips(true);
        return loadTexture(key);
    }

    @Override
    public AudioData loadAudio(AudioKey key) {
        return loadAsset(key);
    }

    @Override
    public AudioData loadAudio(String name) {
        return loadAudio(new AudioKey(name, false));
    }

    @Override
    public BitmapFont loadFont(String name) {
        return loadAsset(new AssetKey<BitmapFont>(name));
    }

    @Override
    public Spatial loadModel(ModelKey key) {
        return loadAsset(key);
    }

    @Override
    public Spatial loadModel(String name) {
        return loadModel(new ModelKey(name));
    }

    @Override
    public FilterPostProcessor loadFilter(FilterKey key) {
        return loadAsset(key);
    }

    @Override
    public FilterPostProcessor loadFilter(String name) {
        return loadFilter(new FilterKey(name));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ShaderGenerator getShaderGenerator(EnumSet<Caps> caps) {
        if (shaderGenerator == null) {
            if (caps.contains(Caps.OpenGLES30) && caps.contains(Caps.GLSL300)) {
                shaderGenerator = new Glsl300ShaderGenerator(this);
            } else if (caps.contains(Caps.GLSL150)) {
                shaderGenerator = new Glsl150ShaderGenerator(this);
            } else {
                shaderGenerator = new Glsl100ShaderGenerator(this);
            }
        }
        return shaderGenerator;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setShaderGenerator(ShaderGenerator shaderGenerator) {
        this.shaderGenerator = shaderGenerator;
    }
}
