
package com.jme3.opencl;

/**
 * Specifies how a buffer object can be accessed by the kernel.
 * @author shaman
 * @see Buffer
 */
public enum MemoryAccess {
    /**
     * A kernel can both read and write the buffer.
     */
    READ_WRITE,
    /**
     * A kernel can only write this buffer.
     */
    WRITE_ONLY,
    /**
     * A kernel can only read this buffer
     */
    READ_ONLY
}
