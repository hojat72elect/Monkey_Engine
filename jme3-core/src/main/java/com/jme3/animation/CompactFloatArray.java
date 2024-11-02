
package com.jme3.animation;

import com.jme3.export.*;

import java.io.IOException;

/**
 * Serialize and compress Float by indexing similar values
 * @author Lim, YongHoon
 */
public class CompactFloatArray extends CompactArray<Float> implements Savable {

    /**
     * Creates a compact vector array
     */
    public CompactFloatArray() {
    }

    /**
     * creates a compact vector array
     * @param dataArray the data array
     * @param index the indices
     */
    public CompactFloatArray(float[] dataArray, int[] index) {
        super(dataArray, index);
    }

    @Override
    protected final int getTupleSize() {
        return 1;
    }

    @Override
    protected final Class<Float> getElementClass() {
        return Float.class;
    }

    @Override
    public void write(JmeExporter ex) throws IOException {
        serialize();
        OutputCapsule out = ex.getCapsule(this);
        out.write(array, "array", null);
        out.write(index, "index", null);
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        InputCapsule in = im.getCapsule(this);
        array = in.readFloatArray("array", null);
        index = in.readIntArray("index", null);
    }

    public void fill(int startIndex, float[] store) {
        for (int i = 0; i < store.length; i++) {
            store[i] = get(startIndex + i, null);
        }
    }

    @Override
    protected void serialize(int i, Float data) {
        array[i] = data;
    }

    @Override
    protected Float deserialize(int i, Float store) {
        return array[i];
    }
}
