
package com.jme3.system;

import java.util.function.Consumer;

/**
 * Holds information about a native library for a particular platform.
 * 
 * @author Kirill Vainer
 */
final class NativeLibrary {
    
    private final String name;
    private final Platform platform;
    private final String pathInNativesJar;
    private final String extractedAsFileName;
    private final Consumer<String> loadFunction;

    /**
     * Key for map to find a library for a name and platform.
     */
    static final class Key {

        private final String name;
        private final Platform platform;

        public Key(String name, Platform platform) {
            this.name = name;
            this.platform = platform;
        }

        @Override
        public int hashCode() {
            int hash = 5;
            hash = 79 * hash + this.name.hashCode();
            hash = 79 * hash + this.platform.hashCode();
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            final Key other = (Key) obj;
            if (!this.name.equals(other.name)) {
                return false;
            }
            if (this.platform != other.platform) {
                return false;
            }
            return true;
        }
    }

    /**
     * Create a new NativeLibrary. The extracted file name will be the same as
     * in jar and will be loaded via {@link System#load(String)}.
     *
     * @param name The name of the library (not null)
     * @param platform The platform associated to this native library (not null)
     * @param pathInNativesJar The path to the library in the classpath (if it is null,
     *                         the library loading will be ignored on this platform)
     **/
    public NativeLibrary(String name, Platform platform, String pathInNativesJar) {
        this(name, platform, pathInNativesJar, null);
    }

    /**
     * Create a new NativeLibrary. The extracted file will be loaded
     * via {@link System#load(String)}.
     *
     * @param name The name of the library (not null)
     * @param platform The platform associated to this native library (not null)
     * @param pathInNativesJar The path to the library in the classpath (if it is null,
     *                         the library loading will be ignored on this platform)
     * @param extractedAsFileName The name that should be given to the extracted file
     *                            (if set to null, then the filename in the natives
     *                            jar shall be used)
     */
    public NativeLibrary(String name, Platform platform, String pathInNativesJar, String extractedAsFileName) {
        this(name, platform, pathInNativesJar, extractedAsFileName, System::load);
    }

    /**
     * Create a new NativeLibrary.
     *
     * @param name The name of the library (not null)
     * @param platform The platform associated to this native library (not null)
     * @param pathInNativesJar The path to the library in the classpath (if it is null,
     *                         the library loading will be ignored on this platform)
     * @param extractedAsFileName The name that should be given to the extracted file
     *                            (if set to null, then the filename in the natives
     *                            jar shall be used)
     * @param loadFunction The function used to load the library from absolute path (not null)
     */
    public NativeLibrary(String name, Platform platform, String pathInNativesJar, String extractedAsFileName, Consumer<String> loadFunction) {
        this.name = name;
        this.platform = platform;
        this.pathInNativesJar = pathInNativesJar;
        this.extractedAsFileName = extractedAsFileName;
        this.loadFunction = loadFunction;
    }
    
    /**
     * The name of the library. 
     * Generally only used as a way to uniquely identify the library.
     * 
     * @return name of the library.
     */
    public String getName() {
        return name;
    }

    /**
     * The OS + architecture combination for which this library
     * should be extracted.
     *
     * @return platform associated to this native library
     */
    public Platform getPlatform() {
        return platform;
    }

    /**
     * The filename that the library should be extracted as.
     *
     * In some cases, this differs from the {@link #getPathInNativesJar() path in the natives jar},
     * since the names of the libraries specified in the jars are often incorrect.
     * If set to <code>null</code>, then the filename in the
     * natives jar shall be used.
     *
     * @return the name that should be given to the extracted file.
     */
    public String getExtractedAsName() {
        return extractedAsFileName;
    }

    /**
     * Path inside the natives jar or classpath where the library is located.
     *
     * This library must be compatible with the {@link #getPlatform() platform}
     * which this library is associated with.
     *
     * @return path to the library in the classpath
     */
    public String getPathInNativesJar() {
        return pathInNativesJar;
    }

    /**
     * @return the load function used for loading this native library.
     * It loads the native library from absolute path on disk.
     * By default, it loads with {@link System#load(java.lang.String) }.
     */
    public Consumer<String> getLoadFunction() {
        return loadFunction;
    }

    /**
     * @return key for map to find a library for a name and platform.
     */
    public Key getKey() {
        return new NativeLibrary.Key(getName(), getPlatform());
    }
}
