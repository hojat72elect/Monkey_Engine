
package com.jme3.scene.mesh;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import com.jme3.scene.VertexBuffer.Format;
import com.jme3.util.BufferUtils;

/**
 * <code>IndexBuffer</code> is an abstraction for integer index buffers,
 * it is used to retrieve indices without knowing in which format they 
 * are stored (ushort or uint).
 *
 * @author lex
 */
public abstract class IndexBuffer {
    
    public static IndexBuffer wrapIndexBuffer(Buffer buf) {
        if (buf instanceof ByteBuffer) {
            return new IndexByteBuffer((ByteBuffer) buf);
        } else if (buf instanceof ShortBuffer) {
            return new IndexShortBuffer((ShortBuffer) buf);
        } else if (buf instanceof IntBuffer) {
            return new IndexIntBuffer((IntBuffer) buf);
        } else {
            throw new UnsupportedOperationException("Index buffer type unsupported: "+ buf.getClass());
        }
    }

    /**
     * Create an IndexBuffer with the specified capacity.
     *
     * @param vertexCount the number of vertices that will be indexed into
     * (determines number of bits per element)
     * @param indexCount the number of indices the IndexBuffer must hold
     * (determines number of elements in the buffer)
     * @return a new, appropriately sized IndexBuffer, which may be an
     * {@link IndexByteBuffer}, an {@link IndexShortBuffer}, or an
     * {@link IndexIntBuffer}
     */
    public static IndexBuffer createIndexBuffer(int vertexCount,
            int indexCount) {
        IndexBuffer result;

        if (vertexCount < 128) { // TODO: could be vertexCount <= 256
            ByteBuffer buffer = BufferUtils.createByteBuffer(indexCount);
            int maxIndexValue = Math.max(0, vertexCount - 1);
            result = new IndexByteBuffer(buffer, maxIndexValue);

        } else if (vertexCount < 65536) { // TODO: could be <= 65536
            ShortBuffer buffer = BufferUtils.createShortBuffer(indexCount);
            int maxIndexValue = vertexCount - 1;
            result = new IndexShortBuffer(buffer, maxIndexValue);

        } else {
            IntBuffer buffer = BufferUtils.createIntBuffer(indexCount);
            result = new IndexIntBuffer(buffer);
        }

        return result;
    }

    /**
     * @see Buffer#rewind()
     */
    public void rewind() {
        getBuffer().rewind();
    }

    /**
     * @return the count (&ge;0)
     * @see Buffer#remaining()
     */
    public int remaining() {
        return getBuffer().remaining();
    }

    /**
     * Returns the vertex index for the current position.
     *
     * @return the index
     */
    public abstract int get();

    /**
     * Returns the vertex index for the given index in the index buffer.
     * 
     * @param i The index inside the index buffer
     * @return the index
     */
    public abstract int get(int i);
    
    /**
     * Absolute put method.
     * 
     * <p>Puts the vertex index at the index buffer's index.
     * Implementations may throw an {@link UnsupportedOperationException}
     * if modifying the IndexBuffer is not supported (e.g. virtual index
     * buffers).</p>
     * 
     * @param i The buffer index
     * @param value The vertex index
     * @return This buffer
     */
    public abstract IndexBuffer put(int i, int value);
    
    /**
     * Relative put method.
     * 
     * <p>Puts the vertex index at the current position, then increments the
     * position. Implementations may throw an 
     * {@link UnsupportedOperationException} if modifying the IndexBuffer is not
     * supported (e.g. virtual index buffers).</p>
     * 
     * @param value The vertex index
     * @return This buffer
     */
    public abstract IndexBuffer put(int value);
    
    /**
     * Returns the size of the index buffer.
     * 
     * @return the size of the index buffer.
     */
    public abstract int size();
    
    /**
     * Returns the underlying data-type specific {@link Buffer}.
     * Implementations may return null if there's no underlying
     * buffer.
     * 
     * @return the underlying {@link Buffer}.
     */
    public abstract Buffer getBuffer();
    
    /**
     * Returns the format of the data stored in this buffer.
     * 
     * <p>This method can be used to set an {@link IndexBuffer} to a 
     * {@link com.jme3.scene.Mesh Mesh}:</p>
     * <pre>
     * mesh.setBuffer(Type.Index, 3, 
     *     indexBuffer.getFormat(), indexBuffer);
     * </pre>
     * @return an enum value
     */
    public abstract Format getFormat();
}
