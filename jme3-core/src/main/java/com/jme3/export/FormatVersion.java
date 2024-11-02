
package com.jme3.export;

/**
 * Specifies the version of the format for jME3 object (j3o) files.
 *
 * @author Kirill Vainer
 */
public final class FormatVersion {
    
    /**
     * Version number of the format.
     * <p>
     * Changes for each version:
     * <ol>
     *   <li>Undocumented
     *   <li>Undocumented
     *   <li>XML prefixes "jme-" to all key names
     * </ol>
     */
    public static final int VERSION = 3;

    /**
     * Signature of the format: currently, "JME3" as ASCII.
     */
    public static final int SIGNATURE = 0x4A4D4533;

    private FormatVersion() {
    }
}
