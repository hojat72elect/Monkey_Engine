
package com.jme3.util;

import java.nio.Buffer;
import java.nio.ByteBuffer;

/**
 * Allocates and destroys direct byte buffers using native code.
 *
 * @author pavl_g.
 */
public final class AndroidNativeBufferAllocator implements BufferAllocator {

    static {
        System.loadLibrary("bufferallocatorjme");
    }

    @Override
    public void destroyDirectBuffer(Buffer toBeDestroyed) {
        releaseDirectByteBuffer(toBeDestroyed);
    }

    @Override
    public ByteBuffer allocate(int size) {
        return createDirectByteBuffer(size);
    }

    /**
     * Releases the memory of a direct buffer using a buffer object reference.
     *
     * @param buffer the buffer reference to release its memory.
     * @see AndroidNativeBufferAllocator#destroyDirectBuffer(Buffer)
     */
    private native void releaseDirectByteBuffer(Buffer buffer);

    /**
     * Creates a new direct byte buffer explicitly with a specific size.
     *
     * @param size the byte buffer size used for allocating the buffer.
     * @return a new direct byte buffer object.
     * @see AndroidNativeBufferAllocator#allocate(int)
     */
    private native ByteBuffer createDirectByteBuffer(long size);
}