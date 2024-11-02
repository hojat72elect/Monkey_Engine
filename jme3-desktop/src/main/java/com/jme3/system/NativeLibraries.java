
package com.jme3.system;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Defines default native libraries that are loaded by
 * {@link NativeLibraryLoader}.
 *
 * @author Ali-RS
 */
public enum NativeLibraries {

    // Note: LWJGL 3 handles its native library extracting & loading using
    // its own SharedLibraryLoader.

    /**
     * Native lwjgl libraries for LWJGL 2 required by jme3-lwjgl backend.
     */
    Lwjgl(new LibraryInfo("lwjgl", libPath ->
            // Delegate loading to lwjgl.
            System.setProperty("org.lwjgl.librarypath",
                    Paths.get(libPath).getParent().toAbsolutePath().toString()))
            .addNativeVariant(Platform.Windows32, "lwjgl.dll")
            .addNativeVariant(Platform.Windows64, "lwjgl64.dll")
            .addNativeVariant(Platform.Linux32, "liblwjgl.so")
            .addNativeVariant(Platform.Linux64, "liblwjgl64.so")
            .addNativeVariant(Platform.MacOSX32, "liblwjgl.dylib")
            .addNativeVariant(Platform.MacOSX64, "liblwjgl.dylib")
    ),

    // OpenAL for LWJGL 2
    // For OSX: Need to add lib prefix when extracting
    /**
     * Native OpenAL audio libraries for LWJGL 2 required by jme3-lwjgl backend.
     */
    OpenAL(new LibraryInfo("openal")
            .addNativeVariant(Platform.Windows32, "OpenAL32.dll")
            .addNativeVariant(Platform.Windows64, "OpenAL64.dll")
            .addNativeVariant(Platform.Linux32,   "libopenal.so")
            .addNativeVariant(Platform.Linux64,   "libopenal64.so")
            .addNativeVariant(Platform.MacOSX32,  "openal.dylib", "libopenal.dylib")
            .addNativeVariant(Platform.MacOSX64,  "openal.dylib", "libopenal.dylib")
    ),

    /**
     * Native bullet physics libraries required by Minie library.
     */
    BulletJme(new LibraryInfo("bulletjme")
            .addNativeVariant(Platform.Windows32, "native/windows/x86/bulletjme.dll", "bulletjme-x86.dll")
            .addNativeVariant(Platform.Windows64, "native/windows/x86_64/bulletjme.dll", "bulletjme-x86_64.dll")
            .addNativeVariant(Platform.Windows_ARM64, "native/windows/arm64/bulletjme.dll", "bulletjme-arm64.dll")
            .addNativeVariant(Platform.Linux32, "native/linux/x86/libbulletjme.so", "libbulletjme-x86.so")
            .addNativeVariant(Platform.Linux64, "native/linux/x86_64/libbulletjme.so", "libbulletjme-x86_64.so")
            .addNativeVariant(Platform.Linux_ARM32, "native/linux/arm32/libbulletjme.so", "libbulletjme-arm32.so")
            .addNativeVariant(Platform.Linux_ARM64, "native/linux/arm64/libbulletjme.so", "libbulletjme-arm64.so")
            .addNativeVariant(Platform.MacOSX32, "native/osx/x86/libbulletjme.dylib", "libbulletjme-x86.dylib")
            .addNativeVariant(Platform.MacOSX64, "native/osx/x86_64/libbulletjme.dylib", "libbulletjme-x86_64.dylib")
            .addNativeVariant(Platform.MacOSX_ARM64, "native/osx/arm64/libbulletjme.dylib", "libbulletjme-arm64.dylib")
    ),

    // For OSX: Need to rename extension jnilib -> dylib when extracting
    /**
     * Native JInput joystick libraries required by jme3-lwjgl backend.
     */
    JInput(new LibraryInfo("jinput", libPath ->
            // Delegate loading to jinput.
            System.setProperty("net.java.games.input.librarypath",
                    Paths.get(libPath).getParent().toAbsolutePath().toString()))
            .addNativeVariant(Platform.Windows32, "jinput-raw.dll")
            .addNativeVariant(Platform.Windows64, "jinput-raw_64.dll")
            .addNativeVariant(Platform.Linux32, "libjinput-linux.so")
            .addNativeVariant(Platform.Linux64, "libjinput-linux64.so")
            .addNativeVariant(Platform.MacOSX32, "libjinput-osx.jnilib", "libjinput-osx.dylib")
            .addNativeVariant(Platform.MacOSX64, "libjinput-osx.jnilib", "libjinput-osx.dylib")
    ),

    /**
     * Native JInput DirectX 8 auxiliary libraries required by jme3-lwjgl backend.
     * (only required on Windows)
     */
    JInputDX8(new LibraryInfo("jinput-dx8")
            .addNativeVariant(Platform.Windows32, "jinput-dx8.dll", null)
            .addNativeVariant(Platform.Windows64, "jinput-dx8_64.dll", null)
            .addNativeVariant(Platform.Linux32, null)
            .addNativeVariant(Platform.Linux64, null)
            .addNativeVariant(Platform.MacOSX32, null)
            .addNativeVariant(Platform.MacOSX64, null)
    );

    private final LibraryInfo library;


    NativeLibraries(LibraryInfo library) {
        this.library = library;
    }

    /**
     * Register native libraries on {@link NativeLibraryLoader} so we can load them
     * later on via {@link NativeLibraryLoader#loadNativeLibrary(String, boolean)}.
     */
    public static void registerDefaultLibraries() {
        Lwjgl.registerLibrary();
        OpenAL.registerLibrary();
        BulletJme.registerLibrary();
        JInput.registerLibrary();
        JInputDX8.registerLibrary();
    }

    public LibraryInfo getLibrary() {
        return library;
    }

    /**
     * @return the library name. This is effectively equivalent to the
     * call {@link LibraryInfo#getName()}
     */
    public String getName() {
        return library.getName();
    }

    /**
     * Registers this library's native variants into {@link NativeLibraryLoader} that can
     * be loaded later via {@link NativeLibraryLoader#loadNativeLibrary(String, boolean)}.
     */
    private void registerLibrary() {
        library.getNativeVariants().forEach(NativeLibraryLoader::registerNativeLibrary);
    }

    /**
     * A helper class that defines a native library by name, list of its native variants
     * for target platforms and a load function used to load library from an absolute
     * path after extracted by {@link NativeLibraryLoader}.
     */
    public static class LibraryInfo {

        private final String name;
        private final List<NativeLibrary> nativeVariants = new ArrayList<>();
        private final Consumer<String> loadFunction;

        /**
         * Define a library by the specified name and a default load function
         * that uses {@link System#load(String)} to load extracted native from
         * absolute path.
         * @param name The library name. (not null)
         */
        public LibraryInfo(String name) {
            this(name, System::load);
        }

        /**
         * Define a library by the specified name and specified load function
         * that is used to load extracted native from an absolute path string.
         *
         * @param name The library name (not null)
         * @param loadFunction The load function for loading library from
         *                     an absolute path string. (not null)
         */
        public LibraryInfo(String name, Consumer<String> loadFunction) {
            this.name = name;
            this.loadFunction = loadFunction;
        }

        /**
         * @return the library name.
         */
        public String getName() {
            return name;
        }

        /**
         * @return the list of native variants, each targeting a specific platform.
         */
        public List<NativeLibrary> getNativeVariants() {
            return nativeVariants;
        }

        /**
         * Adds a new native library that targets specified platform.
         *
         * @param platform The platform this library targets
         * @param pathInNativesJar The path of native file inside library jar
         * @return this
         */
        public LibraryInfo addNativeVariant(Platform platform, String pathInNativesJar) {
            return addNativeVariant(platform, pathInNativesJar, null);
        }

        /**
         * Adds a new native library that targets specified platform.
         *
         * @param platform The platform this library targets
         * @param pathInNativesJar The path of native file inside library jar
         * @param extractedAsFileName The filename that the library should be extracted as
         * @return this
         */
        public LibraryInfo addNativeVariant(Platform platform, String pathInNativesJar, String extractedAsFileName) {
             nativeVariants.add(new NativeLibrary(name, platform, pathInNativesJar, extractedAsFileName, loadFunction));
             return this;
        }
    }
}
