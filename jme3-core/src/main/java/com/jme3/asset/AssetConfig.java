
package com.jme3.asset;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Locale;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.jme3.util.res.Resources;

/**
 * <code>AssetConfig</code> loads a config file to configure the asset manager.
 *
 * The config file is specified with the following format:
 * <code>
 * "INCLUDE" path
 * "LOADER" class : (extension ",")* extension
 * "LOCATOR" path class
 * </code>
 *
 * @author Kirill Vainer
 */
public final class AssetConfig {

    private static final Logger logger = Logger.getLogger(AssetConfig.class.getName());

    private AssetConfig() { }

    private static Class acquireClass(String name) {
        try {
            return Class.forName(name);
        } catch (ClassNotFoundException ex) {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public static void loadText(AssetManager assetManager, URL configUrl) throws IOException {
        InputStream in = configUrl.openStream();
        try {
            Scanner scan = new Scanner(in, "UTF-8");
            scan.useLocale(Locale.US); // Fix commas / periods ??
            while (scan.hasNext()) {
                String cmd = scan.next();
                if (cmd.equals("LOADER")) {
                    String loaderClass = scan.next();
                    String colon = scan.next();
                    if (!colon.equals(":")) {
                        throw new IOException("Expected ':', got '" + colon + "'");
                    }
                    String extensionsList = scan.nextLine();
                    String[] extensions = extensionsList.split(",");
                    for (int i = 0; i < extensions.length; i++) {
                        extensions[i] = extensions[i].trim();
                    }
                    Class clazz = acquireClass(loaderClass);
                    if (clazz != null) {
                        assetManager.registerLoader(clazz, extensions);
                    } else {
                        logger.log(Level.WARNING, "Cannot find loader {0}", loaderClass);
                    }
                } else if (cmd.equals("LOCATOR")) {
                    String rootPath = scan.next();
                    String locatorClass = scan.nextLine().trim();
                    Class clazz = acquireClass(locatorClass);
                    if (clazz != null) {
                        assetManager.registerLocator(rootPath, clazz);
                    } else {
                        logger.log(Level.WARNING, "Cannot find locator {0}", locatorClass);
                    }
                } else if (cmd.equals("INCLUDE")) {
                    String includedCfg = scan.nextLine().trim();
                    URL includedCfgUrl = Resources.getResource(includedCfg);
                    if (includedCfgUrl != null) {
                        loadText(assetManager, includedCfgUrl);
                    } else {
                        logger.log(Level.WARNING, "Cannot find config include {0}", includedCfg);
                    }
                } else if (cmd.trim().startsWith("#")) {
                    scan.nextLine();
                    continue;
                } else {
                    throw new IOException("Expected command, got '" + cmd + "'");
                }
            }
        } finally {
            if (in != null)
                in.close();
        }
    }
}
