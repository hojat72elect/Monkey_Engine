
package com.jme3.system;

/**
 * Enumerate known operating system/architecture pairs.
 */
public enum Platform {

    /**
     * Microsoft Windows 32-bit AMD/Intel
     */
    Windows32(Os.Windows),

    /**
     * Microsoft Windows 64-bit AMD/Intel
     */
    Windows64(Os.Windows, true),

    /**
     * Microsoft Windows 32-bit ARM
     */
    Windows_ARM32(Os.Windows),

    /**
     * Microsoft Windows 64-bit ARM
     */
    Windows_ARM64(Os.Windows, true),

    /**
     * Linux 32-bit Intel
     */
    Linux32(Os.Linux),

    /**
     * Linux 64-bit Intel
     */
    Linux64(Os.Linux, true),

    /**
     * Linux 32-bit ARM
     */
    Linux_ARM32(Os.Linux),

    /**
     * Linux 64-bit ARM
     */
    Linux_ARM64(Os.Linux, true),

    /**
     * Apple Mac OS X 32-bit Intel
     */
    MacOSX32(Os.MacOS),

    /**
     * Apple Mac OS X 64-bit Intel
     */
    MacOSX64(Os.MacOS, true),

    /**
     * Apple Mac OS X 64-bit ARM
     */
    MacOSX_ARM64(Os.MacOS, true),

    /**
     * Apple Mac OS X 32 bit PowerPC
     */
    MacOSX_PPC32(Os.MacOS),

    /**
     * Apple Mac OS X 64 bit PowerPC
     */
    MacOSX_PPC64(Os.MacOS, true),

    /**
     * Android ARM5
     */
    Android_ARM5(Os.Android),

    /**
     * Android ARM6
     */
    Android_ARM6(Os.Android),

    /**
     * Android ARM7
     */
    Android_ARM7(Os.Android),

    /**
     * Android ARM8
     */
    Android_ARM8(Os.Android),

    /**
     * Android x86
     */
    Android_X86(Os.Android),

    /**
     * iOS on x86
     */
    iOS_X86(Os.iOS),

    /**
     * iOS on ARM
     */
    iOS_ARM(Os.iOS),

    /**
     * Android running on unknown platform (could be x86 or mips for example).
     */
    Android_Other(Os.Android),
    
    /**
    * Generic web platform on unknown architecture
    */
    Web(Os.Web, true) // assume always 64-bit, it shouldn't matter for web
    ;

    
    /**
     * Enumerate generic names of operating systems
     */
    public enum Os {
        /**
         * Linux operating systems
         */
        Linux,
        /**
         * Microsoft Windows operating systems
         */
        Windows,
        /**
         * iOS operating systems
         */
        iOS,
        /**
         * macOS operating systems
         */
        MacOS,
        /**
         * Android operating systems
         */
        Android,
        /**
         * Generic web platform
         */
        Web
    }

    private final boolean is64bit;
    private final Os os;

    /**
     * Test for a 64-bit address space.
     *
     * @return true if 64 bits, otherwise false
     */
    public boolean is64Bit() {
        return is64bit;
    }

    /**
     * Returns the operating system of this platform.
     *
     * @return the generic name of the operating system of this platform
     */
    public Os getOs() {
        return os;
    }

    private Platform(Os os, boolean is64bit) {
        this.os = os;
        this.is64bit = is64bit;
    }

    private Platform(Os os) {
        this(os, false);
    }
}
