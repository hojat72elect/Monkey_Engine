
package com.jme3.util;

import java.nio.Buffer;
import java.nio.ByteBuffer;

/**
 * This class contains a primitive allocator with no special logic, should work
 * on any jvm
 */
public final class PrimitiveAllocator implements BufferAllocator {
    /**
     * De-allocate a direct buffer.
     *
     * @param toBeDestroyed ignored
     */
    @Override
    public void destroyDirectBuffer(Buffer toBeDestroyed) {
        // no exception by intent, as this way naively written java7/8
        // applications won't crash on 9 assuming they can dispose buffers
        System.err.println("Warning destroyBuffer not supported");
    }

    /**
     * Allocate a direct ByteBuffer of the specified size.
     *
     * @param size in bytes (&ge;0)
     * @return a new direct buffer
     */
    @Override
    public ByteBuffer allocate(int size) {
        return ByteBuffer.allocateDirect(size);
    }
}
