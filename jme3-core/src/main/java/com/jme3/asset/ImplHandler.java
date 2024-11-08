
package com.jme3.asset;

import com.jme3.asset.cache.AssetCache;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * <code>ImplHandler</code> manages the asset loader and asset locator
 * implementations in a thread safe way. This allows implementations
 * which store local persistent data to operate with a multi-threaded system.
 * This is done by keeping an instance of each asset loader and asset
 * locator object in a thread local.
 */
final class ImplHandler {

    private static final Logger logger = Logger.getLogger(ImplHandler.class.getName());

    private final AssetManager assetManager;

    private final ThreadLocal<AssetKey> parentAssetKey
            = new ThreadLocal<>();

    private final CopyOnWriteArrayList<ImplThreadLocal<AssetLocator>> locatorsList =
            new CopyOnWriteArrayList<>();

    private final HashMap<Class<?>, ImplThreadLocal<AssetLoader>> classToLoaderMap =
            new HashMap<>();

    private final ConcurrentHashMap<String, ImplThreadLocal<AssetLoader>> extensionToLoaderMap =
            new ConcurrentHashMap<>();

    private final ConcurrentHashMap<Class<? extends AssetProcessor>, AssetProcessor> classToProcMap =
            new ConcurrentHashMap<>();

    private final ConcurrentHashMap<Class<? extends AssetCache>, AssetCache> classToCacheMap =
            new ConcurrentHashMap<>();

    public ImplHandler(AssetManager assetManager) {
        this.assetManager = assetManager;
    }

    protected static class ImplThreadLocal<T> extends ThreadLocal<T> {

        private final Class<T> type;
        private final String path;
        private final String[] extensions;

        public ImplThreadLocal(Class<T> type, String[] extensions) {
            this.type = type;
            this.extensions = extensions.clone();
            this.path = null;
        }

        public ImplThreadLocal(Class<T> type, String path) {
            this.type = type;
            this.path = path;
            this.extensions = null;
        }

        public ImplThreadLocal(Class<T> type) {
            this.type = type;
            this.path = null;
            this.extensions = null;
        }

        public String getPath() {
            return path;
        }

        public String[] getExtensions() {
            return extensions;
        }

        public Class<?> getTypeClass() {
            return type;
        }

        @Override
        protected T initialValue() {
            try {
                T obj = type.getDeclaredConstructor().newInstance();

                if (path != null) {
                    ((AssetLocator) obj).setRootPath(path);
                }
                return obj;
            } catch (InstantiationException | IllegalAccessException
                    | IllegalArgumentException | InvocationTargetException
                    | NoSuchMethodException | SecurityException ex) {
                logger.log(Level.SEVERE, "An exception occurred while instantiating asset locator: " + type.getName(), ex);
            }
            return null;
        }
    }

    /**
     * Establishes the asset key that is used for tracking dependent assets
     * that have failed to load. When set, the {@link DesktopAssetManager}
     * gets a hint that it should suppress {@link AssetNotFoundException}s
     * and instead call the listener callback (if set).
     *
     * @param parentKey The parent key
     */
    public void establishParentKey(AssetKey parentKey) {
        if (parentAssetKey.get() == null) {
            parentAssetKey.set(parentKey);
        }
    }

    public void releaseParentKey(AssetKey parentKey) {
        if (parentAssetKey.get() == parentKey) {
            parentAssetKey.set(null);
        }
    }

    public AssetKey getParentKey() {
        return parentAssetKey.get();
    }

    /**
     * Attempts to locate the given resource name.
     * @param key The full name of the resource.
     * @return The AssetInfo containing resource information required for
     * access, or null if not found.
     */
    public AssetInfo tryLocate(AssetKey key) {
        if (locatorsList.isEmpty()) {
            logger.warning("There are no locators currently"
                    + " registered. Use AssetManager."
                    + "registerLocator() to register a"
                    + " locator.");
            return null;
        }

        for (ImplThreadLocal<AssetLocator> local : locatorsList) {
            AssetInfo info = local.get().locate(assetManager, key);
            if (info != null) {
                return info;
            }
        }

        return null;
    }

    public int getLocatorCount() {
        return locatorsList.size();
    }

    /**
     * Returns the AssetLoader registered for the given extension
     * of the current thread.
     * @return AssetLoader registered with addLoader.
     */
    public AssetLoader aquireLoader(AssetKey key) {
        // No need to synchronize() against map, its concurrent
        ImplThreadLocal local = extensionToLoaderMap.get(key.getExtension());
        if (local == null) {
            throw new AssetLoadException("No loader registered for type \""
                    + key.getExtension() + "\"");

        }
        return (AssetLoader) local.get();
    }

    public void clearCache() {
        // The iterator over the values collection is thread-safe.
        synchronized (classToCacheMap) {
            for (AssetCache cache : classToCacheMap.values()) {
                cache.clearCache();
            }
        }
    }

    @SuppressWarnings("unchecked")
    public <T extends AssetCache> T getCache(Class<T> cacheClass) {
        if (cacheClass == null) {
            return null;
        }

        T cache = (T) classToCacheMap.get(cacheClass);
        if (cache == null) {
            synchronized (classToCacheMap) {
                cache = (T) classToCacheMap.get(cacheClass);
                if (cache == null) {
                    try {
                        cache = cacheClass.getDeclaredConstructor().newInstance();
                        classToCacheMap.put(cacheClass, cache);
                    } catch (InstantiationException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException ex) {
                        throw new IllegalArgumentException("The cache class cannot"
                                + " be created, ensure it has empty constructor", ex);
                    } catch (IllegalAccessException | SecurityException ex) {
                        throw new IllegalArgumentException("The cache class cannot "
                                + "be accessed", ex);
                    }
                }
            }
        }
        return cache;
    }

    @SuppressWarnings("unchecked")
    public <T extends AssetProcessor> T getProcessor(Class<T> procClass) {
        if (procClass == null)
            return null;

        T proc = (T) classToProcMap.get(procClass);
        if (proc == null) {
            synchronized (classToProcMap) {
                proc = (T) classToProcMap.get(procClass);
                if (proc == null) {
                    try {
                        proc = procClass.getDeclaredConstructor().newInstance();
                        classToProcMap.put(procClass, proc);
                    } catch (InstantiationException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException ex) {
                        throw new IllegalArgumentException("The processor class cannot"
                                + " be created, ensure it has empty constructor", ex);
                    } catch (IllegalAccessException | SecurityException ex) {
                        throw new IllegalArgumentException("The processor class cannot "
                                + "be accessed", ex);
                    }
                }
            }
        }
        return proc;
    }

    @SuppressWarnings("unchecked")
    public void addLoader(final Class<? extends AssetLoader> loaderType, String ... extensions) {
        // Synchronized access must be used for any ops on classToLoaderMap
        ImplThreadLocal local = new ImplThreadLocal(loaderType, extensions);
        for (String extension : extensions) {
            extension = extension.toLowerCase();
            synchronized (classToLoaderMap) {
                classToLoaderMap.put(loaderType, local);
                extensionToLoaderMap.put(extension, local);
            }
        }
    }

    public void removeLoader(final Class<? extends AssetLoader> loaderType) {
        // Synchronized access must be used for any ops on classToLoaderMap
        // Find the loader ImplThreadLocal for this class
        synchronized (classToLoaderMap) {
            // Remove it from the class->loader map
            ImplThreadLocal local = classToLoaderMap.remove(loaderType);
            if (local == null)
                return;
            // Remove it from the extension->loader map
            for (String extension : local.getExtensions()) {
                extensionToLoaderMap.remove(extension);
            }
        }
    }

    @SuppressWarnings("unchecked")
    public void addLocator(final Class<? extends AssetLocator> locatorType, String rootPath) {
        locatorsList.add(new ImplThreadLocal(locatorType, rootPath));
    }

    public void removeLocator(final Class<? extends AssetLocator> locatorType, String rootPath) {
        ArrayList<ImplThreadLocal<AssetLocator>> locatorsToRemove = new ArrayList<>();
        Iterator<ImplThreadLocal<AssetLocator>> it = locatorsList.iterator();

        while (it.hasNext()) {
            ImplThreadLocal<AssetLocator> locator = it.next();
            if (locator.getPath().equals(rootPath)
                    && locator.getTypeClass().equals(locatorType)) {
                //it.remove();
                // copy on write list doesn't support iterator remove,
                // must use temporary list
                locatorsToRemove.add(locator);
            }
        }

        locatorsList.removeAll(locatorsToRemove);
    }
}
