
package com.jme3.system;

import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.jme3.util.res.Resources;

/**
 * Pulls in version info from the version.properties file.
 * 
 * @author Kirill Vainer
 */
public class JmeVersion {
    
    private static final Logger logger = Logger.getLogger(JmeVersion.class.getName());
    private static final Properties props = new Properties();
    
    static {
        try {
            props.load(Resources.getResourceAsStream("version.properties",JmeVersion.class));
        } catch (IOException | NullPointerException ex) {
            logger.log(Level.WARNING, "Unable to read version info!", ex);
        }
    }
    
    public static final String BUILD_DATE       = props.getProperty("build.date", "1900-01-01");
    public static final String BRANCH_NAME      = props.getProperty("git.branch", "unknown");
    public static final String GIT_HASH         = props.getProperty("git.hash", "");
    public static final String GIT_SHORT_HASH   = props.getProperty("git.hash.short", "");
    public static final String GIT_TAG          = props.getProperty("git.tag", "");
    public static final String VERSION_NUMBER   = props.getProperty("version.number", "");
    public static final String VERSION_TAG      = props.getProperty("version.tag", "");
    public static final String VERSION_FULL     = props.getProperty("version.full", "");
    public static final String FULL_NAME        = props.getProperty("name.full", "jMonkeyEngine (unknown version)");
    
    /**
     * A private constructor to inhibit instantiation of this class.
     */
    private JmeVersion() {
    }
}
