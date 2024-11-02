
package com.jme3.export;

import java.io.IOException;

/**
 * NullSavable is an implementation of Savable with no data.
 * It is used for backward compatibility with versions of the J3O 
 * format that wrote Blender importer's "Properties" class.
 * 
 * @author Kirill Vainer
 */
public class NullSavable implements Savable {
    @Override
    public void write(JmeExporter ex) throws IOException {
    }
    @Override
    public void read(JmeImporter im) throws IOException {
    }
}
