
package com.jme3.system;

import com.jme3.asset.AssetManager;
import com.jme3.asset.DesktopAssetManager;
import com.jme3.audio.AudioRenderer;
import com.jme3.input.SoftTextDialogInput;
import com.jme3.util.res.Resources;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.EnumMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Kirill Vainer, normenhansen
 */
public abstract class JmeSystemDelegate {

    protected final Logger logger = Logger.getLogger(JmeSystem.class.getName());
    protected boolean initialized = false;
    protected boolean lowPermissions = false;
    protected Map<JmeSystem.StorageFolderType, File> storageFolders = new EnumMap<>(JmeSystem.StorageFolderType.class);
    protected SoftTextDialogInput softTextDialogInput = null;

    protected Consumer<String> errorMessageHandler = (message) -> {
        JmeDialogsFactory dialogFactory = null;
        try {
             dialogFactory = (JmeDialogsFactory)Class.forName("com.jme3.system.JmeDialogsFactoryImpl").getConstructor().newInstance();
        } catch(ClassNotFoundException e){
            logger.warning("JmeDialogsFactory implementation not found.");    
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }
        if(dialogFactory != null) dialogFactory.showErrorDialog(message);
        else System.err.println(message);
    };

    protected BiFunction<AppSettings,Boolean,Boolean> settingsHandler = (settings,loadFromRegistry) -> {
        JmeDialogsFactory dialogFactory = null;
        try {
            dialogFactory = (JmeDialogsFactory)Class.forName("com.jme3.system.JmeDialogsFactoryImpl").getConstructor().newInstance();
        } catch(ClassNotFoundException e){
            logger.warning("JmeDialogsFactory implementation not found.");    
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }
        if(dialogFactory != null) return dialogFactory.showSettingsDialog(settings, loadFromRegistry);
        return true;
    };

    public synchronized File getStorageFolder(JmeSystem.StorageFolderType type) {
        File storageFolder = null;

        switch (type) {
            // Internal and External are currently the same folder
            case Internal:
            case External:
                if (lowPermissions) {
                    throw new UnsupportedOperationException("File system access restricted");
                }
                storageFolder = storageFolders.get(type);
                if (storageFolder == null) {
                    // Initialize storage folder
                    storageFolder = new File(System.getProperty("user.home"), ".jme3");
                    if (!storageFolder.exists()) {
                        storageFolder.mkdir();
                    }
                    storageFolders.put(type, storageFolder);
                }
                break;
            default:
                break;
        }
        if (storageFolder != null) {
            if (logger.isLoggable(Level.FINE)) {
                logger.log(Level.FINE, "Storage Folder Path: {0}", storageFolder.getAbsolutePath());
            }
        } else {
            logger.log(Level.FINE, "Storage Folder not found!");
        }
        return storageFolder;
    }

    public String getFullName() {
        return JmeVersion.FULL_NAME;
    }

    public InputStream getResourceAsStream(String name) {
        return Resources.getResourceAsStream(name,this.getClass());
    }

    public URL getResource(String name) {
        return Resources.getResource(name,this.getClass());
    }

    public boolean trackDirectMemory() {
        return false;
    }

    public void setLowPermissions(boolean lowPerm) {
        lowPermissions = lowPerm;
    }

    public boolean isLowPermissions() {
        return lowPermissions;
    }

    public void setSoftTextDialogInput(SoftTextDialogInput input) {
        softTextDialogInput = input;
    }
    
    public SoftTextDialogInput getSoftTextDialogInput() {
        return softTextDialogInput;
    }

    public final AssetManager newAssetManager(URL configFile) {
        return new DesktopAssetManager(configFile);
    }

    public final AssetManager newAssetManager() {
        return new DesktopAssetManager(null);
    }
    
    public abstract void writeImageFile(OutputStream outStream, String format, ByteBuffer imageData, int width, int height) throws IOException;

    /**
     * Set function to handle errors. 
     * The default implementation show a dialog if available.
     * @param handler Consumer to which the error is passed as String
     */
    public void setErrorMessageHandler(Consumer<String> handler){
        errorMessageHandler = handler;
    }

    /**
     * Internal use only: submit an error to the error message handler
     */
    public void handleErrorMessage(String message){
        if(errorMessageHandler != null) errorMessageHandler.accept(message);
    }

    /**
     * Set a function to handler app settings. 
     * The default implementation shows a settings dialog if available.
     * @param handler handler function that accepts as argument an instance of AppSettings 
     * to transform and a boolean with the value of true if the settings are expected to be loaded from 
     * the user registry. The handler function returns false if the configuration is interrupted (eg.the the dialog was closed)
     * or true otherwise.
     */
    public void setSettingsHandler(BiFunction<AppSettings,Boolean, Boolean> handler){
        settingsHandler = handler;
    }

    /**
     * Internal use only: summon the settings handler
     */
    public boolean handleSettings(AppSettings settings, boolean loadFromRegistry){
        if(settingsHandler != null) return settingsHandler.apply(settings,loadFromRegistry);
        return true;
    }

    /**
     * @deprecated Use JmeSystemDelegate.handleErrorMessage(String) instead
     * @param message
     */
    @Deprecated
    public void showErrorDialog(String message){
        handleErrorMessage(message);
    }

    @Deprecated
    public boolean showSettingsDialog(AppSettings settings, boolean loadFromRegistry){
        return handleSettings(settings, loadFromRegistry);
    }


    private boolean is64Bit(String arch) {
        if (arch.equals("x86")) {
            return false;
        } else if (arch.equals("amd64")) {
            return true;
        } else if (arch.equals("x86_64")) {
            return true;
        } else if (arch.equals("ppc") || arch.equals("PowerPC")) {
            return false;
        } else if (arch.equals("ppc64")) {
            return true;
        } else if (arch.equals("i386") || arch.equals("i686")) {
            return false;
        } else if (arch.equals("universal")) {
            return false;
        } else if (arch.equals("aarch32")) {
            return false;
        } else if (arch.equals("aarch64")) {
            return true;
        } else if (arch.equals("armv7") || arch.equals("armv7l")) {
            return false;
        } else if (arch.equals("arm")) {
            return false;
        } else {
            throw new UnsupportedOperationException("Unsupported architecture: " + arch);
        }
    }

    public Platform getPlatform() {
        String os = System.getProperty("os.name").toLowerCase();
        String arch = System.getProperty("os.arch").toLowerCase();
        boolean is64 = is64Bit(arch);
        if (os.contains("windows")) {
            if (arch.startsWith("arm") || arch.startsWith("aarch")) {
                return is64 ? Platform.Windows_ARM64 : Platform.Windows_ARM32;
            } else {
                return is64 ? Platform.Windows64 : Platform.Windows32;
            }
        } else if (os.contains("linux") || os.contains("freebsd") 
                || os.contains("sunos") || os.contains("unix")) {
            if (arch.startsWith("arm") || arch.startsWith("aarch")) {
                return is64 ? Platform.Linux_ARM64 : Platform.Linux_ARM32;
            } else {
                return is64 ? Platform.Linux64 : Platform.Linux32;
            }
        } else if (os.contains("mac os x") || os.contains("darwin")) {
            if (arch.startsWith("ppc")) {
                return is64 ? Platform.MacOSX_PPC64 : Platform.MacOSX_PPC32;
            } else if (arch.startsWith("aarch")) {
                return Platform.MacOSX_ARM64; // no 32-bit version
            } else {
                return is64 ? Platform.MacOSX64 : Platform.MacOSX32;
            }
        } else {
            throw new UnsupportedOperationException("The specified platform: " + os + " is not supported.");
        }
    }

    public String getBuildInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append("Running on ").append(getFullName()).append("\n");
        sb.append(" * Branch: ").append(JmeVersion.BRANCH_NAME).append("\n");
        sb.append(" * Git Hash: ").append(JmeVersion.GIT_SHORT_HASH).append("\n");
        sb.append(" * Build Date: ").append(JmeVersion.BUILD_DATE);
        return sb.toString();
    }
    
    public abstract URL getPlatformAssetConfigURL();
    
    public abstract JmeContext newContext(AppSettings settings, JmeContext.Type contextType);

    public abstract AudioRenderer newAudioRenderer(AppSettings settings);

    public abstract void initialize(AppSettings settings);

    public abstract void showSoftKeyboard(boolean show);
}
