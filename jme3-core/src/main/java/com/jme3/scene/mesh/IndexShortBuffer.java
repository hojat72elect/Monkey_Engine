
package com.jme3.scene.mesh;

import java.nio.Buffer;
import java.nio.ShortBuffer;

import com.jme3.scene.VertexBuffer.Format;

/**
 * IndexBuffer implementation for {@link ShortBuffer}s.
 * 
 * @author lex
 */
public class IndexShortBuffer extends IndexBuffer {

    private final ShortBuffer buf;
    /**
     * the largest index value that can be put to the buffer
     */
    private int maxValue = 65_535;

    /**
     * Instantiate an IndexBuffer using the specified ShortBuffer and a maximum
     * index value of 65_535.
     *
     * @param buffer a pre-existing buffer (not null, alias created)
     */
    public IndexShortBuffer(ShortBuffer buffer) {
        buf = buffer;
        buf.rewind();
    }

    /**
     * Instantiate an IndexBuffer using the specified ShortBuffer and set its
     * maximum index value.
     *
     * @param buffer a pre-existing buffer (not null, alias created)
     * @param maxValue the desired maximum index value (&ge;0, &le;65_535)
     */
    public IndexShortBuffer(ShortBuffer buffer, int maxValue) {
        assert maxValue >= 0 && maxValue <= 65_535 : "out of range: " + maxValue;
        this.maxValue = maxValue;

        buf = buffer;
        buf.rewind();
    }

    @Override
    public int get() {
        return buf.get() & 0x0000FFFF;
    }
    @Override
    public int get(int i) {
        return buf.get(i) & 0x0000FFFF;
    }

    @Override
    public IndexShortBuffer put(int i, int value) {
        assert value >= 0 && value <= maxValue 
                : "IndexBuffer was created with elements too small for value="
                + value;

        buf.put(i, (short) value);
        return this;
    }
    
    @Override
    public IndexShortBuffer put(int value) {
        assert value >= 0 && value <= maxValue 
                : "IndexBuffer was created with elements too small for value="
                + value;

        buf.put((short) value);
        return this;
    }

    @Override
    public int size() {
        return buf.limit();
    }

    @Override
    public Buffer getBuffer() {
        return buf;
    }
    
    @Override
    public Format getFormat () {
        return Format.UnsignedShort;
    }
}
