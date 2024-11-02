
package com.jme3.scene.plugins.fbx.material;

import com.jme3.asset.AssetLoadException;
import com.jme3.asset.AssetManager;
import com.jme3.asset.AssetNotFoundException;
import com.jme3.asset.TextureKey;
import com.jme3.scene.plugins.fbx.file.FbxElement;
import com.jme3.scene.plugins.fbx.obj.FbxObject;
import com.jme3.texture.Image;
import com.jme3.util.PlaceholderAssets;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class FbxImage extends FbxObject {
    
    private static final Logger logger = Logger.getLogger(FbxImage.class.getName());
    
    protected TextureKey key;
    protected String type;             // = "Clip"
    protected String filePath;         // = "C:\Whatever\Blah\Texture.png"
    protected String relativeFilePath; // = "..\Blah\Texture.png"
    protected byte[] content;          // = null, byte[0] OR embedded image data (unknown format?)
    
    public FbxImage(AssetManager assetManager, String sceneFolderName) {
        super(assetManager, sceneFolderName);
    }
    
    @Override
    public void fromElement(FbxElement element) {
        super.fromElement(element);
        if (element.propertiesTypes.length == 3) {
            type = (String) element.properties.get(2);
        } else {
            type = (String) element.properties.get(1);
        }
        if (type.equals("Clip")) {
            for (FbxElement e : element.children) {
                if (e.id.equals("Type")) {
                    type = (String) e.properties.get(0);
                } else if (e.id.equals("FileName")) {
                    filePath = (String) e.properties.get(0);
                } else if (e.id.equals("RelativeFilename")) {
                    relativeFilePath = (String) e.properties.get(0);
                } else if (e.id.equals("Content")) {
                    if (e.properties.size() > 0) {
                        byte[] storedContent = (byte[]) e.properties.get(0);
                        if (storedContent.length > 0) {
                            this.content = storedContent;
                        }
                    }
                }
            }
        }
    }
    
    private Image loadImageSafe(AssetManager assetManager, TextureKey texKey) {
        try {
            return assetManager.loadTexture(texKey).getImage();
        } catch (AssetNotFoundException ex) {
            return null;
        } catch (AssetLoadException ex) {
            logger.log(Level.WARNING, "Error when loading image: " + texKey, ex);
            return null;
        }
    }
    
    private static String getFileName(String filePath) {
        // NOTE: Gotta do it this way because new File().getParent() 
        // will not strip forward slashes on Linux / Mac OS X.
        int fwdSlashIdx = filePath.lastIndexOf("\\");
        int bkSlashIdx = filePath.lastIndexOf("/");

        if (fwdSlashIdx != -1) {
            filePath = filePath.substring(fwdSlashIdx + 1);
        } else if (bkSlashIdx != -1) {
            filePath = filePath.substring(bkSlashIdx + 1);
        }

        return filePath;
    }
    
    /**
     * The texture key that was used to load the image.
     * Only valid after {@link #getJmeObject()} has been called.
     * @return the key that was used to load the image.
     */
    public TextureKey getTextureKey() {
        return key;
    }
    
    @Override
    protected Object toJmeObject() {
        Image image = null;
        String fileName = null;
        String relativeFilePathJme;

        if (filePath != null) {
            fileName = getFileName(filePath);
        } else if (relativeFilePath != null) {
            fileName = getFileName(relativeFilePath);
            
        }

        if (fileName != null) {
            try {
                // Try to load filename relative to FBX folder
                key = new TextureKey(sceneFolderName + fileName);
                key.setGenerateMips(true);
                image = loadImageSafe(assetManager, key);
                
                // Try to load relative filepath relative to FBX folder
                if (image == null && relativeFilePath != null) {
                    // Convert Windows paths to jME3 paths
                    relativeFilePathJme = relativeFilePath.replace('\\', '/');
                    key = new TextureKey(sceneFolderName + relativeFilePathJme);
                    key.setGenerateMips(true);
                    image = loadImageSafe(assetManager, key);
                }
                
                // Try to load embedded image
                if (image == null && content != null && content.length > 0) {
                    key = new TextureKey(fileName);
                    key.setGenerateMips(true);
                    InputStream is = new ByteArrayInputStream(content);
                    image = assetManager.loadAssetFromStream(key, is).getImage();
                    
                    // NOTE: embedded texture doesn't exist in the asset manager,
                    //       so the texture key must not be saved.
                    key = null;
                }
            } catch (AssetLoadException ex) {
                logger.log(Level.WARNING, "Error while attempting to load texture {0}:\n{1}",
                           new Object[]{name, ex.toString()});
            }
        }

        if (image == null) {
            logger.log(Level.WARNING, "Cannot locate {0} for texture {1}", new Object[]{fileName, name});
            image = PlaceholderAssets.getPlaceholderImage(assetManager);
        }
        
        // NOTE: At this point, key will be set to the last
        //       attempted texture key that we attempted to load.

        return image;
    }

    @Override
    public void connectObject(FbxObject object) {
        unsupportedConnectObject(object);
    }

    @Override
    public void connectObjectProperty(FbxObject object, String property) {
        unsupportedConnectObjectProperty(object, property);
    }
}
