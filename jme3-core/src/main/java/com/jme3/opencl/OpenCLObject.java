
package com.jme3.opencl;

/**
 * Base interface of all native OpenCL objects.
 * This interface provides the functionality to safely release the object.
 * @author shaman
 */
public interface OpenCLObject {
    
    /**
     * Releaser for an {@link OpenCLObject}.
     * Implementations of this interface must not hold a reference to the
     * {@code OpenCLObject} directly.
     */
    public static interface ObjectReleaser {
        /**
         * Releases the native resources of the associated {@link OpenCLObject}.
         * This method must be guarded against multiple calls: only the first
         * call should release, the next ones must not throw an exception.
         */
        void release();
    }
    /**
     * Returns the releaser object. Multiple calls should return the same object.
     * The ObjectReleaser is used to release the OpenCLObject when it is garbage
     * collected. Therefore, the returned object must not hold a reference to
     * the OpenCLObject.
     * @return the object releaser
     */
    ObjectReleaser getReleaser();
    /**
     * Releases this native object.
     * 
     * Should delegate to {@code getReleaser().release()}.
     */
    void release();
    /**
     * Registers this object for automatic releasing on garbage collection.
     * By default, OpenCLObjects are not registered in the
     * {@link OpenCLObjectManager}, you have to release it manually 
     * by calling {@link #release() }.
     * Without registering or releasing, a memory leak might occur.
     * <br>
     * Returns {@code this} to allow calls like
     * {@code Buffer buffer = clContext.createBuffer(1024).register();}.
     * @return {@code this}
     */
    OpenCLObject register();
}
