
package com.jme3.scene.plugins.gltf;

import com.jme3.plugins.json.JsonElement;
import java.io.IOException;

/**
 * Interface to handle a glTF extension.
 *
 * Created by Nehon on 20/08/2017.
 */
public interface ExtensionLoader {

    /**
     * Handles a glTF extension.
     *
     * @param loader     the GltfLoader with all the data structures
     * @param parentName the name of the element being read
     * @param parent     the element being read
     * @param extension  the content of the extension found in the element being read
     * @param input      an object containing already loaded data from the element, probably a JME object
     * @return An object of the same type as input, containing the data from the input object and the eventual additional data read from the extension
     * @throws IOException for various error conditions
     */
    Object handleExtension(GltfLoader loader, String parentName, JsonElement parent, JsonElement extension, Object input) throws IOException;

}
