
package com.jme3.scene.mesh;

import java.nio.Buffer;
import java.nio.IntBuffer;

import com.jme3.scene.VertexBuffer.Format;

/**
 * IndexBuffer implementation for {@link IntBuffer}s.
 * 
 * @author lex
 */
public class IndexIntBuffer extends IndexBuffer {

    private final IntBuffer buf;

    public IndexIntBuffer(IntBuffer buffer) {
        buf = buffer;
        buf.rewind();
    }

    @Override
    public int get() {
        return buf.get();
    }
    @Override
    public int get(int i) {
        return buf.get(i);
    }

    @Override
    public IndexIntBuffer put(int i, int value) {
        buf.put(i, value);
        return this;
    }
    
    @Override
    public IndexIntBuffer put(int value) {
        buf.put(value);
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
        return Format.UnsignedInt;
    }
}
