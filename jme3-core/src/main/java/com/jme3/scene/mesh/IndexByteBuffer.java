
package com.jme3.scene.mesh;

import java.nio.Buffer;
import java.nio.ByteBuffer;

import com.jme3.scene.VertexBuffer.Format;

/**
 * IndexBuffer implementation for {@link ByteBuffer}s.
 * 
 * @author lex
 */
public class IndexByteBuffer extends IndexBuffer {

    private final ByteBuffer buf;
    /**
     * the largest index value that can be put to the buffer
     */
    private int maxValue = 255;

    /**
     * Instantiate an IndexBuffer using the specified ByteBuffer and a maximum
     * index value of 255.
     *
     * @param buffer a pre-existing buffer (not null, alias created)
     */
    public IndexByteBuffer(ByteBuffer buffer) {
        buf = buffer;
        buf.rewind();
    }

    /**
     * Instantiate an IndexBuffer using the specified ByteBuffer and set its
     * maximum index value.
     *
     * @param buffer a pre-existing buffer (not null, alias created)
     * @param maxValue the desired maximum index value (&ge;0, &le;255)
     */
    public IndexByteBuffer(ByteBuffer buffer, int maxValue) {
        assert maxValue >= 0 && maxValue <= 255 : "out of range: " + maxValue;
        this.maxValue = maxValue;

        buf = buffer;
        buf.rewind();
    }

    @Override
    public int get() {
        return buf.get() & 0x000000FF;
    }

    @Override
    public int get(int i) {
        return buf.get(i) & 0x000000FF;
    }

    @Override
    public IndexByteBuffer put(int i, int value) {
        assert value >= 0 && value <= maxValue 
                : "IndexBuffer was created with elements too small for value="
                + value;

        buf.put(i, (byte) value);
        return this;
    }
    
    @Override
    public IndexByteBuffer put(int value) {
        assert value >= 0 && value <= maxValue 
                : "IndexBuffer was created with elements too small for value="
                + value;

        buf.put((byte) value);
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
        return Format.UnsignedByte;
    }

}
