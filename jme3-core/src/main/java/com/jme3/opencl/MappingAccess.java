
package com.jme3.opencl;

/**
 * Specifies the access flags when mapping a {@link Buffer} or {@link Image} object.
 * @see Buffer#map(com.jme3.opencl.CommandQueue, long, long, com.jme3.opencl.MappingAccess) 
 * @see Image#map(com.jme3.opencl.CommandQueue, long[], long[], com.jme3.opencl.MappingAccess) 
 * @author shaman
 */
public enum MappingAccess {
    /**
     * Only read access is allowed to the mapped memory.
     */
    MAP_READ_ONLY,
    /**
     * Only write access is allowed to the mapped memory.
     */
    MAP_WRITE_ONLY,
    /**
     * Both read and write access is allowed.
     */
    MAP_READ_WRITE,
    /**
     * The old memory content is completely discarded and the buffer is filled
     * completely with new data. This might be faster than {@link #MAP_WRITE_ONLY}
     */
    MAP_WRITE_INVALIDATE
}
