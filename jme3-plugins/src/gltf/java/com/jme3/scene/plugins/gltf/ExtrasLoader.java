
package com.jme3.scene.plugins.gltf;

import com.jme3.plugins.json.JsonElement;

/**
 * Interface to handle a glTF extra.
 * Created by Nehon on 30/08/2017.
 */
public interface ExtrasLoader {

    /**
     * Handles a glTF extra.
     * This method will be invoked every time an "extras" element is found in the glTF file.
     * The parentName indicates where the "extras" element was found.
     *
     * @param loader     the GltfLoader with all the data structures
     * @param parentName the name of the element being read
     * @param parent     the element being read
     * @param extras     the content of the extras found in the element being read
     * @param input      an object containing already loaded data from the element, this is most probably a JME object
     * @return An object of the same type as input, containing the data from the input object and the eventual additional data read from the extras
     */
    Object handleExtras(GltfLoader loader, String parentName, JsonElement parent, JsonElement extras, Object input);

}
