
package com.jme3.asset.plugins;

import com.jme3.asset.AssetInfo;
import com.jme3.asset.AssetKey;
import com.jme3.asset.AssetLocator;
import com.jme3.asset.AssetManager;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * <code>UrlLocator</code> is a locator that combines a root URL
 * and the given path in the AssetKey to construct a new URL
 * that allows locating the asset.
 * <p>
 * The root path must be a valid {@link URL}, for example, <br>
 * <code>https://www.example.com/assets/</code>
 * 
 * @author Kirill Vainer
 */
public class UrlLocator implements AssetLocator {

    private static final Logger logger = Logger.getLogger(UrlLocator.class.getName());
    private URL root;

    @Override
    public void setRootPath(String rootPath) {
        try {
            this.root = new URL(rootPath);
        } catch (MalformedURLException ex) {
            throw new IllegalArgumentException("Invalid rootUrl specified", ex);
        }
    }

    @Override
    public AssetInfo locate(AssetManager manager, AssetKey key) {
        String name = key.getName();
        try{
            //TODO: remove workaround for SDK
//            URL url = new URL(root, name);
            if(name.startsWith("/")){
                name = name.substring(1);
            }
            URL url = new URL(root.toExternalForm() + name);
            return UrlAssetInfo.create(manager, key, url);
        }catch (FileNotFoundException e){
            return null;
        }catch (IOException ex){
            logger.log(Level.WARNING, "Error while locating " + name, ex);
            return null;
        }
    }


}
