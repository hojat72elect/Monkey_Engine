
package com.jme3.audio;

import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.Savable;
import com.jme3.util.NativeObject;
import java.io.IOException;

public abstract class Filter extends NativeObject implements Savable {

    public Filter() {
        super();
    }

    protected Filter(int id) {
        super(id);
    }

    @Override
    public void write(JmeExporter ex) throws IOException {
        // nothing to save
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        // nothing to read
    }

    @Override
    public void resetObject() {
        this.id = -1;
        setUpdateNeeded();
    }

    @Override
    public void deleteObject(Object rendererObject) {
        ((AudioRenderer) rendererObject).deleteFilter(this);
    }

    @Override
    public abstract NativeObject createDestructableClone();

}
